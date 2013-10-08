/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import java.util.EventListener;

public interface CursorMovementListener extends EventListener {
    
    public void handleCursorMovement(CursorMovementEvent event);
    
}
