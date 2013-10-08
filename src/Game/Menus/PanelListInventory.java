/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Sprites.ImageComponent;
import Units.Inventory;
import Units.Items.Item;
import Units.Items.ItemFilter;
import javax.swing.JPanel;

public class PanelListInventory extends JPanel implements GamePanel {
    private ImageComponent background;
    private ImageComponent[] itemEntries;
    
    public PanelListInventory() {
        background = new ImageComponent(panelFactory.getPanel(MenuList.DISPLAY_WIDTH_UNIT_ITEM, 2*Inventory.MAX_CAPACITY));
        itemEntries = new ImageComponent[Inventory.MAX_CAPACITY];
        for(int i = 0; i < itemEntries.length; i++)
        {
            itemEntries[i] = new ImageComponent();
            itemEntries[i].setLocation(panelFactory.getBorderLeft(), 
                    panelFactory.getBorderTop() + MenuItem.ITEM_COMPONENT_HEIGHT*i);
        }
        
        for(ImageComponent imageComponent : itemEntries)
            add(imageComponent);
        add(background);
        
        setSize(background.getSize());
        setLayout(null);
        setOpaque(false);
        setVisible(false);
    }

    void setInventory(Item[] inventory, ItemFilter itemFilter) {
        for(int i = 0; i < itemEntries.length; i++)
            if(i < inventory.length)
                itemEntries[i].setImage(MenuItem.createItemComponent(inventory[i], itemFilter, getWriteableWidth()));
            else 
                itemEntries[i].setBlank();
    }
    
    public void open() {
        setVisible(true);
    }
    @Override
    public void close() {
        setVisible(false);
    }
    @Override
    public boolean isOpen() {
        return isVisible();
    }

    private int getWriteableWidth() {
        return background.getWidth() - panelFactory.getBorderX();
    }
    
    @Override
    public void setTick(int tick) {}
}
