/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Sprites.AnimationEffect;
import Units.Unit;
import java.util.Collection;

public class ThreadAnimation extends Thread{
    private static int animationTickDelay = 90;
    
    private Level level;
    private int animationTick;
    
    public ThreadAnimation(Level level) {
        this.level = level;
        
        setName("AnimationThread");
    }
    
    public void run() {
        runAnimationLoop();
    }
    
    private void runAnimationLoop() {
        while(true)
        {
            updateAnimations();
            
            try {
                sleep(animationTickDelay);
            } catch ( InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void updateAnimations() {
        animationTick++; // increments the tick
        
        level.getCursor().setTick(animationTick); // sets the tick for the cursor animation
            level.setTick(animationTick); // sets the tick for the level's menus

        // set the tick for the unit animations
        for(Unit unit : level.getUnitList())
            if (unit.getMapAnim().isPlaying())
                unit.getMapAnim().setTick(animationTick);

        // ticks the dead unit animation opacities
        Collection<Unit> deadUnitList = (Collection<Unit>) level.getDeadUnits().clone();
        for (Unit deadUnit : deadUnitList)
            if(deadUnit.getMapAnim().tickOpacity())
                level.removeUnit(deadUnit);

        // ticks the temporary animations
        Collection<AnimationEffect> effectsList = (Collection<AnimationEffect>) level.getAnimationEffects().clone();
        for (AnimationEffect animationEffect : effectsList)
            if(animationEffect.tick())
                level.removeAnimationEffect(animationEffect);
    }
}
