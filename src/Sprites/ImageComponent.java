/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import static Maps.Map.tileS;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageComponent extends Component{
    
    private static final BufferedImage blank = new BufferedImage(tileS, tileS, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage img;
    public String filepath;

    public ImageComponent() {
        setImage(blank);
    }
    public ImageComponent(String filepath) {
        setImage(filepath);
    }
    public ImageComponent(BufferedImage img) {
        setImage(img);
    }
    
    public final void setBlank() {
        setImage(blank);
    }
    public final void setImage(String filepath) {
        this.filepath = filepath;
        img = SpriteUtil.loadImage(filepath);
        
        setSize(img.getWidth(), img.getHeight());
        setPreferredSize(getSize());
    }
    public final void setImage(BufferedImage img) {
        this.filepath = "";
        this.img = img;
        
        setSize(img.getWidth(), img.getHeight());
        setPreferredSize(getSize());
    }
    public final void setImage(ImageComponent imgC) {
        setImage(imgC.img);
        filepath = imgC.filepath;
    }
    public BufferedImage getImage() {
        return img;
    }
    public void saveImage(String filepath) {
        if(SpriteUtil.saveImage(img, filepath))
            this.filepath = filepath;
    }
    
    /**
     * Combines the BufferedImages of two ImageComponents by placing them side by side.
     * @param comp1 The ImageComponent that is placed on the left.
     * @param comp2 The ImageComponent that is placed on the right.
     * @param overlap The number of pixels to the left that the second image will be shifted.
     * @return The combined ImageComponent. Has the combined width and greater of the two heights in dimensions.
     */
    public static ImageComponent combineHorizontally(ImageComponent comp1, ImageComponent comp2, int overlap) {
        int width = comp1.getWidth() + comp2.getWidth() - overlap;
        int height = comp1.getHeight() > comp2.getHeight() ? comp1.getHeight() : comp2.getHeight();
        
        BufferedImage combinedImage = new BufferedImage(width, height, comp1.img.getType());
        
        combinedImage.createGraphics().drawImage(comp1.img, 0, ((height - comp1.getHeight()) / 2), null);
        combinedImage.createGraphics().drawImage(comp2.img, comp1.getWidth() - overlap, ((height - comp2.getHeight()) / 2), null);
        
        return new ImageComponent(combinedImage);
    }
    /**
     * Combines the BufferedImages of the given ImageComponents by placing them side by side.
     * @param imageComponentList The ImageComponents to be concatenated, placed in order from left to right.
     * @param overlap The number of pixels to the left that the second image will be shifted.
     * @return The combined ImageComponent. Has the combined width minus each 
     *         overlap and greatest of the heights in dimensions.
     */
    public static ImageComponent combineHorizontally(ImageComponent[] imageComponentList, int overlap) {
        assert imageComponentList.length > 1 : "ImageComponentList size invalid: " + imageComponentList.length;
        ImageComponent combined = imageComponentList[0];
        for (int i = 1; i < imageComponentList.length; i++)
        {
            combined = combineHorizontally(combined, imageComponentList[i], overlap);
        }
        return combined;
    }
    public static ImageComponent combineVertically(ImageComponent comp1, ImageComponent comp2, int overlap) {
        int width = comp1.getWidth() > comp2.getWidth() ? comp1.getWidth() : comp2.getWidth();
        int height = comp1.getHeight() + comp2.getHeight() - overlap;
        
        BufferedImage combinedImage = new BufferedImage(width, height, comp1.img.getType());
        
        combinedImage.createGraphics().drawImage(comp1.img, 0, 0, null);
        combinedImage.createGraphics().drawImage(comp2.img, 0, comp1.getHeight() - overlap, null);
        
        return new ImageComponent(combinedImage);
    }
    public static ImageComponent combineVertically(ImageComponent[] imageComponentList, int overlap) {
        ImageComponent combined = imageComponentList[0];
        for(int i = 1; i < imageComponentList.length; i++)
        {
            combined = combineVertically(combined, imageComponentList[i], overlap);
        }
        return combined;
    }
    
    @Override
    public void paint(Graphics g) {
//        int x = getWidth() - img.getWidth();
//        int y = getHeight() - img.getHeight();
        g.drawImage(img, 0, 0, null);
    }
}
