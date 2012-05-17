/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

public enum Command {
    UP("Up"),
    DOWN("Down"),
    LEFT("Left"),
    RIGHT("Right"),
    A("A"),
    B("B"),
//    L,
//    R,
//    START,
    SELECT("Select");
    
    public final String name;
    
    Command(String name) {
        this.name = name;
    }
}
