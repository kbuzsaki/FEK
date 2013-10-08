/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Units.Unit;
import java.util.EventObject;


public class MoveCompletionEvent extends EventObject {

    public MoveCompletionEvent(Unit completedUnit) {
        super(completedUnit);
    }
    
    public Unit getCompletedUnit() {
        return (Unit) source;
    }
}
