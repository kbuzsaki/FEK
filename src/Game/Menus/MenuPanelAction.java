/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Sound.SoundManager;
import java.awt.Dimension;

public class MenuPanelAction extends MenuPanel {
    private Cursor cursor; // The cursor (used to tell the cursor what action was taken)
    
    private MenuCommandAction menuEntries[] = MenuCommandAction.values();
    
    public MenuPanelAction(Dimension mapSize, Cursor cursor, SoundManager soundManager) {
        super(mapSize, soundManager, MenuCommandAction.getSpriteFilenames(MenuCommandAction.values()));
        this.cursor = cursor;
    }
    
    public void open() {
        menuEntries = MenuCommandAction.getValidCommands(cursor);
        reconstructMenu(MenuCommandAction.getSpriteFilenames(menuEntries));
        open(cursor.getX());
    }
    public void close() {
        super.close();
        cursor.showCursor();
        cursor.setControl(true);
    }
    public void cancel() {
        close();
        cursor.cancelActionMenu();
    }
    
    protected void performAction() {
        switch(menuEntries[index])
        {
            case ATTACK:
                if (cursor.actionAttack())
                {
//                    cursor.actionWait();
                }
                break;
            case ITEM: 
                if(cursor.actionItem())
                {
//                    cursor.actionWait();
                }
                break;
            case STAFF:
                if(cursor.actionStaff())
                {
//                    cursor.actionWait();
                }
                break;
            case WAIT:
                cursor.actionWait();
                break;
        }
    }
}
