/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Maps.Map;
import java.awt.Point;

public class TileImage extends ImageComponent {
    public static final int tileS = Map.tileS;
    
    private Point position = new Point();

    public TileImage(String filepath, int x, int y) {
        super("resources/tiles/" + filepath + ".png");
        position = new Point(x, y);
        resetPosition();
    }
    public TileImage(String filepath) {
        this(filepath, 0, 0);
    }
    
    public final void resetPosition() {
        setLocation(position.x*tileS, position.y*tileS);
    }
}
