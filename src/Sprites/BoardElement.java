/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Maps.Map;
import java.awt.Point;
import java.util.ArrayList;
        
public abstract class BoardElement {
    
    protected Animation mapAnim;
    protected Point position;
    protected ArrayList<Point> path;
    
    public BoardElement(Animation mapAnim, int x, int y)
    {
        this.mapAnim = mapAnim;
        position = new Point(x, y);
        path = new ArrayList();
        synchMapAnim();
    }
    
    public Animation getMapAnim() {
        return mapAnim;
    }
    
    public final void synchMapAnim() {
        mapAnim.setLocation(position.x * Map.tileS - getOffsetX(), 
                            position.y * Map.tileS - getOffsetY());
    }
    public void setTick(int tick) {
        mapAnim.setTick(tick);
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
    
    public void setPosition(Point position) {
        this.position = new Point(position);
        //resetPosition(); // messes with animHandler
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
        return mapAnim.getX() + getOffsetX();
    }
    public int getPixelY() {
        return mapAnim.getY() + getOffsetY();
    }
    
    private int getOffsetX() {
        return mapAnim.getOffsetX();
    }
    private int getOffsetY() {
        return mapAnim.getOffsetY();
    }
    
}
