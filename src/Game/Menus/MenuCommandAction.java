/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Cursor;
import java.util.ArrayList;

public enum MenuCommandAction {
    ATTACK("Attack"),
    ITEM("Item"),
    STAFF("Staff"),
    WAIT("Wait");
    
    public final String name;
    
    MenuCommandAction(String name) {
        this.name = name;
    }
        
    public static MenuCommandAction[] getValidCommands(Cursor cursor) {
        ArrayList<MenuCommandAction> validCommands = new ArrayList();
        
        if(cursor.getSelectedUnit().getAttackablePoints().size() > 0)
            validCommands.add(ATTACK);
        
        if(cursor.getSelectedUnit().canItem())
            validCommands.add(ITEM);
        
        if(cursor.getSelectedUnit().getStaffPoints().size() > 0)
            validCommands.add(STAFF);
        
        validCommands.add(WAIT);
        
        return validCommands.toArray( new MenuCommandAction[validCommands.size()]);
    }
    public static String[] getSpriteFilenames( MenuCommandAction[] validCommands) {
        String[] spriteFilenames = new String[validCommands.length];
        for (int i = 0; i < validCommands.length; i++) 
        {
            spriteFilenames[i] = validCommands[i].name;
        }
        return spriteFilenames;
    }
}
