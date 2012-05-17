/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Game.Level;

public abstract class LevelEventListener {
    Level level;
    
    public LevelEventListener(Level level) {
        this.level = level;
    }
    
    public abstract void handleMoveEvent(MoveEvent event);
    public abstract void handleDeathEvent(DeathEvent event);
    public abstract void handleEndTurnEvent(EndTurnEvent event);
        
}
