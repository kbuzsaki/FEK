/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The "tile" effects panel.
 * This panel displays tiles (such as movement and attack range indicators) 
 * This panel should be BELOW the units layer, but ABOVE the map layer.
 */
package Sprites.Panels;

import Game.CancelledMovementEvent;
import Game.DeselectionEvent;
import Game.Event.MoveCompletionEvent;
import Game.Event.MoveEvent;
import Game.SelectionEvent;
import Game.SelectionListener;
import Game.UnitMovementListener;
import Sprites.TileImage;
import java.awt.Point;
import java.util.ArrayList;

public class PanelEffectsTile extends PanelEffects implements SelectionListener {
    
    public PanelEffectsTile() {
    }
    
    @Override
    public void handleSelection(SelectionEvent event) {
        updateMoveGraphics(
                event.getSelectedUnit().getPointsInRange(), 
                event.getSelectedUnit().getAttackPointsInMoveRange(),
                event.getSelectedUnit().getStaffPointsInMoveRange());
    }
    @Override
    public void handleDeselection(DeselectionEvent event) {
        reset();
    }
    
    public void updateMoveGraphics(ArrayList<Point> moveTiles, 
            ArrayList<Point> attackTiles, ArrayList<Point> staffTiles) {
        staffTiles.removeAll(attackTiles);
        attackTiles.removeAll(moveTiles);
        ArrayList<TileImage> tileGraphicsList = new ArrayList();
        tileGraphicsList.addAll(getTileGraphics(moveTiles, "blue"));
        tileGraphicsList.addAll(getTileGraphics(attackTiles, "red"));
        tileGraphicsList.addAll(getTileGraphics(staffTiles, "green"));
        
        removeAll();
        
        for(TileImage tileGraphic : tileGraphicsList)
            add(tileGraphic);
        
        repaint();
    }
    
    private void updateTileGraphics(ArrayList<TileImage> tileGraphicsList) {
        removeAll();
        
        for(int i = 0; i < tileGraphicsList.size(); i++)
        {
            add(tileGraphicsList.get(i));
        }
        
        repaint();
    }
    
    public void updateInfluenceablePoints(ArrayList<Point> attackTiles, ArrayList<Point> staffTiles) {
        staffTiles.removeAll(attackTiles);
        ArrayList<TileImage> tileGraphicsList = new ArrayList();
        tileGraphicsList.addAll(getTileGraphics(attackTiles, "red"));
        tileGraphicsList.addAll(getTileGraphics(staffTiles, "green"));
        
        updateTileGraphics(tileGraphicsList);
    }
    public void updateAttackablePoints(ArrayList<Point> attackablePoints) {
        ArrayList<TileImage> tileGraphicsList = new ArrayList();
        tileGraphicsList.addAll(getTileGraphics(attackablePoints, "red"));
        
        updateTileGraphics(tileGraphicsList);
    }
    public void updateStaffPoints(ArrayList<Point> staffPoints) {
        ArrayList<TileImage> tileGraphicsList = new ArrayList();
        tileGraphicsList.addAll(getTileGraphics(staffPoints, "green"));
        
        updateTileGraphics(tileGraphicsList);
    }
    
    private ArrayList<TileImage> getTileGraphics(ArrayList<Point> tiles, String filename) {
        ArrayList<TileImage> tileGraphicsList = new ArrayList();
        
        for (Point tile : tiles)
            tileGraphicsList.add(new TileImage(filename, tile.x, tile.y));
        
        return tileGraphicsList;
    }
}
