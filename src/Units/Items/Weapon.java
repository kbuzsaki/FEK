/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Units.Unit;

public abstract class Weapon extends Equipment {
    protected final int hit;
    protected final int might;
    protected final int crit;
    protected final int weight;

    protected Weapon (String name, int spriteY, int spriteX, String description,
          int uses, int totalUses, int price, int weaponXP, int weaponLevel, 
          int rangeMin, int rangeMax,  int hit, int might, int crit, int weight)
    {
        super (name, spriteY, spriteX, description, uses, totalUses, price, 
               weaponXP, weaponLevel, rangeMin, rangeMax);
        this.hit = hit;
        this.might = might;
        this.crit = crit;
        this.weight = weight;
    }
    
    /**
     * Gets the base hit value for the weapon
     * <b>This method should only be used for displaying weapon stats</b>
     * @return the base hit value
     */
    public final int getHit() {
        return hit;
    }
    /**
     * Gets the hit value for the weapon in combat
     * @param enemyUnit the unit that the owner is fighting
     * @return the effective hit value
     */
    public int getHit(Unit enemyUnit) {
        return hit;
    }
    
    /**
     * Gets the base might value for the weapon
     * <b>This method should only be used for displaying weapon stats</b>
     * @return the base might value
     */
    public final int getMight() {
        return might;
    }
    /**
     * Gets the might value for the weapon in combat
     * @param enemyUnit the unit that the owner is fighting
     * @return the effective might value
     */
    public int getMight(Unit enemyUnit) {
        return might;
    }
    
    /**
     * Gets the base crit value for the weapon
     * <b>This method should only be used for displaying weapon stats</b>
     * @return the base crit value
     */
    public final int getCrit() {
        return crit;
    }
    /**
     * Gets the crit value for the weapon in combat
     * @param enemyUnit the unit that the owner is fighting
     * @return the effective crit value
     */
    public int getCrit(Unit enemyUnit) {
        return crit;
    }
    
    /**
     * Gets the base weight value for the weapon
     * <b>This method should only be used for displaying weapon stats</b>
     * @return the base weight value
     */
    public final int getWeight() {
        return weight;
    }
    /**
     * Gets the weight value for the weapon in combat
     * @param enemyUnit the unit that the owner is fighting
     * @return the effective weight value
     */
    public int getWeight(Unit enemyUnit) {
        return weight;
    }
    
    /**
     * Gets whether or not the weapon's attack is magical while in combat.
     * This is used to determine if defense or magical resistance should be
     * used to calculate net damage.
     * @param enemyUnit the unit that the owner is fighting
     * @return if the attack is magical
     */
    public abstract boolean isMagic(Unit enemyUnit);
    
    public abstract boolean hasWeaponTriangleAdvantage(Unit enemyUnit);
    public abstract boolean hasWeaponTriangleDisadvantage(Unit enemyUnit);
    public abstract boolean isEffectiveAgainst(Unit enemyUnit);
}