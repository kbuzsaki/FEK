/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Sound.SoundManager;
import Sprites.CompletionEvent;
import Sprites.CompletionListener;
import Sprites.Panels.PanelHealthInteraction;

public abstract class ThreadInteraction extends Thread implements CompletionListener{
    public static final int INITIAL_DELAY = 500;

    protected Level level;
    protected SoundManager soundManager;
    protected PanelHealthInteraction interactionHealthPanel;
    
    public ThreadInteraction(Level level, SoundManager soundManager, PanelHealthInteraction interactionHealthPanel) {
        this.level = level;
        this.soundManager = soundManager;
        this.interactionHealthPanel = interactionHealthPanel;
    }
    
    @Override
    public final void run() {
        beginInteraction();
        try {
            sleep(getInitialDelay());  // initial wait
        } catch (InterruptedException ex) {}
        processInteraction();
    }
    
    @Override
    public final void handleCompletion(CompletionEvent event) {
        if(!interactionIsFinished())
        {
            processInteraction();
        }
        else
        {
            endInteraction();
        }
    }
    
    protected int getInitialDelay() {
        return INITIAL_DELAY;
    }
    
    protected abstract void beginInteraction();
    protected abstract void endInteraction();
    protected abstract boolean interactionIsFinished();
    protected abstract void processInteraction();
    
}
