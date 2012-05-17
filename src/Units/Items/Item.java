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
    
    public static final int width = 16;
    public static final int height = 16;
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
        return new ImageComponent(spriteSheet.getSubimage(spriteX*width, spriteY*height, width, height));
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
    public boolean isBroken() {
        return (uses <= 0);
    }
    
    /**
     * "Uses" an item, by removing one use from its use count. If the item is
     * broken (has 0 uses), the method returns true. Otherwise, returns false.
     * @return true if the item is used up
     */
    public boolean use() {
        uses--;
        return isBroken();
    }
}