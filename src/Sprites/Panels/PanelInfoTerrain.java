/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Game.CursorMovementEvent;
import Game.CursorMovementListener;
import Maps.Terrain;
import Sprites.Text;
import Sprites.ColumnLayout;
import Sprites.ImageComponent;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelInfoTerrain extends JPanel implements CursorMovementListener {
    private ImageComponent terrainName;
    private ImageComponent terrainIcon;
    private ImageComponent avoid;
    private ImageComponent defense;
    
    public PanelInfoTerrain() {
        setBorder(new LineBorder(Color.YELLOW));
        setOpaque(false);
        setLayout(new ColumnLayout(10,0,8,ColumnLayout.CENTER));
//        setLayout(null);
        
        terrainName = Text.getImageComponent("Plains");
        terrainIcon = new ImageComponent();
        avoid = Text.getImageComponent("Avoid: " + String.valueOf(20));
        defense = Text.getImageComponent("Defense: " + String.valueOf(4));
        
        add(terrainName);
        add(terrainIcon);
        add(avoid);
        add(defense);
    }
    
    @Override
    public void handleCursorMovement(CursorMovementEvent event) {
        setValues(event.getTerrain(), event.getTerrainIcon());
    }
    
    private void setValues(Terrain terrain, BufferedImage terrainIcon) {
        terrainName.setImage(Text.getImageComponent(terrain.name));
        this.terrainIcon.setImage(terrainIcon);
        avoid.setImage(Text.getImageComponent("Avoid: " + String.valueOf(terrain.avoidBonus)));
        defense.setImage(Text.getImageComponent("Defense: " + String.valueOf(terrain.defenseBonus)));
        repaint();
    }
}
