/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Menus.CancelListener;
import Game.Sound.SoundManager;
import Maps.Map;
import Sprites.Animation;
import Sprites.AnimationMapCursor;
import Sprites.BoardElement;
import Sprites.Panels.PanelMenus;
import Units.Unit;
import java.awt.Point;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

public class Cursor extends BoardElement{
    // REFACTOR: remove Cursor's reference to Level?
    private Level level;
    private Map map;
    private SoundManager soundManager;
    
    private PanelMenus menusPanel;
    
    private AnimationMapCursor mapAnim;
    private EventListenerList listenerList;
    private boolean hasControl = true;
    private Unit focusUnit = null;
    private Unit selectedUnit = null;

    public Cursor(Level level, SoundManager soundManager, Map map, int x, int y) {
        super(x, y);
        this.level = level;
        this.soundManager = soundManager;
        this.map = map;
        
        this.mapAnim = new AnimationMapCursor(Animation.getFilename("cursor"));
        synchMapAnim();
        listenerList = new EventListenerList();
    }
    
    public void setMenusPanel(PanelMenus menusPanel) {
        this.menusPanel = menusPanel;
    }
    
    @Override
    public AnimationMapCursor getMapAnim() {
        return mapAnim;
    }
    public void addCursorMovementListener(CursorMovementListener listener) {
        listenerList.add(CursorMovementListener.class, listener);
    }
    public void addSelectionListener(SelectionListener listener) {
        listenerList.add(SelectionListener.class, listener);
    }
    
    /**
     * Handles input given by the cursor
     * @param button the command sent
     * @return true if the command was used
     */
    public boolean keyHandle(Command button) {
        if (hasControl)
        {
            switch(button)
            {
                case UP:
                    moveUp();
                    return true;
                case DOWN:
                    moveDown();
                    return true;
                case LEFT:
                    moveLeft();
                    return true;
                case RIGHT:
                    moveRight();
                    return true;
                case A:
                    if (hasSelectedUnit())
                    {
                        if(isControllable(getSelectedUnit()))
                        {
                            hideCursor();
                            level.moveUnit(selectedUnit);
                            openActionMenu();
                        }
                        else
                        {
                            cancelSelection();
                        }
                        
                        
                        return true;
                    }
                    else if (selectableUnitIsAt(position))
                    {
                        selectUnit();
                        return true;
                    }
                    break;
                case B:
                    if(hasSelectedUnit())
                    {
                        cancelSelection();
                        return true;
                    }
                    break;
                case SELECT:
                    if(hasSelectedUnit())
                        selectedUnit.getStats().levelUp();
                    else
                        Game.logInfo("No unit selected");
                    break;
            }
        }
        return false;
    }
    
    /**
     * Moves the cursor to the specified point on the level.getMap().
     * First checks if the point exists on the level.getMap(). If it does,
     * moves the cursor there. If it does not, does nothing.
     * @param x The x coordinate of the destination
     * @param y The y coordinate of the destination
     * @return true if move was successful, false if not.
     */
    private boolean move(int x, int y) {
        // Checks if the position is legal
        if (hasControl)
        {
            soundManager.playSoundEffect(SoundManager.cursorBlip);
            return moveTo(x, y);
        }
        return false;
    }
    public boolean moveTo(int x, int y) {
        if( (x >= 0)
              &&(x <= map.width-1)
              &&(y >= 0)
              &&(y <= map.height-1))
            {
                setPosition(x, y);
                updateFocus();
                if(hasSelectedUnit() && isControllable(getSelectedUnit()) && hasControl)
                {
                    selectedUnit.incrementPath(position);
                }
                
                for(CursorMovementListener listener : 
                    listenerList.getListeners(CursorMovementListener.class))
                {
                    listener.handleCursorMovement(new CursorMovementEvent(this, 
                            map.getSquareAt(position)));
                }
                
                return true;
            }
        return false;
    }
    public boolean moveTo(Point position) {
        return moveTo(position.x, position.y);
    }
    public void moveUp() {
        move(position.x, position.y - 1);
    }
    public void moveDown() {
        move(position.x, position.y + 1);
    }
    public void moveLeft() {
        move(position.x - 1, position.y);
    }
    public void moveRight() {
        move(position.x + 1, position.y);
    }
    
    public Unit getFocusUnit() {
        return focusUnit;
    }
    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public boolean selectableUnitIsAt(Point position) {
        if (map.getUnitAt(position) != null)
        {
            if ((!map.getUnitAt(position).isDepleted())
              /*&&(map.getUnitAt(position).getFaction() == level.getCurrentPhaseFaction())*/)
            {
                return true;
            }
        }
        return false;
    }
    public boolean isControllable(Unit unit) {
        return (!unit.isDepleted() && unit.getFaction() == level.getCurrentPhaseFaction());
    }
    public boolean hasSelectedUnit() {
        if (selectedUnit != null) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void setControl(boolean hasControl) {
        this.hasControl = hasControl;
    }
    public void hideCursor() {
        setControl(false);
        mapAnim.setVisible(false);
    }
    public void showCursor() {
        mapAnim.setVisible(true);
    }
    public void showCursor(boolean hasControl) {
        showCursor();
        setControl(hasControl);
    }
    
    public void updateFocus() {
        // If there's already a focus unit, defocus it
        if (focusUnit != null) {
            if (focusUnit != selectedUnit)
            {
                focusUnit.setDepleted(focusUnit.isDepleted());
            }
            focusUnit = null;
            mapAnim.setNormal();
        }
        
        // if there's a selectable unit at the current position and a unit isn't
        // already selected, then set the unit at the current position to be animated
        if ((selectableUnitIsAt(position)) && (!hasSelectedUnit()) 
                && isControllable(map.getUnitAt(position))) 
        {
            focusUnit = map.getUnitAt(position.x, position.y);
            focusUnit.getMapAnim().setFocus();
            mapAnim.setFocus();
        }
    }
    public void selectUnit() {
        if (hasSelectedUnit())
        {
            deselectUnit();
        }
        selectedUnit = map.getUnitAt(position.x, position.y);
        selectedUnit.select(isControllable(selectedUnit));
        
        soundManager.playSoundEffect(SoundManager.select);
        
        for(SelectionListener listener : listenerList.getListeners(SelectionListener.class))
        {
            listener.handleSelection(new SelectionEvent(selectedUnit));
        }
        
    }
    public void deselectUnit() {
        selectedUnit.deselect();
        selectedUnit = null;
        for(SelectionListener listener : listenerList.getListeners(SelectionListener.class))
        {
            listener.handleDeselection(new DeselectionEvent(this));
        }
    }
    private void cancelSelection() {
        soundManager.playSoundEffect(SoundManager.cancel);
        if(isControllable(selectedUnit))
        {
            moveTo(selectedUnit.getPosition());
        }
        deselectUnit();
        showCursor(true);
    }
    
    
    public void openActionMenu() {
        menusPanel.getActionMenu().open(new CancelListener() {
            @Override
            public void notifyCancel() {
                cancelAction();
            }
        });
        // TODO: action menu needs influenceable points
//        level.getPanelEffectsTile().updateInfluenceablePoints(
//                selectedUnit.getAttackPointsInRange(), 
//                selectedUnit.getStaffPointsInRange());
    }
    private void cancelAction() {
        soundManager.playSoundEffect(SoundManager.cancel);
        moveTo(selectedUnit.getPotentialPath().get(selectedUnit.getPotentialPath().size() - 1));
        selectedUnit.cancelMovement();
        showCursor(true);
    }
    
    public void actionWait() {
        // TODO: analagous method in unit "unit.wait" unit.endTurn send completionevent?
        moveTo(selectedUnit.getPosition());
        selectedUnit.endTurn();
        deselectUnit();
        showCursor(true);
    }
    public void endAction() {
        mapAnim.setNormal();
        actionWait();
        updateFocus();
    }

}
