/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The "arrow" effects panel.
 * This panel displays the movement indicator (the blue arrow).
 * This panel is ABOVE the units layer, but BELOW any menus.
 */
package Sprites.Panels;

import Game.CursorMovementEvent;
import Game.CursorMovementListener;
import Game.DeselectionEvent;
import Game.SelectionEvent;
import Game.SelectionListener;
import Sprites.Direction;
import Sprites.PathCode;
import Sprites.TileImage;
import java.awt.Point;
import java.util.ArrayList;

public class PanelEffectsArrow extends PanelEffects implements CursorMovementListener, SelectionListener {
    
    public PanelEffectsArrow() {
    }
    
    @Override
    public void handleCursorMovement(CursorMovementEvent event) {
        if(event.getCursor().hasSelectedUnit())
        {
            updateArrowPath(event.getCursor().getSelectedUnit().getPotentialPath());
        }
    }
    
    @Override
    public void handleSelection(SelectionEvent event) {
        // do nothing
    }
    @Override
    public void handleDeselection(DeselectionEvent event) {
        reset();
    }
    
    private void updateArrowPath(ArrayList<Point> path) {
        removeAll();
        
        ArrayList<TileImage> arrowPath = arrowCalc(path);
        for(TileImage arrow : arrowPath)
            add(arrow);
        
        repaint();
    }
    private ArrayList<TileImage> arrowCalc(ArrayList<Point> path) {

        ArrayList<PathCode> PathCList = 
                PathCode.getPathCodePath(Direction.getDirectionPath(path));
        ArrayList<TileImage> ArrowImageList = new ArrayList();
        
        // for every PCode and corresponding path point, creates a tile
        for (int i = 0; i < PathCList.size(); i++) {
            // path.get(i+1) to account for start point inclusion
            ArrowImageList.add(PathCList.get(i).getTileImage(path.get(i+1)));
        }

        return ArrowImageList;
    }

}
