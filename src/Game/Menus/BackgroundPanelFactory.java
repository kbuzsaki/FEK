/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import static Game.Menus.GamePanel.GUI_WINDOW_DIR;
import Sprites.ImageComponent;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BackgroundPanelFactory {
    public static final String BLUE = GUI_WINDOW_DIR + "windowBlue.png";
    public static final String ORANGE = GUI_WINDOW_DIR + "windowOrange.png";
    private static final String namePlateTextFilename = GUI_WINDOW_DIR + "namePlateWidthText.png";
    private static final String namePlateItemFilename = GUI_WINDOW_DIR + "namePlateWidthItem.png";
    
    private static final BufferedImage namePlate = (new ImageComponent(namePlateTextFilename)).getImage();
    private int namePlateUnitWidth;
    private static final BufferedImage namePlateItem = (new ImageComponent(namePlateItemFilename)).getImage();
    private int namePlateItemUnitWidth;
    
    private int borderTop;
    private int borderBottom;
    private int borderLeft;
    private int borderRight;
    private static final int PATTERN_WIDTH = 8;
    
    // the different segments of the panel. 
    private BufferedImage cornerTopLeft;
    private BufferedImage cornerTopRight;
    private BufferedImage cornerBottomLeft;
    private BufferedImage cornerBottomRight;
    private BufferedImage sideTop;
    private BufferedImage sideBottom;
    private BufferedImage sideLeft;
    private BufferedImage sideRight;
    private BufferedImage centerPattern;

    public BackgroundPanelFactory(String spriteFilename) {
        loadPanelSprites(spriteFilename);
        // TODO: refactor to deal with different border sizes (center namePlate around panel)
        // REFACTOR: potentially dangerous, namePlate border width is not necessarily the same as the panel's
        namePlateUnitWidth = getUnitWidth(namePlate.getWidth());
        namePlateItemUnitWidth = getUnitWidth(namePlateItem.getWidth());
    }
    public BackgroundPanelFactory() {
        this(BLUE);
    }
    
    private void loadPanelSprites(String spriteFilename) {
        BufferedImage panelSpriteSheet = null;
        try {
            panelSpriteSheet = ImageIO.read(new File(spriteFilename));
            /*
            * Determines the dimensions of the sprites with the following rules:
            * * The central pattern is always 8x8.
            * * The central pattern is always centered in the sprite sheet.
            * * If the sprite sheet is of odd dimensions (the borders are of unequal
            * dimensions), then the lower and right borders will be larger than 
            * the up and left borders..
            */
            int borderY = panelSpriteSheet.getHeight() - PATTERN_WIDTH;
            borderTop = borderY / 2;
            borderBottom = (borderY % 2 == 0) ? borderY / 2 : borderY / 2 + 1;
            int borderX = panelSpriteSheet.getWidth() - PATTERN_WIDTH;
            borderLeft = borderX / 2;
            borderRight = (borderX % 2 == 0) ? borderX / 2 : borderX / 2 + 1;

            // cuts up the sprite sheet into the appropriate panel sprites
            // using the dimensions determined above
            cornerTopLeft = panelSpriteSheet.getSubimage(
                    0, 0, borderLeft, borderTop);
            cornerTopRight = panelSpriteSheet.getSubimage(
                    borderLeft + PATTERN_WIDTH, 0, borderRight, borderTop);
            cornerBottomLeft = panelSpriteSheet.getSubimage(
                    0, borderTop + PATTERN_WIDTH, borderLeft, borderBottom);
            cornerBottomRight = panelSpriteSheet.getSubimage(
                    borderLeft + PATTERN_WIDTH, borderTop + PATTERN_WIDTH, 
                    borderRight, borderBottom);
            sideTop = panelSpriteSheet.getSubimage(
                    borderLeft, 0, PATTERN_WIDTH, borderTop);
            sideLeft = panelSpriteSheet.getSubimage(
                    0, borderTop, borderLeft, PATTERN_WIDTH);
            sideRight = panelSpriteSheet.getSubimage(borderLeft + PATTERN_WIDTH, 
                    borderTop, borderRight, PATTERN_WIDTH);
            sideBottom = panelSpriteSheet.getSubimage(borderLeft, 
                    borderTop + PATTERN_WIDTH, PATTERN_WIDTH, borderBottom);
            centerPattern = panelSpriteSheet.getSubimage(borderLeft, borderTop, 
                    PATTERN_WIDTH, PATTERN_WIDTH);
        } catch (IOException ex) {
            System.err.println("file: " + spriteFilename + " does not exist. Cannot load menu images.");
            // FIXME: somehow recover from missing resources?
        }
   }
    
    public BufferedImage getPanel(int unitWidth, int unitHeight) {
        int sideWidth = borderLeft + borderRight + PATTERN_WIDTH*unitWidth;
        int sideHeight = borderTop + borderBottom + PATTERN_WIDTH*unitHeight;
        BufferedImage panel = new BufferedImage(sideWidth, sideHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D panelGraphics = panel.createGraphics();
        
        // paint the corner borders
        panelGraphics.drawImage(cornerTopLeft, 0, 0, null);
        panelGraphics.drawImage(cornerTopRight, 
                sideWidth - borderRight, 0, null);
        panelGraphics.drawImage(cornerBottomLeft, 0, 
                sideHeight - borderBottom, null);
        panelGraphics.drawImage(cornerBottomRight, 
                sideWidth - borderRight,
                sideHeight - borderBottom, null);
        
        // paint the top and bottom borders
        for(int x = 0; x < unitWidth; x++) {
            panelGraphics.drawImage(sideTop, 
                    borderLeft + x*PATTERN_WIDTH, 0, null);
            panelGraphics.drawImage(sideBottom, 
                    borderLeft + x*PATTERN_WIDTH, 
                    sideHeight - borderBottom, null);
        }
        // paint the left and right borders
        for(int y = 0; y < unitHeight; y++) {
            panelGraphics.drawImage(sideLeft, 
                    0, borderTop + y*PATTERN_WIDTH, null);
            panelGraphics.drawImage(sideRight,
                    sideWidth - borderRight,
                    borderTop + y*PATTERN_WIDTH, null);
        }
        
        // paint the center pattern
        for(int x = 0; x < unitWidth; x++)
        {
            for(int y = 0; y < unitHeight; y++)
            {
                panelGraphics.drawImage(centerPattern, 
                        borderLeft + x*PATTERN_WIDTH,
                        borderTop + y*PATTERN_WIDTH, null);
            }
        }   
        
        return panel;
    }
    public BufferedImage getNamePanel(int unitHeight) {
        BufferedImage panel = getPanel(namePlateUnitWidth, unitHeight);
        BufferedImage namePanel = new BufferedImage(panel.getWidth(), 
                panel.getHeight() - getBorderTop() + namePlate.getHeight(), 
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D namePanelGraphics = namePanel.createGraphics();
        
        namePanelGraphics.drawImage(namePlate, 0, 0, null);
        namePanelGraphics.drawImage(panel.getSubimage(0, getBorderTop(), 
                panel.getWidth(), panel.getHeight() - getBorderTop()), 
                0, namePlate.getHeight(), null);
        
        return namePanel;
    }
    public BufferedImage getNamePanelItem(int unitHeight) {
        BufferedImage panel = getPanel(namePlateItemUnitWidth, unitHeight);
        BufferedImage namePanel = new BufferedImage(panel.getWidth(), 
                panel.getHeight() - getBorderTop() + namePlateItem.getHeight(), 
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D namePanelGraphics = namePanel.createGraphics();
        
        namePanelGraphics.drawImage(namePlateItem, 0, 0, null);
        namePanelGraphics.drawImage(panel.getSubimage(0, getBorderTop(), 
                panel.getWidth(), panel.getHeight() - getBorderTop()), 
                0, namePlateItem.getHeight(), null);
        
        return namePanel;
    }
    
    public int getBorderY() {
        return borderTop + borderBottom;
    }
    public int getBorderTop() {
        return borderTop;
    }
    public int getBorderBottom() {
        return borderBottom;
    }
    
    public int getBorderX() {
        return borderLeft + borderRight;
    }
    public int getBorderLeft() {
        return borderLeft;
    }
    public int getBorderRight() {
        return borderRight;
    }
    
    public int getPatternWidth() {
        return PATTERN_WIDTH;
    }
    
    public int getNamePlateWidth() {
        return namePlate.getWidth();
    }
    public int getNamePlateHeight() {
        return namePlate.getHeight();
    }
    public int getNamePlateItemWidth() {
        return namePlateItem.getWidth();
    }
    public int getNamePlateItemHeight() {
        return namePlateItem.getHeight();
    }
    
    public int getUnitWidth(int pixelWidth) {
        return ceilingDivide(pixelWidth - getBorderX(), PATTERN_WIDTH);
    }
    public int getUnitHeight(int pixelHeight) {
        return ceilingDivide(pixelHeight - getBorderY(), PATTERN_WIDTH);
    }
    public int getMinimumUnitWidth(int contentPixelWidth) {
        return ceilingDivide(contentPixelWidth, PATTERN_WIDTH);
    }
    public int getMinimumUnitHeight(int contentPixelHeight) {
        return ceilingDivide(contentPixelHeight, PATTERN_WIDTH);
    }
    
    private static int ceilingDivide(int numerator, int divisor) {
        return (numerator + divisor - 1) / divisor;
    }
}
