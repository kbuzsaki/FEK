/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Maps;

import Sprites.ImageComponent;
import Units.Faction;
import Units.MoveType;
import Units.Unit;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Map {
    public static final int tileS = 16;
    
    private Terrain[][] terrainIndex;
    private Unit[][] unitIndex;
    public final ImageComponent image;
    private final String filename;
    public final int height; // height of the map in tiles
    public final int width; // width of the map in tiles

    public Map (String fileName) {
        int w = 0;
        int h = 0;
        image = new ImageComponent("resources/maps/" + fileName + ".png");
        image.setVisible(true);
        this.filename = "resources/maps/" + fileName + ".txt";
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.filename))){
            String line;
            
            while ((line = bufferedReader.readLine()) != null)
            {
                h++;
                if(line.length() > w)
                {
                    w = line.length();
                }
            }
        } catch (IOException ex) { System.out.println("failed to load map"); }
        
        width = w;
        height = h;
        terrainIndex = new Terrain[width][height];
        unitIndex = new Unit[width][height];
        loadMap(this.filename);
        
        String line;
        for (int y = 0; y < height; y++)
        {
            line = "";
            for (int x = 0; x < width; x++)
            {
                line += terrainIndex[x][y].C;
            }
        }
    }
    
    // map load functions
    private void loadMap(String filename) {

        try (BufferedReader mapReader = new BufferedReader(new FileReader(filename))) {

            int h = 0;
            String line;
            while ((line = mapReader.readLine()) != null) 
            {
                for (int w = 0; w < width; w++)
                {
                    char tileChar = line.charAt(w);
                    terrainIndex[w][h] = parseChar(tileChar);
                }
                h++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    private Terrain parseChar (char tileChar) {
        for (Terrain terrain : Terrain.values())
            if (terrain.C == tileChar)
                return terrain;
        
        return Terrain.PLAINS;
    }
    // map save functions
    private void saveMap (String filename) {
         try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            bufferedWriter.write(writeMap());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private String writeMap () {
        String map = "";
        for (int h = 0; h < terrainIndex[0].length; h++)
        {
            map += writeLine(h) + "\n";
        }
        return map;
    }
    private String writeLine (int h) {
        String line = "";
        for (int w = 0; w < terrainIndex.length; w++)
        {
            line += terrainIndex[w][h].C;
        }
        return line;
    }
    
    // load / assignment functions for units
    public void addUnit(Unit unit) {
        if(!unitAt(unit.getPosition()))
        {
            setUnitAt(unit.getPosition(), unit);
        }
        else
        {
            throw new IllegalArgumentException(
                    "Attempting to assign two units to the same space: (" 
                    + unit.getX() + ", " + unit.getY() + ")");
        }
    }
    public void removeUnit(Unit unit) {
        setUnitAt(unit.getPosition(), null);
    }
    public void moveUnit(Point startPosition, Point endPosition) {
        if(startPosition != endPosition)
        {
            if (!unitAt(endPosition)) // if the tile is not occupied
            {
                // move the unit
                setUnitAt(endPosition, getUnitAt(startPosition));
                setUnitAt(startPosition, null);
            }
            else // the target point is occupied by another unit
            {
                throw new IllegalArgumentException(
                        "Attempting to move to an occupied space: " 
                        + startPosition.toString() + " to " 
                        + endPosition.toString());
            }
        }
    }
    
    public int getMoveCostOf(Point position, MoveType moveT, Faction faction) {
        int cost = Terrain.UNCROSSABLE;
        
        if (getTerrainAt(position) != null) // if the terrain is in bounds
        {
            cost = getTerrainAt(position).getMoveCost(moveT); // get the move cost
        }
        
        if (getUnitAt(position) != null) // if there is a unit at the point
        {
            if(!getUnitAt(position).getFaction().isFriendlyTowards(faction)) // and it is not friendly
            {
                cost = Terrain.UNCROSSABLE; // it cannot be passed
            }
        }
        
        return cost;
    }
    
    private boolean isInBounds(int x, int y) {
        if ((x >= 0) &&(x < width)
          &&(y >= 0) &&(y < height))
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    public Square getSquareAt(int x, int y) {
        return new Square(new Point(x, y),
                          unitIndex[x][y],
                          terrainIndex[x][y],
                          getTerrainIconAt(x,y));
    }
    public Square getSquareAt(Point position) {
        return getSquareAt(position.x, position.y);
    }
    
    public Terrain getTerrainAt(int x, int y) {
        if (isInBounds(x, y))
        {
            return terrainIndex[x][y];
        }
        else 
        {
            return null;
        }
    }
    public Terrain getTerrainAt(Point position) {
        return getTerrainAt(position.x, position.y);
    }
    
    public BufferedImage getTerrainIconAt(int x, int y) {
        if(isInBounds(x, y))
        {
            return image.getImage().getSubimage(
                    x*tileS, 
                    y*tileS, 
                      tileS, 
                      tileS);
        }
        else
        {
            return null;
        }
    }
    public BufferedImage getTerrainIconAt(Point position) {
        return getTerrainIconAt(position.x, position.y);
    }
    
    /**
     * Returns true if there is a unit at the given coordinates.
     * @param x  the x coordinate, must be between 0 (inclusive) and width (exclusive)
     * @param y  the y coordinate, must be between 0 (inclusive) and height (exclusive)
     * @return true if there is a unit at the coordinates.
     */
    public boolean unitAt(int x, int y) {
        return (getUnitAt(x, y) != null);
    }
    /**
     * Returns true if there is a unit at the given point.
     * @param position  the point to check. 
     * x must be between 0 (inclusive) and width (exclusive)
     * y must be between 0 (inclusive) and height (exclusive)
     * @return true if there is a unit at the coordinates.
     */
    public boolean unitAt(Point position) {
        return unitAt(position.x, position.y);
    }
    public Unit getUnitAt(int x, int y) {
        if (isInBounds(x,y))
        {
            return unitIndex[x][y];
        }
        else 
        {
            return null;
        }
    }
    public Unit getUnitAt(Point position) {
        return getUnitAt(position.x, position.y);
    }
    private void setUnitAt(int x, int y, Unit unit) {
        if(isInBounds(x, y))
        {
            unitIndex[x][y] = unit;
        }
    }
    private void setUnitAt(Point position, Unit unit) {
        setUnitAt(position.x, position.y, unit);
    }
    public ArrayList<Unit> getUnitList() {
        ArrayList<Unit> unitList = new ArrayList();
        for (int row = 0; row < unitIndex.length; row++)
        {
            for (int col = 0; col < unitIndex[row].length; col++)
            {
                if(unitIndex[row][col] != null)
                {
                    unitList.add(unitIndex[row][col]);
                }
            }
        }
        return unitList;
    }
    public ArrayList<Unit> getUnitsAt(ArrayList<Point> positions) {
        ArrayList<Unit> unitsAt = new ArrayList();
        
        for (Point point : positions)
            if(getUnitAt(point) != null)
                unitsAt.add(getUnitAt(point));
        
        return unitsAt;
    }
    
}
