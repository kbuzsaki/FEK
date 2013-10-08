/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Game.Cursor;
import Game.Settings.GameSettings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class GameScreen extends JPanel {
    private GameSettings settings;
    private MapScreen mapScreen;
    private Cursor cursor;
    
    public GameScreen(GameSettings settings, MapScreen mapScreen) {
        this.settings = settings;
        this.mapScreen = mapScreen;
        setLayout(null);
    }
    
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        centerMap();
    }
    @Override
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }
    
    // TODO: redo screen size and cursor position queries to allow for gameScreens smaller than mapScreen
    public Dimension getScreenSize() {
        return new Dimension(getScreenWidth(), getScreenHeight());
    }
    public int getScreenWidth() {
        return mapScreen.getWidth() / settings.getZoom();
    }
    public int getScreenHeight() {
        return mapScreen.getHeight() / settings.getZoom();
    }
    
    public Point getRelativeCursorPosition() {
        return new Point(getRelativeCursorX(), getRelativeCursorY());
    }
    public int getRelativeCursorX() {
        return cursor.getTileX()/* + mapScreen.getX()*/;
    }
    public int getRelativeCursorY() {
        return cursor.getTileY()/* + mapScreen.getY()*/;
    }
    
    public boolean cursorIsOnLeft() {
        return getRelativeCursorX() < getScreenWidth() / 2;
    }
    public boolean cursorIsOnRight() {
        return getRelativeCursorX() >= getScreenWidth() / 2;
    }
    public boolean cursorIsOnTop() {
        return getRelativeCursorY() < getScreenHeight() / 2;
    }
    public boolean cursorIsOnBottom() {
        return getRelativeCursorY() >= getScreenHeight() / 2;
    }
    
    private Point getRelativeComponentPosition(Component c) {
        return new Point(getRelativeComponentX(c), getRelativeComponentY(c));
    }
    private int getRelativeComponentX(Component c) {
        return c.getX() + mapScreen.getX();
    }
    private int getRelativeComponentY(Component c) {
        return c.getY() + mapScreen.getY();
    }
    
    public boolean isOnLeft(Component c) {
        return getRelativeComponentX(c) < getScreenWidth() / 2;
    }
    public boolean isOnRight(Component c) {
        return getRelativeComponentX(c) > getScreenWidth() / 2;
    }
    public boolean isOnTop(Component c) {
        return getRelativeComponentY(c) < getScreenHeight() / 2;
    }
    public boolean isOnBottom(Component c) {
        return getRelativeComponentY(c) > getScreenHeight() / 2;
    }
    
    public void centerMap() {
        mapScreen.setLocation((getWidth() - mapScreen.getWidth())/2, (getHeight() - mapScreen.getHeight())/2);
    }

}
