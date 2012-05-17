/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Command;
import Game.Command;

public interface Menu {  
    
    /**
     * closes the menu, assumes cleanup has been done
     */
    public void close();
    /** 
     * Cancels the menu, has cleanup code
     */
    public abstract void cancel();
    
    /**
     * Returns true if the menu is currently open
     * @return the menu's state
     */
    public boolean isOpen();
    
    /**
     * Sets the tick for all animations associated with the menu
     * @param tick the tick set to all animations
     */
    public void setTick(int tick);
    /**
     * Handles input given by the cursor
     * @param button the command sent
     * @return true if the menu is finished taking commands
     */
    public boolean keyHandle(Command button);
    
    /**
     * Resets the menu's position relative to the cursor
     * @param x the x coordinate of the cursor
     */
    public void updatePosition(int x);

}
