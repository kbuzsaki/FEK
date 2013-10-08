/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * The All-Encompasing "Level" class. Holds all level specific information.
 * All information panels are here, as are the map itself, the game's cursor, 
 * and the eventlistener for the game. 
 * The constructor is still a bit arbitrary, but it's mostly generalized.
 */
package Game;

import Game.Event.*;
import Game.Menus.Notification;
import Game.Sound.SoundManager;
import Maps.Map;
import Sprites.AnimationEffect;
import Sprites.AnimationMapUnit;
import Sprites.Direction;
import Sprites.Panels.*;
import Sprites.SpriteUtil;
import Sprites.Text;
import Sprites.ThreadAnimationEffect;
import Sprites.ThreadTick;
import Units.Faction;
import Units.Unit;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Level implements SelectionListener {
    private static final int offsetWidth = 120;
    private static final int offsetHeight = 160;
    
    private Game game;
    private SoundManager soundManager;
    private LevelEventListener listener;
    private ThreadAnimationEffect threadAnimationEffect;
    
    // Panels for the display of graphics
    private JPanel levelScreen; // the "master" panel. the game should append this.
        private GameScreen gameScreen;
            private MapScreen mapScreen; // the "map viewing screen"
                private JPanel panelTop;
                private JPanel panelEffects; // temporary animation effects
                private PanelEffectsArrow arrowEffectsPanel; // the blue arrow
                private JLayeredPane unitsPane; // the units (is layered in overlapReset)
                private PanelEffectsTile tileEffectsPanel; // the selection tiles (blue/red/green)
        private PanelInfoUnit unitInfoPanel; // the upper panel for unit information
        private PanelInfoRight rightInfoPanel;
            private PanelInfoGame gameInfoPanel;
            private PanelInfoTerrain terrainInfoPanel; // the bottom left panel of terrain information
        private PanelHealthInteraction interactionHealthPanel; // the healths displayed in interaction
    
    private PanelMenus menusPanel;
    
    private Map map; // the game map
    private Cursor cursor; // the game cursor
    
    private int turnCount; // the turn count
    private Faction currentFaction; // the faction whose turn it is
    
    private ArrayList<AnimationEffect> animationEffects = new ArrayList();
    private Notification currentNotification;
    private ArrayList<Notification> queuedNotifications = new ArrayList();
    private ArrayList<Unit> deadUnits = new ArrayList();

    public Level(Game game, SoundManager soundManager, String mapName) {
        this.game = game;
        this.soundManager = soundManager;
        
        unitsPane = new JLayeredPane();
            unitsPane.setLayout(null);
        
        panelTop = new JPanel(null);
            panelTop.setOpaque(false);
            
        panelEffects = new JPanel(null); // null layout
            panelEffects.setOpaque(false);
        
        arrowEffectsPanel = new PanelEffectsArrow();
        tileEffectsPanel = new PanelEffectsTile();
        
        mapScreen = new MapScreen(game.getSettings());
          
        gameScreen = new GameScreen(game.getSettings(), mapScreen); 
            gameScreen.add(mapScreen);
        
        unitInfoPanel = new PanelInfoUnit();
        gameInfoPanel = new PanelInfoGame();
        terrainInfoPanel = new PanelInfoTerrain();
        
        rightInfoPanel = new PanelInfoRight();
            rightInfoPanel.add(terrainInfoPanel);
            rightInfoPanel.add(gameInfoPanel);
        
        levelScreen = new JPanel(null);
            levelScreen.setFocusable(true);
            levelScreen.add(gameScreen);
            levelScreen.add(unitInfoPanel);
            levelScreen.add(rightInfoPanel);
            
        // end generic initialization (aside from very end)
            
        map = new Map(mapName);
        cursor = new Cursor(this, soundManager, map, 5, 5);
            cursor.addCursorMovementListener(unitInfoPanel);
            cursor.addSelectionListener(unitInfoPanel);
            cursor.addCursorMovementListener(terrainInfoPanel);
            cursor.addSelectionListener(tileEffectsPanel);
            cursor.addCursorMovementListener(arrowEffectsPanel);
            cursor.addSelectionListener(arrowEffectsPanel);
            cursor.addSelectionListener(this);
        gameScreen.setCursor(cursor);
        listener = new BasicEventListener(this, soundManager);
        
        menusPanel = new PanelMenus(this, gameScreen, soundManager, cursor);
        cursor.setMenusPanel(menusPanel);
        
        interactionHealthPanel = new PanelHealthInteraction(mapScreen, map.image.getSize());
        
        listener.initializeLevel();        
        
        // more generic initialization
        
        mapScreen.add(interactionHealthPanel);
        mapScreen.addComponentToResize(menusPanel);
        mapScreen.add(cursor.getMapAnim());  
        mapScreen.addComponentToResize(panelTop);
        mapScreen.addComponentToResize(arrowEffectsPanel);
        mapScreen.addComponentToResize(panelEffects);
        mapScreen.addComponentToResize(unitsPane);
        mapScreen.addComponentToResize(tileEffectsPanel);
        mapScreen.add(map.image);

        overlapReset();
        cursor.updateFocus();
    }

    public void setZoom(int zoom) {
        int mapScreenWidth = map.image.getWidth()*game.getSettings().getZoom();
        int mapScreenHeight = map.image.getHeight()*game.getSettings().getZoom();
        
        mapScreen.setSize(mapScreenWidth, mapScreenHeight);
        gameScreen.centerMap();
        
    }
    public void snapToZoom() {
        setSize(mapScreen.getWidth() + offsetWidth, mapScreen.getHeight() + offsetHeight);
    }
    public void setSize(int width, int height) {
        int gamePanelWidth = width - offsetWidth;
        int gamePanelHeight = height - offsetHeight;
        
        gameScreen.setLocation(0, offsetHeight);
        gameScreen.setSize(gamePanelWidth, gamePanelHeight);
        unitInfoPanel.setSize(gamePanelWidth + offsetWidth, offsetHeight);
        rightInfoPanel.setLocation(gamePanelWidth, offsetHeight);
        rightInfoPanel.setSize(offsetWidth, gamePanelHeight);
        levelScreen.setSize(gamePanelWidth + offsetWidth, gamePanelHeight + offsetHeight);
    }
    public int getMinimumWidth() {
        return map.image.getWidth() + offsetWidth;
    }
    public int getMinimumHeight() {
        return map.image.getHeight() + offsetHeight;
    }
    
    public void keyHandle(Command button) {
        if(hasCurrentNotification())
        {
            // wait til the user has sent an "A button" to close the window
            if(button == Command.A)
            {
                // close the notification and eat the current command
                clearCurrentNotification();
            }
        }
        else 
        {
            if(button == Command.SELECT)
            {
                int x = 7; // for pausing
                postNotification(new Notification(Text.getImageComponent("This is a Notification"), SoundManager.cancel));
            }
            else if(!menusPanel.keyHandle(button))
            {
                if (!cursor.keyHandle(button))
                {
                    switch (button)
                    {
                        case A:
                            menusPanel.getLevelMenu().open(null);
                            break;
                    }
                }
            }
        }
    }
    public void setTick(int tick) {
        menusPanel.setTick(tick);
        unitInfoPanel.setTick(tick);
    }
    
    // all of the elements of the current levelScreen
    public JPanel getLevelScreen() {
        return levelScreen;
    }
    public MapScreen getMapScreen() {
        return mapScreen;
    }
    public PanelEffectsTile getPanelEffectsTile() {
        return tileEffectsPanel;
    }
    public PanelHealthInteraction getPanelInfoHealths() {
        return interactionHealthPanel;
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
    public Faction getCurrentPhaseFaction() {
        return currentFaction;
    }
    public ArrayList<Faction> getFactionList() {
        ArrayList<Faction> factionList = new ArrayList();
        
        for (Unit unit : getUnitList())
            if(!factionList.contains(unit.getFaction()))
                factionList.add(unit.getFaction());
        
        return factionList;
    }
    
    public void addAnimationEffect(AnimationEffect effect) {
        panelEffects.add(effect);
        effect.setSoundManager(soundManager);
        animationEffects.add(effect);
        
        if(threadAnimationEffect == null || !threadAnimationEffect.isAlive())
        {
            threadAnimationEffect = new ThreadAnimationEffect(this);
            threadAnimationEffect.start();
        }
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
    
    public void sendNotification(Notification notification) {
        // there are no current or queued notifications
        // note that if currentNotification == null, but queuedNotifications 
        // is not empty, this means that another thread is currently calling
        // clearNotification, so this thread must only queue the notification,
        // not post it directly.
        if(currentNotification == null && queuedNotifications.isEmpty())
        {
            // post the notification
            postNotification(notification);
        }
        else 
        {
            // queue the notification
            queuedNotifications.add(notification);
        }
    }
    private void postNotification(Notification notification) {
        if(currentNotification != null)
        {
            currentNotification.close();
        }
        
        currentNotification = notification;
        menusPanel.addPanel(notification);
        SpriteUtil.centerIn(notification,
                new Rectangle(0, 0,
                gameScreen.getScreenWidth(), 
                gameScreen.getScreenHeight()));
        notification.open(soundManager);
    }
    public void clearCurrentNotification() {
        currentNotification.close();
        menusPanel.removePanel(currentNotification);
        currentNotification = null;
        if(!queuedNotifications.isEmpty())
        {
            postNotification(queuedNotifications.remove(0));
        }
    }
    private boolean hasCurrentNotification() {
        return currentNotification != null;
    }
    
    /**
     * Adds a unit to the level's map and screen.
     * The unit is added to the map per the map's 
     * @param unit the unit added to the level
     */
    public final void addUnit(Unit unit) {
        map.addUnit(unit);
        unitsPane.add(unit.getMapAnim());
    }
    /**
     * Removes a unit from the level's map and screen.
     * The unit is removed from the level's map, screen, and list of dead units.
     * <br>This method should generally only be called by the animationThread
     * once a unit has "faded out", or during computer controlled cutscenes.
     * Units should be marked dead using the <code>markDeadUnit</code> method.
     * @param unit the unit to be removed from the level.
     * @see Game.Level#markUnitDead(Units.Unit) 
     */
    public final void removeUnit(Unit unit) {
        deadUnits.remove(unit);
        map.removeUnit(unit);
        unitsPane.remove(unit.getMapAnim());
        cursor.updateFocus();
    }
    
    // never called
    public void moveCompleted(Unit completedUnit) {
        listener.handleMoveCompletionEvent(new MoveCompletionEvent(completedUnit));
    }
    
    /**
     * Animatedly move the unit along its path.
     * Returns when movement has finished. Does not end the unit's turn.
     * This method blocks the thread it runs on, so it must be invoked on a worker thread.
     * @param unit the unit to be moved.
     */
    public void moveUnit(Unit unit) {
        int moveSpeed = 250;
        long perPixMoveSpeed = moveSpeed / Map.tileS;
        
        arrowEffectsPanel.reset(); // clear the effects
        tileEffectsPanel.reset();
        unit.moveUnit();
        
        boolean isDone = false;
        while(!isDone)
        {
            if(!moveTick(unit))
            {
                isDone = true;
            }
            
            // repaint the screen so that unit movement is smooth
            getMapScreen().requestRepaint(); 
            
            try {
                Thread.sleep(perPixMoveSpeed);
            } catch (InterruptedException ex) {
                Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        // TODO: moveunit event
    }
    private static boolean moveTick(Unit unit) {
        SpriteUtil.nudgeUnit(unit.getMapAnim(), unit.getPixelPoint(), unit.getNextTilePosition());
        SpriteUtil.setAppropriateAnimation(unit, unit.getPixelPoint(), unit.getNextTilePosition());

        if (unit.getPixelPoint().equals(unit.getNextTilePosition()))
        {
            unit.getPath().remove(0);
        }
        if (unit.getPath().isEmpty())
        {
            unit.endMovement();
            return false; // returns false: that the unit is done moving
        }
        return true; // returns true: that the unit must still move;
    }
    
    public void runBattle(Battle battle) {
        // Before battle - open graphical elements
        SpriteUtil.setAppropriateAnimation(battle.getAttacking(), 
                battle.getAttacking().getPosition(), battle.getDefending().getPosition());
        SpriteUtil.setAppropriateAnimation(battle.getDefending(), 
                battle.getDefending().getPosition(), battle.getAttacking().getPosition());
        
        interactionHealthPanel.setValues(battle.getAttacking(), battle.getDefending());
        interactionHealthPanel.open((battle.getAttacking().getY() + battle.getDefending().getY()) / 2);
        
        try {
            Thread.sleep(ThreadInteraction.INITIAL_DELAY);  // initial wait
        } catch (InterruptedException ex) {}
        
        // Battle logic 
        while( !battle.isFinished() )
        {
            Attack currentAttack = battle.nextAttack();
            
            interactionHealthPanel.update();
            ThreadBattle.playSoundEffect(currentAttack, soundManager);
            // battle animation
            currentAttack.getAttacking().getMapAnim().actionBump(
                    Direction.getDirection(
                    currentAttack.getAttacking().getPosition(), 
                    currentAttack.getDefending().getPosition()), 
                    getMapScreen());
            // hack. actionbump doesn't properly reset to the right spot
            currentAttack.getAttacking().synchMapAnim();
        } 
        // End of Battle logic
        
        // After battle - Clean up stats / game elements
        if (battle.getAttacking().isDead())
        {
            markUnitDead(battle.getAttacking());
        }
        else if (battle.getDefending().isDead())
        {
            markUnitDead(battle.getDefending());
        }
        
        // After battle - Clean up graphical elements
        battle.getAttacking().getMapAnim().setStand();
        battle.getDefending().getMapAnim().setStand();
        
        getPanelInfoHealths().delayedClose();
        
    }
    public void runStaffInteraction(StaffInteraction staffInteraction) {
        
    }
    public void runItemUseage(ItemUseage itemUseage) {
        // before item useage
        itemUseage.getActor().getMapAnim().setMoveSouth();
        itemUseage.getActor().getMapAnim().setIsPlaying(false);
        
        interactionHealthPanel.setValues(itemUseage.getActor());
        interactionHealthPanel.open(itemUseage.getActor().getY());
        try {
            Thread.sleep(ThreadInteraction.INITIAL_DELAY);  // initial wait
        } catch (InterruptedException ex) {}
        
        // interaction logic
        itemUseage.getActor().getMapAnim().actionFocus();
        itemUseage.execute(null);
        interactionHealthPanel.update();
        AnimationEffect effect = itemUseage.getEffects().get(0);
        try
        {
            Thread.sleep(effect.getDelay());
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        panelEffects.add(effect);
        effect.setSoundManager(soundManager);
        new ThreadTick(effect, ThreadAnimationEffect.animationEffectDelay).run();
        panelEffects.remove(effect);
        
//        while(!(itemUseage.hasCasted() && itemUseage.hasExecuted()))
//        {
//            if(!itemUseage.hasCasted())
//            {
//                itemUseage.getActor().getMapAnim().actionFocus();
//                itemUseage.setCasted();
//            }
//            else if(!itemUseage.hasExecuted())
//            {
//                itemUseage.execute(null);
//                interactionHealthPanel.update();
//                AnimationEffect effect = itemUseage.getEffects().get(0);
//                try
//                {
//                    Thread.sleep(effect.getDelay());
//                } catch (InterruptedException ex)
//                {
//                    Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//                }
//                
//                panelEffects.add(effect);
//                effect.setSoundManager(soundManager);
//                new ThreadTick(effect, ThreadAnimationEffect.animationEffectDelay).run();
//                panelEffects.remove(effect);
//                /*
//                CompletionNotifier completionNotifier = new CompletionNotifier(this);
//
//                itemUseage.execute(completionNotifier);
//                interactionHealthPanel.update(50, completionNotifier);
//
//                completionNotifier.addNotifiers(itemUseage.getEffects());
//                completionNotifier.addNotifier(interactionHealthPanel);
//                completionNotifier.beginListening();
//
//                ExecutorService threadPool = Executors.newCachedThreadPool();
//
//                for(final AnimationEffect effect : itemUseage.getEffects())
//                {
//                    threadPool.submit(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(effect.getDelay() > 0)
//                            {
//                                try {
//                                    sleep(effect.getDelay());
//                                } catch(InterruptedException ex) {
//
//                                }
//                            }
//
//                            level.addAnimationEffect(effect);
//                        }
//                    });
//                } */
//
//            }
//        }
        
        // after item useage
        interactionHealthPanel.delayedClose();
        // TODO: check this 
        getCursor().endAction();
    }
    // TODO: staff and other interaction fixes
    
    @Override
    public void handleSelection(SelectionEvent event) {
        bringToFront(event.getSelectedUnit());
    }
    @Override
    public void handleDeselection(DeselectionEvent event) {
        overlapReset();
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
        ThreadBattle battleThread = new ThreadBattle(this, soundManager, interactionHealthPanel, battle);
        battleThread.start();
    }
    public void initiateStaffInteraction(StaffInteraction staffInteraction) {
        ThreadStaffInteraction threadStaffInteraction 
                = new ThreadStaffInteraction(this, soundManager, interactionHealthPanel, staffInteraction);
        threadStaffInteraction.start();
    }
    public void initiateItemUseage(ItemUseage itemUseage) {
        ThreadItemUseage threadItemUseage 
                = new ThreadItemUseage(this, soundManager, interactionHealthPanel, itemUseage);
        threadItemUseage.start();
    }
    
    // REFACTOR: make Level.setFactionPhase() private?
    public final void setFactionPhase(Faction startingFaction) {
        Faction endingFaction = currentFaction;
        currentFaction = startingFaction;
        Game.logInfo(currentFaction.name + "'s Turn!");
        
        gameInfoPanel.setCurrentFactionPhase(currentFaction);
        cursor.updateFocus();
        listener.handlePhaseChangeEvent(new PhaseChangeEvent(endingFaction, currentFaction));
    }
    public void openOptions() {
        game.openOptions();
    }
    public void suspend() {
        // saving the stuff
        
        game.closeGame();
    }
    // TODO: Cursor not disabled on enemy turn
    public void endPhase() {
        refreshUnits();
        cursor.updateFocus();
        
        ArrayList<Faction> factionList = getFactionList();
        int indexOfNextFaction = (factionList.indexOf(currentFaction) + 1) % factionList.size();
        setFactionPhase(factionList.get(indexOfNextFaction));
        
        // if the next faction index rolls over, then it is the next turn
        if(indexOfNextFaction == 0)
        {
            endTurn();
        }
    }
    public void endTurn() {
        Game.logInfo("End Turn");
        turnCount++;
        listener.handleEndTurnEvent(new EndTurnEvent(this));
    }

    private void refreshUnits() {
        for (Unit unit : getUnitList())
        {
            unit.refresh();
        }
    }
    private void overlapReset () { // TODO: put overlapReset in a unitsPane class
        // resets the overlap for units
        for(Component comp : panelTop.getComponents())
        {
            if(comp instanceof AnimationMapUnit)
            {
                panelTop.remove(comp);
                unitsPane.add(comp);
            }
        }
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
    private void bringToFront(Unit unit) {
        unitsPane.remove(unit.getMapAnim());
        panelTop.add(unit.getMapAnim());
    }
}
