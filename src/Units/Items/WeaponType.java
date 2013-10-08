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
    
    public static WeaponType getTypeOf(Equipment equip) {
        if(equip instanceof Sword) {
            return SWORD;
        } else if (equip instanceof Lance) {
            return LANCE;
        } else if (equip instanceof Axe) {
            return AXE;
//        } else if (equip instanceof Bow) {
//            return LANCE;
//        } else if (equip instanceof Anima) {
//            return LANCE;
//        } else if (equip instanceof Light) {
//            return LANCE;
//        } else if (equip instanceof Dark) {
//            return LANCE;
        } else if (equip instanceof Staff) {
            return STAFF;
        }
        return null;
    }
}
