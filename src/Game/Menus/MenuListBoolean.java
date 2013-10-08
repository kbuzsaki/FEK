/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Game;
import Game.Sound.SoundManager;
import Sprites.Panels.GameScreen;

public class MenuListBoolean extends MenuList {

    private static final String[] menuCommands = {"Yes", "No"};
    private BooleanListener booleanListener;
    
    public MenuListBoolean(GameScreen gameScreen, SoundManager soundManager) {
        super(gameScreen, soundManager, getMenuComponents(menuCommands));
    }
    
    @Override
    protected void reconstructMenu(int index) {
        reconstructMenu(getMenuComponents(menuCommands));
    }
    
    @Override
    void openQuietly(Menu parentMenu, CancelListener cancelListener) {
        throw new UnsupportedOperationException("Boolean Menu cannot be opened without BooleanListener");
    }
    void openQuietly(BooleanListener booleanListener, Menu parentMenu, CancelListener cancelListener) {
        this.booleanListener = booleanListener;
        super.openQuietly(parentMenu, cancelListener);
    }
    
    @Override
    protected void performAction(int index) {
        switch(menuCommands[index])
        {
            case "Yes":
                booleanListener.handleBoolean(true);
                break;
            case "No":
                booleanListener.handleBoolean(false);
                break;
            default:
                Game.logError("Boolean menu has an invalid selection.");
        }
        close();
    }
}
