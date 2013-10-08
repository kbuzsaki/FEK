/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Game.Battle;
import Game.Level;
import Game.Sound.SoundManager;
import Units.Faction;
import Units.Unit;
import java.awt.Point;
import java.util.ArrayList;

public abstract class LevelEventListener {
    protected Level level;
    protected SoundManager soundManager;
    
    protected LevelEventListener() {
        this(null, null);
    }
    protected LevelEventListener(Level level, SoundManager soundManager) {
        this.level = level;
        this.soundManager = soundManager;
    }
    
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }
    
    public abstract void initializeLevel();
    
    public abstract void handleMoveEvent(MoveEvent event);
    // never called
    public abstract void handleMoveCompletionEvent(MoveCompletionEvent event);
    public abstract void handleDeathEvent(DeathEvent event);
    public abstract void handleEndTurnEvent(EndTurnEvent event);
    public abstract void handlePhaseChangeEvent(PhaseChangeEvent event);
    
    protected void runAiMoves(Faction faction) {
        System.out.println("In AI method");
        ArrayList<Unit> rawUnitList = level.getUnitList();
        ArrayList<Unit> unitList = new ArrayList<>();
        for(Unit unit : rawUnitList)
        {
            if(unit.getFaction() == faction)
            {
                unitList.add(unit);
            }
        }
        
        for(Unit unit : unitList)
        {
            Point dest = new Point(unit.getX() + 1, unit.getY());
            // ensures that the destination isn't occupied
            if(!level.getMap().unitAt(dest)) // if the position is not occupied
            {
                unit.incrementPath(dest);
                level.moveUnit(unit);
                // attack things etc
                unit.endTurn();
            }
            else if(unit.canAttack(level.getMap().getUnitAt(dest))) {
                Battle battle = new Battle(level, unit, level.getUnitAt(dest));
                level.runBattle(battle);
            }
        }
        level.endPhase();
            
    }
        
}
