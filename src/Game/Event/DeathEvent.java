/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Units.Unit;
import java.util.EventObject;

public class DeathEvent extends EventObject {
    
    public DeathEvent(Unit deadUnit) {
        super(deadUnit);
    }

    public Unit getSource() {
        return (Unit) super.getSource();
    }
}
