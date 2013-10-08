/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Level;
import Game.Sound.SoundManager;
import Sprites.Panels.GameScreen;

public class MenuListLevel extends MenuList {
    
    private Level level; // The main game level (needed to end turn etc)
    
    private static final CommandLevel menuCommands[] = CommandLevel.values();
    
    public MenuListLevel(Level level, GameScreen gameScreen, SoundManager soundManager) {
        super(gameScreen, soundManager,
                getMenuComponents(CommandLevel.getMenuNames(menuCommands)));
        this.level = level;
    }
    
    @Override
    protected void reconstructMenu(int index) {
        this.reconstructMenu(getMenuComponents(CommandLevel.getMenuNames(menuCommands)));
    }
    
    @Override
    protected void performAction(int index) {
        switch(menuCommands[index])
        {
            case UNIT:
                break;
            case STATUS:
                break;
            case OPTIONS:
                level.openOptions();
                break;
            case QUIT:
                level.suspend();
                break;
            case END:
                level.endPhase();
                break;
                
        }
    }

}
