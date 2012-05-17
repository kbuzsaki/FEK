/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites.Panels;

import Sprites.Character;
import Sprites.ImageComponent;
import Sprites.SmallNumber;
import Sprites.Tickable;
import Units.Faction;
import javax.swing.JPanel;

public class PanelHealth extends JPanel implements Tickable{
    private ImageComponent background;
    private ImageComponent healthBar1;
    private ImageComponent healthBar2;
    
    private ImageComponent name;
    private ImageComponent healthNumber;
    
    private final int healthBarMaxWidth;
    private int healthCap;
    
    private int health;
    private int futureHealth;
    
    public PanelHealth() {
        background = new ImageComponent("resources/gui/window/healthDisplayBlue.png");
        healthBar1 = new ImageComponent("resources/gui/window/healthDisplayBar.png");
        healthBar2 = new ImageComponent("resources/gui/window/healthDisplayBar.png");
        
        healthBarMaxWidth = healthBar1.getWidth();
        
        healthBar1.setLocation(27 + 1, 19);
        healthBar2.setLocation(27, 19 + 1);
        
        name = Character.getImageComponent("Lyn");
        name.setLocation( 9 + (60 - name.getWidth()) / 2, 3 );
        
        healthNumber = SmallNumber.getImageComponent("16");
        healthNumber.setLocation(23 - healthNumber.getWidth(), 16);
        
        
        add(name);
        add(healthNumber);
        add(healthBar1);
        add(healthBar2);
        add(background);
        
        setLayout(null);
        setOpaque(false);
        setVisible(true);
        
        setSize(background.getWidth(), background.getHeight());
        setPreferredSize(getSize());
    }
    
    public void setFaction (Faction faction) {
        background.setImage("resources/gui/window/healthDisplay" + faction.name + ".png");
    }
    
    public void setValues (String unitName, int health, int maxHealth) {
        name.setImage(Character.getImageComponent(unitName));
        name.setLocation( 9 + (60 - name.getWidth()) / 2, 3 );
        
        this.healthCap = maxHealth;
        
        setValues(health);
    }
    
    public void setValues(int health) {
        this.health = health;
        
        healthNumber.setImage(SmallNumber.getImageComponent(String.valueOf(health)));
        healthNumber.setLocation(23 - healthNumber.getWidth(), 16);
        
        int healthBarWidth = healthBarMaxWidth * health / healthCap;
        
        healthBar1.setSize(healthBarWidth, healthBar1.getHeight());
        healthBar2.setSize(healthBarWidth, healthBar2.getHeight());
        
        repaint();
    }
    
    public void setFutureValues(int futureHealth) {
        this.futureHealth = futureHealth;
    }
    
    public boolean tick() {
        if (health == futureHealth)
        {
            return true;
        }
        else if(health > futureHealth)
        {
            health--;
        }
        else if(health < futureHealth)
        {
            health++;
        }
        setValues(health);
        return false;
    }
}
