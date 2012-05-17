/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.Character;
import Sprites.ImageComponent;
import Units.Items.Item;
import Units.Items.Sword;
import Units.Items.WeaponFactory;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoItem extends JPanel{
    private ImageComponent icon;
    private ImageComponent name;
    private ImageComponent uses;
    
    public PanelInfoItem(int width) {
        setSize(width, 16);
        setPreferredSize(getSize());
        setBorder(new LineBorder(Color.BLACK));
        
        Item item = WeaponFactory.lanceIron();
        
        icon = item.getIcon();
        name = Character.getImageComponent(item.getName());
        uses = Character.getImageComponent(item.getUses() + "/" + item.getTotalUses());
        
        add(icon);
        add(name);
        add(uses);
    }
    
    public void setValues(Item item) {
        if (item != null)
        {
            icon.setImage(item.getIcon());
            name.setImage(Character.getImageComponent(item.getName()));
            uses.setImage(Character.getImageComponent(item.getUses() + "/" + item.getTotalUses()));
        }
        else
        {
            clear();
        }
        validate();
//        repaint(); // validate seems to do the trick instead (was having trouble with layout recalc)
    }
    public void clear() {
        icon.setImage(ImageComponent.blank);
        name.setImage(ImageComponent.blank);
        uses.setImage(ImageComponent.blank);
        
        validate();
    }
}
