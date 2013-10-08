/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Game.Game;
import Game.Sound.SoundManager;
import Game.Sound.Stoppable;
import Maps.Map;
import Sprites.AnimationMapUnit;
import Sprites.BoardElement;
import Units.Effects.Effect;
import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.ItemFilter;
import Units.Items.Staff;
import Units.Items.UseableItem;
import Units.Items.Weapon;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 */
public class Unit extends BoardElement implements ItemFilter {
    private Map map;
    
    private boolean isDepleted;
    
    private AnimationMapUnit mapAnim;
    private String name;
    private Faction faction;
    private MoveType moveType;
    private Stats stats;
    private Inventory inventory;
    
    private boolean hasTraded;
    
    private Point startPosition;
    private ArrayList<Point> potentialPath;
    
    private ArrayList<ArrayList<Point>> pathList; // The list of shortest paths to each of the points a unit can reach
    private ArrayList<Point> pointsInRange; // The list of points that the unit can potentially reach
    private ArrayList<Point> attackPointsInMoveRange; // The list of points that the unit can potentially attack
    private ArrayList<Point> staffPointsInMoveRange; // The list of points that the unit can potentially use a staff on
    private ArrayList<Point> attackPointsInRange; // The list of points that the unit can attack from its position
    private ArrayList<Point> staffPointsInRange; // The list of points that the unit can use a staff on frm its position
    
    private SoundManager soundManager;
    private Stoppable moveSound;
    
    public Unit(SoundManager soundManager, Map map, int x, int y, Faction faction, 
            UnitClass unitClass, int level, Inventory inventory)
    {
        super(x, y);
        mapAnim = new AnimationMapUnit(unitClass.spriteFileName, faction.colorTemplate);
        synchMapAnim();
        
        this.soundManager = soundManager;
        this.map = map;
        
        name = unitClass.className;
        this.faction = faction;
        moveType = unitClass.moveT;
        stats = new Stats(unitClass, level);
        path = new ArrayList();
        potentialPath = new ArrayList<>();
        potentialPath.add(position);
        
        this.inventory = inventory;
        inventory.setOwner(this);
    }
    public Unit(SoundManager soundManager, Map map, int x, int y, Faction faction, UnitClass unitClass)
    {
        this(soundManager, map, x, y, faction, unitClass, 1, new Inventory());
    }
    
    // "Base" stat getters
    public String getName() {
        return name;
    }
    @Override
    public AnimationMapUnit getMapAnim() {
            return mapAnim;
    }
    public Faction getFaction() {
        return faction;
    }
    public Stats getStats() {
        return stats;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public Item[] getItems() {
        return inventory.getItems();
    }
    public int getHP() {
        return stats.getHP().get();
    }
    public int getStrength() {
        return stats.getStr().get();
    }
    public int getSkill() {
        return stats.getSkl().get();
    }
    public int getSpeed() {
        return stats.getSpd().get();
    }
    public int getLuck() {
        return stats.getLck().get();
    }
    public int getDefense() {
        return stats.getDef().get();
    }
    public int getResistance() {
        return stats.getRes().get();
    }
    public int getConstitution() {
        return stats.getCon().get();
    }
    public int getAid() {
        return stats.getCon().get() - 1;
    }
    public int getMovePoints() {
        return stats.getMov().get();
    }
    
    // state-based getters
    public Equipment getEquiped() {
        return inventory.getEquiped();
    }
    public boolean hasEquiped() {
        return inventory.hasEquiped();
    }
    public Weapon getEquipedWeapon() {
        return inventory.getEquipedWeapon();
    }
    public boolean hasEquipedWeapon() {
        return inventory.hasEquipedWeapon();
    }
    public Staff getEquipedStaff() {
        return inventory.getEquipedStaff();
    }
    public boolean hasEquipedStaff() {
        return inventory.hasEquipedStaff();
    }
    
    // battle stat getters
    protected int getDamage(Weapon weapon, Unit enemyUnit) {
        int damage = 0;
        
        // weapon's natural might
        damage += weapon.getMight();

        // unit's own strength
        if(getEquipedWeapon().isMagic(enemyUnit))
            damage += getStrength(); // get mag goes here
        else
            damage += getStrength();
        
        if (enemyUnit != null)
        {
            // weapon triangle changes 
            if(getEquipedWeapon().hasWeaponTriangleAdvantage(enemyUnit))
                damage += getEquipedWeapon().getWeaponTriangleDeltaMight();
            else if(getEquipedWeapon().hasWeaponTriangleDisadvantage(enemyUnit))
                damage -= getEquipedWeapon().getWeaponTriangleDeltaMight();
        }
        
        return damage;
    }
    public final int getDamage(Weapon weapon) {
        return getDamage(weapon, null);
    }
    public final int getDamage(Unit enemyUnit) {
        return getDamage(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getReduction(Weapon weapon, Unit enemyUnit) {
        int reduction = 0;
        if(enemyUnit != null)
        {
            if(enemyUnit.getEquipedWeapon().isMagic(this))
            {
                reduction += getResistance();
                reduction += map.getTerrainAt(position).resistanceBonus;
            }
            else
            {
                reduction += getDefense();
                reduction += map.getTerrainAt(position).defenseBonus;
            }
        }
        return reduction;
    }
    public final int getReduction(Weapon weapon) {
        return getReduction(weapon, null);
    }
    public final int getReduction(Unit enemyUnit) {
        return getReduction(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getHit(Weapon weapon, Unit enemyUnit) {
        // (Skill x 2) + (Luck / 2) + Support bonus + Weapon triangle bonus + S Rank bonus + Tactician bonus
        int hit = weapon.getHit();
        hit += getSkill()*2;
        hit += getLuck()/2;
        
//        if(stats.hasSRank(getEquipedWeapon()))
//            hit += 5;
        
        if(enemyUnit != null)
        {
            if(getEquipedWeapon().hasWeaponTriangleAdvantage(enemyUnit))
                hit += getEquipedWeapon().getWeaponTriangleDeltaHit();
            else if(getEquipedWeapon().hasWeaponTriangleDisadvantage(enemyUnit))
                hit -= getEquipedWeapon().getWeaponTriangleDeltaHit();
        }
        
        return hit;
    }
    public final int getHit(Weapon weapon) {
        return getHit(weapon, null);
    }
    public final int getHit(Unit enemyUnit) {
        return getHit(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getAvoid(Weapon weapon, Unit enemyUnit) {
//        (Attack Speed x 2) + Luck + Support bonus + Terrain bonus + Tactician bonus
        int avoid = 0;
        avoid += getAttackSpeed(weapon, enemyUnit)*2;
        avoid += getLuck();
        avoid += map.getTerrainAt(position).avoidBonus;
        return avoid;
    }
    public final int getAvoid(Weapon weapon) {
        return getAvoid(weapon, null);
    }
    public final int getAvoid(Unit enemyUnit) {
        return getAvoid(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getCriticalChance(Weapon weapon, Unit enemyUnit) {
//        Weapon Critical + (Skill / 2) + Support bonus + Critical bonus + S Rank bonus
        int crit = weapon.getCrit();
        crit += getSkill()/2;
        return crit;
    }
    public final int getCriticalChance(Weapon weapon) {
        return getCriticalChance(weapon, null);
    }
    public final int getCriticalChance(Unit enemyUnit) {
        return getCriticalChance(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getCriticalAvoid(Weapon weapon, Unit enemyUnit) {
//        Luck + Support bonus + Tactician bonus
        int criticalAvoid = 0;
        criticalAvoid += getLuck();
        return criticalAvoid;
    }
    public final int getCriticalAvoid(Weapon weapon) {
        return getCriticalAvoid(weapon, null);
    }
    public final int getCriticalAvoid(Unit enemyUnit) {
        return getCriticalAvoid(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getAttackSpeed(Weapon weapon, Unit enemyUnit) {
//        Speed - (Weapon Weight - Constitution, take as 0 if negative)
        int attackSpeed = getSpeed();
        if(weapon != null)
        {
            if(weapon.getWeight() > getConstitution())
            {
                attackSpeed -= (weapon.getWeight() - getConstitution());
            }
        }
        return attackSpeed;
    }
    public final int getAttackSpeed(Weapon weapon) {
        return getAttackSpeed(weapon, null);
    }
    public final int getAttackSpeed(Unit enemyUnit) {
        return getAttackSpeed(getEquipedWeapon(), enemyUnit);
    }
    
    protected int getNumberAttacks(Weapon weapon, Unit enemyUnit) {
        int numberAttacks = 1;
        if(enemyUnit != null)
        {
            if(getAttackSpeed(weapon, enemyUnit) - enemyUnit.getAttackSpeed(this) >= 4)
            {
                numberAttacks *= 2;
            }
        }
        return numberAttacks;
    }
    public final int getNumberAttacks(Weapon weapon) {
        return getNumberAttacks(weapon, null);
    }
    public final int getNumberAttacks(Unit enemyUnit) {
        return getNumberAttacks(getEquipedWeapon(), enemyUnit);
    }
    
    // boolean state queries
    public boolean isDepleted() {
        return isDepleted;
    }
    public boolean isDead() {
        return getHP() <= 0;
    }
    public boolean canEquip(Equipment equip) {
        if(equip.isEquipableBy(this))
            return true;
        return false;
    }
    public boolean canAttack(Unit target) {
        // If the unit does not exist
        if (target == null)
        {
            return false;
        }
        // If the unit is not an enemy
        if (faction.isFriendlyTowards(target.getFaction()))
        {
            return false;
        }
        
        // If this unit does not have a weapon
        if (!hasEquipedWeapon())
        {
            return false;
        }
        
        return true;
    }
    public boolean canRetaliate(Unit unit) {
        // If the unit is not an enemy
        if (faction.isFriendlyTowards(unit.getFaction()))
        {
            return false;
        }
        
        // If the unit does not have an equiped weapon
        if(!inventory.hasEquipedWeapon())
        {
            return false;
        }
        
        // Calculating if the unit is within range
        if(!((position.distance(unit.getPosition()) <= inventory.getEquipedWeapon().getRangeMax())
           &&(position.distance(unit.getPosition()) >= inventory.getEquipedWeapon().getRangeMin())))
        {
            return false;
        }
        
        return true;
    }
    public boolean canStaff(Unit unit) {
        // If the unit does not exist
        if (unit == null)
        {
            return false;
        }
        
        if (!hasEquipedStaff())
        {
            return false;
        }
        // If the unit is an enemy (need to change this to can the staff act on it)
        if (!getEquipedStaff().canTarget(unit))
        {
            return false;
        }
        
        // If the targetted unit is this unit
        if(unit == this)
        {
            return false;
        }
        
        return true;
    }
    public boolean canTradeWith(Unit unit) {
        if(unit == null)
            return false;
        
        if(!faction.isFriendlyTowards(unit.getFaction()))
            return false;
        
        if(unit.getInventory().isEmpty() && getInventory().isEmpty())
            return false;
        
        if(hasTraded)
            return false;
        
        return true;
    }
    public boolean canItem() {
        return true;
    }
    public boolean canUse(UseableItem item) {
        return item.canBeUsed();
    }
    public boolean canCancel() {
        return !hasTraded;
    }
    
    @Override
    public boolean isTargetable(Item item) {
        return true;
    }
    @Override
    public boolean isUseable(Item item) {
        if(item instanceof Equipment)
            return ((Equipment)item).isEquipableBy(this);
        return true;
    }
    
    // actions
    public void select(boolean isControllable) {
        recalculateRanges();
        
        startPosition = position;
        potentialPath = new ArrayList<>();
        potentialPath.add(position);
        
        if(isControllable)
            mapAnim.setMoveSouth();
    }
    public void deselect() {
        setDepleted(isDepleted);
        synchMapAnim();
    }
    public void endTurn() {
        Game.logInfo(getName() + " waits!");
        setDepleted(true);
    }
    public void refresh() {
        potentialPath = new ArrayList<>();
        potentialPath.add(position);
        setDepleted(false);
        
        stats.resetStats();
        hasTraded = false;
    }
    
    public void incrementPath(Point position) {
        if (potentialPath.contains(position)) 
        {
            while( potentialPath.indexOf(position) < potentialPath.size() - 1)
            {
                potentialPath.remove(potentialPath.size() - 1);
            }
        }
        else if ((getMovePoints() >= pathMoveCost(potentialPath) + pointMoveCost(position)) // if you can move there with your current path
              &&(potentialPath.get(potentialPath.size() - 1).distance(position) == 1)) // and if the point is adjacent to the end of your path
        {
            potentialPath.add(new Point(position));
        }
        // if pathList is null, then it wasn't properly initialized elswhere
        // this is likely because an illegal add attempt was made. Will throw exception
        else if ((pathList != null) && contains(position, pathList))
        {
            potentialPath = pathTo(position);
        }
        else
        {
            String errorMessage = "Could not add point: " + position;
            if(pathList == null)
            {
                errorMessage += " (pathList is null, likely not initialized correctly)";
            }
            boolean isAdjacentToPath = false;
            for(Point pathPoint : potentialPath)
            {
                // if is adjacent
                if(pathPoint.distance(position) == 1)
                {
                    isAdjacentToPath = true;
                    break;
                }
            }
            if(isAdjacentToPath)
            {
                errorMessage += " (point is not adjacent to path)";
            }
            throw new IllegalArgumentException(errorMessage);
        }
    }
    public void moveUnit() {
        map.moveUnit(position, potentialPath.get(potentialPath.size() - 1));
        setPath((ArrayList<Point>) potentialPath.clone());
        moveSound = soundManager.playSoundEffect(SoundManager.moveFoot, true);
    }
    public void cancelMovement() {
        if(canCancel())
        {
            // TODO: cancel movement fails when start is the same as end
            map.moveUnit(position, startPosition);
            setPosition(startPosition);
        }
        else
        {
            endTurn();
        }
    }
    /** 
     * Performs unit specific cleanup after moving.
     * Stops the move sound.
     * Updates the attackPointsInRange (specific to the point the unit is on)
     * Updates the staffPointsInRange  (specific to the point the unit is on)
     */
    public void endMovement() {
        setPosition(potentialPath.get(potentialPath.size() - 1));
        moveSound.stop();
        attackPointsInRange = calculateAttackPointsInRange();
        staffPointsInRange = calculateStaffPointsInRange();
    }
    
    /**
     * Alters the effective or current health of the unit.
     * futureHealth is found using: <code>futureHealth = currentHealth + dHealth</code>
     * Damage should be negative.
     * Healing should be positive.
     * @param dHealth the change in health
     */
    protected void alterHealth(int dHealth) {
        int futureHealth = getHP() + dHealth;
        
        if(futureHealth <= 0)
        {
            stats.getHP().set(0);
        }
        else if(futureHealth >= stats.getHP().getValT())
        {
            stats.getHP().set(stats.getHP().getValT());
        }
        else
        {
            stats.getHP().set(futureHealth);
        }
    }
    public void damage(int damage) {
        alterHealth(damage*-1);
    }
    public void heal(int healing) {
        alterHealth(healing);
    }
    
    public void setDepleted(boolean isDepleted) {
        this.isDepleted = isDepleted;
        mapAnim.setDepleted(isDepleted);
        mapAnim.setStand();
    }
    public void setHasTraded(boolean hasTraded) {
        this.hasTraded = true;
    }
    public void clearEffect(Effect effect) {
        
    }
    
    // Movement and range related calculations.
    // Abandon all hope, ye who enter here.
    private void recalculateRanges() {
        // Note that they must be called in this order-- points in range uses pathlist, attackpointlist uses pointsinrange
        pathList = calculatePathList();
        pointsInRange = calculatePointsInRange();
        attackPointsInMoveRange = calculateAttackPointsInMoveRange();
        staffPointsInMoveRange = calculateStaffPointsInMoveRange();
    }
    /**
     * Gets the current potential path for the unit. 
     * The potential path is the path that the unit is considering taking
     * (as specified by the cursor), before the unit has been ordered to move.
     * @return the current potential path for the unit.
     */
    public ArrayList<Point> getPotentialPath() {
        return potentialPath;
    }
    /**
     * @return points that the unit can move to
     */
    public ArrayList<Point> getPointsInRange() {
        return pointsInRange;
    }
    /**
     * @return points that may be attacked after the unit moves
     */
    public ArrayList<Point> getAttackPointsInMoveRange() {
        return attackPointsInMoveRange;
    }
    /**
     * @return points that a staff may be used on after the unit moves
     */
    public ArrayList<Point> getStaffPointsInMoveRange() {
        return staffPointsInMoveRange;
    }
    /**
     * @return points specific to the current position which may be attacked
     */
    public ArrayList<Point> getAttackPointsInRange() {
        return attackPointsInRange;
    }
    /**
     * @return points specific to the current position which a staff may be used on;
     */
    public ArrayList<Point> getStaffPointsInRange() {
        return staffPointsInRange;
    }
    
    /**
     * Gets points that have a unit that the current unit can attack.
     * @return list of points with attackable units
     */
    public ArrayList<Point> getAttackablePoints() {
        ArrayList<Point> attackablePoints = new ArrayList();
        
        for (Point attackPoint : attackPointsInRange)
            if (canAttack(map.getUnitAt(attackPoint)))
                attackablePoints.add(attackPoint);
        
        return attackablePoints;
    }
    /**
     * Gets points that have a unit that the current unit can use a staff on.
     * @return The list of points with units that can be targeted by the staff.
     */
    public ArrayList<Point> getStaffPoints() {
        ArrayList<Point> staffPoints = new ArrayList();
        
        for (Point staffPoint : staffPointsInRange)
            if (canStaff(map.getUnitAt(staffPoint)))
                staffPoints.add(staffPoint);
        
        return staffPoints;
    }
    public ArrayList<Point> getTradePoints() {
        ArrayList<Point> tradeablePoints = new ArrayList();
        
        for (Point adjacentPoint : getAdjacentPoints())
            if(canTradeWith(map.getUnitAt(adjacentPoint))) // unit is at
                tradeablePoints.add(adjacentPoint);
        
//        System.out.println(tradeablePoints.size() + " trade points");
        return tradeablePoints;
    }
    public ArrayList<Point> getAdjacentPoints() {
        ArrayList<Point> adjacentPoints = new ArrayList();
        
        for(int dx = -1; dx <= 1; dx++)
            for(int dy = -1; dy <= 1; dy++)
                if(Math.abs(dx) + Math.abs(dy) == 1)
                    adjacentPoints.add(new Point(position.x + dx, position.y + dy));
        
        return adjacentPoints;
    }
    
    public ArrayList<Point> getPointsInRangeWith(Equipment equipment) {
        return calculatePointsInRange(position, equipment.getRangeMin(), equipment.getRangeMax());
    }
    
    public ArrayList<Point> pathTo(Point position) {
        for (ArrayList<Point> path : pathList)
        {
            if (path.contains(position))
            {
                return new ArrayList(path.subList(0, path.indexOf(position) + 1));
            }
        }
        return null;
    }
    private ArrayList<Point> calculatePointsInRange() {
        ArrayList<Point> pathPointList = new ArrayList();
        
        for (ArrayList<Point> path : pathList)
        {
            for (Point point : path)
            {
                if(!pathPointList.contains(point))
                {
                    pathPointList.add(point);
                }
            }
        }
        
        return pathPointList;
    }
    
    private ArrayList<ArrayList<Point>> calculatePathList() {
        ArrayList<ArrayList<Point>> pathList = new ArrayList(
                Arrays.asList(new ArrayList(Arrays.asList(position))));
        
        for (int movePoints = 0; movePoints <= getMovePoints(); movePoints++)
        {
            ArrayList<ArrayList<Point>> tempPathList = new ArrayList(pathList);
            for (int j = 0; j < tempPathList.size(); j++)
            {
                ArrayList<Point> currentPath = tempPathList.get(j);
                Point endPoint = currentPath.get(currentPath.size() - 1);
                
                int movePointsLeft = movePoints - pathMoveCost(currentPath);
                boolean hasNorth = false;
                boolean hasSouth = false;
                boolean hasEast  = false;
                boolean hasWest  = false;
                
                // North
                Point pointNorth = new Point(endPoint.x, endPoint.y - 1);
                if(shouldAdd(pointNorth, movePointsLeft, pathList))
                {
                    ArrayList<Point> newPath = new ArrayList(currentPath);
                    newPath.add(pointNorth);
                    pathList.add(newPath);
                    hasNorth = true;
                }
                // South
                Point pointSouth = new Point(endPoint.x, endPoint.y + 1);
                if(shouldAdd(pointSouth, movePointsLeft, pathList))
                {
                    ArrayList<Point> newPath = new ArrayList(currentPath);
                    newPath.add(pointSouth);
                    pathList.add(newPath);
                    hasSouth = true;
                }
                // East
                Point pointEast = new Point(endPoint.x + 1, endPoint.y);
                if(shouldAdd(pointEast, movePointsLeft, pathList))
                {
                    ArrayList<Point> newPath = new ArrayList(currentPath);
                    newPath.add(pointEast);
                    pathList.add(newPath);
                    hasEast = true;
                }
                // West
                Point pointWest = new Point(endPoint.x - 1, endPoint.y);
                if(shouldAdd(pointWest, movePointsLeft, pathList))
                {
                    ArrayList<Point> newPath = new ArrayList(currentPath);
                    newPath.add(pointWest);
                    pathList.add(newPath);
                    hasWest = true;
                }
                
                if( (hasNorth || contains(pointNorth, pathList))
                  &&(hasSouth || contains(pointSouth, pathList))
                  &&(hasEast  || contains(pointEast, pathList))
                  &&(hasWest  || contains(pointWest, pathList)))
                {
                    pathList.remove(currentPath);
                }
            }
        }
        return pathList;
    }
    private boolean shouldAdd( Point position, int movePointsLeft, ArrayList<ArrayList<Point>> pathList) {
        
        return (movePointsLeft >= pointMoveCost(position))
              &&!contains(position, pathList);
    }
    private boolean contains(Point position, ArrayList<ArrayList<Point>> pathList) {
        for (ArrayList<Point> path : pathList)
        {
            if (path.contains(position))
            {
                return true;
            }
        }
        return false;
    }
    private int pointMoveCost(Point position) {
        return map.getMoveCostOf(position, moveType, faction);
    }
    private int pathMoveCost(ArrayList<Point> path) {
        int cost = 0;
        
        // path contains start position, which does not count in path cost
        for(int i = 1; i < path.size(); i++)
            cost += pointMoveCost(path.get(i));
        
        return cost;
    }
    
    private ArrayList<Point> calculateAttackPointsInMoveRange() {
        return getPointsInRangeOf(inventory.getEquipableWeapons());
    }
    private ArrayList<Point> calculateStaffPointsInMoveRange() {
        return getPointsInRangeOf(inventory.getEquipableStaves());
    }
    private ArrayList<Point> calculateAttackPointsInRange() {
        return calculatePointsInRange(inventory.getEquipableWeapons());
    }
    private ArrayList<Point> calculateStaffPointsInRange() {
        return calculatePointsInRange(inventory.getEquipableStaves());
    }
    
    private ArrayList<Point> getPointsInRangeOf(Equipment[] equipment) {
        ArrayList<Point> attackPointList = new ArrayList();
        
        for (Point position : pointsInRange) 
        {
            ArrayList<Point> tempAttackPointList = calculatePointsInRange(position, equipment);
            for (Point point : tempAttackPointList)
            {
                if (!attackPointList.contains(point))
                {
                    attackPointList.add(point);
                }
            }
        }
        
        return attackPointList;
    }
    private ArrayList<Point> calculatePointsInRange(Equipment[] equipment) {
        return calculatePointsInRange(position, equipment);
    }
    private ArrayList<Point> calculatePointsInRange(Point position, Equipment[] equipment) {
        ArrayList<Point> attackPointList = new ArrayList();
        for (Equipment equip : equipment)
        {
            ArrayList<Point> tempAttackPointList = calculatePointsInRange(
                    position, equip.getRangeMin(), equip.getRangeMax());
            
            for (Point point : tempAttackPointList)
                if (!attackPointList.contains(point))
                    attackPointList.add(point);
        }
        return attackPointList;
    } 
    /**
     * Calculates all of the points that can be hit from one point with the given range.
     * @param position The position that range will be calculated relative to.
     * @param minRange The minimum range that can be hit, inclusive. Adjacent is 1.
     * @param maxRange The maximum range that can be hit, inclusive. Adjacent is 1.
     * @return The list of points that can be hit.
     */
    private ArrayList<Point> calculatePointsInRange(Point position, int minRange, int maxRange) {
        ArrayList<Point> attackPointList = new ArrayList();
        
        for (int difX = (-1)*maxRange; difX <= maxRange; difX++ )
        {
            for (int difY = (-1)*(maxRange - Math.abs(difX)); difY <= (maxRange - Math.abs(difX)); difY++ )
            {
                attackPointList.add(new Point(position.x + difX, position.y + difY));
            }
        }
        
        for (int difX = (-1)*(minRange - 1 ); difX < minRange; difX++ )
        {
            for (int difY = (-1)*((minRange - Math.abs(difX)) - 1); difY < (minRange - Math.abs(difX)); difY++ )
            {
                attackPointList.remove(new Point(position.x + difX, position.y + difY));
            }
        }
        
        return attackPointList;
    }
}