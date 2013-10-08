/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Event.MoveCompletionEvent;
import Game.Event.MoveEvent;
import java.util.EventListener;

public interface UnitMovementListener extends EventListener {
    
    public void handleMovementStart(MoveEvent event);
    public void handleMovementEnd(MoveEvent event);
    public void handleMovementCompletion(MoveCompletionEvent event);
    public void handleCancelledMovement(CancelledMovementEvent event);
    
}
