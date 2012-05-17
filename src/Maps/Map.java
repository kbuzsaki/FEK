/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Maps;

import Game.Game;
import Sprites.BoardElement;
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
    private final String fileName;
    public final int height; // height of the map in tiles
    public final int width; // width of the map in tiles

    public Map (String fileName) {
        int w = 0;
        int h = 0;
        image = new ImageComponent("resources/maps/" + fileName + ".png");
        image.setVisible(true);
        this.fileName = "resources/maps/" + fileName + ".txt";
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.fileName));
            String line;
            
            while ((line = bufferedReader.readLine()) != null)
            {
                h++;
                if(line.length() > w)
                {
                    w = line.length();
                }
            }
        } catch (IOException ex) { System.out.println("failed to load map");}
        height = h;
        width = w;
        terrainIndex = new Terrain[width][height];
        unitIndex = new Unit[width][height];
        loadMap(this.fileName);
        
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
    private void loadMap(String fileName) {

        BufferedReader mapReader = null;
        try {
            
            mapReader = new BufferedReader(new FileReader(fileName));
            String line = null;

            int h = 0;
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
    public void saveMap (String fileName) {
        BufferedWriter bufferedWriter = null;

        try {

            //Construct the BufferedWriter object
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));

            bufferedWriter.write(writeMap());

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public String writeMap () {
        String map = "";
        for (int h = 0; h < terrainIndex[0].length; h++)
        {
            map += writeLine(h) + "\n";
        }
        return map;
    }
    public String writeLine (int h) {
        String line = "";
        for (int w = 0; w < terrainIndex.length; w++)
        {
            line += terrainIndex[w][h].C;
        }
        return line;
    }
    
    // load / assignment functions for units
    public boolean addUnit(Unit unit) {
        if(getUnitAt(unit.getPosition()) != null)
        {
            Game.log("Attempting to assign two units to the same place");
            return false;
        }
        else
        {
            unitIndex[unit.getX()][unit.getY()] = unit;
//            unit.setTerrain(getTerrainAt(unit.getPosition()));
            return true;
        }
    }
    public void removeUnit(Unit unit) {
        unitIndex[unit.getX()][unit.getY()] = null;
    }
    public boolean moveUnit(Point startPosition, Point endPosition) {
        if (unitIndex[endPosition.x][endPosition.y] == null) // if the tile is not occupied
        {
            // move the unit
            unitIndex[endPosition.x][endPosition.y] = unitIndex[startPosition.x][startPosition.y];
            unitIndex[startPosition.x][startPosition.y] = null;
            
            // the unit gets its terrain directly from the map (has map field)
//            unitIndex[endPosition.x][endPosition.y].setTerrain(getTerrainAt(endPosition));
            return true; // the unit can move
        }
        else if (startPosition == endPosition) // if the tile is the same tile as the start
        {
            return true; // the unit does not need to move
        }
        else // the target point is occupied by another unit
        {
            return false; // the unit cannot move
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
        if ((x >= 0) &&(x <= width)
          &&(y >= 0) &&(y <= height))
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
                          terrainIndex[x][y]);
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
