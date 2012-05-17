/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Game.Sound.PlayerThread;
import Game.Sound.SoundManager;
import Maps.Map;
import Sprites.AnimationMapUnit;
import Sprites.BoardElement;
import Units.Effects.Effect;
import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.Staff;
import Units.Items.Weapon;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class Unit extends BoardElement{
    private Map map;
    
    private boolean isDepleted;
    
    private AnimationMapUnit mapAnim;
    private String name;
    private Faction faction;
    private MoveType moveType;
    private Stats stats;
    private Inventory inventory;
    
    private Point startPosition;
    private ArrayList<Point> potentialPath;
    
    private ArrayList<ArrayList<Point>> pathList; // The list of shortest paths to each of the points a unit can reach
    private ArrayList<Point> pointsInRange; // The list of points that the unit can potentially reach
    private ArrayList<Point> attackPointsInMoveRange; // The list of points that the unit can potentially attack
    private ArrayList<Point> staffPointsInMoveRange; // The list of points that the unit can potentially use a staff on
    private ArrayList<Point> attackPointsInRange;
    private ArrayList<Point> staffPointsInRange;
    
    private SoundManager soundManager;
    private PlayerThread moveSound;
    
    public Unit(SoundManager soundManager, Map map, int x, int y, Faction faction, 
            UnitClass unitClass, int level, Inventory inventory)
    {
        super(new AnimationMapUnit(unitClass.spriteFileName), x, y);
        mapAnim = (AnimationMapUnit) super.mapAnim;
        
        this.soundManager = soundManager;
        this.map = map;
        
        name = unitClass.className;
        this.faction = faction;
        moveType = unitClass.moveT;
        stats = new Stats(unitClass, level);
        path = new ArrayList();
        
        this.inventory = inventory;
        inventory.setOwner(this);
    }
    public Unit(SoundManager soundManager, Map map, int x, int y, Faction faction, UnitClass unitClass)
    {
        this(soundManager, map, x, y, faction, unitClass, 1, new Inventory());
    }
    
    public void setDepleted(boolean isDepleted) {
        this.isDepleted = isDepleted;
        
        if (this.isDepleted)
        {
            mapAnim.setDepleted();
        }
        else 
        {
            mapAnim.setStand();
        }
    }
    public boolean isDepleted() {
        return isDepleted;
    }
    public boolean isDead() {
        if(stats.getHP().get() > 0)
        {
            return false;
        }
        return true;
    }
    
    public String getName() {
        return name;
    }
    public AnimationMapUnit getMapAnim() {
            return mapAnim;
    }
    public Faction getFaction() {
        return faction;
    }
    
    public Stats getStats() {
        return stats;
    }
    public int getMov() {
        return stats.getMOV().get();
    }
    
    public Equipment getEquiped() {
        return inventory.getEquiped();
    }
    public Weapon getEquipedWeapon() {
        return inventory.getEquipedWeapon();
    }
    public Staff getEquipedStaff() {
        return inventory.getEquipedStaff();
    }
    
    public Item[] getItems() {
        return inventory.getItems();
    }
    
    public void clearEffect(Effect effect) {
        
    }
    
    /**
     * Alters the effective or current health of the unit.
     * futureHealth is found using: <code>futureHealth = currentHealth + dHealth</code>
     * Damage should be negative.
     * Healing should be positive.
     * @param dHealth the change in health
     */
    protected void alterHealth(int dHealth) {
        int futureHealth = getHealth() + dHealth;
        
        if(futureHealth <= 0)
        {
            stats.getHP().set(0);
        }
        else if(futureHealth >= stats.getHP().getValue())
        {
            stats.getHP().set(stats.getHP().getValue());
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
    
    
    public int getHealth() {
        return stats.getHP().get();
    }
    public int getDamage(Unit enemyUnit) {
        int damage = 0;
        
        if(getEquiped() instanceof Weapon)
        {
            Weapon weapon = (Weapon) getEquiped();
            damage += weapon.getMight(enemyUnit);
            
            if(weapon.isMagic(enemyUnit))
            {
//                damage += stats.getMAG().get();
            }
            else
            {
                damage += stats.getSTR().get();
            }
        }
        
        return damage;
    }
    public int getReduction(Unit enemyUnit) {
        int reduction = 0;
        if(enemyUnit.getEquipedWeapon().isMagic(enemyUnit))
        {
            reduction += getStats().getRES().get();
            reduction += map.getTerrainAt(position).resistanceBonus;
        }
        else
        {
            reduction += getStats().getDEF().get();
            reduction += map.getTerrainAt(position).defenseBonus;
        }
        return reduction;
    }
    public int getHit(Unit enemyUnit) {
        // (Skill x 2) + (Luck / 2) + Support bonus + Weapon triangle bonus + S Rank bonus + Tactician bonus
        int hit = getEquipedWeapon().getHit(enemyUnit);
        hit += stats.getSKL().get()*2;
        hit += stats.getLCK().get()/2;
//        if(stats.hasSRank(getEquipedWeapon().getType()))
//        {
//            hit += 5;
//        }
//        if(getEquipedWeapon().hasAdvantageOver(enemyUnit.getEquipedWeapon()))
//        {
//            hit += 5;
//        }
        return hit;
    }
    public int getAvoid(Unit enemyUnit) {
//        (Attack Speed x 2) + Luck + Support bonus + Terrain bonus + Tactician bonus
        int avoid = 0;
        avoid += getAttackSpeed(enemyUnit)*2;
        avoid += stats.getLCK().get();
        avoid += map.getTerrainAt(position).avoidBonus;
        return avoid;
    }
    public int getCriticalChance(Unit enemyUnit) {
//        Weapon Critical + (Skill / 2) + Support bonus + Critical bonus + S Rank bonus
        int crit = getEquipedWeapon().getCrit(enemyUnit);
        crit += stats.getSKL().get()/2;
        return crit;
    }
    public int getCriticalAvoid(Unit enemyUnit) {
//        Luck + Support bonus + Tactician bonus
        int criticalAvoid = 0;
        criticalAvoid += stats.getLCK().get();
        return criticalAvoid;
    }
    public int getAttackSpeed(Unit enemyUnit) {
//        Speed - (Weapon Weight - Constitution, take as 0 if negative)
        int attackSpeed = stats.getSPD().get();
        if(getEquipedWeapon().getWeight(enemyUnit) > stats.getCON().get())
        {
            attackSpeed -= (getEquipedWeapon().getWeight(enemyUnit) - stats.getCON().get());
        }
        return attackSpeed;
    }
    public int getNumberAttacks(Unit enemyUnit) {
        int numberAttacks = 1;
        return numberAttacks;
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
        
        return true;
    }
    public boolean canStaff(Unit unit) {
        // If the unit does not exist
        if (unit == null)
        {
            return false;
        }
        // If the unit is an enemy (need to change this to can the staff act on it)
        if (!faction.isFriendlyTowards(unit.getFaction()))
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
    public boolean canItem() {
        return false;
    }
    
    public void select() {
        // Note that they must be called in this order-- points in range uses pathlist, attackpointlist uses pointsinrange
        pathList = calculatePathList();
        pointsInRange = calculatePointsInRange();
        attackPointsInMoveRange = calculateAttackPointsInMoveRange();
        staffPointsInMoveRange = calculateStaffPointsInMoveRange();
        
        startPosition = position;
        potentialPath = new ArrayList();
        potentialPath.add(position);
        
        mapAnim.setMoveSouth();
    }
    public void deselect() {
        setDepleted(isDepleted);
        synchMapAnim();
    }
    public void cancel() {
        map.moveUnit(position, startPosition);
        setPosition(startPosition);
        deselect();
    }
    public void incrementPath(Point position) {
        if (potentialPath.contains(position)) 
        {
            while( potentialPath.indexOf(position) < potentialPath.size() - 1)
            {
                potentialPath.remove(potentialPath.size() - 1);
            }
        }
        else if ((getMov() >= pathMoveCost(potentialPath) + pointMoveCost(position)) // if you can move there with your current path
              &&(potentialPath.get(potentialPath.size() - 1).distance(position) == 1)) // and if the point is adjacent to the end of your path
        {
            potentialPath.add(new Point(position));
        }
        else 
        {
        if (contains(position, pathList))
        {
            potentialPath = pathTo(position);
        }}
    }
    public boolean moveUnit() {
        if (map.moveUnit(position, potentialPath.get(potentialPath.size() - 1)))
        {
            setPosition(potentialPath.get(potentialPath.size() - 1));
            setPath(potentialPath);
            moveSound = soundManager.playSoundEffect(SoundManager.moveFly, true);
            return true;
        }
        return false;
    }
    /** 
     * Performs unit specific cleanup after moving
     * Stops the move sound.
     * Updates the attackPointsInRange (specific to the point the unit is on)
     * Updates the staffPointsInRange  (specific to the point the unit is on)
     */
    public void endMovement() {
        moveSound.stopPlaying();
        attackPointsInRange = calculateAttackPointsInRange();
        staffPointsInRange = calculateStaffPointsInRange();
//        soundManager.playSoundEffect(SoundManager.confirm);
    }
    
    // Movement and range related calculations.
    // Abandon all hope, ye who enter here.
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
        
        for (int movePoints = 0; movePoints <= getMov(); movePoints++)
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
        
        for (Point point : path)
            cost += pointMoveCost(point);
        
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