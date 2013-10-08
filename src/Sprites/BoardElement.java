/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Maps.Map;
import java.awt.Point;
import java.util.ArrayList;
        
public abstract class BoardElement {
    
    protected Point position;
    protected ArrayList<Point> path;
    
    public BoardElement(int x, int y)
    {
        position = new Point(x, y);
        path = new ArrayList();
    }
    
    public abstract Animation getMapAnim();
    
    public final void synchMapAnim() {
        getMapAnim().setLocation(position.x * Map.tileS - getOffsetX(), 
                            position.y * Map.tileS - getOffsetY());
    }
    public void setTick(int tick) {
        getMapAnim().setTick(tick);
    }
    
    public ArrayList<Point> getPath() {
        return path;
    }
    public void setPath(ArrayList<Point> path) {
        this.path = path;
    }
    
    public Point getNextTilePosition() {
        return new Point(path.get(0).x*Map.tileS, path.get(0).y*Map.tileS);
    }
    public int getNextTileX() {
        return path.get(0).x*Map.tileS;
    }
    public int getNextTileY() {
        return path.get(0).y*Map.tileS;
    }
    
    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
        synchMapAnim();
    }
    public void setPosition(Point position) {
        setPosition(position.x, position.y);
    }
    public Point getPosition() {
        return position;
    }
    public int getX() {
        return position.x;
    }
    public int getY() {
        return position.y;
    }
    
    public Point getTilePosition() {
        return new Point(position.x*Map.tileS, position.y*Map.tileS);
    }
    public int getTileX() {
        return position.x*Map.tileS;
    }
    public int getTileY() {
        return position.y*Map.tileS;
    }
    
    public Point getPixelPoint() {
        return new Point(getPixelX(), getPixelY());
    }
    public int getPixelX() {
        return getMapAnim().getX() + getOffsetX();
    }
    public int getPixelY() {
        return getMapAnim().getY() + getOffsetY();
    }
    
    private int getOffsetX() {
        return getMapAnim().getOffsetX();
    }
    private int getOffsetY() {
        return getMapAnim().getOffsetY();
    }
    
}
