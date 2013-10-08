/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public enum Text {
    UPPERCASE_A("A", 0, 0, 2),
    UPPERCASE_B("B", 1, 0, 2),
    UPPERCASE_C("C", 2, 0, 2),
    UPPERCASE_D("D", 3, 0, 2),
    UPPERCASE_E("E", 4, 0, 2),
    UPPERCASE_F("F", 5, 0, 2),
    UPPERCASE_G("G", 6, 0, 2),
    UPPERCASE_H("H", 7, 0, 2),
    UPPERCASE_I("I", 8, 0, 3),
    UPPERCASE_J("J", 9, 0, 2),
    UPPERCASE_K("K", 10, 0, 2),
    UPPERCASE_L("L", 11, 0, 3),
    UPPERCASE_M("M", 12, 0, 1),
    UPPERCASE_N("N", 13, 0, 2),
    UPPERCASE_O("O", 14, 0, 2),
    UPPERCASE_P("P", 0, 1, 2),
    UPPERCASE_Q("Q", 1, 1, 1),
    UPPERCASE_R("R", 2, 1, 2),
    UPPERCASE_S("S", 3, 1, 2),
    UPPERCASE_T("T", 4, 1, 1),
    UPPERCASE_U("U", 5, 1, 2),
    UPPERCASE_V("V", 6, 1, 1),
    UPPERCASE_W("W", 7, 1, 1),
    UPPERCASE_X("X", 8, 1, 1),
    UPPERCASE_Y("Y", 9, 1, 1),
    UPPERCASE_Z("Z", 10, 1, 2),
    LOWERCASE_A("a", 0, 2, 1),
    LOWERCASE_B("b", 1, 2, 2),
    LOWERCASE_C("c", 2, 2, 2),
    LOWERCASE_D("d", 3, 2, 2),
    LOWERCASE_E("e", 4, 2, 2),
    LOWERCASE_F("f", 5, 2, 3),
    LOWERCASE_G("g", 6, 2, 2),
    LOWERCASE_H("h", 7, 2, 2),
    LOWERCASE_I("i", 8, 2, 5),
    LOWERCASE_J("j", 9, 2, 3),
    LOWERCASE_K("k", 10, 2, 2),
    LOWERCASE_L("l", 11, 2, 5),
    LOWERCASE_M("m", 12, 2, 1),
    LOWERCASE_N("n", 13, 2, 2),
    LOWERCASE_O("o", 14, 2, 2),
    LOWERCASE_P("p", 0, 3, 2),
    LOWERCASE_Q("q", 1, 3, 2),
    LOWERCASE_R("r", 2, 3, 3),
    LOWERCASE_S("s", 3, 3, 2),
    LOWERCASE_T("t", 4, 3, 3),
    LOWERCASE_U("u", 5, 3, 2),
    LOWERCASE_V("v", 6, 3, 1),
    LOWERCASE_W("w", 7, 3, 1),
    LOWERCASE_X("x", 8, 3, 1),
    LOWERCASE_Y("y", 9, 3, 2),
    LOWERCASE_Z("z", 10, 3, 2),
    ONE("1", 0, 4, 0),
    TWO("2", 1, 4, 0),
    THREE("3", 2, 4, 0),
    FOUR("4", 3, 4, 0),
    FIVE("5", 4, 4, 0),
    SIX("6", 5, 4, 0),
    SEVEN("7", 6, 4, 0),
    EIGHT("8", 7, 4, 0),
    NINE("9", 8, 4, 0),
    ZERO("0", 9, 4, 0),
    SPACE(" ", 10, 4, 4),
    EXCLAMATION_POINT("!", 11, 1, 3),
    QUESTION_MARK("?", 12, 1, 1),
    COMMA(",", 13, 1, 5),
    PERIOD(".", 14, 1, 5),
    COLON(":", 11, 3, 4),
    FORWARD_SLASH("/", 12, 3, 2),
    AMPERSAND("&", 13, 3, 1),
    DASH("-", 14, 3, 3);
    
    private static final String textSpriteSheetFilepath[] = 
                {"resources/gui/text/textWhite.png",
                 "resources/gui/text/textYellow.png",
                 "resources/gui/text/textGreen.png",
                 "resources/gui/text/textGrey.png",
                 "resources/gui/text/textBlue.png"};
    public static final int WHITE = 0;
    public static final int YELLOW = 1;
    public static final int GREEN = 2;
    public static final int GREY = 3;
    public static final int BLUE = 4;
    private static final BufferedImage textSpriteSheet[];
    static {
        /*BufferedImage[]*/ textSpriteSheet = new BufferedImage[textSpriteSheetFilepath.length];
        for (int i = 0; i < textSpriteSheetFilepath.length; i++)
        {
            try {
                textSpriteSheet[i] = ImageIO.read(new File(textSpriteSheetFilepath[i]));
            } catch (IOException ex) {
                System.out.println("file: " + textSpriteSheetFilepath[i] + " does not exist.");
            }
        }
        int x;
//        textSpritesheet = loadedSpriteSheet;
    }
    public static final int WIDTH = 8;
    public static final int HEIGHT = 13;
    public static final int DEFAULT_X_OVERLAP = 1;
    public static final int DEFAULT_Y_OVERLAP = 0;
    
    public final String character;
    public final int x;
    public final int y;
    public final int overlap;
    
    Text(String character, int x, int y, int overlap) {
        this.character = character;
        this.x = x;
        this.y = y;
        this.overlap = overlap;
        
        // Is now declared statically
//        BufferedImage[] loadedSpriteSheet = new BufferedImage[spriteSheetFilepath.length];
//        for (int i = 0; i < spriteSheetFilepath.length; i++)
//        {
//        try {
//            loadedSpriteSheet[i] = ImageIO.read(new File(spriteSheetFilepath[i]));
//        } catch (IOException ex) {
//            System.out.println("file: " + spriteSheetFilepath[WHITE] + " does not exist.");
//        }
//        }
//        spriteSheet = loadedSpriteSheet;
    }
    
    public ImageComponent getImageComponent() {
        return getImageComponent(WHITE);
    }
    public ImageComponent getImageComponent(int color) {
        return new ImageComponent(textSpriteSheet[color].getSubimage(x*WIDTH, y*HEIGHT, WIDTH - overlap, HEIGHT));
    }
    
    public static ImageComponent getImageComponent(String text) {
        return getImageComponent(text, WHITE);
    }
    public static ImageComponent getImageComponent(String text, int color) {
        return ImageComponent.combineHorizontally(getImageComponents(text, color), DEFAULT_X_OVERLAP);
    }
    public static ImageComponent getWrappedImageComponent(String text, int maxWidth) {
        return getWrappedImageComponent(text, maxWidth, WHITE);
    }
    public static ImageComponent getWrappedImageComponent(String text, int maxWidth, int color) {
        String[] words = parseWords(text);
        
        ArrayList<ImageComponent> lines = new ArrayList();
        String line = "";
        for(String word : words)
        {
            int width = getWidth(line + word);
            if(width <= maxWidth)
            {
                line += word;
            }
            else 
            {
                lines.add(getImageComponent(line, color));
                line = word.substring(1); // removes leading space
            }
        }
        lines.add(getImageComponent(line, color)); // adds the last line
        
        return ImageComponent.combineVertically(lines.toArray(new ImageComponent[lines.size()]), DEFAULT_Y_OVERLAP);
    }
    
    private static ImageComponent[] getImageComponents(String text, int color) {
        ImageComponent[] imageComponentList = new ImageComponent[text.length()];
        Text[] characterList = getCharacters(text);
        
        for (int i = 0; i < imageComponentList.length; i++)
        {
            imageComponentList[i] = characterList[i].getImageComponent(color);
        }
        
        return imageComponentList;
    }
    private static Text[] getCharacters(String text) {
        Text[] characterList = new Text[text.length()];
        
        for (int i = 0; i < text.length(); i++)
        {
            characterList[i] = getCharacter(text.substring(i, i+1));
        }
        
        return characterList;
    }
    private static Text getCharacter(String character) {
        for (Text c : Text.values())
            if (character.equals(c.character))
                return c;
        
        return UPPERCASE_A;
    }
    /**
     * Parses a String of text to an array of words. 
     * The method parses the String into words, breaking on white space.
     * Each word in the line of text with a space before it will be parsed 
     * into a word with exactly one space before it, and none after it.
     * Extraneous spacing is ignored.
     * <br>All other characters (e.g. <code>\n</code>) are parsed as if they 
     * were normal characters. Avoid their use.
     * <br>Example parsing:
     * <code>"This is a   sample          sentence."</code>
     * <br>becomes:
     * <code>{"This", " is", " a", " sample", " sentence."}</code>
     * @param text the line of text to be parsed to words.
     * @return A String array of the words in the line of text.
     */
    public static String[] parseWords(String text) {
        // ensures that there is a space at the end of the string for the 
        // parser to token at.
        String parseableText = text + " ";
        
        ArrayList<String> words = new ArrayList();
        int nextWordStart = 0;
        boolean wordHasStarted = false;
        for(int i = 0; i < parseableText.length(); i++) // for every character in the string
        {
            if (parseableText.charAt(i) == ' ')     // if it is a space (word break)
            {
                if(wordHasStarted) // and if a there were characters (for a word) before it
                {
                    String word = parseableText.substring(nextWordStart, i);
                    words.add(word); // add the word to the list of words
                    wordHasStarted = false;
                }
                nextWordStart = i;
            }
            else
            {
                wordHasStarted = true;
            }
        }
        
        return words.toArray(new String[words.size()]);
    }
    private static int getWidth(String text) {
        int width = 0;
        for(Text c : getCharacters(text))
            width += (WIDTH - c.overlap);
        
        width -= ((text.length() - 1)*DEFAULT_X_OVERLAP);
        
        return width;
    }
}
