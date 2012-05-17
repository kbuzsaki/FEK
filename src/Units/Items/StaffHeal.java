/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public class StaffHeal extends Staff {
    
    public StaffHeal() {
        super("Heal", 7, 0, "", 30, 30, 12, 1, Equipment.WEAPON_LEVEL_E,
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 100, null);
    }
    
    public String getStatName(Unit target) {
        return target.getStats().getHP().getName();
    }
    public int getStatStartValue(Unit target) {
        return target.getStats().getHP().get();
    }
    public int getStatStartTotal(Unit target) {
        return target.getStats().getHP().getValue();
    }
    public int getStatEndValue(Unit target) {
        if((target.getStats().getHP().get() + getHealthBonus()) > target.getStats().getHP().getValue())
        {
            return target.getStats().getHP().getValue();
        }
        else
        {
            return target.getStats().getHP().get() + getHealthBonus();
        }
    }
    public int getStatEndTotal(Unit target) {
        return target.getStats().getHP().getValue();
    }

    private int getHealthBonus() {
       int healBonus = 10;
//       healBonus += owner.getStats().getMAG();
       // extra processing for this goes here.
       return healBonus;
    }
    
    public void performAction(Unit target) {
        target.heal(getHealthBonus());
    }

}
