/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Sprites.Animateable;

public interface GamePanel extends Animateable {
    // locations for the menu image resources
    public static final String RESOURCES_DIR = "resources/";
    public static final String GUI_DIR = RESOURCES_DIR + "gui/";
    public static final String GUI_WINDOW_DIR = GUI_DIR + "window/";
    public static final BackgroundPanelFactory panelFactory = new BackgroundPanelFactory();
    
    /**
     * closes the panel, assumes cleanup has been done or is not needed
     */
    public void close();
    
    /**
     * Returns true if the panel is currently open
     * @return the panel's state
     */
    public boolean isOpen();

}
