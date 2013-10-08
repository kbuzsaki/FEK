/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import java.io.Closeable;
import java.io.IOException;

public class ThreadDelayedClose extends Thread{
    private Closeable closeable;
    private int delay;
    
    public ThreadDelayedClose(Closeable closeable, int delay) {
        this.closeable = closeable;
        this.delay = delay;
        
        setName("ThreadDelayedClose: " + closeable.getClass().getName());
    }
    
    public void run() {
        try {
            sleep(delay);
        } catch (InterruptedException ex) {
        }
        
        try {
            closeable.close();
        } catch (IOException ex) {
        }
    }
}
