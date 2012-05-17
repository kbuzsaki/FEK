/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public abstract class Equipment extends Item {
    protected final int weaponXP;
    protected final int weaponLevel;
    protected final int rangeMin;
    protected final int rangeMax;
    
    public static final int WEAPON_LEVEL_E = 1;
    public static final int WEAPON_LEVEL_D = 31;
    public static final int WEAPON_LEVEL_C = 71;
    public static final int WEAPON_LEVEL_B = 121;
    public static final int WEAPON_LEVEL_A = 181;
    public static final int WEAPON_LEVEL_S = 251;
    
    public static final int RANGE_MELEE = 1;
    public static final int RANGE_BOLTING_MIN = 3;
    public static final int RANGE_BOLTING_MAX = 10; 
    
    protected Equipment(String name, int spriteY, int spriteX, String description, 
            int uses, int totalUses, int price, int weaponXP, 
            int weaponLevel, int rangeMin, int rangeMax) {
        super (name, spriteY, spriteX, description, uses, totalUses, price);
        
        this.weaponXP = weaponXP;
        this.weaponLevel = weaponLevel;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public int getRangeMax() {
        return rangeMax;
    }
    public int getRangeMin() {
        return rangeMin;
    }
    public final int getWeaponLevel() {
        return weaponLevel;
    }
    public final int getWeaponXP() {
        return weaponXP;
    }
    
    public boolean isEquipableBy() {
        return isEquipableBy(owner);
    }
    public boolean isEquipableBy(Unit equiper) {
        if(equiper.getStats().getWeaponXP(this) > weaponLevel)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
}
