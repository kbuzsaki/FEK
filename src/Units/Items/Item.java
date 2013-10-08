/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

import Sprites.ImageComponent;
import Units.Unit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

public abstract class Item implements Serializable {
    protected final String name;
    protected final String description;
    protected final int spriteX;
    protected final int spriteY;
    protected final int totalUses;
    protected int uses;
    protected final int price;
    
    protected Unit owner;
    
    public static final int ICON_EDGE_LENGTH = 16;
    public static final BufferedImage spriteSheet;
    static {
        BufferedImage tempSpriteSheet = null;
        try { 
            tempSpriteSheet = ImageIO.read(new File("resources/item.png"));
        } catch (IOException ex) {
            System.out.println("file: " + "resources/item.png" + " does not exist.");
        }
        spriteSheet = tempSpriteSheet;
    }

    protected Item(String name, int spriteY, int spriteX, String description,
            int uses, int totalUses, int price) {
        this.name = name;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.description = description;
        this.uses = uses;
        this.totalUses = totalUses;
        this.price = price;
    }
    
    public void setOwner(Unit owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    public ImageComponent getIcon() {
        return new ImageComponent(getIconImage());
    }
    public BufferedImage getIconImage() {
        return spriteSheet.getSubimage(
                spriteX*ICON_EDGE_LENGTH, 
                spriteY*ICON_EDGE_LENGTH, 
                ICON_EDGE_LENGTH, 
                ICON_EDGE_LENGTH);
    }
    public String getDescription() {
        return description;
    }
    public final int getValue() {
        return uses*price;
    }
    public final int getUses() {
        return uses;
    }
    public final int getTotalUses() {
        return totalUses;
    }
    
    protected boolean shouldBreak() {
        return (uses <= 0);
    }
    
    public boolean isDiscardable() {
        return true;
    }

    public void decrementUses() {
        uses--;
        
        if(shouldBreak())
        {
            owner.getInventory().breakItem(this);
        }
    }
}