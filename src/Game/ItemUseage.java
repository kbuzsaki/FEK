/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Sprites.AnimationEffect;
import Sprites.AnimationEffectFactory;
import Sprites.CompletionListener;
import Units.Items.UseableItem;
import Units.Unit;
import java.util.ArrayList;
import java.util.Collection;

public class ItemUseage {
    private Unit actor;
    private UseableItem usedItem;
    
    private ArrayList<AnimationEffect> effectList = new ArrayList();
    private boolean hasCasted = false;
    private boolean hasExecuted = false;
    
    public ItemUseage(Unit actor, UseableItem usedItem) {
        this.actor = actor;
        this.usedItem = usedItem;
    }
    
    public Unit getActor() {
        return actor;
    }
    
    public boolean hasCasted() {
        return hasCasted;
    }
    public void setCasted() {
        hasCasted = true;
    }
    
    public boolean hasExecuted() {
        return hasExecuted;
    }
    
    public ArrayList<AnimationEffect> getEffects() {
        return effectList;
    }
    
    public void execute(final CompletionListener listener) {
        usedItem.use();
        hasExecuted = true;
        
        AnimationEffect effect = AnimationEffectFactory.newHealSmall();
        effect.setCenteredOn(actor.getMapAnim().getBounds());
        effect.setCompletionListener(listener);
        effectList.add(effect);
    }
}
