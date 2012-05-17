/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The "arrow" effects panel.
 * This panel displays the movement indicator (the blue arrow).
 * This panel is ABOVE the units layer, but BELOW any menus.
 */
package Sprites.Panels;

import Sprites.PathCode;
import Sprites.TileImage;
import java.awt.Point;
import java.util.ArrayList;

public class PanelEffectsArrow extends PanelEffects{
    
    public PanelEffectsArrow(int width, int height) {
        super(width, height);
    }
    
    public void updateArrowPath(ArrayList<Point> path) {
        removeAll();
        
        ArrayList<TileImage> arrowPath = ArrowCalc(path);
        for(TileImage arrow : arrowPath)
            add(arrow);
        
        repaint();
    }
    
    private static final int Same = 0;
    private static final int North = 1;
    private static final int South = 2;
    private static final int West  = 3;
    private static final int East  = 4;

    private PathCode[][] PathCodeArray =
   {{null, PathCode.ArrowNorth, PathCode.ArrowSouth, PathCode.ArrowWest, PathCode.ArrowEast},
    {PathCode.ArrowSouth, null, PathCode.NorthSouth, PathCode.NorthWest, PathCode.NorthEast},
    {PathCode.ArrowNorth, PathCode.NorthSouth, null, PathCode.SouthWest, PathCode.SouthEast},
    {PathCode.ArrowEast, PathCode.NorthWest, PathCode.SouthWest, null, PathCode.EastWest},
    {PathCode.ArrowWest, PathCode.NorthEast, PathCode.SouthEast, PathCode.EastWest}, null};

    // YES, the path arraylist does contain the start point of the unit
    private ArrayList<TileImage> ArrowCalc(ArrayList<Point> path) {

        ArrayList<PathCode> PathCList = PathCCalc(path);
        ArrayList<TileImage> ArrowImageList = new ArrayList();
        
        // for every PCode and corresponding path point, creates a tile
        for (int i = 0; i < PathCList.size(); i++) {
            // path.get(i+1) to account for start point inclusion
            ArrowImageList.add(getTileImage(PathCList.get(i), path.get(i+1)));
        }

        return ArrowImageList;
    }

    private TileImage getTileImage(PathCode code, Point position) {
        String filepath = code.filepath;
        return new TileImage(filepath, position.x, position.y);
    }
    private ArrayList<PathCode> PathCCalc(ArrayList<Point> path) {
        ArrayList<PathCode> CodePath = new ArrayList();

            Point before;
            Point current;
            Point after;
        for ( int i = 1; i < path.size(); i++)
        {
            before = path.get(i-1);
            current = path.get(i);
            
            try {
                after = path.get(i+1);
            } catch (IndexOutOfBoundsException ex) {
                after = path.get(i);
            }

            int beforeDirection = getDirection(before, current);
            int afterDirection = getDirection(after, current);

            CodePath.add(
                    PathCodeArray[beforeDirection][afterDirection]);
        }

        return CodePath;
    }

    private int getDirection(Point origin, Point compared){
        if (isNorth(origin, compared)) {
            return North;
        }
        if (isSouth(origin, compared)) {
            return South;
        }
        if (isWest(origin, compared)) {
            return West;
        }
        if (isEast(origin, compared)) {
            return East;
        }
        else
        {
        return Same;
        }
    }
    private boolean isNorth (Point origin, Point compared) {
        if (origin.y < compared.y) 
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    private boolean isSouth (Point origin, Point compared) {
        if (origin.y > compared.y)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isWest  (Point origin, Point compared) {
        if (origin.x < compared.x)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isEast  (Point origin, Point compared) {
        if (origin.x > compared.x)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
