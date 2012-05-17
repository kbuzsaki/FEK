/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The "Board Square" class
 * This is a compact way of transfering data about a map square without having 
 * to transfer the map itself, or a series of arguments. In the future,
 * may take the map itself as an argument (without getter) in order to test for
 * relations between close-by squares
 */
package Maps;

import Units.Unit;
import java.awt.Point;

public class Square {
    private Point position;
    private Unit unit;
    private Terrain terrain;

    public Square(Point position, Unit unit, Terrain terrain) {
        this.position = position;
        this.unit = unit;
        this.terrain = terrain;
    }

    public Point getPosition() {
        return position;
    }
    public Terrain getTerrain() {
        return terrain;
    }
    public Unit getUnit() {
        return unit;
    }
    
    
}
