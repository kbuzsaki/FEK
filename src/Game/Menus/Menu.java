/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Command;
import Game.Game;
import Game.Sound.SoundManager;
import Sprites.Panels.GameScreen;
import javax.swing.JPanel;

public abstract class Menu extends JPanel implements CancelListener, GamePanel{
    private int index = 0; // the index of the user's choice in the menu
    private int maxIndex;
    
    private boolean isOpen = false;
    private boolean isAcceptingCommands = false;
    private CancelListener cancelListener;
    
    protected GameScreen gameScreen;
    protected SoundManager soundManager;
    protected Menu parentMenu;
    
    public Menu(GameScreen gameScreen, SoundManager soundManager) {
        this.gameScreen = gameScreen;
        this.soundManager = soundManager;
        
        setLayout(null);
        setOpaque(false);
        setVisible(false);
    }
    
    protected abstract void reconstructMenu(int index);
    protected abstract void updateIndex(int index);
    
    private void setWrappedIndex(int index) {
        this.index = getWrapped(index);
        updateIndex(this.index);
    }
    protected int getIndex() {
        return index;
    }
    protected int getWrapped(int index) {
        if (index < 0)
        {
            return (maxIndex - 1);
        }
        else 
        {
            return (index % maxIndex);
        }
    }
    protected void incrementIndex() {
        setWrappedIndex(index + 1);
    }
    protected void decrementIndex() {
        setWrappedIndex(index - 1);
    }
    protected int getDecrementedIndex() {
        return getWrapped(index - 1);
    }
    protected void setMaxIndex(int maxIndex) {
        if(maxIndex > 0)
            this.maxIndex = maxIndex;
        else
            throw new IndexOutOfBoundsException("maxIndex for menu must be a positive integer. index: " + maxIndex);
    }
    
    protected void resetMenu() {
        reconstructMenu(0);
        updatePosition();
        resetIndex();
    }
    protected void resetIndex() {
        setWrappedIndex(getResetIndex());
    }
    protected int getResetIndex() {
        return 0;
    }
    
    public final void open(CancelListener cancelListener) {
        soundManager.playSoundEffect(SoundManager.confirm);
        openQuietly(null, cancelListener);
    }
    void openQuietly(Menu parentMenu, CancelListener cancelListener) {
        this.parentMenu = parentMenu;
        this.cancelListener = cancelListener;
        openQuietly();
    }
    private void openQuietly() {
        resetMenu();
        
        isOpen = true;
        setAcceptingCommands(true);
        
        setVisible(true);
    }
    @Override
    public void close() {
        setVisible(false);
        isOpen = false;
        setAcceptingCommands(false);
    }
    protected final void closeHard() {
        close();
        if(parentMenu != null && parentMenu.isOpen())
            parentMenu.closeHard();
    }
    protected void cancel() {
        close();
        if(cancelListener != null)
            cancelListener.notifyCancel();
        else
            Game.logDebug(getClass().getName() + " did not have cancelListener");
    }
    @Override
    public void notifyCancel() {
        openQuietly();
    }
    
    @Override
    public boolean isOpen() {
        return isOpen;
    }
    protected boolean shouldCloseOnAction(int index) {
        return true;
    }
    public boolean isAcceptingCommands() {
        return isOpen() && isAcceptingCommands;
    }
    protected void setAcceptingCommands(boolean isAcceptingCommands) {
        this.isAcceptingCommands = isAcceptingCommands;
    }
    
    /**
     * Handles input given by the cursor
     * @param button the command sent
     * @return true if the menu used the command
     */
    public boolean keyHandle(Command button) {
        switch (button)
        {
            case A:
                soundManager.playSoundEffect(SoundManager.confirm);
                if(shouldCloseOnAction(index))
                    close();
                performAction(index);
                return true;
            case B:
                soundManager.playSoundEffect(SoundManager.cancel);
                cancel();
                return true;
            case UP:
                soundManager.playSoundEffect(SoundManager.menuBlipLow);
                decrementIndex();
                return true;
            case DOWN:
                soundManager.playSoundEffect(SoundManager.menuBlipLow);
                incrementIndex();
                return true;
        }
        return false;
    }
    protected abstract void performAction(int index);
    // FIXME: Magic numbers (make work for different menu sizes?)
    protected void updatePosition() {
        if(parentMenu != null && parentMenu.isOpen())
        {
            Game.logDebug("Parent menu is open for: " + this.toString());
            // if the parent menu is on the left side of the screen
            if(gameScreen.isOnLeft(parentMenu))
            {
                Game.logDebug("Parent menu is on the left for: " + this.toString());
                setLocation(
                        (parentMenu.getX() + parentMenu.getWidth()) - getWidth()*1/4,
                        (parentMenu.getY() + parentMenu.getHeight()*1/4));
            }
            else // else the parent menu must be on the right side of the screen
            {
                Game.logDebug("Parent menu is on the right for: " + this.toString());
                setLocation(
                        (parentMenu.getX() - getWidth()*1/4),
                        (parentMenu.getY() + parentMenu.getHeight()*1/4));
            }
        }
        else
        {
            // If the cursor is on the left half of the screen
            if (gameScreen.cursorIsOnLeft()) 
            {
                Game.logDebug("Cursor is on the left for: " + this.toString());
                // set the panel's rightmost edge to be 1/12th from the screen edge
                // set the panel's topmost edge to be 1/6th from the screen edge
                setLocation(
                        gameScreen.getScreenWidth() * 11/12 - getWidth(), 
                        gameScreen.getScreenHeight() * 1/6);
            }
            else // Otherwise the cursor must be on the right half of the screen
            {
                Game.logDebug("Cursor is on the right for: " + this.toString());
                // set the panel's leftmost edge to be 1/12th from the screen edge
                // set the panel's topmost edge to be 1/6th from the screen edge
                setLocation(
                        gameScreen.getScreenSize().width * 1/12, 
                        gameScreen.getScreenSize().height * 1/6);
            }
        }
    }
    protected void maximize() {
        setLocation(0,0);
        setSize(gameScreen.getScreenSize());
    }
    
}