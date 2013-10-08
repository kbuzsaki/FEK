/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public class StaffHeal extends Staff {
    
    public StaffHeal(String name, int spriteX, String description, int uses, 
            int maxUses, int price, int weaponXP, int weaponLevel, int rangeMin, 
            int rangeMax, int hit) {
        super(name, spriteX, description, uses, maxUses, price, weaponXP, weaponLevel,
                rangeMin, rangeMax, hit, null);
    }
    
    @Override
    public String getStatName(Unit target) {
        return target.getStats().getHP().getName();
    }
    @Override
    public int getStatStartValue(Unit target) {
        return target.getStats().getHP().get();
    }
    @Override
    public int getStatStartTotal(Unit target) {
        return target.getStats().getHP().getValT();
    }
    @Override
    public int getStatEndValue(Unit target) {
        if((target.getStats().getHP().get() + getHealthBonus()) > target.getStats().getHP().getValT())
        {
            return target.getStats().getHP().getValT();
        }
        else
        {
            return target.getStats().getHP().get() + getHealthBonus();
        }
    }
    @Override
    public int getStatEndTotal(Unit target) {
        return target.getStats().getHP().getValT();
    }

    private int getHealthBonus() {
       int healBonus = 10;
//       healBonus += owner.getStats().getMAG();
       // extra processing for this goes here.
       return healBonus;
    }
    
    @Override
    public boolean canTarget(Unit target) {
        if(owner.getFaction().isFriendlyTowards(target.getFaction()))
        {
            if(target.getStats().getHP().get() < target.getStats().getHP().getValT())
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public void performAction(Unit target) {
        target.heal(getHealthBonus());
    }

}
