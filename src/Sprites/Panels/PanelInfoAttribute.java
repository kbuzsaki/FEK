/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.Character;
import Sprites.ImageComponent;
import Units.Attribute;
import Units.AttributeType;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

public class PanelInfoAttribute extends JPanel{
    private ImageComponent imgLabel;
    private PanelInfoValue valueDisplayPanel;
    
    public PanelInfoAttribute(AttributeType type, Dimension dimensions) {
        setBorder(new LineBorder(Color.RED));
        setSize(dimensions);
        setPreferredSize(getSize());
        setLayout(null);
        
        imgLabel = Character.getImageComponent(type.name, Character.YELLOW);
        imgLabel.setLocation(getWidth()/16, (getHeight()-imgLabel.getHeight())/2);
        valueDisplayPanel = new PanelInfoValue();
        valueDisplayPanel.setLocation(getWidth()*2/5, (getHeight()-valueDisplayPanel.getHeight())/2);
        
        add(imgLabel);
        add(valueDisplayPanel);
    }
    
    public void setValues(Attribute attribute) {
        if(attribute != null)
        {
            valueDisplayPanel.setValue(attribute.get(), attribute.getCap());
        }
        else
        {
            clear();
        }
    }
    public void clear() {
        valueDisplayPanel.setValue(0, 30);
    }
}
