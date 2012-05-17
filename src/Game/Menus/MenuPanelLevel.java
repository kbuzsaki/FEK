/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Level;
import Game.Level;
import Game.Sound.SoundManager;
import java.awt.Dimension;

public class MenuPanelLevel extends MenuPanel {
    private Level level; // The main game level (needed to end turn etc)
    
    private static final MenuCommandLevel menuEntries[] = MenuCommandLevel.values();
    
    public MenuPanelLevel(Dimension mapSize, Level level, SoundManager soundManager) {
        super(mapSize, soundManager, MenuCommandLevel.getSpriteFilenames(menuEntries));
        this.level = level;
    }
    
    public void cancel() {
        close();
    }
    
    protected void performAction() {
        switch(menuEntries[index])
        {
            case UNIT:
                break;
            case STATUS:
                break;
            case OPTIONS:
                level.openOptions();
                break;
            case SUSPEND:
                level.suspend();
                break;
            case END:
                level.endTurn();
                break;
                
        }
    }
}
