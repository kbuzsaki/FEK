/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Sound.SoundManager;
import Sprites.Text;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import Units.Items.Item;
import static Units.Items.Item.ICON_EDGE_LENGTH;
import Units.Items.ItemFactory;
import Units.Items.ItemFilter;
import Units.Unit;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public abstract class MenuItem extends MenuList implements ItemFilter {
    static final int ITEM_COMPONENT_HEIGHT = ICON_EDGE_LENGTH;
    private static final int backgroundUnitHeight = 10;
    private static final Item[] dummyInv = {ItemFactory.swordIron()};
    
    protected Cursor cursor;
    private Item[] items;
    
    private PanelInventoryInfo infoPanel;
    
    public MenuItem (GameScreen gameScreen, SoundManager soundManager, 
            Cursor cursor, PanelInventoryInfo infoPanel) {
        super(gameScreen, soundManager, DISPLAY_WIDTH_UNIT_ITEM, backgroundUnitHeight, 
                createInventory(dummyInv, null, 100));
        this.cursor = cursor;
        this.infoPanel = infoPanel;
    }
    
    private ImageComponent[] createInventoryIcons(Item[] inventory) {
        return createInventory(inventory, this, background.getWidth() - (panelFactory.getBorderX()));
    }
    static ImageComponent[] createInventory(Item[] inventory, ItemFilter itemFilter, int maxWidth) {
        ImageComponent[] itemComponents = new ImageComponent[inventory.length];
        
        for(int i = 0; i < inventory.length; i++)
            itemComponents[i] = createItemComponent(inventory[i], itemFilter, maxWidth);
        
        return itemComponents;
    }
    static ImageComponent createItemComponent(Item item, ItemFilter itemFilter, int maxWidth) {
        BufferedImage image = new BufferedImage( maxWidth, ITEM_COMPONENT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        
        int nameColor;
        int usesColor;
        
        if(itemFilter != null && !itemFilter.isUseable(item))
        {
            // colors are used if the items are not useable
            nameColor = Text.GREY;
            usesColor = Text.GREY;
        }
        else
        {
            // colors are used if the item is useable or there is no itemFilter
            nameColor = Text.WHITE;
            usesColor = Text.BLUE;
        }
        
        BufferedImage icon = item.getIcon().getImage();
        BufferedImage name = Text.getImageComponent(item.getName(), nameColor).getImage();
        BufferedImage uses = Text.getImageComponent(
                String.valueOf(item.getUses()), usesColor).getImage();

        imageGraphics.drawImage(icon, 0, 0, null);
        imageGraphics.drawImage(name, ICON_EDGE_LENGTH, TEXT_OFFSET, null);
        imageGraphics.drawImage(uses, image.getWidth() - uses.getWidth(), TEXT_OFFSET, null);
        
        return new ImageComponent(image);
    }   
    
    public PanelInventoryInfo getPanelInventoryInfo() {
        return infoPanel;
    }
    
    @Override
    void openQuietly(Menu parentMenu, CancelListener cancelListener) {
        items = getItems();
        super.openQuietly(parentMenu, cancelListener);
        cursor.hideCursor();
        infoPanel.open();
    }
    @Override
    public void close() {
        infoPanel.close();
        super.close();
    }
    
    @Override
    protected void reconstructMenu(int index) {
        reconstructMenu(createInventoryIcons(items));
        updateIndex(index);
    }
    @Override
    protected void updateIndex(int index) {
        super.updateIndex(index);
        infoPanel.setItemInfo(cursor.getSelectedUnit(), getItems()[index]);
    }
    
    protected Unit getActor() {
        return cursor.getSelectedUnit();
    }
    protected Item[] getItems() {
        Item[] inventory = getActor().getItems();
        ArrayList<Item> items = new ArrayList(inventory.length);
        
        for(Item item : inventory)
            if(isTargetable(item))
                items.add(item);
        
        return items.toArray(new Item[items.size()]);
    }
    
    protected Item getItemAt(int index) {
        if(items != null)
            return items[index];
        else
        {
            System.err.println("Item Menu inventory has not been properly loaded");
            return null;
        }
    }
    
    @Override
    protected void updatePosition() {
        super.updatePosition();
        infoPanel.updatePosition();
    }
    
    @Override
    public void setTick(int tick) {
        super.setTick(tick);
        infoPanel.setTick(tick);
    }
    
}
