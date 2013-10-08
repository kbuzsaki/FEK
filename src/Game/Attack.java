/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Units.Unit;

public class Attack {
    private Unit attacking;
    private Unit defending;
    private int damage;
    private boolean isHit;
    private boolean isCrit;
    private boolean isKillingBlow;
    
    private boolean isComplete = false;

    public Attack(Unit attacking, Unit defending, int damage, boolean isHit, boolean isCrit, boolean isKillingBlow) {
        this.attacking = attacking;
        this.defending = defending;
        this.damage = damage;
        this.isHit = isHit;
        this.isCrit = isCrit;
        this.isKillingBlow = isKillingBlow;
    }

    public Unit getAttacking() {
        return attacking;
    }
    public int getDamage() {
        return damage;
    }
    public Unit getDefending() {
        return defending;
    }
    public boolean isKillingBlow() {
        return isKillingBlow;
    }
    public boolean isCrit() {
        return isCrit;
    }
    public boolean isHit() {
        return isHit;
    }
    
    public void setComplete() {
        isComplete = true;
    }
    public boolean isComplete() {
        return isComplete;
    }
}
