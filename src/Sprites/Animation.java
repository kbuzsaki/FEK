/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Maps.Map;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Animation extends Component implements Animateable {
    
    /**
     * The desired sequence for frames to be played in. 
     * Each row is a separate animation, each column is frame.
     */
    private final int sequence[][]; 
    
    protected String filepath;
    private BufferedImage wholeSpriteSheet;
    private int height; // The height of an individual frame
    private int width; // The width of an individual frame
    private int numberAnimations;
    private int maxNumberFrames;
    /**
     * A two-dimensional array of frames for each animation 
     */
    private BufferedImage[][] animation;
    protected int animNum; // which animation is shown, assigned from final ints
    private int tick; // which frame of which animation is shown
    private boolean isPlaying = true;
    
    private final float[] scales = { 1f, 1f, 1f, 1f };
    private static final float[] offsets = new float[4];
    private RescaleOp effects = new RescaleOp(scales, offsets, null);
    private int opacityTick = 3;
    
    public Animation(BufferedImage spriteSheet, int numberAnimations, int maxNumberFrames, int[][] sequence) {
        animNum = 0;
        tick = 0;
        this.sequence = sequence.clone();
        
        loadAnimation(spriteSheet, numberAnimations, maxNumberFrames);
    }
    public Animation(String filepath, int[] colorTemplate, int numberAnimations, int maxNumberFrames, int[][] sequence) {
        this(SpriteUtil.loadDesaturatedImage(filepath, colorTemplate), numberAnimations, maxNumberFrames, sequence);
        this.filepath = filepath;
    }
    public Animation(String filepath, int numberAnimations, int maxNumberFrames, int[][] sequence) {
        this(filepath, null, numberAnimations, maxNumberFrames, sequence);
    }
    
    protected void loadAnimation(BufferedImage spriteSheet, int numberAnimations, int maxNumberFrames) {
        this.numberAnimations = numberAnimations;
        this.maxNumberFrames = maxNumberFrames;
        this.animation = new BufferedImage[numberAnimations][maxNumberFrames];
        
        wholeSpriteSheet = spriteSheet;
        
        height = wholeSpriteSheet.getHeight()/numberAnimations;
        width = wholeSpriteSheet.getWidth()/maxNumberFrames;
        for (int row = 0; row < numberAnimations; row++)
        {
            for (int column = 0; column < maxNumberFrames; column++)
            {
                animation[row][column] = wholeSpriteSheet.getSubimage(
                       column*width, row*height, width, height);
            }
        }
        setSize(width,height);
        setPreferredSize(getSize());
    }
    public void loadAnimation(Animation anim) {
        loadAnimation(anim.wholeSpriteSheet, anim.numberAnimations, anim.maxNumberFrames);
        animation = anim.animation.clone();
    }
    
    public void setCenteredOn(Rectangle rectangle) {
        SpriteUtil.centerIn(this, rectangle);
    }
    public void setLocationCentered(int x, int y) {
        setLocation(x - getOffsetX(),
                    y - getOffsetY());
    }
    int getOffsetX() {
        
        return (getWidth() - Map.tileS)/2;
    }
    int getOffsetY() {
        return (getHeight() - Map.tileS)/2;
    }
    
    public void translate(int dx, int dy) {
        setLocation(
                getX() + dx,
                getY() + dy);
    }
    
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        forceTick(0);
    }
    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Sets the general tick for the animation.
     * @param tick general tick, can be any integer
     */
    @Override
    public void setTick(int tick) {
        if (isPlaying) 
        {
            this.tick = tick;
        }
    }
    protected void forceTick(int tick) {
        this.tick = tick;
        repaint();
    }
    /**
     * Sets which animation will be displayed.
     * @param animNum must be between 0 (inclusive) and animation.length (inclusive).
     */
    protected void setAnimation(int animNum) {
        this.animNum = animNum;
    }
    protected int getFrame() {
        return findFrame();
    }
    protected int getTick() {
        return tick;
    }
    /**
     * Takes a general tick and returns the corresponding frame of the current animation.
     * @return The frame index corresponding to the tick.
     */
    private int findFrame() {
        return tick % sequence[animNum].length;
    }
    
    public boolean tickOpacity() {
        if (opacityTick > 0)
        {
            opacityTick--;
            setOpacity(((float) opacityTick)/ 3);
            return false;
        }
        else
        {
            return true;
        }
    }
    private void setOpacity(float opacity) {
        scales[3] = opacity;
        effects = new RescaleOp(scales, offsets, null);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        g2d.scale(2,2);
        g2d.drawImage(animation[animNum][sequence[animNum][getFrame()]], effects, 0, 0);
    }
    
    private static final String animationDirectory = "resources/animations/";
    public static String getFilename(String filename) {
        return animationDirectory + filename + ".png";
    }
    
}
