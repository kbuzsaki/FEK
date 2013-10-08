/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.ItemUseage;
import Game.Level;
import Game.Sound.SoundManager;
import Sprites.CompletionEvent;
import Sprites.CompletionListener;
import Sprites.Panels.GameScreen;
import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.UseableItem;
import Units.Unit;

public class MenuListItem extends MenuList {
    
    private Level level;
    private MenuListBoolean booleanMenu;
    
    private CommandItem[] menuCommands;
    private Unit actor;
    private Item targetItem;
    private CompletionListener listener;

    public MenuListItem(GameScreen gameScreen, SoundManager soundManager, Level level, MenuListBoolean booleanMenu) {
        super(gameScreen, soundManager, 
                getMenuComponents(CommandItem.getMenuNames(CommandItem.values())));
        this.level = level;
        this.booleanMenu = booleanMenu;
    }
    
    @Override
    void openQuietly(Menu parentMenu, CancelListener cancelListener) {
        throw new UnsupportedOperationException("Cannot open MenuListItem without an item, unit, and completionListener");
    }
    void openQuietly(Unit actor, Item targetItem, CompletionListener listener, Menu parentMenu, CancelListener cancelListener) {
        this.actor = actor;
        this.targetItem = targetItem;
        this.listener = listener;
        super.openQuietly(parentMenu, cancelListener);
    }
    
    @Override
    public void close() {
        super.close();
        listener.handleCompletion(new CompletionEvent(this));
    }
    
    @Override
    public void reconstructMenu(int index) {
        menuCommands = CommandItem.getValidCommands(actor, targetItem);
        reconstructMenu(getMenuComponents(CommandItem.getMenuNames(menuCommands)));
    }
    
    @Override
    protected boolean shouldCloseOnAction(int index) {
        return false;
    }
    
    @Override
    protected void performAction(int index) {
        switch(menuCommands[index])
        {
            case USE:
                // TODO: using an item fails to close the menu
                System.out.println("Initiating item useage from menu");
                level.runItemUseage(new ItemUseage(actor, (UseableItem)targetItem));
                closeHard();
                break;
            case EQUIP:
                actor.getInventory().equip((Equipment)targetItem);
                close();
                break;
            case DISCARD:
                setAcceptingCommands(false);
                booleanMenu.openQuietly(new BooleanListener() {
                    @Override
                    public void handleBoolean(boolean result) {
                        if(result)
                            actor.getInventory().drop(targetItem);
                        close();
                        setAcceptingCommands(true);
                    }
                }, this, this);
            default:
                close();
        }
    }
}
