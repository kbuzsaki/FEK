/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Units.Items.Equipment;
import Units.Unit;

public class Battle {
    private Level level;
    private Unit attacker;
    private Unit defender;
    private boolean isAttackersTurn = true;
    private int attackerAttacks = 0;
    private int defenderAttacks = 0;
    
    public Battle(Level level, Unit attacker, Unit defender) {
        this.level = level;
        this.attacker = attacker;
        this.defender = defender;
    }
    
    public Unit getAttacking() {
        return attacker;
    }
    public Unit getDefending() {
        return defender;
    }
    
    private static Equipment getEquiped(Unit unit) {
        return unit.getEquipedWeapon();
    }
    public Equipment getAttackerEquiped() {
        return getEquiped(attacker);
    }
    public Equipment getDefenderEquiped() {
        return getEquiped(defender);
    }
    
    private static int getHP(Unit unit) {
        return unit.getStats().getHP().get();
    }
    public int getAttackerHP() {
        return getHP(attacker);
    }
    public int getDefenderHP() {
        return getHP(defender);
    }
    
    private static int getDamage(Unit attacking, Unit defending) {
        return attacking.getDamage(defending) - defending.getReduction(attacking);
    }
    public int getAttackerDamage() {
        return getDamage(attacker, defender);
    }
    public int getDefenderDamage() {
        return getDamage(defender, attacker);
    }
    
    private static int getAccuracy(Unit attacking, Unit defending) {
        int accuracy = attacking.getHit(defending);
        int avoid = defending.getAvoid(attacking);
        return (accuracy - avoid);
//        return attacking.getHit() - defending.getAvoid();
    }
    public int getAttackerAccuracy() {
        return getAccuracy(attacker, defender);
    }
    public int getDefenderAccuracy() {
        return getAccuracy(defender, attacker);
    }
    
    private static int getCriticalChance(Unit attacking, Unit defending) {
        return attacking.getCriticalChance(defending) - defending.getCriticalAvoid(attacking);
    }
    public int getAttackerCriticalChance() {
        return getCriticalChance(attacker, defender);
    }
    public int getDefenderCriticalChance() {
        return getCriticalChance(defender, attacker);
    }
    
    private static int getAttackNumber(Unit attacking, Unit defending) {
        if((attacking.getAttackSpeed(defending) - defending.getAttackSpeed(attacking)) > 4)
        {
            return attacking.getNumberAttacks(defending) * 2;
        }
        else
        {
            return attacking.getNumberAttacks(attacking);
        }
    }
    public int getAttackerAttackNumber() {
        return getAttackNumber(attacker, defender);
    }
    public int getDefenderAttackNumber() {
        return getAttackNumber(defender, attacker);
    }
    
    public boolean isFinished() {
        return  (attacker.isDead())
             || (defender.isDead())
             || ( (( attackerAttacks>= getAttackerAttackNumber()) || !attacker.canAttack(defender))
               && (( defenderAttacks>= getDefenderAttackNumber()) || !defender.canAttack(attacker)) );
    }
    
    private Attack attack(Unit attacking, Unit defending) {
        boolean isHit = false;
        boolean isCrit = false;
        boolean isKillingBlow = false;
        
        if (Game.getRandNum() < getAccuracy(attacking, defending)) // if hits
        {
            isHit = true;
            attacking.getEquipedWeapon().use();
            Game.log(attacking.getName() + " hits " + defending.getName());
            if (Game.getRandNum() < getCriticalChance(attacking, defending)) // if crits
            {
                isCrit = true;
            }
        }
        else // else misses
        {
            Game.log(attacking.getName() + " misses " + defending.getName());
        }
        int damage = 0;
        
        if(isHit)
        {
            damage = getDamage(attacking, defending);
        }
        if(isCrit)
        {
            damage *= 3;
        }
        
        defending.damage(damage);
        
        if(defending.isDead())
        {
            isKillingBlow = true;
        }
        
        return new Attack(attacking, defending, damage, isHit, isCrit, isKillingBlow);
        
    }
    
    public Attack nextAttack() {
        Attack attack = null;
        
        if((isAttackersTurn) && (attackerAttacks < getAttackerAttackNumber()))
        {
            attack = attack(attacker, defender); // assign to attack object to send to animhandler
            attackerAttacks++;
        }
        else if (defenderAttacks < getDefenderAttackNumber() && defender.canAttack(attacker)) // isFinished means this won't be called if the defender can't attack
        {
            attack = attack(defender, attacker); // assign to attack object to send to animhandler
            defenderAttacks++;
        }
        
        return attack;
    }
}
