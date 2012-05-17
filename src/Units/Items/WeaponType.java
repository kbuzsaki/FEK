/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

public enum WeaponType {
    SWORD(false),
    LANCE(false),
    AXE(false),
    BOW(false),
    ANIMA(true),
    LIGHT(true),
    DARK(true),
    STAFF(true);
    
    public final boolean isMagic;
    
    WeaponType(boolean isMagic) {
        this.isMagic = isMagic;
    }
}
