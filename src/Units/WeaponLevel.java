/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Units.Items.WeaponType;


public class WeaponLevel {
    
    public static final int WEAPON_LEVEL_0 = 0;
    public static final int WEAPON_LEVEL_E = 1;
    public static final int WEAPON_LEVEL_D = 31;
    public static final int WEAPON_LEVEL_C = 71;
    public static final int WEAPON_LEVEL_B = 121;
    public static final int WEAPON_LEVEL_A = 181;
    public static final int WEAPON_LEVEL_S = 251;
    
    private WeaponType weaponType;
    private int weaponXP;
    
    public WeaponLevel(WeaponType weaponType, int weaponXP) {
        this.weaponType = weaponType;
        this.weaponXP = weaponXP;
    }
    public WeaponLevel(WeaponType weaponType) {
        this(weaponType, WEAPON_LEVEL_0);
    }
    
    public int getWeaponXP() {
        return weaponXP;
    }
    public void addWeaponXP(int dXP) {
        weaponXP += dXP;
    }
    public boolean isSRank() {
        return (weaponXP >= WEAPON_LEVEL_S);
    }

}
