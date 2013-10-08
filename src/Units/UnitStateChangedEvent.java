/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import java.util.EventObject;

public class UnitStateChangedEvent extends EventObject {
    public UnitStateChangedEvent(Unit unit) {
        super(unit);
    }
    
    public Unit getUnit() {
        return (Unit) source;
    }
}
