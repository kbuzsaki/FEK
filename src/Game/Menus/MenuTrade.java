/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Command;
import static Game.Menus.MenuList.POINTER_OFFSET;
import Game.Sound.SoundManager;
import Sprites.Animation;
import Sprites.AnimationFactory;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import Units.Inventory;
import Units.Unit;

public class MenuTrade extends Menu {
    private static final int MAX_INVENTORIES = 2;
    
    private Animation pointer;
    private ImageComponent pointerMarker;
    
    private PanelListInventory[] inventories;
    private Unit[] units;
    
    private int maxItemIndex;
    private int itemIndex;
    
    private boolean hasSelectedItem;
    private int selectedInventoryIndex;
    private int selectedItemIndex;
    
    public MenuTrade(GameScreen gameScreen, SoundManager soundManager) {
        super(gameScreen, soundManager);
        setMaxIndex(MAX_INVENTORIES);
        
        pointer = AnimationFactory.newPointer();
        pointerMarker = new ImageComponent(GamePanel.GUI_DIR + "frozenPointer.png");
        pointerMarker.setVisible(false);
        
        inventories = new PanelListInventory[MAX_INVENTORIES];
        for(int i = 0; i < inventories.length; i++)
        {
            inventories[i] = new PanelListInventory();
            inventories[i].setLocation(
                    i > 0 ? inventories[i-1].getX() + inventories[i-1].getWidth() : POINTER_OFFSET,
                    0);
        }
        
        
        add(pointer);
        add(pointerMarker);
        for (PanelListInventory panelListInventory : inventories)        
        {
            add(panelListInventory);
        }
        setSize(inventories[inventories.length - 1].getX() + inventories[inventories.length - 1].getWidth(), 
                inventories[inventories.length - 1].getHeight());
    }
    
    void openQuietly(CancelListener cancelListener, Unit... units) {
        this.units = units;
        setMaxIndex(this.units.length);
        deselectItem();
        
        super.openQuietly(null, cancelListener);
        for (int i = 0; i < inventories.length; i++)
            if(i < this.units.length)
                inventories[i].open();
    }
    @Override
    protected boolean shouldCloseOnAction(int index) {
        return false;
    }
    
    @Override
    protected void reconstructMenu(int index) {
        for (int i = 0; i < inventories.length; i++)        
        {
            if(i < units.length)
            {
                inventories[i].setInventory(units[i].getItems(), units[i]);
            }
            else
            {
                // set blank;
            }
        }
    }
    @Override
    protected void updateIndex(int index) {
        setMaxItemIndex(units[index].getInventory().size()); 
        pointer.setLocation(calculatePointerX(index), pointer.getY());
        setWrappedItemIndex(itemIndex);
    }
    @Override
    protected int getResetIndex() {
        if(!units[selectedInventoryIndex].getInventory().isEmpty())
            return selectedInventoryIndex;
        else
            return (selectedInventoryIndex + 1) % MAX_INVENTORIES;
    }
    private void updateItemIndex() {
        pointer.setLocation(
                pointer.getX(), 
                calculatePanelY() + panelFactory.getBorderTop() + itemIndex * 16);
    }
    
    private void setWrappedItemIndex(int itemIndex) {
        if (itemIndex < 0)
        {
            this.itemIndex = (getMaxItemIndex() - 1);
        }
        else 
        {
            this.itemIndex = (itemIndex % getMaxItemIndex());
        }
        updateItemIndex();
    }
    private void setCappedItemIndex(int itemIndex) {
        if(itemIndex >= getMaxItemIndex())
        {
            setWrappedItemIndex(getMaxItemIndex() - 1);
        }
        else
        {
            setWrappedItemIndex(itemIndex);
        }
    }
    private void incrementItemIndex() {
        setWrappedItemIndex(itemIndex + 1);
    }
    private void decrementItemIndex() {
        setWrappedItemIndex(itemIndex - 1);
    }
    
    private int getMaxItemIndex() {
        if(hasSelectedItem && !units[getIndex()].getInventory().isFull())
            return maxItemIndex + 1;
        else if(maxItemIndex > 0)
            return maxItemIndex;
        else
            return 1; // always has at least one index
    }
    private void setMaxItemIndex(int maxItemIndex) {
        this.maxItemIndex = maxItemIndex;
    }
    
    private void selectItem() {
        selectedItemIndex = itemIndex;
        pointerMarker.setLocation(pointer.getLocation());
        pointerMarker.setVisible(true);
        hasSelectedItem = true;
    }
    private void deselectItem() {
        hasSelectedItem = false;
        pointerMarker.setVisible(false);
    }
    
    private int calculatePointerX(int index) {
        return inventories[index].getX() - POINTER_OFFSET;
    }
    private int calculatePanelY() {
        return (getHeight() - inventories[0].getHeight()) / 2;
    }
    private int getNumberOpenPanels() {
        return units.length; // somewhat hacky
    }
    private void reSpacePanels(int numberPanels) {
        int totalPanelWidth = 0;
        for (int i = 0; i < numberPanels; i++)        
        {
            totalPanelWidth += inventories[i].getWidth();
        }
        // the spacing between the panels and the edge. Number of spaces is one per panel plus the final edge
        int spacingWidth = (getWidth() - totalPanelWidth) / (numberPanels + 1);
        
        int panelY = calculatePanelY();
        int nextX = spacingWidth;
        for (int i = 0; i < numberPanels; i++)
        {
            // spaces the panels along the X axis and centers them all vertically
            inventories[i].setLocation(nextX, panelY);
            nextX += (inventories[i].getWidth() + spacingWidth);
        }
    }

    @Override
    public void setTick(int tick) {
        pointer.setTick(tick);
    }
    
    @Override
    public boolean keyHandle(Command button) {
        switch(button)
        {
            case LEFT:
            case RIGHT:
                soundManager.playSoundEffect(SoundManager.menuBlipHigh);
                toggleInventory();
                return true;
            case UP:
                soundManager.playSoundEffect(SoundManager.menuBlipLow);
                decrementItemIndex();
                return true;
            case DOWN:
                soundManager.playSoundEffect(SoundManager.menuBlipLow);
                incrementItemIndex();
                return true;
            case B:
                if(hasSelectedItem)
                {
                    soundManager.playSoundEffect(SoundManager.cancel);
                    deselectItem();
                    setWrappedItemIndex(selectedItemIndex);
                    resetIndex();
                    return true;
                }
                else
                    return super.keyHandle(button);
            default:
                return super.keyHandle(button);
        }
    }
    private void toggleInventory() {
        if(!units[getDecrementedIndex()].getInventory().isEmpty())
        {
            decrementIndex();
        }
    }
    @Override
    protected void performAction(int index) {
        if(hasSelectedItem)
        {
            Inventory.trade(
                    units[selectedInventoryIndex].getInventory(), selectedItemIndex, 
                    units[index].getInventory(), itemIndex);
            getActor().setHasTraded(true);
            deselectItem();
            resetMenu();
            setCappedItemIndex(selectedItemIndex);
        }
        else
        {
            selectItem();
            selectedInventoryIndex = index;
            incrementIndex();
            setWrappedItemIndex(getMaxItemIndex() - 1);
        }
    }
    
    private Unit getActor() {
        return units[0];
    }
    
    @Override
    protected void updatePosition() {
        maximize();
        reSpacePanels(getNumberOpenPanels());
    }
}
