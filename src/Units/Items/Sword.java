/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public class Sword extends Weapon{
    
    public Sword(String name, int spriteX, String description, 
            int uses, int totalUses, int price, int weaponXP, int weaponLevel, 
            int rangeMin, int rangeMax,  int hit, int might, int crit, 
            int weight) {
        super(name, 0, spriteX, description, uses, totalUses, price,
                weaponXP, weaponLevel, rangeMin, rangeMax, hit, might, crit, weight);
    }
    
    @Override
    public boolean isMagic(Unit enemyUnit) {
        return false;
    }
    
    @Override
    public boolean hasWeaponTriangleAdvantage(Unit enemyUnit) {
        Weapon enemyWeapon = enemyUnit.getEquipedWeapon();
        
        if((enemyWeapon instanceof Axe)
         /*&&!(enemyWeapon instanceof AxeReaver)*/)
            return true;
        
//        if(enemyWeapon instanceof LanceReaver)
//            return true;
        
        return false;
    }
    @Override
    public boolean hasWeaponTriangleDisadvantage(Unit enemyUnit) {
        Weapon enemyWeapon = enemyUnit.getEquipedWeapon();
        
        if((enemyWeapon instanceof Lance)
         /*&&!(enemyWeapon instanceof LanceReaver)*/)
            return true;
        
//        if(enemyWeapon instanceof AxeReaver)
//            return true;
        
        return false;
    }
}
