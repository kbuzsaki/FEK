/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.Staff;
import Units.Items.Weapon;
import java.util.ArrayList;

/*
 * Needs to:
 * 1. hold the items in order
 * 2. hold the equiped item
 * 3. be able to tell if an item can be equiped
 * 4. be able to get the next equipable item (unit should call this)
 * 5. drop items
 * 6. trade items with another inventory
 */
public final class Inventory 
{
    
    // FIXME: Redo inventory, esp functions
    private ArrayList<Item> inventory;
    private Unit owner;
    
    public static final int MAX_CAPACITY = 5;
    
    public Inventory() {
        inventory = new ArrayList();
    }
    public Inventory(Item... inventory) {
        this.inventory = new ArrayList(5);
        for (int i = 0; (i < MAX_CAPACITY) && (i < inventory.length); i++) 
        {
            this.add(inventory[i]);
        }
    }
    
    public final void setOwner(Unit owner) {
        this.owner = owner;
        
        for (int i = 0; i < inventory.size(); i++) 
        {
            inventory.get(i).setOwner(owner);
        }
    }
    public final int getSize() {
        return inventory.size();
    }
    public final void add(Item item) {
        inventory.add(item);
        item.setOwner(owner);
    }
    public final void drop(int index) {
        inventory.remove(index);
    }
    
    public Item[] getItems() {
        return inventory.toArray(new Item[inventory.size()]);
    }
    public Item getItem(int index) {
        assert (index >= 0)&&(index < MAX_CAPACITY) : "Invalid inventory index";
        try {
            return inventory.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    public Equipment[] getEquipment() {
        ArrayList<Equipment> equips = new ArrayList();
        
        for (Item item : inventory)
            if (item instanceof Equipment)
                equips.add((Equipment) item);
        
        return equips.toArray( new Equipment[equips.size()]);
    }
    public Equipment getEquiped() {
        for (Equipment equip : getEquipment())
            if(owner.canEquip(equip))
                return equip;
        return null;
    }
    public boolean hasEquiped() {
        if (getEquiped() != null)
            return true;
        return false;
    }
    
    public Weapon[] getWeapons() {
        ArrayList<Weapon> weapons = new ArrayList();
        
        for (Item item : inventory)
            if (item instanceof Weapon)
                weapons.add((Weapon) item);
        
        return weapons.toArray( new Weapon[weapons.size()]);
    }
    public Weapon[] getEquipableWeapons() {
        ArrayList<Weapon> equipableWeapons = new ArrayList();
        
        for(Weapon weapon : getWeapons())
            if(owner.canEquip(weapon))
                equipableWeapons.add(weapon);
        
        return equipableWeapons.toArray(new Weapon[equipableWeapons.size()]);
    }
    public Weapon getEquipedWeapon() {
        for (Weapon weapon : getWeapons())
            if(owner.canEquip(weapon))
                return weapon;
        return null;
    }
    public boolean hasEquipedWeapon() {
        if (getEquipedWeapon() != null)
            return true;
        return false;
    }
    
    public Staff[] getStaves() {
        ArrayList<Staff> staves = new ArrayList();
        
        for (Item item : inventory)
            if (item instanceof Staff)
                staves.add((Staff) item);
        
        return staves.toArray( new Staff[staves.size()]);
    }
    public Staff[] getEquipableStaves() {
        ArrayList<Staff> equipableStaves = new ArrayList();
        
        for(Staff staff : getStaves())
            if(owner.canEquip(staff))
                equipableStaves.add(staff);
        
        return equipableStaves.toArray(new Staff[equipableStaves.size()]);
    }
    public Staff getEquipedStaff() {
        for (Staff staff : getStaves())
            if(owner.canEquip(staff))
                return staff;
        return null;
    }
    public boolean hasEquipedStaff() {
        if (getEquipedStaff() != null)
            return true;
        return false;
    }
    
    public void useUp(Item item) {
        inventory.remove(item);
    }
    
}
