package com.coffeejawa.mcDungeons.Level;

import java.util.HashMap;

public class mcdLevel {
    
    private int number;
    
    private double damageX;
    private double healthX;
    private double speedX;
    private double activationRange;
    
    //TODO: implement later if this granularity is needed
//    private double zombieDamageMultiplier;
//    private double zombieHealthMultiplier;
//    
//    private double spiderDamageMultiplier;
//    private double spiderHealthMultiplier;
//    
//    private double skeletonDamageMultiplier;
//    private double skeletonHealthMultiplier;
    
    mcdLevel(int number, double damageX, double healthX, double speedX, double activationRange){
        this.damageX = damageX;
        this.healthX = healthX;
        this.speedX = speedX;
        this.activationRange = activationRange;
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public HashMap<String,Object> getSettings(){
        HashMap<String,Object> tempMap = new HashMap<String,Object>();
        tempMap.put("damageX", getDamageX());
        tempMap.put("healthX", getHealthX());
        tempMap.put("speedX", getSpeedX());
        tempMap.put("activationRange", getActivationRange());
        return tempMap;
    }
    
    
    public double getDamageX() {
        return damageX;
    }

    public void setDamageX(double damageX) {
        this.damageX = damageX;
    }

    public double getHealthX() {
        return healthX;
    }

    public void setHealthX(double healthX) {
        this.healthX = healthX;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getActivationRange() {
        return activationRange;
    }

    public void setActivationRange(double activationRange) {
        this.activationRange = activationRange;
    }

    /*
     * scaledDamageToPlayer()
     * Takes in a single param for initial base damage
     * Applies any applicable level modifiers
     * Returns new damage value
     */
    public double scaledDamageToPlayer(double inDmg){
        return inDmg * damageX;
    }
    /* scaledDamageToCreature()
     * Takes in a single param for initial base damage
     * Applies any applicable level modifiers
     * Returns new damage value
     */
    public double scaledDamageToCreature(double inDmg){
        return inDmg * healthX;
    }
    
   
    
}
