/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Units.Unit;
import java.util.Collection;

public class ThreadAnimation extends Thread{
    public static final int animationTickDelay = 90;
    
    private Level level;
    private int animationTick = 0;
    
    public ThreadAnimation(Level level) {
        this.level = level;
        
        setName("AnimationThread");
    }
    
    @Override
    public void run() {
        runAnimationLoop();
    }
    
    private void runAnimationLoop() {
        while(true)
        {
            updateAnimations();
            // repaint the screen to show the new animated state
            level.getMapScreen().repaint();
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
            unit.getMapAnim().setTick(animationTick);

        // ticks the dead unit animation opacities
        Collection<Unit> deadUnitList = (Collection<Unit>) level.getDeadUnits().clone();
        for (Unit deadUnit : deadUnitList)
            if(deadUnit.getMapAnim().tickOpacity())
                level.removeUnit(deadUnit);
    }
}
