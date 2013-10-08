/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Command;
import Game.Cursor;
import Game.Level;
import Game.Sound.SoundManager;
import Sprites.Panels.GameScreen;
import Units.Items.Item;
import Units.Items.ItemFilter;
import Units.Unit;
import java.util.ArrayList;

public abstract class MenuSelection extends Menu implements ItemFilter {
    protected Level level;
    protected Cursor cursor;
    
    private ArrayList<Unit> selectableUnits;
    
    public MenuSelection(Level level, GameScreen gameScreen, 
            SoundManager soundManager, Cursor cursor) {
        super(gameScreen, soundManager);
        this.level = level;
        this.cursor = cursor;
        
        setPreferredSize(getSize());
    }
    
    @Override
    protected void updateIndex(int index ) {
        cursor.moveTo(selectableUnits.get(index).getPosition());
        cursor.showCursor();
        reconstructMenu(index);
    }
    
    @Override // unsupported, needs equipmentindex and selectable units
    final void openQuietly(Menu parentMenu, CancelListener cancelListener) {
        throw new UnsupportedOperationException("Cannot directly open selection menu");
    }
    protected void openQuietly(ArrayList<Unit> selectableUnits, CancelListener cancelListener) {
        // menu now assumes that the correct item has been equiped already
        
        this.selectableUnits = selectableUnits;
        setMaxIndex(selectableUnits.size());
        cursor.showCursor();
        cursor.getMapAnim().setSelect();
        
        super.openQuietly(null, cancelListener);
    }
    abstract void openQuietly(CancelListener cancelListener);
    
    @Override
    public void close() {
        super.close();
        cursor.hideCursor();
        level.getPanelEffectsTile().reset();
    }
    
    @Override
    public final boolean keyHandle(Command button) {
        switch(button)
        {
            case LEFT:
                soundManager.playSoundEffect(SoundManager.menuBlipLow);
                decrementIndex();
                return true;
            case RIGHT: 
                soundManager.playSoundEffect(SoundManager.menuBlipLow);
                incrementIndex();
                return true;
            default:
                return super.keyHandle(button);
        }
    }
    
    protected Unit getActor() {
        return cursor.getSelectedUnit();
    }
    protected Unit getTarget(int index) {
        return selectableUnits.get(index);
    }
    
    abstract void updateRanges(Item item);
    
    // FIXME: Magic numbers (make work for different menu sizes?)
    @Override
    protected void updatePosition() {
        // If the cursor is on the left half of the screen
        if (gameScreen.cursorIsOnLeft())
        {
            setLocation((gameScreen.getScreenSize().width * 51/52 - getWidth()), gameScreen.getScreenSize().height * 1/32);
        }
        else // Otherwise the cursor must be on the right half of the screen
        {
            setLocation((gameScreen.getScreenSize().width * 1/52), gameScreen.getScreenSize().height * 1/32);
        }
    }
    
}
