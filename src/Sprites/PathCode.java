/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

public enum PathCode {
    NorthSouth("NorthSouth"),
    EastWest("EastWest"),
    NorthWest("NorthWest"),
    NorthEast("NorthEast"),
    SouthWest("SouthWest"),
    SouthEast("SouthEast"),
    
    ArrowNorth("ArrowNorth"),
    ArrowSouth("ArrowSouth"),
    ArrowWest("ArrowWest"),
    ArrowEast("ArrowEast");

    public final String filepath;

    PathCode(String filepath) {
        this.filepath = filepath;
    }

}
