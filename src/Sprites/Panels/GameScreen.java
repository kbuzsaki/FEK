/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Game.Settings.GameSettings;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameScreen extends JPanel{
    private static final int DEFAULT_ZOOM = 1;
    
    private GameSettings settings;
    
    public GameScreen(Rectangle bounds, GameSettings settings) {
        setLayout(null);
        setBounds(bounds);
        
        this.settings = settings;
    }
    
    public void paintComponent(Graphics g) {
        updateZoom(g);
        super.paintComponent(g);
    }
    // TODO: optimize updateZoom, updateComponentTreeUI() is very wasteful
    private void updateZoom(Graphics g) {
        if(settings.getZoom() != DEFAULT_ZOOM)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(settings.getZoom(), settings.getZoom());
            SwingUtilities.updateComponentTreeUI(this); // very wasteful
        }
    }
}
