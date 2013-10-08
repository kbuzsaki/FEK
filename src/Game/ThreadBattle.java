/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Sound.SoundManager;
import Sprites.SpriteUtil;
import Sprites.Direction;
import Sprites.Panels.PanelHealthInteraction;

public class ThreadBattle extends ThreadInteraction {
    
    private Battle battle;
    private Attack currentAttack;
    
    public ThreadBattle(Level level, SoundManager soundManager, 
            PanelHealthInteraction interactionHealthPanel, Battle battle) {
        super(level, soundManager, interactionHealthPanel);
        this.battle = battle;
        
        setName("BattleThread " + battle.getAttacking().getName() + " v.s. " + battle.getDefending().getName() );
    }
    
    @Override
    protected void beginInteraction() {
        SpriteUtil.setAppropriateAnimation(battle.getAttacking(), 
                battle.getAttacking().getPosition(), battle.getDefending().getPosition());
        SpriteUtil.setAppropriateAnimation(battle.getDefending(), 
                battle.getDefending().getPosition(), battle.getAttacking().getPosition());
        
        interactionHealthPanel.setValues(battle.getAttacking(), battle.getDefending());
        interactionHealthPanel.open((battle.getAttacking().getY() + battle.getDefending().getY()) / 2);
    }
    @Override
    protected void endInteraction() {
        // the battle is over
        if (battle.getAttacking().isDead())
        {
            level.markUnitDead(battle.getAttacking());
        }
        else if (battle.getDefending().isDead())
        {
            level.markUnitDead(battle.getDefending());
        }
        
        battle.getAttacking().getMapAnim().setStand();
        battle.getDefending().getMapAnim().setStand();
        
        level.getPanelInfoHealths().delayedClose();
        
        level.getCursor().endAction();
    }
    @Override
    protected void processInteraction() {
        if((currentAttack == null) || currentAttack.isComplete()) // if the current attack is complete / doesn't exist
        {
            if(!battle.isFinished()) // if the battle is not finished
            {
                currentAttack = battle.nextAttack();
                interactionHealthPanel.update(this);
            }
        }
        else // the current attack must take place
        {
            playSoundEffect(currentAttack, soundManager);
            currentAttack.getAttacking().getMapAnim().actionBump(
                    Direction.getDirection(
                    currentAttack.getAttacking().getPosition(), 
                    currentAttack.getDefending().getPosition()), 
                    level.getMapScreen(), this);
            currentAttack.setComplete();
        }
    }
    
    @Override
    protected boolean interactionIsFinished() {
        return battle.isFinished() && ((currentAttack == null) || currentAttack.isComplete());
    }
    
    public static void playSoundEffect(Attack currentAttack, SoundManager soundManager) {
        if(currentAttack.isHit())
        {
            if(currentAttack.isCrit())
            {
                if(currentAttack.isKillingBlow())
                {
                    soundManager.playSoundEffect(SoundManager.hitCritKill);
                }
                else
                {
                    soundManager.playSoundEffect(SoundManager.hitCrit);
                }
            }
            else
            {
                if(currentAttack.isKillingBlow())
                {
                    soundManager.playSoundEffect(SoundManager.hitKill);
                }
                else
                {
                    soundManager.playSoundEffect(SoundManager.hit);
                }
            }
        }
        else
        {
            soundManager.playSoundEffect(SoundManager.miss);
        }
    }
}
