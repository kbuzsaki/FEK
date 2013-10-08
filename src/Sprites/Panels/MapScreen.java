/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Game.Settings.GameSettings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MapScreen extends JPanel{
    private static final int DEFAULT_ZOOM = 1;
    private static final long REFRESH_RATE = 10;
    
    private GameSettings settings;
    
    private JLayeredPane unitsPane;
    private JPanel panelTop;
    private JPanel panelEffects;
    private PanelEffectsArrow arrowEffectsPanel;
    private PanelEffectsTile tileEffectsPanel;
    private ArrayList<Component> componentsToResize;
    
    public MapScreen(GameSettings settings) {
        this.settings = settings;
        componentsToResize = new ArrayList();
        setLayout(null);
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        for(Component comp : componentsToResize) 
            comp.setSize(width, height);
    }
    @Override
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }
    
    public void addComponentToResize(Component c) {
        add(c);
        componentsToResize.add(c);
    }
    
    private long lastRepaint = 0;
    public void requestRepaint() {
        if(lastRepaint > REFRESH_RATE)
        {
            repaint();
//            System.out.println("Repainting");
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
//        updateZoom(g);
//        super.paintComponent(g);
        
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        super.paintComponent(imageGraphics);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(settings.getZoom(), settings.getZoom());
        g2d.drawImage(image, 0, 0, null);
        // must be repainted manually (see ThreadAnimation and ThreadMovement)
//        repaint();
    }
    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        // all calls to repaint repaint the entire screen
        lastRepaint = System.currentTimeMillis();
        super.repaint(tm, 0, 0, getWidth(), getHeight());
//        int zoom;
//        if(settings != null)
//            zoom = settings.getZoom();
//        else
//            zoom = 1;
//        super.repaint(tm, x*zoom, y*zoom, getWidth()*zoom, getHeight()*zoom);
    }
    private void updateZoom(Graphics g) {
        if(settings.getZoom() != DEFAULT_ZOOM)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(settings.getZoom(), settings.getZoom());
            SwingUtilities.updateComponentTreeUI(this); // very wasteful
        }
    }
}
