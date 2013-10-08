/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Units.Inventory;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoInventory extends JPanel{
    private PanelInfoItem[] itemPanels = new PanelInfoItem[Inventory.MAX_CAPACITY];
    
    public PanelInfoInventory() {
        GridLayout inventoryLayout = new GridLayout(5, 1);
        setLayout(inventoryLayout);
        setBorder(new LineBorder(Color.PINK));
        
        for (int i = 0; i < itemPanels.length; i++)
        {
            itemPanels[i] = new PanelInfoItem();
            add(itemPanels[i]);
        }
    }
    
    public void setValues(Inventory inventory) {
        if (inventory != null)
        {
            for (int i = 0; i < itemPanels.length; i++)
            {
                if(i < inventory.size())
                    itemPanels[i].setValues(inventory.getItem(i));
                else
                    itemPanels[i].clear();
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