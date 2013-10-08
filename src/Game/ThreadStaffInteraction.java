/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Sound.SoundManager;
import Sprites.AnimationEffect;
import Sprites.CompletionEvent;
import Sprites.CompletionNotifier;
import Sprites.Panels.PanelHealthInteraction;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadStaffInteraction extends ThreadInteraction {
    
    private StaffInteraction staffInteraction;
    
    public ThreadStaffInteraction(Level level, SoundManager soundManager, 
            PanelHealthInteraction interactionHealthPanel, StaffInteraction staffInteraction) {
        super(level, soundManager, interactionHealthPanel);
        this.staffInteraction = staffInteraction;
        
        setName("StaffInteraction: " + staffInteraction.getStaffUser().getName() 
                + " and " + staffInteraction.getTarget().getName());
    }
    
    @Override
    protected void beginInteraction() {
        interactionHealthPanel.setValues(staffInteraction.getTarget());
        interactionHealthPanel.open((staffInteraction.getStaffUser().getY() + staffInteraction.getTarget().getY()) / 2);
    }
    @Override
    protected void endInteraction() {
        interactionHealthPanel.delayedClose();
        level.getCursor().endAction();
    }
    @Override
    protected void processInteraction() {
        if(!staffInteraction.hasCasted())
        {
            staffInteraction.getStaffUser().getMapAnim().actionFocus(this);
            staffInteraction.setCasted();
        }
        else if(!staffInteraction.hasExecuted())
        {
            CompletionNotifier completionNotifier = new CompletionNotifier(this);
            
            staffInteraction.execute(completionNotifier);
            interactionHealthPanel.update(50, completionNotifier);
            
            completionNotifier.addNotifiers(staffInteraction.getEffects());
            completionNotifier.addNotifier(interactionHealthPanel);
            completionNotifier.beginListening();
            
            ExecutorService threadPool = Executors.newCachedThreadPool();
            
            for(final AnimationEffect effect : staffInteraction.getEffects())
            {
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        if(effect.getDelay() > 0)
                        {
                            try {
                                sleep(effect.getDelay());
                            } catch(InterruptedException ex) {
                                
                            }
                        }
                        
                        level.addAnimationEffect(effect);
                    }
                });
            }
            
        }
    }
    
    @Override
    protected boolean interactionIsFinished() {
        return staffInteraction.hasCasted() && staffInteraction.hasExecuted();
    }
    
}