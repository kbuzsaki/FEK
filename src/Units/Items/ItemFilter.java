/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units.Items;

public interface ItemFilter {

    /**
     * Returns true if an item can be targeted at all.
     * This returns true if an item should show up in a menu for this action in 
     * the first place, 
     * regardless of whether or not the item is usable in this instance.
     * @param item The item to be filtered
     * @return true if the item can be targeted
     */
    public boolean isTargetable(Item item);
    /**
     * Returns true if an item can be used in this instance.
     * This returns true if an item is currently usable-- in other words, it
     * returns false if the item should be "greyed out".
     * @param item The item to be filtered
     * @return true if the item is currently useable.
     */
    public boolean isUseable(Item item);
}
