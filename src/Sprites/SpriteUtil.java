/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * AnimUtils is a static utilities class for dealing with the movement of 
 * unit animations on the board.
 */
package Sprites;

import Units.Unit;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class SpriteUtil {

    // private constructor to prevent accidental instantiation
    private SpriteUtil() {} 
    
    public static void centerIn(Component component, Rectangle bounds) {
        int offsetX = (component.getWidth() - bounds.width)/2;
        int offsetY = (component.getHeight() - bounds.height)/2;
//        System.out.println(
//                "x: " + bounds.x +
//                " y: " + bounds.y +
//                " pwidth: " + bounds.width +
//                " pheight: " + bounds.height +
//                " width: " + component.getWidth() +
//                " height: " + component.getHeight() +
//                " offsetX: " + offsetX +
//                " offsetY: " + offsetY +
//                " finalx: " + String.valueOf(bounds.x - offsetX) +
//                " finaly: " + String.valueOf(bounds.y - offsetY));
        component.setLocation(bounds.x - offsetX,
                    bounds.y - offsetY);
    }
    // be careful of using with magnified containers
    public static void center(Component component) {
        centerIn(component, 
                new Rectangle(0, 0, 
                component.getParent().getWidth(), 
                component.getParent().getHeight()));
    }
    
    /**
     * "Nudges" a unit's animation a set distance towards its target.
     * @param unitAnim the animation to be moved.
     * @param position the starting position of the unit
     * @param target the target position that the unit is moving towards
     * @param distance the number of pixels to move the unit
     */
    public static void nudgeUnit(AnimationMapUnit unitAnim, Point position, Point target, int distance) {
        int difX = (target.x - position.x);
        int difY = (target.y - position.y);

        unitAnim.translate(
                distance*Integer.signum(difX),
                distance*Integer.signum(difY));
    }
    public static void nudgeUnit(AnimationMapUnit unitAnim, Point position, Point target) {
        nudgeUnit(unitAnim, position, target, 1);
    }
    public static void setAppropriateAnimation(Unit unit, Point postion, Point target) {
        setAppropriateAnimation(unit, 
                target.x - postion.x, 
                target.y - postion.y);
    }
    private static void setAppropriateAnimation(Unit unit, int difX, int difY) {
        if (difX > 0) {
            unit.getMapAnim().setMoveEast();
        } else if (difX < 0) {
            unit.getMapAnim().setMoveWest();
        } else if (difY > 0) {
            unit.getMapAnim().setMoveSouth();
        } else if (difY < 0) {
            unit.getMapAnim().setMoveNorth();
        } 
    }
    
    public static BufferedImage loadImage(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch(IOException ex) {
            System.err.println("Failed to load image from: \"" + filename + "\""); 
        }
        return image;
    }
    public static BufferedImage loadDesaturatedImage(String filename, int[] colorTemplate) {
        BufferedImage image = loadImage(filename);
        image = saturate(image, colorTemplate);
        return image;
    }
    public static boolean saveImage(BufferedImage image, String filename) {
        try {
            ImageIO.write(image, "PNG", new File(filename));
            return true;
        } catch (IOException ex) {
            System.out.println("Error saving image to: \"" + filename + "\"");
            return false;
        }
    }
    public static boolean desaturateAndSave(BufferedImage image, String filename, int[] colorTemplate) {
        return saveImage(desaturate(image, colorTemplate), filename);
    }
    
    private static final int[] templateKeys; static {
        templateKeys = new int[16];
        templateKeys[0] = Color.MAGENTA.getRGB();
        for(int i = 1; i < templateKeys.length; i++)
        {
            templateKeys[i] = templateKeys[i - 1] - 1;
        }
    }
    
    public static final int[] templateBlue = loadTemplate(loadImage("resources/templateBlue.png"));
    public static final int[] templateRed = loadTemplate(loadImage("resources/templateRed.png"));
    
    public static BufferedImage saturate(BufferedImage image, int[] colorTemplate) {
        BufferedImage saturatedImage = new BufferedImage(image.getWidth(), 
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        saturatedImage.getGraphics().drawImage(image, 0, 0, null);
        
        if(colorTemplate != null)
            filterImage(saturatedImage, templateKeys, colorTemplate);
        
        return saturatedImage;
    }
    public static BufferedImage desaturate(BufferedImage image, int[] colorTemplate) {
        BufferedImage desaturatedImage = new BufferedImage(image.getWidth(), 
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        desaturatedImage.getGraphics().drawImage(image, 0, 0, null);
        
        filterImage(desaturatedImage, colorTemplate, templateKeys);
        
        return desaturatedImage;
    }
    
    private static void filterImage(BufferedImage image, int[] keyColors, int[] templateColors) {
        // iterate across every pixel
        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                // iterate through the array of key colors
                for(int i = 0; i < keyColors.length && i < templateColors.length; i++)
                {
                    // if the pixel is one of the key colors, replace it
                    // with the appropriate template color
                    if(image.getRGB(x, y) == keyColors[i])
                    {
                        image.setRGB(x, y, templateColors[i]);
                        break; // break after the replace to prevent multiple writes
                    }
                }
            }
        }   
    }

    public static int[] loadTemplate(String filename) {
        return loadTemplate(loadImage(filename));
    }
    public static int[] loadTemplate(BufferedImage image) {
        int[] colorTemplate = new int[image.getWidth()*image.getHeight()];
        
        for(int x = 0; x < image.getWidth(); x++)
            for(int y = 0; y < image.getHeight(); y++)
                colorTemplate[x*image.getHeight() + y] = image.getRGB(x, y);
        
        return colorTemplate;
    }
    public static BufferedImage createTemplateImage(int[] colorTemplate) {
        BufferedImage template = new BufferedImage(colorTemplate.length, 1, BufferedImage.TYPE_INT_ARGB);
        
        for(int i = 0; i < colorTemplate.length; i++)
        {
            template.setRGB(i, 0, colorTemplate[i]);
        }
            
        return template;
    }
    public static void saveTemplate(int[] colorTemplate, String filename) {
        saveImage(createTemplateImage(colorTemplate), filename);
    }
    
    private static ArrayList<Integer> getColorSet(BufferedImage image) {
        ArrayList<Integer> colorSet = new ArrayList();
        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                int color = image.getRGB(x, y);
                if(!colorSet.contains(color))
                    colorSet.add(color);
            }
        }
        return colorSet;
    }
    private static ArrayList<ArrayList<Integer>> getExclusiveTemplates(BufferedImage... images) {
        ArrayList<ArrayList<Integer>> colorSets = new ArrayList(images.length);
        for(int i = 0; i < images.length; i++)
        {
            colorSets.add(getColorSet(images[i]));
        }
        
        // for every color in the first set (colors that could potentially be in each set)
        for(Integer color : colorSets.get(0))
        {
            // check if it is contained in each set (not part of a color Template)
            boolean containedInEach = true;
            for(int i = 1; i < colorSets.size(); i++)
            {
                if(!colorSets.get(i).contains(color))
                {
                    containedInEach = false;
                    break;
                }
            }
            
            // if it is contained in each set, remove it from each set
            if(containedInEach)
            {
                for(ArrayList<Integer> colorSet : colorSets)
                    colorSet.remove(color);
            }
        }
        // remaining colors in each set are not shared by at least one of the sets
        return colorSets;
    }
}
