/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Command;
import Game.Command;
import Game.Cursor;
import Game.Cursor;
import Game.Level;
import Game.Level;
import Maps.Map;
import Maps.Square;
import Sprites.BoardElement;
import Units.Unit;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;

public abstract class MenuSelection extends JPanel implements Menu{
    protected Level level;
    private Dimension mapSize;
    protected Cursor cursor;
    private ArrayList<Unit> selectableUnits;
    private boolean isOpen = false;
    private int index = 0;
    
    public MenuSelection(Level level, Dimension mapSize, Cursor cursor) {
        this.level = level;
        this.mapSize = mapSize;
        this.cursor = cursor;
        
        setLayout(null);
        setOpaque(false);
        setVisible(false);
        
        setPreferredSize(getSize());
    }
    
    public final boolean keyHandle(Command button) {
        switch(button)
        {
            case A:
                performAction();
                close();
                return true;
            case B:
                cancel();
                return true;
            case UP:
            case RIGHT:
                setIndex(index + 1);
                break;
            case DOWN:
            case LEFT:
                setIndex(index - 1);
                break;
                
        }
        return false;
    }
    private void setIndex(int index) {
        if (index < 0)
        {
            this.index = (selectableUnits.size() - 1);
        }
        else 
        {
            this.index = (index % selectableUnits.size());
        }
        cursor.moveTo(selectableUnits.get(this.index).getPosition());
        reconstructMenu();
    }
    
    public void open(ArrayList<Unit> selectableUnits) {
        updatePosition(cursor.getX());
        isOpen = true;
        setVisible(true);
        this.selectableUnits = selectableUnits;
        this.setIndex(0);
    }
    public void close() {
        isOpen = false;
        index = 0;
        setVisible(false);
        level.getPanelEffectsTile().reset();
    }
    public boolean isOpen() {
        return isOpen;
    }
    public void cancel() {
        close();
        cursor.cancelTargetSelection(); // TODO: make a less limiting cancel
    }
    
    protected Unit getTargetedUnit() {
        return selectableUnits.get(index);
    }
    protected Square getTargetedSquare() {
        return level.getMap().getSquareAt(cursor.getPosition());
    }
    
    // FIXME: Magic numbers (make work for different menu sizes?)
    public void updatePosition(int x) {
        // If the cursor is on the left half of the screen
        if (x + 1 < (mapSize.width / Map.tileS) / 2)
        {
            setLocation((mapSize.width * 51/52 - getWidth()), mapSize.height * 1/32);
        }
        else // Otherwise the cursor must be on the right half of the screen
        {
            setLocation((mapSize.width * 1/52), mapSize.height * 1/32);
        }
    }
    
    protected abstract void reconstructMenu();
    /**
     * sets the tick for all animations associated with the menu
     * @param tick the tick set
     */
    public abstract void setTick(int tick);
    protected abstract void performAction();
}
