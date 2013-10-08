/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Sound.SoundManager;
import Sprites.AnimationEffect;
import Sprites.CompletionNotifier;
import Sprites.Panels.PanelHealthInteraction;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadItemUseage extends ThreadInteraction {
    private ItemUseage itemUseage;
    
    public ThreadItemUseage(Level level, SoundManager soundManager, PanelHealthInteraction interactionHealthPanel, ItemUseage itemUseage) {
        super(level, soundManager, interactionHealthPanel);
        this.itemUseage = itemUseage;
    }
    
    @Override
    protected void beginInteraction() {
        itemUseage.getActor().getMapAnim().setMoveSouth();
        itemUseage.getActor().getMapAnim().setIsPlaying(false);
        
        interactionHealthPanel.setValues(itemUseage.getActor());
        interactionHealthPanel.open(itemUseage.getActor().getY());
    }
    @Override
    protected void endInteraction() {
        interactionHealthPanel.delayedClose();
        level.getCursor().endAction();
    }
    @Override
    protected void processInteraction() {
        if(!itemUseage.hasCasted())
        {
            itemUseage.getActor().getMapAnim().actionFocus(this);
            itemUseage.setCasted();
        }
        else if(!itemUseage.hasExecuted())
        {
            CompletionNotifier completionNotifier = new CompletionNotifier(this);
            
            itemUseage.execute(completionNotifier);
            interactionHealthPanel.update(50, completionNotifier);
            
            completionNotifier.addNotifiers(itemUseage.getEffects());
            completionNotifier.addNotifier(interactionHealthPanel);
            completionNotifier.beginListening();
            
            ExecutorService threadPool = Executors.newCachedThreadPool();
            
            for(final AnimationEffect effect : itemUseage.getEffects())
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
        return itemUseage.hasCasted() && itemUseage.hasExecuted();
    }
}
