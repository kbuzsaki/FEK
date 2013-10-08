/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Game.Command;
import Game.Cursor;
import Game.Level;
import Game.Menus.*;
import Game.Sound.SoundManager;
import Sprites.Animateable;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PanelMenus extends JPanel implements Animateable {
    private final MenuListLevel levelMenu;
    private final MenuListAction actionMenu;
    
    private final MenuTrade tradeMenu;
    
    private final MenuSelectionAttack attackMenu;
    private final MenuSelectionStaff staffMenu;
    private final MenuSelectionTrade tradeSelectionMenu;
    
    private final MenuListBoolean booleanMenu;
    
    private final PanelInventoryInfo inventoryInfoPanel;
    private final MenuListItem itemMenu;
    private final MenuItemSelection itemSelectionMenu;
    private final MenuItemInventory inventoryMenu;
    
    private final ArrayList<Menu> menuList = new ArrayList();
    private final ArrayList<GamePanel> panelList = new ArrayList();

    public PanelMenus(Level level, GameScreen gameScreen, SoundManager soundManager, Cursor cursor) {
        levelMenu = new MenuListLevel(level, gameScreen, soundManager);
        actionMenu = new MenuListAction(gameScreen, soundManager, this, cursor, level.getPanelEffectsTile());
        
        tradeMenu = new MenuTrade(gameScreen, soundManager);
        
        attackMenu = new MenuSelectionAttack(level, gameScreen, soundManager, cursor);
        staffMenu  = new MenuSelectionStaff(level, gameScreen, soundManager, cursor);
        tradeSelectionMenu = new MenuSelectionTrade(level, gameScreen, soundManager, cursor, tradeMenu);
        
        booleanMenu = new MenuListBoolean(gameScreen, soundManager);
        
        inventoryInfoPanel = new PanelInventoryInfo(gameScreen);
        itemMenu = new MenuListItem(gameScreen, soundManager, level, booleanMenu);
        itemSelectionMenu = new MenuItemSelection(gameScreen, soundManager, cursor, 
                inventoryInfoPanel);
        inventoryMenu = new MenuItemInventory(gameScreen, soundManager, cursor, 
                inventoryInfoPanel, itemMenu);
        
        addMenu(levelMenu);
        addMenu(actionMenu);
        addMenu(tradeMenu);
        addMenu(attackMenu);
        addMenu(staffMenu);
        addMenu(tradeSelectionMenu);
        
        addMenu(booleanMenu);
        
        addPanel(inventoryInfoPanel);
        addMenu(itemMenu);
        addMenu(itemSelectionMenu);
        addMenu(inventoryMenu);
        
        setLayout(null);
        setOpaque(false);
//        setBorder(new LineBorder(Color.RED));
    }
    
    public final void addMenu(Menu menu) {
        menuList.add(menu);
        add(menu);
    }
    public final void addPanel(GamePanel gamePanel) {
        panelList.add(gamePanel);
        if(gamePanel instanceof Component)
            add((Component)gamePanel);
    }
    public void removePanel(GamePanel gamePanel) {
        panelList.remove(gamePanel);
        if(gamePanel instanceof Component)
            remove((Component)gamePanel);
    }
    
    public boolean keyHandle(Command button) {
        for(Menu menu : menuList)
        {
            if(menu.isAcceptingCommands())
                if (menu.keyHandle(button))
                    return true;
        }
        return false;
    }
    
    @Override
    public void setTick(int tick) {
        for(Menu menu : menuList)
            menu.setTick(tick);
        for(GamePanel gamePanel : panelList)
            gamePanel.setTick(tick);
    }

    public MenuListAction getActionMenu() {
        return actionMenu;
    }
    public MenuSelectionAttack getAttackMenu() {
        return attackMenu;
    }
    public MenuSelectionTrade getTradeSelectionMenu() {
        return tradeSelectionMenu;
    }
    public MenuItemInventory getInventoryMenu() {
        return inventoryMenu;
    }
    public MenuItemSelection getItemSelectionMenu() {
        return itemSelectionMenu;
    }
    public MenuListLevel getLevelMenu() {
        return levelMenu;
    }
    public MenuSelectionStaff getStaffMenu() {
        return staffMenu;
    }
    
}
