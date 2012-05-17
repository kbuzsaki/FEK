/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageComponent extends Component{
    
    public static final ImageComponent blank = new ImageComponent("resources/blank.png");
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
    
    public final void setImage() {
        setImage(blank);
    }
    public final void setImage(String filepath) {
        this.filepath = filepath;
        try {
            img = ImageIO.read(new File(filepath));
        } catch (IOException ex) {
            System.out.println("file: " + filepath + " does not exist.");
        }
        
        setSize(img.getWidth(), img.getHeight());
        setPreferredSize(getSize());
    }
    public final void setImage(BufferedImage img) {
        this.filepath = "";
        this.img = img;
        
        setSize(img.getWidth(), img.getHeight());
        setPreferredSize(getSize());
    }
    public final void setImage(ImageComponent img) {
        setImage(img.img);
        filepath = img.filepath;
    }
    public BufferedImage getImage() {
        return img;
    }
    public void saveImage(String filepath) {
        try {
        ImageIO.write(img, "PNG", new File(filepath));
        this.filepath = filepath;
        } catch (IOException ex) {
            System.out.println("Error saving image to \"" + filepath + "\"");
        }
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
        ImageComponent combined = imageComponentList[0];
        for (int i = 1; i < imageComponentList.length; i++)
        {
            combined = combineHorizontally(combined, imageComponentList[i], overlap);
        }
        return combined;
    }
    
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
