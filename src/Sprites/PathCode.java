/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

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
    public TileImage getTileImage(Point position) {
        return new TileImage(filepath, position.x, position.y);
    }
    
    private static final Map<Direction, EnumMap<Direction, PathCode>> LOOKUP;
    // initialization of LOOKUP:
    static {
        EnumMap<Direction, PathCode> northLookup = new EnumMap<>(Direction.class);
        northLookup.put(Direction.SOUTH, NorthSouth);
        northLookup.put(Direction.EAST, NorthEast);
        northLookup.put(Direction.WEST, NorthWest);
        
        EnumMap<Direction, PathCode> southLookup = new EnumMap<>(Direction.class);
        southLookup.put(Direction.NORTH, NorthSouth);
        southLookup.put(Direction.EAST, SouthEast);
        southLookup.put(Direction.WEST, SouthWest);
        
        EnumMap<Direction, PathCode> eastLookup = new EnumMap<>(Direction.class);
        eastLookup.put(Direction.NORTH, NorthEast);
        eastLookup.put(Direction.SOUTH, SouthEast);
        eastLookup.put(Direction.WEST, EastWest);
        
        EnumMap<Direction, PathCode> westLookup = new EnumMap<>(Direction.class);
        westLookup.put(Direction.NORTH, NorthWest);
        westLookup.put(Direction.SOUTH, SouthWest);
        westLookup.put(Direction.EAST, EastWest);
        
        EnumMap<Direction, EnumMap<Direction, PathCode>> tempLookup = new EnumMap<>(Direction.class);
        tempLookup.put(Direction.NORTH, northLookup);
        tempLookup.put(Direction.SOUTH, southLookup);
        tempLookup.put(Direction.EAST, eastLookup);
        tempLookup.put(Direction.WEST, westLookup);
        
        LOOKUP = Collections.unmodifiableMap(tempLookup);
    }
    
    public static PathCode getPathCode(Direction directionTo, Direction directionFrom) {
        return LOOKUP.get(directionTo.opposite()).get(directionFrom);
    }
    
    public static PathCode getArrowOf(Direction directionFrom) {
        switch(directionFrom)
        {
            case NORTH: return ArrowNorth;
            case SOUTH: return ArrowSouth;
            case EAST: return ArrowEast;
            case WEST: return ArrowWest;
                
            default: return null;
        }
    }
    
    public static ArrayList<PathCode> getPathCodePath(ArrayList<Direction> directionPath) {
        ArrayList<PathCode> pathCodePath = new ArrayList(directionPath.size());
        
        if(!directionPath.isEmpty())
        {
            for(int i = 0; i < directionPath.size() - 1; i++)
            {
                pathCodePath.add(getPathCode(directionPath.get(i), directionPath.get(i+1)));
            }
            pathCodePath.add(getArrowOf(directionPath.get(directionPath.size() - 1)));
        }

        return pathCodePath;
    }
}
