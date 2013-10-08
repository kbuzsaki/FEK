/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public class Lance extends Weapon {
    
    public Lance(String name, int spriteX, String description, 
            int uses, int totalUses, int price, int weaponXP, int weaponLevel, 
            int rangeMin, int rangeMax,  int hit, int might, int crit, 
            int weight) {
        super(name, 1, spriteX, description, uses, totalUses, price,
                weaponXP, weaponLevel, rangeMin, rangeMax, hit, might, crit, weight);
    }
    
    @Override
    public boolean isMagic(Unit enemyUnit) {
        return false;
    }
    
    @Override
    public boolean hasWeaponTriangleAdvantage(Unit enemyUnit) {
        Weapon enemyWeapon = enemyUnit.getEquipedWeapon();
        
        if((enemyWeapon instanceof Sword)
         /*&&!(enemyWeapon instanceof SwordReaver)*/)
            return true;
        
//        if(enemyWeapon instanceof AxeReaver)
//            return true;
        
        return false;
    }
    @Override
    public boolean hasWeaponTriangleDisadvantage(Unit enemyUnit) {
        Weapon enemyWeapon = enemyUnit.getEquipedWeapon();
        
        if((enemyWeapon instanceof Axe)
         /*&&!(enemyWeapon instanceof AxeReaver)*/)
            return true;
        
//        if(enemyWeapon instanceof SwordReaver)
//            return true;
        
        return false;
    }
}
