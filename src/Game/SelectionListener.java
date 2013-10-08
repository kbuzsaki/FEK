/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import java.util.EventListener;

public interface SelectionListener extends EventListener {
    
    public void handleSelection(SelectionEvent event);
    public void handleDeselection(DeselectionEvent event);
    
}
