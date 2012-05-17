/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Units.Inventory;
import Units.Items.Item;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoInventory extends JPanel{
    private PanelInfoItem[] itemPanels = new PanelInfoItem[Inventory.MAX_CAPACITY];
    
    public PanelInfoInventory(Rectangle bounds) {
        GridLayout inventoryLayout = new GridLayout(5, 1);
        setLayout(inventoryLayout);
        setBorder(new LineBorder(Color.PINK));
        setBounds(bounds);
        
        for (int i = 0; i < itemPanels.length; i++)
        {
            itemPanels[i] = new PanelInfoItem(getWidth());
            add(itemPanels[i]);
        }
    }
    
    public void setValues(Item[] inventory) {
        if (inventory != null)
        {
            for (int i = 0; i < itemPanels.length; i++)
            {
                itemPanels[i].setValues(inventory[i]);
            }
        }
        else
        {
            clear();
        }
    }
    public void clear() {
        for (PanelInfoItem itemPanel : itemPanels)
            itemPanel.clear();
    }
}