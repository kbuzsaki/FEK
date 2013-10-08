/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import java.util.EventListener;

public interface UnitStateChangeListener extends EventListener {
    public void handleUnitStateChanged(UnitStateChangedEvent event);
}
