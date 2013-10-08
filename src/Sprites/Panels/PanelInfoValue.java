/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.Text;
import Sprites.ImageComponent;
import javax.swing.JPanel;

public class PanelInfoValue extends JPanel{
    private JPanel barJPanel;
    private ImageComponent barLeft;
    private ImageComponent barRight;
    private ImageComponent barFill;
    private ImageComponent valueText;
    
    public PanelInfoValue() {
        barLeft = new ImageComponent("resources/gui/window/barLeft.png");
        barLeft.setLocation(0, 0);
        
        barRight = new ImageComponent("resources/gui/window/barRight.png");
        barRight.setLocation(barLeft.getWidth(), 0);
        
        barFill = new ImageComponent("resources/gui/window/barFill.png");
        barFill.setLocation(2, 1);
        barFill.setSize(0,2);
        
        barJPanel = new JPanel();
        barJPanel.add(barFill);
        barJPanel.add(barRight);
        barJPanel.add(barLeft);
        barJPanel.setLayout(null);
        barJPanel.setSize(barRight.getWidth() + barRight.getX(), barRight.getHeight());
        barJPanel.setPreferredSize(getSize());
        
        valueText = Text.getImageComponent("0");
        //valueText.setLocation((getWidth() - valueText.width)/2, (getHeight() - valueText.height)/2);
        
        setLayout(null);
        setSize(barJPanel.getWidth(), valueText.getHeight());
        setPreferredSize(getSize());
        
        barJPanel.setLocation(0, (getHeight() - barJPanel.getHeight())/2);
        valueText.setLocation((getWidth() - valueText.getWidth())/2, 0);
        
        add(valueText);
        add(barJPanel);
    }
    
    public final void setValue(int value, int cap) {
        int barRightX = cap - barRight.getWidth() + 4;
        barRight.setLocation(barRightX, barRight.getY());

        int barFillLength = value;
        barFill.setSize(barFillLength, barFill.getHeight());
        barJPanel.setSize(barRight.getWidth() + barRight.getX(), barRight.getHeight());
        
        setSize(barJPanel.getWidth(), valueText.getHeight());
        
        valueText.setImage(Text.getImageComponent(String.valueOf(value)));
        valueText.setLocation((getWidth() - valueText.getWidth())/2, 0);
        repaint();
    }
}
