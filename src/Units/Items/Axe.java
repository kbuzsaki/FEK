/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public class Axe extends Weapon {
    
    public Axe(String name, int spriteX, String description, 
            int uses, int totalUses, int price, int weaponXP, int weaponLevel, 
            int rangeMin, int rangeMax,  int hit, int might, int crit, 
            int weight) {
        super(name, 2, spriteX, description, uses, totalUses, price,
                weaponXP, weaponLevel, rangeMin, rangeMax, hit, might, crit, weight);
    }
    
    public boolean isMagic(Unit enemyUnit) {
        return false;
    }
    public boolean hasWeaponTriangleAdvantage(Unit enemyUnit) {
        return false;
    }
    public boolean hasWeaponTriangleDisadvantage(Unit enemyUnit) {
        return false;
    }
    public boolean isEffectiveAgainst(Unit enemyUnit) {
        return false;
    }
}
