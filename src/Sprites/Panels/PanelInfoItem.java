/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.Text;
import Sprites.ImageComponent;
import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.ItemFactory;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoItem extends JPanel{
    private ImageComponent icon;
    private ImageComponent name;
    private ImageComponent uses;
    
    public PanelInfoItem() {
        setBorder(new LineBorder(Color.BLACK));
        Item item = ItemFactory.lanceIron();
        
        icon = item.getIcon();
        name = Text.getImageComponent(item.getName());
        uses = Text.getImageComponent(item.getUses() + "/" + item.getTotalUses());
        
        add(icon);
        add(name);
        add(uses);
    }
    
    public void setValues(Item item) {
        if (item != null)
        {
            int nameColor;
            int usesColor;
        
            if(item instanceof Equipment && !((Equipment)item).isEquipable())
            {
                // colors are used if the item is not equipable
                nameColor = Text.GREY;
                usesColor = Text.GREY;
            }
            else
            {
                // colors are used if the item is equipable or it is not an equipment
                nameColor = Text.WHITE;
                usesColor = Text.BLUE;
            }
            
            icon.setImage(item.getIcon());
            name.setImage(Text.getImageComponent(item.getName(), nameColor));
            uses.setImage(Text.getImageComponent(item.getUses() + "/" + item.getTotalUses(), usesColor));
        }
        else
        {
            clear();
        }
        validate();
//        repaint(); // validate seems to do the trick instead (was having trouble with layout recalc)
    }
    public void clear() {
        icon.setBlank();
        name.setBlank();
        uses.setBlank();
        
        validate();
    }
}
