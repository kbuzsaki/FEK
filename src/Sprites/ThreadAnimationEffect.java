/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Game.Level;
import java.util.Collection;


public class ThreadAnimationEffect extends Thread {
    public static final int animationEffectDelay = 40;
    
    private Level level;
    private boolean isFinished = false;
    
    public ThreadAnimationEffect(Level level) {
        this.level = level;
        setName("AnimationEffectThread");
    }
    
    public void run() {
        runAnimationLoop();
    }
    
    private void runAnimationLoop() {
        boolean animEffectsNotDone;
        
        do
        { 
            animEffectsNotDone = false;
            Collection<AnimationEffect> animEffects = 
                    (Collection<AnimationEffect>)level.getAnimationEffects().clone();
            for (AnimationEffect animEffect : animEffects)
            {
                if (animEffect.tick())
                {
                    level.removeAnimationEffect(animEffect);
                }
                else
                {
                    animEffectsNotDone = true;
                }
            }
            
            try {
                sleep(animationEffectDelay);
            } catch (InterruptedException ex) {
            }
        } while (animEffectsNotDone); // runs until no more animation effects must animate
        
        isFinished = true;
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
}
