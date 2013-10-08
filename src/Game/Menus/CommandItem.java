/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.UseableItem;
import Units.Unit;
import java.util.ArrayList;

public enum CommandItem {
    USE("Use"),
    EQUIP("Equip"),
    TRADE("Trade"),
    DISCARD("Discard");
    
    public final String menuName;
    
    CommandItem(String menuName) {
        this.menuName = menuName;
    }
    
    private boolean shouldAddFor(Unit unit, Item item) {
        switch(this)
        {
            case USE: return item instanceof UseableItem && unit.canUse((UseableItem)item);
            case EQUIP: return item instanceof Equipment && ((Equipment)item).isEquipable();
            case TRADE: return false;
            case DISCARD: return item.isDiscardable();
        }
        return false;
    }
    
    public static CommandItem[] getValidCommands(Unit unit, Item item) {
        ArrayList<CommandItem> validCommands = new ArrayList();
        
        for(CommandItem command : CommandItem.values())
            if(command.shouldAddFor(unit, item))
                validCommands.add(command);
        
        return validCommands.toArray( new CommandItem[validCommands.size()]);
    }
    public static String[] getMenuNames( CommandItem[] validCommands) {
        String[] spriteFilenames = new String[validCommands.length];
        for (int i = 0; i < validCommands.length; i++) 
        {
            spriteFilenames[i] = validCommands[i].menuName;
        }
        return spriteFilenames;
    }
}
