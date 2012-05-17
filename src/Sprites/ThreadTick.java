/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

public class ThreadTick extends Thread{
    Tickable tickable;
    int delay;
    boolean isFinished = false;
    
    public ThreadTick(Tickable tickable, int delay) {
        this.tickable = tickable;
        this.delay = delay;
        
        setName("TickThread");
    }
    
    public void run() {
        while(!tickable.tick())
        {
            try {
                sleep(delay);
            } catch (InterruptedException ex) {
                
            }
        }
        isFinished = true;
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
}
