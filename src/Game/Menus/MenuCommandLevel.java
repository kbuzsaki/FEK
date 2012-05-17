/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

public enum MenuCommandLevel {
    UNIT("Unit"),
    STATUS("Status"),
    OPTIONS("Options"),
    SUSPEND("Suspend"),
    END("End");
    
    public final String name;
    
    MenuCommandLevel(String name) {
        this.name = name;
    }
    
    public static String[] getSpriteFilenames( MenuCommandLevel[] validCommands) {
        String[] spriteFilenames = new String[validCommands.length];
        for (int i = 0; i < validCommands.length; i++) 
        {
            spriteFilenames[i] = validCommands[i].name;
        }
        return spriteFilenames;
    }
}
