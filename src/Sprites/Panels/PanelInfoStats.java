/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The Stats Info Panel
 * This is the panel that displays the unit's stats or attributes.
 * 
 * Constituents:
 *  STRAttrPanel: The panel that displays the unit's strength
 *  SKLAttrPanel: The panel that displays the unit's skill
 *  SPDAttrPanel: The panel that displays the unit's speed
 *  LCKAttrPanel: The panel that displays the unit's luck
 *  DEFAttrPanel: The panel that displays the unit's defense
 *  RESAttrPanel: The panel that displays the unit's magical resistance
 * 
 * The panel is updated with a call to the method "setUnit(unit)", which updates
 * the panel to display the given unit's information.
 */
package Sprites.Panels;

import Units.AttributeType;
import Units.Stats;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.EnumMap;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoStats extends JPanel{
    private static final AttributeType[] attributesDisplayed = 
        { AttributeType.STR, AttributeType.SKL, AttributeType.SPD,
            AttributeType.LCK, AttributeType.DEF, AttributeType.RES
        };
    private EnumMap<AttributeType, PanelInfoAttribute> attrPanels;
    
    /**
     * Creates a PanelInfoStats object, which displays the unit's stats.
     */
    public PanelInfoStats() {
        GridLayout statsLayout = new GridLayout(6,1);
        setLayout(statsLayout);
        setBorder(new LineBorder(Color.GREEN));
        
        attrPanels = new EnumMap(AttributeType.class);
        
        for (AttributeType attributeType : attributesDisplayed)
        {
            attrPanels.put(attributeType, new PanelInfoAttribute(attributeType));
            add(attrPanels.get(attributeType));
        }
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        for (PanelInfoAttribute attrPanel : attrPanels.values())
            attrPanel.setSize(getWidth(), getHeight()/6);
    }
    @Override
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }
    
    public void setValues(Stats stats) {
        if (stats != null) // if the unit sent exists, send that unit's stats
        {
            for (PanelInfoAttribute attrPanel : attrPanels.values())
                attrPanel.setValues(stats.get(attrPanel.getType()));
        }
        else // otherwise, send a blank value
        {
            clear();
        }
    }
    public void clear() {
        for (PanelInfoAttribute attrPanel : attrPanels.values())
            attrPanel.clear();
    }
}
