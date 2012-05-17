/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Game.Game;
import Game.Level;
import Units.Unit;

public class BasicEventListener extends LevelEventListener {
    
    public BasicEventListener(Level level) {
        super(level);
    }

    public void handleMoveEvent(MoveEvent event) {
        
    }
    public void handleDeathEvent(DeathEvent event) {
        boolean hasAllies = false;
        for (Unit unit : level.getUnitList())
        {
            if ((event.getSource().getFaction() == unit.getFaction())
                    && (unit != event.getSource()))
            {
                hasAllies = true;
                break;
            }
        }
        if(!hasAllies)
        {
            Game.log(event.getSource().getFaction().name + " has lost.");
        }
            
    }
    public void handleEndTurnEvent(EndTurnEvent event) {
        Game.log("Turn " + event.getSource().getTurnCount() + ": " 
                + event.getSource().getCurrentTurnFaction().name + "'s turn.");
    }
    
}
