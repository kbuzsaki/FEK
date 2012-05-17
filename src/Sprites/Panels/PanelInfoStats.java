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
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoStats extends JPanel{
    private PanelInfoAttribute STRAttrPanel;
    private PanelInfoAttribute SKLAttrPanel;
    private PanelInfoAttribute SPDAttrPanel;
    private PanelInfoAttribute LCKAttrPanel;
    private PanelInfoAttribute DEFAttrPanel;
    private PanelInfoAttribute RESAttrPanel;
    
    /**
     * Creates a PanelInfoStats object, which displays the unit's stats.
     * @param bounds The size of of the info panel created.
     */
    public PanelInfoStats(Rectangle bounds) {
        GridLayout statsLayout = new GridLayout(6,1);
        setLayout(statsLayout);
        setBorder(new LineBorder(Color.GREEN));
        setBounds(bounds);
        
        STRAttrPanel = new PanelInfoAttribute(AttributeType.STR, new Dimension(bounds.width, bounds.height/6));
        SKLAttrPanel = new PanelInfoAttribute(AttributeType.SKL, new Dimension(bounds.width, bounds.height/6));
        SPDAttrPanel = new PanelInfoAttribute(AttributeType.SPD, new Dimension(bounds.width, bounds.height/6));
        LCKAttrPanel = new PanelInfoAttribute(AttributeType.LCK, new Dimension(bounds.width, bounds.height/6));
        DEFAttrPanel = new PanelInfoAttribute(AttributeType.DEF, new Dimension(bounds.width, bounds.height/6));
        RESAttrPanel = new PanelInfoAttribute(AttributeType.RES, new Dimension(bounds.width, bounds.height/6));
        
        add(STRAttrPanel);
        add(SKLAttrPanel);
        add(SPDAttrPanel);
        add(LCKAttrPanel);
        add(DEFAttrPanel);
        add(RESAttrPanel);
    }
    
    public void setValues(Stats stats) {
        if (stats != null) // if the unit sent exists, send that unit's stats
        {
            STRAttrPanel.setValues(stats.getSTR());
            SKLAttrPanel.setValues(stats.getSKL());
            SPDAttrPanel.setValues(stats.getSPD());
            LCKAttrPanel.setValues(stats.getLCK());
            DEFAttrPanel.setValues(stats.getDEF());
            RESAttrPanel.setValues(stats.getRES());
        }
        else // otherwise, send a blank value
        {
            clear();
        }
    }
    public void clear() {
        STRAttrPanel.clear();
        SKLAttrPanel.clear();
        SPDAttrPanel.clear();
        LCKAttrPanel.clear();
        DEFAttrPanel.clear();
        RESAttrPanel.clear();
    }
}
