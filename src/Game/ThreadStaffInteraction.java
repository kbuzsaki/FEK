/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Sound.SoundManager;
import Sprites.ThreadTick;

public class ThreadStaffInteraction extends Thread {
    private static final int INITIAL_DELAY = 500;
    private static final int BATTLE_DELAY = 20;
    private static final int BAR_FILL_DELAY = 10;
    
    private Level level;
    private SoundManager soundManager;
    private StaffInteraction staffInteraction;
    
    private ThreadTick tickThread;
    private boolean isFinished = false;
    
    public ThreadStaffInteraction(Level level, SoundManager soundManager, StaffInteraction staffInteraction) {
        
    }
    
    public void run() {
        // TODO: staff interaction thread graphics
        try {
                sleep(INITIAL_DELAY);
        } catch (InterruptedException ex) {}
        
        runStaffInteractionLoop();
    }
    
    private void runStaffInteractionLoop() {
        while(!isFinished) 
        {
            if((tickThread == null) || tickThread.isFinished())
            {
                processStaffInteraction();
            }
            
            try {
                sleep(BATTLE_DELAY);
            } catch (InterruptedException ex) {}
        }
    }
    
    private void processStaffInteraction() {
        
    }
}