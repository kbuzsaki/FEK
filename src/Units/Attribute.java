/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Game.Game;
import java.util.Random;

public class Attribute {
    private AttributeType tag;
    private int value;
    private int effectiveValue;
    private int valueCap;
    /**
     * The integer equivalent of the growth rate percentage. 
     * <br>Random number input ranges from 0 (inclusive) to 99 (inclusive)
     * <br>Growth rates above 100 will have a guaranteed growth per 100, and
     * only one test will be run on the result of:
     * <br> <code>(growthRate % 100)</code>.
     */
    private int growthRate; 
    static final int maxGrowthChance = 100;
    private Random randNum = new Random();
    
    public Attribute(Attribute attribute) {
        this.tag = attribute.tag;
        this.value = attribute.value;
        this.effectiveValue = attribute.value;
        this.valueCap = attribute.valueCap;
        this.growthRate = attribute.growthRate;
    }
    
    public Attribute(AttributeType tag, int value, int valueCap, int growthRate) {
        this.tag = tag;
        this.value = value;
        this.effectiveValue = value;
        this.valueCap = valueCap;
        this.growthRate = growthRate;
    }
    
    public void reset() {
        effectiveValue = value;
    }
    /**
     * Uses the rng to determine increase in an attribute during level up.
     * <br> Increases value per 100 percent of growth rate, and runs a check
     * on the result of <code>(growthRate % 100)</code> for extra increases.
     * @return total number of increments.
     */
    public int levelUp() {
        int valueIncrease = 0; 
        
        // increases once per guaranteed chance
        valueIncrease = growthRate / maxGrowthChance; 
        
        // rolls for growth chance
        if((growthRate%maxGrowthChance) > getRand()) {
            valueIncrease++; 
        }
        
        if (value + valueIncrease <= valueCap) {
            value += valueIncrease;
        }
        else {
            valueIncrease = valueCap - value;
            value = valueCap;
        }
        
        Game.log(this.getName() + " " + valueIncrease + " - " + value + "/" + valueCap);
        reset();
        return valueIncrease;
    }
    
    public String getName() {
        return tag.name;
    }
    public String getDescription() {
        return tag.description;
    }
    /**
     * Accesses the base value for the attribute, before modifiers.
     * @return value
     */
    public int getValue() {
        return value;
    }
    /**
     * Access the current effective value of the attribute.
     * @return effectiveValue
     */
    public int get() {
        return effectiveValue;
    }
    /**
     * Accesses the maximum value for this attribute in this class
     * @return valueCap
     */
    public int getCap() {
        return valueCap;
    }
    
    /**
     * Assigns a new real value to the Attribute.
     * <br>proportionately 
     * @param value The desired real value to be assigned.
     */
    public void setValue(int value) {
        effectiveValue += (this.value-value);
        this.value = value;
    }
    public void set(int effectiveValue) {
        this.effectiveValue = effectiveValue;
    }
    
    private int getRand() {
        return randNum.nextInt(maxGrowthChance);
    }
}
