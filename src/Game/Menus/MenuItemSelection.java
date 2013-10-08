/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Sound.SoundManager;
import Sprites.Panels.GameScreen;
import Units.Items.Equipment;
import Units.Items.Item;

public class MenuItemSelection extends MenuItem {
    private MenuSelection selectionMenuToOpen;
    
    public MenuItemSelection(GameScreen gameScreen, SoundManager soundManager, 
            Cursor cursor, PanelInventoryInfo infoPanel) {
        super(gameScreen, soundManager, cursor, infoPanel);
    }

    @Override // unsupported
    void openQuietly(Menu parentMenu, CancelListener cancelListener) {
        throw new UnsupportedOperationException("Cannot open Item Selection Menu without selectionMenuToOpen");
    }
    void openQuietly(MenuSelection selectionMenuToOpen, CancelListener cancelListener) {
        this.selectionMenuToOpen = selectionMenuToOpen;
        super.openQuietly(null, cancelListener);
    }
    
    @Override
    protected void updateIndex(int index) {
        super.updateIndex(index);
        if(hasSelectionMenuToOpen())
            getSelectionMenuToOpen().updateRanges(getItemAt(index));
    }
    
    @Override
    public boolean isTargetable(Item item) {
        return isUseable(item);
    }
    @Override
    public boolean isUseable(Item item) {
        return getSelectionMenuToOpen().isUseable(item);
    }
    
    private boolean hasSelectionMenuToOpen() {
        if(selectionMenuToOpen != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private MenuSelection getSelectionMenuToOpen() {
        if(selectionMenuToOpen != null)
            return selectionMenuToOpen;
        else
        {
            System.err.println("Item Selection Menu has not been properly opened (missing a selection menu)");
            return null;
        }
    }
    
    @Override
    protected void performAction(int index) {
        getActor().getInventory().equip((Equipment)getItemAt(index));
        getSelectionMenuToOpen().openQuietly(this);
        close();
    }

}
