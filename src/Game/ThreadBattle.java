/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Sound.SoundManager;
import Sprites.AnimUtils;
import Sprites.ThreadTick;

public class ThreadBattle extends Thread{
    private static final int INITIAL_DELAY = 500;
    private static final int BATTLE_DELAY = 20;
    private static final int BAR_FILL_DELAY = 10;
    
    private Level level;
    private SoundManager soundManager;
    private Battle battle;
    private Attack currentAttack;
    
    private ThreadTick tickThread;
    private boolean isFinished = false;
    
    public ThreadBattle(Level level, SoundManager soundManager, Battle battle) {
        this.level = level;
        this.soundManager = soundManager;
        this.battle = battle;
        
        setName("BattleThread " + battle.getAttacking().getName() + " v.s. " + battle.getDefending().getName() );
    }
    
    public void run() {
        // initial wait
        try {
                sleep(INITIAL_DELAY);
        } catch (InterruptedException ex) {}
        
        runBattleLoop();
    }
    
    private void runBattleLoop() {
        while(!isFinished) 
        {
            if((tickThread == null) || tickThread.isFinished())
            {
                processBattle();
            }
            
            try {
                sleep(BATTLE_DELAY);
            } catch (InterruptedException ex) {}
        }
    }
    private void processBattle() {
        if((currentAttack == null) || currentAttack.isComplete()) // if the current attack is complete / doesn't exist
        {
            if(!battle.isFinished()) // if the battle is not finished
            {
                currentAttack = battle.nextAttack();
                level.getPanelInfoHealths().update();
                
                tickThread = new ThreadTick(level.getPanelInfoHealths(), BAR_FILL_DELAY);
                tickThread.start();
            }
            else
            {
                // the battle is over
                level.getPanelInfoHealths().delayedClose();
                
                battle.getAttacking().getMapAnim().setStand();
                battle.getDefending().getMapAnim().setStand();
                
                if (battle.getAttacking().isDead())
                {
                    level.markUnitDead(battle.getAttacking());
                }
                else if (battle.getDefending().isDead())
                {
                    level.markUnitDead(battle.getDefending());
                }
                level.getCursor().endAction();
                isFinished = true;
            }
        }
        else // the current attack is continuing
        {
            if(currentAttack.playSoundEffect())
            {
                playSoundEffect(currentAttack);
            }
            
            currentAttack.addTick();
            
            int distance = 0;
            if(currentAttack.shouldMoveForward())
            {
                Game.log(currentAttack.getAttacking().getName() + " should move forward");
                distance = 1;
            }
            else if (currentAttack.isPastHalf())
            {
                Game.log(currentAttack.getAttacking().getName() + " is past half.");
                distance = -1;
            }
            
            AnimUtils.nudgeUnit(currentAttack.getAttacking().getMapAnim(), 
                        currentAttack.getAttacking().getPosition(), currentAttack.getDefending().getPosition(),
                        distance);
        }
    }
    private void playSoundEffect(Attack currentAttack) {
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
