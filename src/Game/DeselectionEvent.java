/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import java.util.EventObject;

public class DeselectionEvent extends EventObject {
    
    public DeselectionEvent(Cursor cursor) {
        super(cursor);
    }
    
    public Cursor getCursor() {
        return (Cursor) source;
    }
    
}
