/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Game.Game;
import Game.ThreadAnimation;
import Sprites.Panels.MapScreen;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnimationMapUnit extends Animation {
    
    private static final int defaultRowNum = 7;
    private static final int defaultColumnNum = 4;
    
    private static final int stand = 0;
    private static final int active = 1;
    private static final int moveNorth = 2;
    private static final int moveSouth = 3;
    private static final int moveWest = 4;
    private static final int moveEast = 5;
    private static final int depleted = 6;
    
    private static final int[][] sequence = { {0,0,1,2,2,2,2,1,0,0},
                                             {0,0,1,2,2,2,2,1,0,0},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,2,2,2,2,1,0,0}};
    
    private Tickable action;
    private boolean isDepleted = false;

    public AnimationMapUnit(String filepath, int[] colorTemplate) {
        super(filepath, colorTemplate, defaultRowNum, defaultColumnNum, sequence);
    }
    public AnimationMapUnit(String filepath, int[] colorTemplate, int rowNum, int columnNum) {
        super(filepath, colorTemplate, rowNum, columnNum, sequence);
    }
    
    public void loadAnimation(AnimationMapUnit animation) {
        super.loadAnimation(animation);
    }
        
    private void setAction(Tickable action, int delay) {
        if(this.action == null)
        {
            this.action = action;
            setIsPlaying(false);
            new ThreadTick(action, delay).run();
        }
    }
    private void clearAction() {
        action = null;
        setIsPlaying(true);
    }
    
    // Callback oriented movements
    public void actionFocus(final CompletionListener listener) {
        setTick(0);
        setFocus();
        
        setAction( new Tickable() {
            private int tick = 0;
            
            @Override
            public boolean tick() {
                if(getFrame() != 2) // if the frame is not at the raised frame
                {
                    tick++; // continue to increment the tick
                    forceTick(tick);
                    return false;
                }
                else // when the animation is complete
                { 
                    if(listener != null)
                        listener.handleCompletion(new CompletionEvent(this)); // notify the listener
                    clearAction();
                    return true;
                }
            }
        }, ThreadAnimation.animationTickDelay);
    }
    private void actionBump(final Direction direction, final int distance, final int delay, 
            final MapScreen mapScreen, final CompletionListener listener) {
        Game.logDebug("Beginning bump! : " + filepath);
        switch(direction) {
            case NORTH: setMoveNorth(); break;
            case SOUTH: setMoveSouth(); break;
            case EAST:  setMoveEast();  break;
            case WEST:  setMoveWest();  break;
        }
        
        setAction( new Tickable() {
            private int tick = 0;
            
            @Override
            public boolean tick() {
                tick++;
                
                if(tick < distance)
                {
                    move(direction);
                    forceTick(tick);
                }
                else if(tick < distance + delay)
                {
                    // do nothing (wait for delay to elapse)
                }
                else if(tick < (distance*2 + delay))
                {
                    move(direction.opposite());
                    forceTick(tick - (distance + delay));
                }
                else // the bump has been completed
                {
                    Game.logDebug("Bump complete! : " + filepath);
                    if(listener != null)
                        listener.handleCompletion(new CompletionEvent(this));
                    clearAction();
                    mapScreen.requestRepaint();
                    return true;
                }
                mapScreen.requestRepaint();
                
                return false;
            }
            
            private void move(Direction direction) {
                switch(direction)
                {
                    case NORTH:
                        translate(0, -1);
                        break;
                    case SOUTH:
                        translate(0, 1);
                        break;
                    case EAST: 
                        translate(1, 0);
                        break;
                    case WEST:
                        translate(-1, 0);
                        break;
                }
            }
            
        }, 20);
    }
    public void actionBump(final Direction direction, MapScreen mapScreen, final CompletionListener listener) {
//        setActionB(null, 50, listener);
        actionBump(direction, 4, 12, mapScreen, listener);
    }
    
    // Thread Blocking movements
    public void actionFocus() {
        setTick(0);
        setFocus();
        
        setAction( new Tickable() {
            private int tick = 0;
            
            @Override
            public boolean tick() {
                if(getFrame() != 2) // if the frame is not at the raised frame
                {
                    tick++; // continue to increment the tick
                    forceTick(tick);
                    return false;
                }
                else // when the animation is complete
                { 
                    clearAction();
                    return true;
                }
            }
        }, ThreadAnimation.animationTickDelay);
    }
    private void actionBump(final Direction direction, final int distance, final int delay, 
            final MapScreen mapScreen) {
        Game.logDebug("Beginning bump! : " + filepath);
        
        switch(direction) {
            case NORTH: setMoveNorth(); break;
            case SOUTH: setMoveSouth(); break;
            case EAST:  setMoveEast();  break;
            case WEST:  setMoveWest();  break;
        }
        
        setAction( new Tickable() {
            private int tick = 0;
            
            @Override
            public boolean tick() {
                boolean isFinished = false;
                
                if(tick < distance) {
                    move(direction);
                    forceTick(tick);
                }
                else if(tick >= (distance + delay) 
                     && tick < (distance*2 + delay)) {
                    move(direction.opposite());
                    forceTick(tick - (distance + delay));
                }
                else { // the bump has been completed
                    Game.logDebug("Bump complete! : " + filepath);
                    clearAction();
                    isFinished = true;
                }
                
                mapScreen.requestRepaint();
                tick++;
                return isFinished;
            }
            
            private void move(Direction direction) {
                switch(direction)
                {
                    case NORTH:
                        translate(0, -1);
                        break;
                    case SOUTH:
                        translate(0, 1);
                        break;
                    case EAST: 
                        translate(1, 0);
                        break;
                    case WEST:
                        translate(-1, 0);
                        break;
                }
            }
            
        }, 20);
    }
    public void actionBump(final Direction direction, MapScreen mapScreen) {
        actionBump(direction, 4, 12, mapScreen);
    }
    
    // REFACTOR: setDepleted animation handling is a bit overcomplicated
    public void setDepleted(boolean isDepleted) {
        this.isDepleted = isDepleted;
        if(isStanding())
        {
            setStand();
        }
    }
    private boolean isStanding() {
        return (animNum == stand || animNum == depleted);
    }
    
    @Override
    protected void setAnimation(int animNum) {
        super.setAnimation(animNum);
        // TODO: Is this setIsPlaying correct?
        setIsPlaying(true);
    }
    public void setStand() {
        if(!isDepleted)
        {
            setAnimation(stand);
        }
        else
        {
            setAnimation(depleted);
        }
    }
    public void setFocus() {
        setAnimation(active);
    }
    public void setMoveNorth() {
        setAnimation(moveNorth);
    }
    public void setMoveSouth() {
        setAnimation(moveSouth);
    }
    public void setMoveWest() {
        setAnimation(moveWest);
    }
    public void setMoveEast() {
        setAnimation(moveEast);
    }
    
}
