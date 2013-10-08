/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Level;
import Game.Sound.SoundManager;
import Sprites.AnimationMapUnit;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import Sprites.Text;
import Units.Items.Item;
import Units.UnitClass;
import java.awt.image.BufferedImage;

public class MenuSelectionTrade extends MenuSelection {
    private MenuTrade tradeMenu;
    
    private ImageComponent background;
    private ImageComponent[] itemEntries;
    private AnimationMapUnit unitInfoAnim;
    private ImageComponent name;
    
    private static final int ANIM_X = 9;
    private static final int ANIM_OFFSET_Y = 16;
    private static final int namePanelItemUnitHeight = 4;
    private static final ImageComponent[] emptyInventory;
    static {
        emptyInventory = new ImageComponent[1];
        ImageComponent nothing = Text.getImageComponent("Nothing", Text.GREY);
        BufferedImage nothingImage = new BufferedImage(
                MenuItem.ITEM_COMPONENT_HEIGHT + nothing.getWidth(),
                MenuItem.ITEM_COMPONENT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        nothingImage.createGraphics().drawImage(nothing.getImage(), 
                MenuItem.ITEM_COMPONENT_HEIGHT, MenuList.TEXT_OFFSET, null);
        emptyInventory[0] = new ImageComponent(nothingImage);
    }
    
    public MenuSelectionTrade(Level level, GameScreen gameScreen, 
            SoundManager soundManager, Cursor cursor, MenuTrade tradeMenu) {
        super(level, gameScreen, soundManager, cursor);
        this.tradeMenu = tradeMenu;
        
        background = new ImageComponent(panelFactory.getNamePanelItem(namePanelItemUnitHeight));
        setSize(background.getSize());
        
        itemEntries = new ImageComponent[10];
        for(int i = 0; i < itemEntries.length; i++)
        {
            itemEntries[i] = new ImageComponent();
            itemEntries[i].setLocation(panelFactory.getBorderLeft(), 
                    panelFactory.getNamePlateItemHeight() + MenuItem.ITEM_COMPONENT_HEIGHT*i);
        }
        
        unitInfoAnim = new AnimationMapUnit(UnitClass.BANDIT.spriteFileName, null);
        unitInfoAnim.setLocationCentered(ANIM_X, panelFactory.getNamePlateHeight() - ANIM_OFFSET_Y);
//        unitInfoAnim.setLocationCentered(9,6);
        name = Sprites.Text.getImageComponent("Kent");
        name.setLocation(28, 7); // names are always at this set of coords
        
        add(unitInfoAnim);
        add(name);
        for(ImageComponent imageComponent : itemEntries)
            add(imageComponent);
        add(background);
    }

    @Override
    public void openQuietly(CancelListener cancelListener) {
        openQuietly(level.getMap().getUnitsAt(getActor().getTradePoints()), cancelListener);
    }
    @Override
    public void notifyCancel() {
        // the selection menu does not automatically
        // re-open after the trade menu cancels
        cancel();
    }
    
    @Override
    protected void reconstructMenu(int index) {
        unitInfoAnim.loadAnimation(getTarget(index).getMapAnim());
        name.setImage(Text.getImageComponent(getTarget(index).getName()));
        
        ImageComponent[] tempEntries = MenuItem.createInventory(getTarget(index).getItems(), 
                this, background.getWidth() - panelFactory.getBorderX());
        
        // if the inventory would be empty, force the menu to display the emptyInventory message
        if(tempEntries.length <= 0)
            tempEntries = emptyInventory;
        
        background.setImage(panelFactory.getNamePanelItem(tempEntries.length*2));
        setSize(background.getWidth(), background.getHeight());
        
        for (int i = 0; i < itemEntries.length; i++)
        {
            if (i < tempEntries.length)
            {
                itemEntries[i].setImage(tempEntries[i]);
            }
            else 
            {
                itemEntries[i].setBlank();
            }
        }
    }
    @Override
    public void setTick(int tick) {
        unitInfoAnim.setTick(tick);
    }
    
    @Override
    public boolean isTargetable(Item item) {
        return true;
    }
    @Override
    public boolean isUseable(Item item) {
        return isTargetable(item);
    }
    
    @Override
    void updateRanges(Item item) {
        level.getPanelEffectsTile().reset();
    }
    
    @Override
    protected void performAction(int index) {
        tradeMenu.openQuietly(this, getActor(), getTarget(index));
    }
}
