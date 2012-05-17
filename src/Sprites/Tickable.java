/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

public interface Tickable {
    
    /**
     * The tick method for the tickable object. 
     * Increments whatever the tickable object wishes to.
     * @return true if the tickable is complete
     */
    public boolean tick();
}
