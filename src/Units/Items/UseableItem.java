/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

public abstract class UseableItem extends Item {
    private static final int SPRITE_Y = 8;
    
    public UseableItem (String name, int spriteX, String description, int uses,
            int totalUses, int price) {
        super(name, SPRITE_Y, spriteX, description, uses, totalUses, price);
    }
    
    public abstract boolean canBeUsed();
    public void use() {
        performAction();
        decrementUses();
    }
    protected abstract void performAction();
    
}
