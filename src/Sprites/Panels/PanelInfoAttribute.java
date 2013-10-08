/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.Text;
import Sprites.ImageComponent;
import Units.Attribute;
import Units.AttributeType;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

public class PanelInfoAttribute extends JPanel{
    private AttributeType type;
    private ImageComponent imgLabel;
    private PanelInfoValue valueDisplayPanel;
    
    public PanelInfoAttribute(AttributeType type) {
        this.type = type;
        imgLabel = Text.getImageComponent(type.name, Text.YELLOW);
        valueDisplayPanel = new PanelInfoValue();
        
        add(imgLabel);
        add(valueDisplayPanel);
        
        setBorder(new LineBorder(Color.RED));
        setLayout(null);
    }
    public void setSize(int width, int height) {
        super.setSize(width, height);
        imgLabel.setLocation(getWidth()/16, (getHeight()-imgLabel.getHeight())/2);
        valueDisplayPanel.setLocation(getWidth()*2/5, (getHeight()-valueDisplayPanel.getHeight())/2);
    }
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }
    
    public AttributeType getType() {
        return type;
    }
    
    public void setValues(Attribute attribute) {
        if(attribute != null)
        {
            valueDisplayPanel.setValue(attribute.get(), attribute.getValCap());
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
