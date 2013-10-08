/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.ColumnLayout;
import Units.Faction;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PanelInfoGame extends JPanel {
    private JLabel phaseLabel;
    
    public PanelInfoGame() {
        setLayout(new ColumnLayout(30,0,8,ColumnLayout.CENTER));
        phaseLabel = new JLabel();
        add(phaseLabel);
    }
    
    public void setCurrentFactionPhase(Faction faction) {
        phaseLabel.setText(faction.name + "'s Turn");
        Color color = Color.BLACK;
        switch (faction) 
        {
            case BLUE:
                color = Color.BLUE;
                break;
            case RED:
                color = Color.RED;
                break;
            
        }
        phaseLabel.setForeground(color);
    }

}
