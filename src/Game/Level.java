/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The All-Encompasing "Level" class. Holds all level specific information.
 * All information panels are here, as are the map itself, the game's cursor, 
 * and the eventlistener for the game. 
 * The constructor is still a bit arbitrary, but it's mostly generalized.
 */
package Game;

import Game.Menus.MenuSelectionStaff;
import Game.Menus.MenuPanelAction;
import Game.Menus.MenuPanelLevel;
import Game.Menus.MenuSelectionAttack;
import Game.Event.*;
import Game.Sound.SoundManager;
import Maps.Map;
import Sprites.AnimUtils;
import Sprites.AnimationEffect;
import Sprites.Panels.*;
import Units.Faction;
import Units.Inventory;
import Units.Items.WeaponFactory;
import Units.Unit;
import Units.UnitClass;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Level {
    private Game game;
    private SoundManager soundManager;
    private LevelEventListener listener;
    private ThreadMovement threadMovement;
    
    // Panels for the display of graphics
    private JPanel levelScreen; // the "master" panel. the game should append this.
        private GameScreen gameScreen; // the "map viewing screen"
            private JPanel panelEffects; // temporary animation effects
            private PanelEffectsArrow arrowEffectsPanel; // the blue arrow
            private JLayeredPane unitsPane; // the units (is layered in overlapReset)
            private PanelEffectsTile tileEffectsPanel; // the selection tiles (blue/red/green)
        private PanelInfoUnit unitInfoPanel; // the upper panel for unit information
        private PanelInfoTerrain terrainInfoPanel; // the bottom left panel of terrain information
        private PanelHealthInteraction interactionHealthsPanel; // the healths displayed in interaction
    
    private MenuPanelLevel levelMenu; // level options (unit/status/options/suspend/end turn)
    private MenuPanelAction actionMenu; // the action menu (attack/item/wait)
    private MenuSelectionAttack attackMenu; // the attack menu diplay (damage and whatnot)
    private MenuSelectionStaff staffMenu; // the staff menu display (health healed)
    
    private Map map; // the game map
    private Cursor cursor; // the game cursor
    
    private int turnCount; // the turn count
    private Faction currentTurnFaction; // the faction whose turn it is
    
    private ArrayList<AnimationEffect> animationEffects = new ArrayList();
    private ArrayList<Unit> deadUnits = new ArrayList();

    public Level(Game game, SoundManager soundManager, String mapName) {
        this.game = game;
        this.soundManager = soundManager;
        
        map = new Map(mapName);
        cursor = new Cursor(this, soundManager, 5, 5);
        listener = new BasicEventListener(this);
        
        levelMenu = new MenuPanelLevel(map.image.getSize(), this, soundManager);
        actionMenu = cursor.getActionMenu();
        attackMenu = cursor.getAttackMenu();
        staffMenu = cursor.getStaffMenu();
        
        int gameScreenWidth = map.image.getWidth()*game.getSettings().getZoom();
        int gameScreenHeight = map.image.getHeight()*game.getSettings().getZoom();
        int offsetWidth = 120;
        int offsetHeight = 160;
        
        unitsPane = new JLayeredPane();
            unitsPane.setLayout(null);
            unitsPane.setSize(gameScreenWidth, gameScreenHeight);
        
        panelEffects = new JPanel(null); // null layout
            panelEffects.setSize(gameScreenWidth, gameScreenHeight);
            panelEffects.setOpaque(false);
        
        arrowEffectsPanel = new PanelEffectsArrow(gameScreenWidth, gameScreenHeight);
        tileEffectsPanel = new PanelEffectsTile(gameScreenWidth, gameScreenHeight);
        
        interactionHealthsPanel = new PanelHealthInteraction(map.image.getSize());
        
        gameScreen = new GameScreen(new Rectangle(0, offsetHeight, gameScreenWidth, gameScreenHeight), game.getSettings());
            gameScreen.add(interactionHealthsPanel);
            gameScreen.add(levelMenu);
            gameScreen.add(actionMenu);
            gameScreen.add(attackMenu);
            gameScreen.add(staffMenu);
            gameScreen.add(cursor.getMapAnim());  
            gameScreen.add(arrowEffectsPanel); 
            gameScreen.add(unitsPane);    
            gameScreen.add(panelEffects);
            gameScreen.add(tileEffectsPanel);
            gameScreen.add(map.image);
            
        unitInfoPanel = new PanelInfoUnit(new Dimension(gameScreenWidth + offsetWidth, offsetHeight));
        terrainInfoPanel = new PanelInfoTerrain(new Rectangle(gameScreenWidth, offsetHeight, offsetWidth, gameScreenHeight));
        
        levelScreen = new JPanel(null);
            levelScreen.setSize(gameScreenWidth + offsetWidth, gameScreenHeight + offsetHeight);
            levelScreen.setFocusable(true);
            levelScreen.add(gameScreen);
            levelScreen.add(unitInfoPanel);
            levelScreen.add(terrainInfoPanel);
            
        // end generic initialization (aside from very end)
        
        soundManager.playMusic(SoundManager.Companions);
        setCurrentTurnFaction(Faction.BLUE);
        Unit bandit1 = new Unit(soundManager, map, 6, 2, Faction.RED, UnitClass.BANDIT, 1, new Inventory(WeaponFactory.axeIron()));
        Unit bandit2 = new Unit(soundManager, map, 4, 4, Faction.RED, UnitClass.BANDIT, 1, new Inventory(WeaponFactory.axeIron()));
        Unit merc1 =   new Unit(soundManager, map, 5, 5, Faction.BLUE, UnitClass.MERCENARY, 1, new Inventory(WeaponFactory.swordIron()));
        Unit merc2 =   new Unit(soundManager, map, 6, 6, Faction.BLUE, UnitClass.MERCENARY, 1, new Inventory(WeaponFactory.swordIron()));
        Unit cleric1 = new Unit(soundManager, map, 7, 5, Faction.BLUE, UnitClass.CLERIC, 1, new Inventory(new Units.Items.StaffHeal()));
        addUnit(bandit1);
        addUnit(bandit2);
        addUnit(merc1);
        addUnit(merc2);
        addUnit(cleric1);

        // more generic initialization
        overlapReset();
        cursor.updateFocus();
    }
    // TODO: set zoom method that resizes panels

    public void keyHandle(Command button) {
        // send the command to the map cursor
        
        if (levelMenu.isOpen())
        {
            levelMenu.keyHandle(button);
        }
        else 
        {
            if (!cursor.keyHandle(button))
            {
                switch (button)
                {
                    case A:
                        levelMenu.open(cursor.getX());
                        break;
                }
            }
        }
        
        // resets the overlaping so that the graphics allign properly
        // REFACTOR: put this somewhere more eventy
        overlapReset();
    }
    public void setTick(int tick) {
        levelMenu.setTick(tick);
        actionMenu.setTick(tick);
        staffMenu.setTick(tick);
        unitInfoPanel.setTick(tick);
    }
    
    // all of the elements of the current levelScreen
    public JPanel getLevelScreen() {
        return levelScreen;
    }
    public GameScreen getGameScreen() {
        return gameScreen;
    }
    public PanelEffectsTile getPanelEffectsTile() {
        return tileEffectsPanel;
    }
    public PanelEffectsArrow getPanelEffectsArrow() {
        return arrowEffectsPanel;
    }
    public PanelInfoUnit getPanelInfoUnit() {
        return unitInfoPanel;
    }
    public PanelInfoTerrain getPanelInfoTerrain() {
        return terrainInfoPanel;
    }
    public PanelHealthInteraction getPanelInfoHealths() {
        return interactionHealthsPanel;
    }
    
    // the elements of the current level
    public Map getMap() {
        return map;
    }
    public Cursor getCursor() {
        return cursor;
    }
    public int getTurnCount() {
        return turnCount;
    }
    /**
     * 
     * @return the faction whose turn it currently is
     */
    public Faction getCurrentTurnFaction() {
        return currentTurnFaction;
    }
    
    public void addAnimationEffect(AnimationEffect effect) {
        panelEffects.add(effect);
        effect.setSoundManager(soundManager);
        animationEffects.add(effect);
    }
    public void removeAnimationEffect(AnimationEffect effect) {
        panelEffects.remove(effect);
        animationEffects.remove(effect);
    }
    /**
     * Gets the list of animation effects currently on the gamescreen
     * Note: removing an animation from this list directly will not remove it
     * from the game screen. You must use the removeAnimationEffect method.
     * @return 
     */
    public ArrayList<AnimationEffect> getAnimationEffects() {
        return animationEffects;
    }
    
    public final void addUnit(Unit unit) {
        if(map.addUnit(unit))
        {
            unitsPane.add(unit.getMapAnim());
        }
    }
    public final void removeUnit(Unit unit) {
        deadUnits.remove(unit);
        map.removeUnit(unit);
        unitsPane.remove(unit.getMapAnim());
        cursor.updateFocus();
    }
    
    public boolean moveUnit(Unit movingUnit) {
        listener.handleMoveEvent(new MoveEvent(movingUnit));
        
        if(movingUnit.moveUnit())
        {
            if(threadMovement == null || !threadMovement.isAlive())
            {
                threadMovement = new ThreadMovement(this);
                threadMovement.start();
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void markUnitDead(Unit unit) {
        listener.handleDeathEvent(new DeathEvent(unit));
        deadUnits.add(unit);
    }
    public ArrayList<Unit> getDeadUnits() {
        return deadUnits;
    }
    
    public boolean unitIsAt(Point position) {
        if (map.getUnitAt(position) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public Unit getUnitAt(Point position) {
        return map.getUnitAt(position);
    }
    public ArrayList<Unit> getUnitList() {
        return map.getUnitList();
    }
    
    public void initiateBattle(Battle battle) {
        interactionHealthsPanel.setValues(battle.getAttacking(), battle.getDefending());
        
        AnimUtils.setAppropriateAnimation(battle.getAttacking(), 
                battle.getAttacking().getPosition(), battle.getDefending().getPosition());
        AnimUtils.setAppropriateAnimation(battle.getDefending(), 
                battle.getDefending().getPosition(), battle.getAttacking().getPosition());
        
        interactionHealthsPanel.open((battle.getAttacking().getY() + battle.getDefending().getY()) / 2);
        
        ThreadBattle battleThread = new ThreadBattle(this, soundManager, battle);
        battleThread.start();
    }
    public void initiateStaffInteraction(StaffInteraction staffInteraction) {
        interactionHealthsPanel.setValues(staffInteraction.getTarget());
        interactionHealthsPanel.open((staffInteraction.getStaffUser().getY() + staffInteraction.getTarget().getY()) / 2);
        ThreadStaffInteraction threadStaffInteraction 
                = new ThreadStaffInteraction(this, soundManager, staffInteraction);
        threadStaffInteraction.start();
    }
    
    public final void setCurrentTurnFaction(Faction nextFaction) {
        currentTurnFaction = nextFaction;
        cursor.updateFocus();
    }
    public void openOptions() {
        game.openOptions();
    }
    public void suspend() {
        // saving the stuff
        
        game.closeGame();
    }
    public void endTurn() {
        Game.log("End Turn");
        turnCount++;
        listener.handleEndTurnEvent(new EndTurnEvent(this));
        ArrayList<Unit> unitList = getUnitList();
        ArrayList<Faction> factionList = new ArrayList();
        for (int i = 0; i < unitList.size(); i++ )
        {
            unitList.get(i).setDepleted(false);
            if(!factionList.contains(unitList.get(i).getFaction()))
            {
                factionList.add(unitList.get(i).getFaction());
            }
        }
        int indexOfNextFaction = (factionList.indexOf(currentTurnFaction) + 1) % factionList.size();
        setCurrentTurnFaction(factionList.get(indexOfNextFaction));
        cursor.updateFocus();
    }

    public final void overlapReset () { // TODO: put overlapReset in a unitsPane class
        // resets the overlap for units
        int count = 0;
        for (int y = map.height-1; y >= 0; y--) 
        {
            for (int x = map.width-1; x >= 0; x--) 
            {
                if (map.getUnitAt(x, y) != null)
                {
                count++;
                unitsPane.setPosition(map.getUnitAt(x, y).getMapAnim(), count);
                }
            }
        }
    }
}
