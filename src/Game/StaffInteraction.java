/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Sprites.AnimationEffect;
import Sprites.AnimationEffectFactory;
import Sprites.AnimationMapUnit;
import Sprites.CompletionListener;
import Units.Unit;
import java.util.ArrayList;
import java.util.Collection;

public class StaffInteraction {
    private Unit staffUser;
    private Unit target;
    
    private ArrayList<AnimationEffect> effectList = new ArrayList();
    private boolean hasCasted = false;
    private boolean hasExecuted = false;
    
    public StaffInteraction(Unit staffUser, Unit target) {
        this.staffUser = staffUser;
        this.target = target;
    }
    
    public Unit getStaffUser() {
        return staffUser;
    }
    public Unit getTarget() {
        return target;
    }
    
    public AnimationMapUnit getTargetAnimation() {
        return target.getMapAnim();
    }
    public String getTargetName() {
        return target.getName();
    }
    
    public String getStatName() {
        return staffUser.getEquipedStaff().getStatName(target);
    }
    public int getStatStartCurrent() {
        return staffUser.getEquipedStaff().getStatStartValue(target);
    }
    public int getStatStartTotal() {
        return staffUser.getEquipedStaff().getStatStartTotal(target);
    }
    public int getStatEndCurrent() {
        return staffUser.getEquipedStaff().getStatEndValue(target);
    }
    public int getStatEndTotal() {
        return staffUser.getEquipedStaff().getStatEndTotal(target);
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
    
    public Collection<AnimationEffect> getEffects() {
        return effectList;
    }
    
    public void execute(final CompletionListener listener) {
        staffUser.getEquipedStaff().performAction(target);
        hasExecuted = true;
        
        AnimationEffect effect = AnimationEffectFactory.newHealSmall();
        effect.setCenteredOn(target.getMapAnim().getBounds());
        effect.setCompletionListener(listener);
        effectList.add(effect);
    }
}
