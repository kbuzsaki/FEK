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

public class Animation extends Component{
    
    /**
     * The desired sequence for frames to be played in. 
     * Each row is a separate animation, each column is frame.
     */
    private final int sequence[][]; 
    
    private String filepath;
    private BufferedImage wholeImage;
    private int height; // The height of an individual frame
    private int width; // The width of an individual frame
    private int numberAnimations;
    private int maxNumberFrames;
    /**
     * A two-dimensional array of frames for each animation 
     */
    private BufferedImage[][] animation;
    private int animNum; // which animation is shown, assigned from final ints
    private int tick; // which frame of which animation is shown
    private boolean isPlaying = true;
    
    private final float[] scales = { 1f, 1f, 1f, 1f };
    private static final float[] offsets = new float[4];
    private RescaleOp effects = new RescaleOp(scales, offsets, null);
    private int opacityTick = 3;
    
    public Animation(String filepath, int numberAnimations, int maxNumberFrames, int[][] sequence) {
        animNum = 0;
        tick = 0;
        this.sequence = sequence;
        
        loadAnimation(filepath, numberAnimations, maxNumberFrames);
    }
    
    protected void loadAnimation(String filepath, int numberAnimations, int maxNumberFrames) {
        this.numberAnimations = numberAnimations;
        this.maxNumberFrames = maxNumberFrames;
        
        this.filepath = "resources/animations/" + filepath + ".png";
        animation = new BufferedImage[numberAnimations][maxNumberFrames];
        try {
            wholeImage = ImageIO.read(new File(this.filepath));
        } catch (IOException ex) {}
        
        height = wholeImage.getHeight()/numberAnimations;
        width = wholeImage.getWidth()/maxNumberFrames;
        for (int row = 0; row < numberAnimations; row++)
        {
            for (int column = 0; column < maxNumberFrames; column++)
            {
                animation[row][column] = wholeImage.getSubimage(
                       column*width, row*height, width, height);
            }
        }
        setSize(width,height);
        setPreferredSize(getSize());
    }
    protected void loadAnimation(Animation anim) {
        loadAnimation(anim.filepath, anim.numberAnimations, anim.maxNumberFrames);
        animation = anim.animation.clone();
    }
    
    public void setCenteredOn(Rectangle rectangle) {
        int offsetX = (getWidth() - rectangle.width)/2;
        int offsetY = (getHeight() - rectangle.height)/2;
        setLocation(rectangle.x - offsetX,
                    rectangle.y - offsetY);
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
    }
    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Sets the general tick for the animation.
     * @param tick general tick, can be any integer
     */
    public void setTick(int tick) {
        if (isPlaying) 
        {
            this.tick = tick;
        }
        repaint();
    }
    /**
     * Sets which animation will be displayed.
     * @param animNum must be between 0 (inclusive) and animation.length (inclusive).
     */
    protected void setAnimation(int animNum) {
        this.animNum = animNum;
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
    
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(animation[animNum][sequence[animNum][findFrame()]], effects, 0, 0);
    }
    
}
