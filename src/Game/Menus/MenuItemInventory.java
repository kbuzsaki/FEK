/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Sound.SoundManager;
import Sprites.CompletionEvent;
import Sprites.CompletionListener;
import Sprites.Panels.GameScreen;
import Units.Items.Equipment;
import Units.Items.Item;


public class MenuItemInventory extends MenuItem implements CompletionListener {
    private MenuListItem itemMenu;
    
    public MenuItemInventory (GameScreen gameScreen, SoundManager soundManager, 
            Cursor cursor, PanelInventoryInfo infoPanel, MenuListItem itemMenu) {
        super(gameScreen, soundManager, cursor, infoPanel);
        this.itemMenu = itemMenu;
    }
    
    @Override
    public boolean isTargetable(Item item) {
        return true;
    }
    @Override
    public boolean isUseable(Item item) {
        if(item instanceof Equipment)
            return ((Equipment)item).isEquipable();
        else
            return true;
    }
    
    @Override
    public void handleCompletion(CompletionEvent event) {
        setAcceptingCommands(true);
        resetMenu();
    }
    
    @Override
    protected boolean shouldCloseOnAction(int index) {
        return false;
    }
    
    @Override
    protected void performAction(int index) {
        setAcceptingCommands(false);
        itemMenu.openQuietly(getActor(), getItemAt(index), this, this, this);
    }

}
