/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Menus.MenuSelectionStaff;
import Game.Menus.MenuPanelAction;
import Game.Menus.MenuSelectionAttack;
import Game.Sound.SoundManager;
import Sprites.AnimationMapCursor;
import Sprites.BoardElement;
import Units.Unit;
import java.awt.Point;

public class Cursor extends BoardElement{
    private AnimationMapCursor mapAnim;
    private Level level;
    private SoundManager soundManager;
    
    private MenuPanelAction actionMenu;
    private MenuSelectionAttack attackMenu;
    private MenuSelectionStaff staffMenu;

    private boolean hasControl = true;
    private Unit focusUnit = null;
    private Unit selectedUnit = null;

    public Cursor(Level level, SoundManager soundManager, int x, int y) {
        super(new AnimationMapCursor("cursor"), x, y);
        this.mapAnim = (AnimationMapCursor) super.mapAnim;
        this.level = level;
        this.soundManager = soundManager;
        
        actionMenu = new MenuPanelAction(level.getMap().image.getSize(), this, soundManager);
        attackMenu = new MenuSelectionAttack(level, level.getMap().image.getSize(), this);
        staffMenu  = new MenuSelectionStaff(level, level.getMap().image.getSize(), this);
    }
    
    public AnimationMapCursor getMapAnim() {
        return mapAnim;
    }
    
    /**
     * Handles input given by the cursor
     * @param button the command sent
     * @return true if the command was used
     */
    public boolean keyHandle(Command button) {
        if (actionMenu.isOpen())
        {
            if (actionMenu.keyHandle(button)) // if the actionMenu is finished
            {
                return true;
            }
        }
        else if (attackMenu.isOpen())
        {
            if (attackMenu.keyHandle(button)) //if the attackMenu is finished
            {
                return true;
            }
        }
        else if (staffMenu.isOpen())
        {
            if (staffMenu.keyHandle(button)) // if the staffMenu is finished
            {
                return true;
            }
        }
        else if (!hasControl)
        {
            return true;
        }
        else
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
                        moveUnit();
                        return true;
                    }
                    else if (selectableUnitIsAt(position))
                    {
                        selectUnit();
                        return true;
                    }
                    break;
                case B:
                    cancel();
                    return true;
                case SELECT:
                    try {
                        selectedUnit.getStats().levelUp();
                        level.getPanelInfoUnit().setUnit(selectedUnit);
                    } catch (NullPointerException ex) {
                        Game.log("No unit selected");
                    }
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
            if(moveTo(x, y))
            {
                if(hasSelectedUnit())
                {
                    incrementPath();
                }
                return true;
            }
        }
        return false;
    }
    public boolean moveTo(int x, int y) {
        if( (x >= 0)
              &&(x <= level.getMap().width-1)
              &&(y >= 0)
              &&(y <= level.getMap().height-1))
            {
                position.setLocation(x, y);
                synchMapAnim();
                
                updateFocus();
                
                if (actionMenu.isOpen())
                {
                    actionMenu.updatePosition(x);
                }
                if (attackMenu.isOpen())
                {
                    attackMenu.updatePosition(x);
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
    
    public MenuPanelAction getActionMenu() {
        return actionMenu;
    }
    public MenuSelectionAttack getAttackMenu() {
        return attackMenu;
    }
    public MenuSelectionStaff getStaffMenu() {
        return staffMenu;
    }
    public Unit getFocusUnit() {
        return focusUnit;
    }
    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public boolean selectableUnitIsAt(Point position) {
        if (level.getMap().getUnitAt(position) != null)
        {
            if ((!level.getMap().getUnitAt(position).isDepleted())
              &&(level.getMap().getUnitAt(position).getFaction() == level.getCurrentTurnFaction()))
            {
                return true;
            }
        }
        return false;
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
        setControl(true);
    }
    
    public void updateFocus() {
        if (focusUnit != null) {
            if (focusUnit != selectedUnit)
            {
                focusUnit.setDepleted(focusUnit.isDepleted());
            }
            focusUnit = null;
            mapAnim.setNormal();
        }
        
        level.getPanelInfoTerrain().setValues(level.getMap().getTerrainAt(position), 
                level.getMap().getTerrainIconAt(position));
        if(!hasSelectedUnit() || (level.getMap().getUnitAt(position) != null))
        {
            level.getPanelInfoUnit().setUnit(level.getMap().getUnitAt(position));
        }
        else
        {
            level.getPanelInfoUnit().setUnit(selectedUnit);
        }
        
        if ((selectableUnitIsAt(position)) && (!hasSelectedUnit())) 
        {
            focusUnit = level.getMap().getUnitAt(position.x, position.y);
            focusUnit.getMapAnim().setFocus();
            mapAnim.setFocus();
        }
    }
    public void selectUnit() {
        if (hasSelectedUnit())
        {
            deselectUnit();
        }
        selectedUnit = level.getMap().getUnitAt(position.x, position.y);
        selectedUnit.select();
        
        soundManager.playSoundEffect(SoundManager.select);
        level.getPanelEffectsTile().updateMoveGraphics(
                selectedUnit.getPointsInRange(), 
                selectedUnit.getAttackPointsInMoveRange(),
                selectedUnit.getStaffPointsInMoveRange());
    }
    public void deselectUnit() {
        selectedUnit.deselect();
        selectedUnit = null;
        level.getPanelEffectsTile().reset();
        level.getPanelEffectsArrow().reset();
    }
    private void cancel() {
        soundManager.playSoundEffect(SoundManager.cancel);
        if (hasSelectedUnit()) 
        {
            selectedUnit.cancel();
            setPosition(selectedUnit.getPosition());
            deselectUnit();
        }
        synchMapAnim();
        updateFocus();
        showCursor();
        setControl(true);
    }
    
    public void cancelActionMenu() {
        // TODO: make this the proper cancel and play the right sound
        cancel();
    }
    public void cancelTargetSelection() {
        // TODO: play the right cancel sound here
        hideCursor();
        openActionMenu();
    }
    
    private void incrementPath() {
        selectedUnit.incrementPath(position);
        level.getPanelEffectsArrow().updateArrowPath(selectedUnit.getPotentialPath());
    }
    private void moveUnit() {
        if (level.moveUnit(selectedUnit)) // if the unit begins movement successfully
        {
            hideCursor(); // hide the cursor
            level.getPanelEffectsArrow().reset(); // clear the effects
            level.getPanelEffectsTile().reset();
            
            //level.overlapReset();
            
        }
    }
    
    public void openActionMenu() {
        actionMenu.open();
        level.getPanelEffectsTile().updateInfluenceablePoints(
                selectedUnit.getAttackPointsInRange(), 
                selectedUnit.getStaffPointsInRange());
    }
    
    public boolean actionAttack() {
        Game.log(selectedUnit.getName() + " attacks!");
        
        level.getPanelEffectsTile().updateAttackablePoints(selectedUnit.getAttackablePoints());
        attackMenu.open(level.getMap().getUnitsAt(selectedUnit.getAttackablePoints()));
        return false;
    }
    public boolean actionStaff() {
        Game.log(selectedUnit.getName() + " uses a staff!");
        
        level.getPanelEffectsTile().updateStaffPoints(selectedUnit.getStaffPoints());
        staffMenu.open(level.getMap().getUnitsAt(selectedUnit.getStaffPoints()));
        return false;
    }
    public boolean actionItem() {
        Game.log(selectedUnit.getName() + " uses an item!");
        return true;
    }
    public void actionWait() {
        Game.log(selectedUnit.getName() + " waits!");
        selectedUnit.setDepleted(true);
        deselectUnit();
        showCursor(true);
    }
    public void endAction() {
        mapAnim.setNormal();
        actionWait();
        updateFocus();
    }
}
