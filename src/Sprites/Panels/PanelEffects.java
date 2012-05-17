/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import javax.swing.JPanel;

public class PanelEffects extends JPanel {
    
    public PanelEffects(int width, int height) {
        setBounds(0,0, width, height);
        setLayout(null);
        setOpaque(false);
    }
    
    public final void reset() {
        removeAll();
        repaint();
    }
    
}
