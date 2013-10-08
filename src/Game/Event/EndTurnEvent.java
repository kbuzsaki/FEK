/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Game.Level;
import java.util.EventObject;

public class EndTurnEvent extends EventObject {
    
    public EndTurnEvent(Level level) {
        super(level);
    }

    public Level getLevel() {
        return (Level) source;
    }
    public int getTurnCount() {
        return getLevel().getTurnCount();
    }
}
