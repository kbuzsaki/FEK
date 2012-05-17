/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Units.Unit;
import java.util.EventObject;

public class MoveEvent extends EventObject {
    
    public MoveEvent(Unit movingUnit) {
        super(movingUnit);
    }
    
    public Unit getSource() {
        return (Unit) super.getSource();
    }
}
