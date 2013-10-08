/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Maps.Square;
import Maps.Terrain;
import Units.Unit;
import java.awt.image.BufferedImage;
import java.util.EventObject;

public class CursorMovementEvent extends EventObject {
    private Cursor cursor;
    private Square square;
    
    public CursorMovementEvent(Cursor cursor, Square square) {
        super(cursor);
        this.cursor = cursor;
        this.square = square;
    }
    
    public Cursor getCursor() {
        return cursor;
    }
    public Unit getUnit() {
        return square.getUnit();
    }
    public Terrain getTerrain() {
        return square.getTerrain();
    }
    public BufferedImage getTerrainIcon() {
        return square.getTerrainIcon();
    }
    
}