/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Sprites.AnimationMapUnit;
import Units.Unit;

public class StaffInteraction {
    private Unit staffUser;
    private Unit target;
    
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
    
    public void execute() {
        staffUser.getEquipedStaff().performAction(target);
    }
}
