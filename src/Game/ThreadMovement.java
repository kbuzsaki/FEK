/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Maps.Map;
import Sprites.AnimUtils;
import Units.Unit;

public class ThreadMovement extends Thread {
    public static final int moveSpeed = 250;
    public static final int perPixMoveSpeed = moveSpeed / Map.tileS;
    
    private Level level;
    
    public ThreadMovement(Level level) {
        this.level = level;
        setName("Movement Thread");
    }
    
    public void run() {
        runMovementLoop();
    }
    
    private void runMovementLoop() {
        boolean unitsMustMove;
        
        do
        { 
            unitsMustMove = false;
            for (Unit unit : level.getUnitList())
                if (!unit.getPath().isEmpty())
                    if(moveUnit(unit))
                        unitsMustMove = true;
            
            try {
                sleep(perPixMoveSpeed);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } while (unitsMustMove); // runs until no more units must move
    }
    private boolean moveUnit (Unit unit) {
        
        AnimUtils.nudgeUnit(unit.getMapAnim(), unit.getPixelPoint(), unit.getNextTilePosition());
        AnimUtils.setAppropriateAnimation(unit, unit.getPixelPoint(), unit.getNextTilePosition());

        if (unit.getPixelPoint().equals(unit.getNextTilePosition()))
        {
            unit.getPath().remove(0);
        }
        if (unit.getPath().isEmpty())
        {
            unit.endMovement();
            if(unit == level.getCursor().getSelectedUnit())
            {
                level.getCursor().openActionMenu();
            }
            return false; // returns false: that the unit is done moving
        }
        return true; // returns true: that the unit must still move;
    }
    
}
