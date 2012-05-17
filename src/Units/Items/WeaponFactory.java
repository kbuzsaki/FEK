/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

public class WeaponFactory {

    // Swords
    public static Sword swordIron() {
        return new Sword("Iron Sword", 0, "", 
                46, 46, 10, // Uses, Total Uses, Price-Per-Use
                1, Equipment.WEAPON_LEVEL_E, 
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 
                90, 5, 0, 5); // Hit, Might, Crit, Weight
    }
    
    // Lances
    public static Lance lanceIron() {
        return new Lance("Iron Lance", 0, "", 
                45, 45, 8, // Uses, Total Uses, Price-Per-Use
                1, Equipment.WEAPON_LEVEL_E, 
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 
                80, 7, 0, 8); // Hit, Might, Crit, Weight
    }
    
    // Axes
    public static Axe axeIron() {
        return new Axe("Iron Axe", 0, "", 
                45, 45, 6, // Uses, Total Uses, Price-Per-Use
                1, Equipment.WEAPON_LEVEL_E, 
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 
                75, 8, 0, 10); // Hit, Might, Crit, Weight
    }
}
