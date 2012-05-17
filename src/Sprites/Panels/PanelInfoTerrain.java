/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Maps.Terrain;
import Sprites.Character;
import Sprites.ImageComponent;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoTerrain extends JPanel {
    private ImageComponent terrainName;
    private ImageComponent terrainIcon;
    private ImageComponent avoid;
    private ImageComponent defense;
    
    public PanelInfoTerrain(Rectangle bounds) {
        this.setBounds(bounds);
        setPreferredSize(getSize());
        setBorder(new LineBorder(Color.YELLOW));
        setOpaque(false);
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 40, 20);
        setLayout(layout);
//        setLayout(null);
        
        terrainName = Character.getImageComponent("Plains");
        terrainIcon = new ImageComponent();
        avoid = Character.getImageComponent("Avoid: " + String.valueOf(20));
        defense = Character.getImageComponent("Defense: " + String.valueOf(4));
        
        add(terrainName);
        add(terrainIcon);
        add(avoid);
        add(defense);
    }
    
    public void setValues(Terrain terrain, BufferedImage terrainIcon) {
        terrainName.setImage(Character.getImageComponent(terrain.name));
        this.terrainIcon.setImage(terrainIcon);
        avoid.setImage(Character.getImageComponent("Avoid: " + String.valueOf(terrain.avoidBonus)));
        defense.setImage(Character.getImageComponent("Defense: " + String.valueOf(terrain.defenseBonus)));
        repaint();
    }
}
