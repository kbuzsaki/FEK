/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import java.util.ArrayList;

public enum CommandAction {
    ATTACK("Attack"),
    STAFF("Staff"),
    ITEM("Item"),
    TRADE("Trade"),
    WAIT("Wait");
    
    public final String menuName;
    
    CommandAction(String menuName) {
        this.menuName = menuName;
    }
        
    private boolean shouldAddFor(Cursor cursor) {
        switch(this)
        {
            case ATTACK: return cursor.getSelectedUnit().getAttackablePoints().size() > 0;
            case STAFF: return cursor.getSelectedUnit().getStaffPoints().size() > 0;
            case ITEM: return cursor.getSelectedUnit().canItem();
            case TRADE: return cursor.getSelectedUnit().getTradePoints().size() > 0;
            case WAIT: return true;
        }
        return false;
    }
    
    public static CommandAction[] getValidCommands(Cursor cursor) {
        ArrayList<CommandAction> validCommands = new ArrayList();
        
        for(CommandAction command : CommandAction.values())
            if(command.shouldAddFor(cursor))
                validCommands.add(command);
        
        return validCommands.toArray( new CommandAction[validCommands.size()]);
    }
    public static String[] getMenuNames( CommandAction[] validCommands) {
        String[] spriteFilenames = new String[validCommands.length];
        for (int i = 0; i < validCommands.length; i++) 
        {
            spriteFilenames[i] = validCommands[i].menuName;
        }
        return spriteFilenames;
    }
}
