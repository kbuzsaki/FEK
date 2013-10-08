/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

public class ItemFactory {

    // Swords
    public static Sword swordIron() {
        return new Sword("Iron Sword", 0, "", 
                46, 46, 10, // Uses, Total Uses, Price-Per-Use
                1, Equipment.WEAPON_LEVEL_E, // wXP, weap level req
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 
                90, 5, 0, 5); // Hit, Might, Crit, Weight
    }
    public static Sword swordSteel() {
        return new Sword("Steel Sword", 1, "", 
                30, 30, 20, // Uses, Total Uses, Price-Per-Use
                1, Equipment.WEAPON_LEVEL_D, // wXP, weap level req
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 
                75, 8, 0, 10); // Hit, Might, Crit, Weight
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
    public static Axe axeSteel() {
        return new Axe("Steel Axe", 1, "", 
                30, 30, 12, // Uses, Total Uses, Price-Per-Use
                2, Equipment.WEAPON_LEVEL_D, 
                Equipment.RANGE_MELEE, Equipment.RANGE_MELEE, 
                65, 11, 0, 15); // Hit, Might, Crit, Weight
    }
    public static Axe axeHand() {
        return new Axe("Hand Axe", 3, "", 
                20, 20, 15, // Uses, Total Uses, Price-Per-Use
                1, Equipment.WEAPON_LEVEL_E, 
                Equipment.RANGE_MELEE, 2, 
                60, 7, 0, 12); // Hit, Might, Crit, Weight
    }
    
    // Staves
    private static final String healStaffDescription = "Heals the target for a small amount of HP.";
    private static final String mendStaffDescription = "Heals the target for a large amount of HP.";
    public static StaffHeal staffHeal() {
        return new StaffHeal("Heal", 0, healStaffDescription, 30, 30, 12, 1, 
                Equipment.WEAPON_LEVEL_E, Equipment.RANGE_MELEE, 
                Equipment.RANGE_MELEE, 100);
    }
    public static StaffHeal staffMend() {
        return new StaffHeal("Mend", 1, mendStaffDescription, 20, 20, 50, 1, 
                Equipment.WEAPON_LEVEL_D, Equipment.RANGE_MELEE, 
                Equipment.RANGE_MELEE, 100);
    }
    
    // Items
    public static Vulnerary vulnerary() {
        return new Vulnerary();
    }
}
