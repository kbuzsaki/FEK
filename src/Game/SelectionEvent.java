/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Units.Unit;
import java.util.EventObject;

public class SelectionEvent extends EventObject {
    
    public SelectionEvent(Unit selectedUnit) {
        super(selectedUnit);
    }
    
    public Unit getSelectedUnit() {
        return (Unit) source;
    }
    
}
