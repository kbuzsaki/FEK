/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum SmallNumber {
    ONE("1", 0),
    TWO("2", 1),
    THREE("3", 2),
    FOUR("4", 3),
    FIVE("5", 4),
    SIX("6", 5),
    SEVEN("7", 6),
    EIGHT("8", 7),
    NINE("9", 8),
    ZERO("0", 9);
    
    private static final String spriteSheetFilepath = "resources/gui/text/numbersSmall.png";
    
    private static final BufferedImage spritesheet;
    static {
        BufferedImage loadedSpriteSheet = null;
        try {
            loadedSpriteSheet = ImageIO.read(new File(spriteSheetFilepath));
        } catch (IOException ex) {
            System.out.println("file: " + spriteSheetFilepath + " does not exist.");
        }
        spritesheet = loadedSpriteSheet;
    }
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int DEFAULT_OVERLAP = 0;
    
    public final String character;
    public final int x;
    
    private SmallNumber(String character, int x) {
        this.character = character;
        this.x = x;
    }
    
    public ImageComponent getImageComponent() {
        return new ImageComponent(spritesheet.getSubimage(x*WIDTH, 0, WIDTH, HEIGHT));
    }
    
    public static ImageComponent getImageComponent(String text) {
        return ImageComponent.combineHorizontally(getImageComponents(text), DEFAULT_OVERLAP);
    }
    private static ImageComponent[] getImageComponents(String text) {
        ImageComponent[] imageComponentList = new ImageComponent[text.length()];
        SmallNumber[] characterList = getCharacters(text);
        
        for (int i = 0; i < imageComponentList.length; i++)
        {
            imageComponentList[i] = characterList[i].getImageComponent();
        }
        
        return imageComponentList;
    }
    private static SmallNumber[] getCharacters(String text) {
        SmallNumber[] characterList = new SmallNumber[text.length()];
        
        for (int i = 0; i < text.length(); i++)
        {
            characterList[i] = getCharacter(text.substring(i, i+1));
        }
        
        return characterList;
    }
    private static SmallNumber getCharacter(String character) {
        for (SmallNumber number : SmallNumber.values())
            if (character.equals(number.character))
                return number;
        
        return ZERO;
    }
    
    
    
    
    
}
