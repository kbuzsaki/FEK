/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Effects;

import Units.Unit;

public abstract class Effect {
    private Unit target;
    private int duration;
    
    public abstract void activate();
    
    protected void tickDuration() {
        duration--;
        
        if(duration <= 0)
        {
            target.clearEffect(this);
        }
    }
    
}
