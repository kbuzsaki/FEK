/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Command;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class KeyMapper implements Serializable {
    private HashMap<Integer, Command> keyMap; // the array that maps the keys to commands
    private EnumMap<Command, ArrayList<Integer>> commandMap; // the hashmap that maps the commands to the list of keys
    
    public KeyMapper() {
        keyMap = new HashMap();
        commandMap = new EnumMap(Command.class);
        // for each command, make a list of keys bound to it
        for (Command command : Command.values())
            commandMap.put(command, new ArrayList());
        
        resetDefaultBindings();
    }
    
    public Command getCommand(KeyEvent event) {
        return keyMap.get(event.getKeyCode());
    }
    public ArrayList<Integer> getKeys(Command command) {
        return commandMap.get(command);
    }
    
    public void bindKey(Command command, int keyCode) {
        clearKey(keyCode); // ensure that the key isn't bound to anything else
        keyMap.put(keyCode, command); // map the key to the command
        commandMap.get(command).add(keyCode); // add the key to list of keys mapped to that command
    }
    public void clearKey(int keyCode) {
        // for each command that 
        for (Command command : Command.values())
            commandMap.get(command).remove((Integer) keyCode);
    }
    
    public void clearCommand(Command command) {
        for(Integer key : commandMap.get(command)) // for each key bound to the command
        {
            keyMap.remove(key); // remove that binding
        }
        commandMap.get(command).clear(); // clear the list of keys bound to the 
    }
    
    public void resetDefaultBindings() {
        keyMap.clear();
        for (Command command : Command.values())
            commandMap.get(command).clear();
        
        bindKey(Command.UP, KeyEvent.VK_UP);
        bindKey(Command.DOWN, KeyEvent.VK_DOWN);
        bindKey(Command.LEFT, KeyEvent.VK_LEFT);
        bindKey(Command.RIGHT, KeyEvent.VK_RIGHT);
        
        bindKey(Command.A, KeyEvent.VK_ENTER);
        bindKey(Command.B, KeyEvent.VK_SPACE);
        
        bindKey(Command.UP, KeyEvent.VK_W);
        bindKey(Command.DOWN, KeyEvent.VK_S);
        bindKey(Command.LEFT, KeyEvent.VK_A);
        bindKey(Command.RIGHT, KeyEvent.VK_D);
        
        bindKey(Command.A, KeyEvent.VK_Z);
        bindKey(Command.B, KeyEvent.VK_X);
    }
}
