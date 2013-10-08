/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Game;
import Game.Sound.SoundManager;
import Sprites.Panels.GameScreen;
import Sprites.Panels.PanelEffectsTile;
import Sprites.Panels.PanelMenus;
import Units.Unit;

public class MenuListAction extends MenuList {
    private Cursor cursor; // The cursor (used to tell the cursor what action was taken)
    private PanelMenus menusPanel;
    private PanelEffectsTile tileEffectsPanel;
    
    private CommandAction menuCommands[] = CommandAction.values();
    
    public MenuListAction(GameScreen gameScreen, SoundManager soundManager, 
            PanelMenus menusPanel, Cursor cursor, PanelEffectsTile tileEffectsPanel) {
        super(gameScreen, soundManager, 
                getMenuComponents(CommandAction.getMenuNames(CommandAction.values())));
        this.cursor = cursor;
        this.menusPanel = menusPanel;
        this.tileEffectsPanel = tileEffectsPanel;
    }
    
    @Override
    protected void reconstructMenu(int index) {
        menuCommands = CommandAction.getValidCommands(cursor);
        reconstructMenu(getMenuComponents(CommandAction.getMenuNames(menuCommands)));
    }
    
    @Override
    protected void updateIndex(int index) {
        super.updateIndex(index);
        switch(menuCommands[index])
        {
            case ATTACK:
                tileEffectsPanel.updateAttackablePoints(getActor().getAttackPointsInRange());
                break;
            case STAFF:
                tileEffectsPanel.updateStaffPoints(getActor().getStaffPointsInRange());
                break;
            default:
                tileEffectsPanel.reset();
        }
    }
    
    @Override
    public void close() {
        super.close();
        tileEffectsPanel.reset();
    }
    
    @Override
    protected void performAction(int index) {
        switch(menuCommands[index])
        {
            case ATTACK:
                actionAttack();
                break;
            case STAFF:
                actionStaff();
                break;
            case TRADE:
                actionTrade();
                break;
            case ITEM: 
                actionItem();
                break;
            case WAIT:
                actionWait();
                break;
        }
    }
    
    private Unit getActor() {
        return cursor.getSelectedUnit();
    }
    
    private void actionAttack() {
        Game.logInfo(cursor.getSelectedUnit().getName() + " attacks!");
        menusPanel.getItemSelectionMenu().openQuietly(menusPanel.getAttackMenu(), this);
    }
    private void actionStaff() {
        Game.logInfo(cursor.getSelectedUnit().getName() + " attacks!");
        menusPanel.getItemSelectionMenu().openQuietly(menusPanel.getStaffMenu(), this);
    }
    private void actionItem() {
        Game.logInfo(cursor.getSelectedUnit().getName() + " uses an item!");
        menusPanel.getInventoryMenu().openQuietly(this, this);
    }
    private void actionTrade() {
        Game.logInfo(cursor.getSelectedUnit().getName() + " initiated a trade.");
        menusPanel.getTradeSelectionMenu().openQuietly(this);
    }
    private void actionWait() {
        cursor.actionWait();
    }
}
