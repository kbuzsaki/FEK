/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The Unit Info Panel
 * This is the panel that displays all of the information about a specific unit.
 * It is analogous to the "R" menu in traditional fire emblem.
 * 
 * Constituents:
 *  statsPanel: The panel that displays a unit's stats or attributes.
 *  inventoryPanel: The panel that displays a unit's inventory.
 * 
 * The panel is updated with a call to the method "setUnit(unit)", which updates
 * the panel to display the given unit's information.
 */
package Sprites.Panels;

import Sprites.AnimationMapUnit;
import Units.Unit;
import Units.UnitClass;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoUnit extends JPanel {
    private PanelInfoStats statsPanel; 
    private PanelInfoInventory inventoryPanel;
    private PanelHealth healthInfoPanel;
    private JPanel infoPanel;
    private AnimationMapUnit unitInfoAnim;
    
    
    public PanelInfoUnit(Dimension size) {
        setSize(size);
        setPreferredSize(getSize());
        setBorder(new LineBorder(Color.BLUE));
        setLayout(null);
        
        statsPanel = new PanelInfoStats(
                new Rectangle(getWidth()/4,0,getWidth()*3/16,getHeight()));    
        inventoryPanel = new PanelInfoInventory(
                new Rectangle(getWidth()*7/16, 0, getWidth()*5/16, getHeight()));
        
        infoPanel = new JPanel();
        infoPanel.setBounds(0,0, getWidth()/4, getHeight());
        
        healthInfoPanel = new PanelHealth();
        unitInfoAnim = new AnimationMapUnit(UnitClass.BANDIT.spriteFileName);
        unitInfoAnim.setFocus();
        
        infoPanel.add(healthInfoPanel);
        infoPanel.add(unitInfoAnim);
        
        add(statsPanel);
        add(inventoryPanel);
        add(infoPanel);
    }
    
    public void setUnit(Unit unit) {
        
        if(unit != null)
        {
            statsPanel.setValues(unit.getStats());
            inventoryPanel.setValues(unit.getItems());
            healthInfoPanel.setFaction(unit.getFaction());
            healthInfoPanel.setValues(unit.getName(), unit.getStats().getHP().get(), unit.getStats().getHP().getValue());
            unitInfoAnim.setVisible(true);
            unitInfoAnim.loadAnimation(unit.getMapAnim());
        }
        else 
        {
            clear();
        }
    }
    public void clear() {
        statsPanel.clear();
        inventoryPanel.clear();
        
        healthInfoPanel.setValues(" ", 0, 1);
        unitInfoAnim.setVisible(false);
    }
    
    public void setTick(int tick) {
        unitInfoAnim.setTick(tick);
    }
}
