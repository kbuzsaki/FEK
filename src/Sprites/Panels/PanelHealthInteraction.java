/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Game.Game;
import Sprites.*;
import Units.Faction;
import Units.Unit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class PanelHealthInteraction extends JPanel implements Closeable {
    private static final int BAR_FILL_DELAY = 15;
    
    private PanelHealth healthPanel1;
    private PanelHealth healthPanel2;
    
    private Unit unit1;
    private Unit unit2;
    
    private boolean displayingUnit2;
    
    private MapScreen mapScreen;
    private Dimension mapSize;
    
    private static final int CLOSE_DELAY = 500; // time, in milliseconds, until the menu disappears after delayedClose().
    
    public PanelHealthInteraction(MapScreen mapScreen, Dimension mapSize) {
        this.mapScreen = mapScreen;
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
        displayingUnit2 = false;
    }
    
    public void setValues(Unit unit) {
        healthPanel1.setFaction(unit.getFaction());
        healthPanel1.setValues(unit.getName(), unit.getHP(), unit.getStats().getHP().getValT());
        unit1 = unit;
        
        remove(healthPanel2);
        displayingUnit2 = false;
        
        int previousXCenter = getX() + getWidth() / 2;
        setSize(healthPanel1.getSize());
        setPreferredSize(getSize());
        setLocation(previousXCenter - getWidth() / 2, getY());
    }
    public void setValues(Unit unit1, Unit unit2) {
        healthPanel1.setFaction(unit1.getFaction());
        healthPanel1.setValues(unit1.getName(), unit1.getHP(), unit1.getStats().getHP().getValT());
        this.unit1 = unit1;
        healthPanel2.setFaction(unit2.getFaction());
        healthPanel2.setValues(unit2.getName(), unit2.getHP(), unit2.getStats().getHP().getValT());
        this.unit2 = unit2;
        
        add(healthPanel2);
        displayingUnit2 = true;
        
        int previousXCenter = getX() + getWidth() / 2;
        setSize(healthPanel1.getWidth() + healthPanel2.getWidth(), healthPanel1.getHeight());
        setPreferredSize(getSize());
        setLocation(previousXCenter - getWidth() / 2, getY());
    }
    
    public void update(int barFillDelay) {
        Game.logDebug("HealthPanel Update Beginning");
        healthPanel1.setFutureValues(unit1.getHP());
        if(displayingUnit2)
        {
            healthPanel2.setFutureValues(unit2.getHP());
        }
        
        while(!(healthPanel1.tick() && healthPanel2.tick()))
        {
            mapScreen.requestRepaint();
            try
            {
                Thread.sleep(barFillDelay);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(PanelHealthInteraction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void update() {
        update(BAR_FILL_DELAY);
    }
    public void update(int barFillDelay, final CompletionListener listener) {
        Game.logDebug("HealthPanel Update Beginning");
        healthPanel1.setFutureValues(unit1.getHP());
        if(displayingUnit2)
        {
            healthPanel2.setFutureValues(unit2.getHP());
        }
        
        final PanelHealthInteraction thisPanel = this;
        
        Tickable updateAction = new Tickable() {
            @Override
            public boolean tick() {
                if (healthPanel1.tick() && healthPanel2.tick())
                {
                    mapScreen.repaint();
                    Game.logDebug("HealthPanel Update Complete");
                    if(listener != null)
                        listener.handleCompletion(new CompletionEvent(thisPanel));
                    return true;
                }
                else
                {
                    mapScreen.repaint();
                    return false;
                }
            }
        };
        
        ThreadTick ticker = new ThreadTick(updateAction, barFillDelay);
        ticker.start();
    }
    public void update(final CompletionListener listener) {
        update(BAR_FILL_DELAY, listener);
    }
    
    public void open(int y) {
        updatePosition(y);
        setVisible(true);
    }
    @Override
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
