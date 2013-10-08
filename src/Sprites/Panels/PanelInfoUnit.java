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

import Game.CursorMovementEvent;
import Game.CursorMovementListener;
import Game.DeselectionEvent;
import Game.SelectionEvent;
import Game.SelectionListener;
import Sprites.Animateable;
import Sprites.AnimationMapUnit;
import Sprites.ColumnLayout;
import Units.Unit;
import Units.UnitClass;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoUnit extends JPanel implements Animateable, CursorMovementListener, SelectionListener {
    private PanelInfoStats statsPanel; 
    private PanelInfoInventory inventoryPanel;
    private PanelHealth healthInfoPanel;
    private JPanel infoPanel;
    private AnimationMapUnit unitInfoAnim;
    
    
    public PanelInfoUnit() {
        setBorder(new LineBorder(Color.BLUE));
        setLayout(null);
        
        statsPanel = new PanelInfoStats();  
        inventoryPanel = new PanelInfoInventory();
        
        infoPanel = new JPanel();
            infoPanel.setOpaque(false);
            infoPanel.setLayout(new ColumnLayout(0,0,5,ColumnLayout.CENTER));
        
        healthInfoPanel = new PanelHealth();
        unitInfoAnim = new AnimationMapUnit(UnitClass.BANDIT.spriteFileName, null);
        unitInfoAnim.setFocus();
        
        infoPanel.add(healthInfoPanel);
        infoPanel.add(unitInfoAnim);
        
        add(statsPanel);
        add(inventoryPanel);
        add(infoPanel);
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        
        statsPanel.setLocation(getWidth()/4, 0);
        statsPanel.setSize(getWidth()*3/16, getHeight());
        
        inventoryPanel.setLocation(getWidth()*7/16, 0);
        inventoryPanel.setSize(getWidth()*5/16, getHeight());
        
        infoPanel.setBounds(0,0, getWidth()/4, getHeight());
        
    }
    @Override
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }
    
    @Override
    public void handleCursorMovement(CursorMovementEvent event) {
        if(!event.getCursor().hasSelectedUnit())
        {
            setUnit(event.getUnit());
        }
    }
    @Override
    public void handleSelection(SelectionEvent event) {
        setUnit(event.getSelectedUnit());
    }
    @Override
    public void handleDeselection(DeselectionEvent event) {
        setUnit(event.getCursor().getFocusUnit());
    }
    
    private void setUnit(Unit unit) {
        
        if(unit != null)
        {
            statsPanel.setValues(unit.getStats());
            inventoryPanel.setValues(unit.getInventory());
            healthInfoPanel.setFaction(unit.getFaction());
            healthInfoPanel.setValues(unit.getName(), unit.getStats().getHP().get(), unit.getStats().getHP().getValT());
            unitInfoAnim.setVisible(true);
            unitInfoAnim.loadAnimation(unit.getMapAnim());
        }
        else 
        {
            clear();
        }
    }
    private void clear() {
        statsPanel.clear();
        inventoryPanel.clear();
        
        healthInfoPanel.setValues(" ", 0, 1);
        unitInfoAnim.setVisible(false);
    }
    
    @Override
    public void setTick(int tick) {
        unitInfoAnim.setTick(tick);
        unitInfoAnim.repaint();
    }
}
