/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

public enum CommandLevel {
    UNIT("Unit"),
    STATUS("Status"),
    OPTIONS("Options"),
    QUIT("Quit"),
    END("End");
    
    public final String menuName;
    
    CommandLevel(String menuName) {
        this.menuName = menuName;
    }
    
    public static String[] getMenuNames( CommandLevel[] validCommands) {
        String[] spriteFilenames = new String[validCommands.length];
        for (int i = 0; i < validCommands.length; i++) 
        {
            spriteFilenames[i] = validCommands[i].menuName;
        }
        return spriteFilenames;
    }
}
