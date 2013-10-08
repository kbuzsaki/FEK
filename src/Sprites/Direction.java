/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import java.awt.Point;
import java.util.ArrayList;


public enum Direction {
    
    // ordering of element declaration is depended upon in Sprites.PathCode.java
    NORTH,
    SOUTH,
    EAST,
    WEST;
    
    public Direction opposite() {
        switch(this)
        {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST: return WEST;
            case WEST: return EAST;
                
            default: return null; // should never be reached
        }
    }
    
    public static Direction getDirection(Point start, Point end) {
        if(start.y > end.y)
        {
            return NORTH;
        }
        else if(start.y < end.y)
        {
            return SOUTH;
        }
        else if(start.x > end.x)
        {
            return WEST;
        }
        else if(start.x < end.x)
        {
            return EAST;
        }
        else
        {
            return null; // happens if points have same coords
        }
    }
    public static ArrayList<Direction> getDirectionPath(ArrayList<Point> pointPath) {
        // directionPath will always be one shorter than pointPath
        ArrayList<Direction> directionPath = new ArrayList(pointPath.size() - 1);
        
        // for each point aside from the final point
        // get the direction from the point to the next point
        for (int i = 0; i < pointPath.size() - 1; i++)
        {
            directionPath.add(getDirection(pointPath.get(i), pointPath.get(i+1)));
        }
        
        return directionPath;
    }
}
