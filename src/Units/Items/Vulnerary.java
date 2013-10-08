/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

public class Vulnerary extends UseableItem {

    public Vulnerary() {
        super("Vulnerary", 0, "Restores HP.", 3, 3, 10);
    }
    
    @Override
    public boolean canBeUsed() {
        return owner.getStats().getHP().get() < owner.getStats().getHP().getValT();
    }
    
    @Override
    public void performAction() {
        owner.heal(10);
    }
}
