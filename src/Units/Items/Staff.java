/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public abstract class Staff extends Equipment {
    public Object effect;
    public int hit;

    public Staff (String name, int spriteX, int spriteY, String description, int uses,
            int totalUses, int price, int weaponXP, int weaponLevel,
            int rangeMin, int rangeMax, int hit, Object effect)
    {
        super (name, spriteX, spriteY, description, uses, totalUses, price, 
                weaponXP, weaponLevel, rangeMin, rangeMax);
        this.effect = effect;
        this.hit = hit;
    }
    
    public int getHit() {
        return hit;
    }
    
    public abstract String getStatName(Unit target);
    public abstract int getStatStartValue(Unit target);
    public abstract int getStatStartTotal(Unit target);
    public abstract int getStatEndValue(Unit target);
    public abstract int getStatEndTotal(Unit target);
    
    public abstract void performAction(Unit target);
}