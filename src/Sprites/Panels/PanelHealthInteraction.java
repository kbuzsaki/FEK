/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.ThreadDelayedClose;
import Sprites.Tickable;
import Units.Faction;
import Units.Unit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Closeable;
import javax.swing.JPanel;

public class PanelHealthInteraction extends JPanel implements Closeable, Tickable{
    private PanelHealth healthPanel1;
    private PanelHealth healthPanel2;
    
    private Unit unit1;
    private Unit unit2;
    
    private Dimension mapSize;
    
    private static final int CLOSE_DELAY = 250; // time, in milliseconds, until the menu disappears after delayedClose().
    
    public PanelHealthInteraction(Dimension mapSize) {
        this.mapSize = mapSize;
        
        healthPanel1 = new PanelHealth();
        healthPanel2 = new PanelHealth();
        healthPanel2.setFaction(Faction.RED);
        
        add(healthPanel1);
        add(healthPanel2);
        
        setSize(healthPanel1.getWidth() + healthPanel2.getWidth(), healthPanel1.getHeight());
        setPreferredSize(getSize());
        
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER, -1, 0);
        setLayout(layout);
        setOpaque(false);
        setVisible(false);
        
        healthPanel1.setValues("Joe", 10, 20);
    }
    
    public void setValues(Unit unit) {
        healthPanel1.setFaction(unit.getFaction());
        healthPanel1.setValues(unit.getName(), unit.getHealth(), unit.getStats().getHP().getValue());
        unit1 = unit;
        
        remove(healthPanel2);
        
        int previousXCenter = getX() + getWidth() / 2;
        setSize(healthPanel1.getSize());
        setPreferredSize(getSize());
        setLocation(previousXCenter - getWidth() / 2, getY());
    }
    public void setValues(Unit unit1, Unit unit2) {
        healthPanel1.setFaction(unit1.getFaction());
        healthPanel1.setValues(unit1.getName(), unit1.getHealth(), unit1.getStats().getHP().getValue());
        this.unit1 = unit1;
        healthPanel2.setFaction(unit2.getFaction());
        healthPanel2.setValues(unit2.getName(), unit2.getHealth(), unit2.getStats().getHP().getValue());
        this.unit2 = unit2;
        
        add(healthPanel2);
        
        int previousXCenter = getX() + getWidth() / 2;
        setSize(healthPanel1.getWidth() + healthPanel2.getWidth(), healthPanel1.getHeight());
        setPreferredSize(getSize());
        setLocation(previousXCenter - getWidth() / 2, getY());
    }
    
    public void update() {
        healthPanel1.setFutureValues(unit1.getHealth());
        healthPanel2.setFutureValues(unit2.getHealth());
    }
    
    public boolean tick() {
        return (healthPanel1.tick() && healthPanel2.tick());
    }
    
    public void open(int y) {
        updatePosition(y);
        setVisible(true);
    }
    public void close() {
        setVisible(false);
    }
    public void delayedClose()  {
        ThreadDelayedClose threadDelayedClose = new ThreadDelayedClose(this, CLOSE_DELAY);
        threadDelayedClose.start();
    }
    
    private void updatePosition(int y) {
        if(y < mapSize.height/2) // if the battle is taking place above the midpoint
        {
            setLocation((mapSize.width - getWidth()) / 2, mapSize.height * 3/4 - getHeight()/2 );
        }
        else
        {
            setLocation((mapSize.width - getWidth()) / 2, mapSize.height * 1/4 - getHeight()/2);
        }
    }
}
