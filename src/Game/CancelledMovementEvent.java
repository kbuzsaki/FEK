/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Units.Unit;
import java.util.EventObject;

public class CancelledMovementEvent extends EventObject {
    
    public CancelledMovementEvent(Unit cancelledUnit) {
        super(cancelledUnit);
    }
    
    public Unit getCancelledUnit() {
        return (Unit) source;
    }
    
}
