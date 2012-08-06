package com.coffeejawa.mcDungeons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Entity;

import com.coffeejawa.mcDungeons.Level.mcdLevel;
import com.coffeejawa.mcDungeons.Region.mcdRegion;

public class EntityRegistry {

    mcDungeons plugin;
    
    private HashMap<Entity,ArrayList<String>> entityRegistry;
    
    public EntityRegistry(mcDungeons plugin){
        this.plugin = plugin;
        entityRegistry = new HashMap<Entity, ArrayList<String>>();
    }
    
    public void add(Entity entity, ArrayList<String> regionNames){
        if(!entityRegistry.containsKey(entity)){
            entityRegistry.put(entity, regionNames);
        }
    }
    
    public void remove(Entity entity){
        if(entityRegistry.containsKey(entity)){
            entityRegistry.remove(entity);
        }
    }
    public ArrayList<String> getRegions(Entity entity){
        if(entityRegistry.containsKey(entity)){
            return entityRegistry.get(entity);
        }
        return new ArrayList<String>();
    }
    
    public double getMaxHealthX(Entity entity)
    {
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        return (Double) getMaxRegionDoubleAttribute(entity, "healthX");
    }
    public double getMaxDamageX(Entity entity){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        return (Double) getMaxRegionDoubleAttribute(entity, "damageX"); 
    }
    
    public double getMaxSpeedX(Entity entity){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        return getMaxRegionDoubleAttribute(entity, "speedX");
    }
    
    public double getMinActivationRange(Entity entity) { 
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return (Double) getWorldAttribute("activationRange");
        }
        
        return (Double) getMinRegionDoubleAttribute(entity, "activationRange");
    }
    
    public Object getWorldAttribute(String attributeName){
        // get default level
        int serverDefaultLevel = plugin.getConfig().getInt("defaultLevel");
        mcdLevel level = plugin.getLevelConfigManager().getLevelObject(serverDefaultLevel);
        return level.getSettings().get(attributeName);
    }
    
    private double getMaxRegionDoubleAttribute(Entity entity, String attributeName){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        double maxAttribValue = (Double) getWorldAttribute(attributeName);    

        ArrayList<String> regionNames = getRegions(entity);
        
        for(String regionName : regionNames) {
            // get region object
            mcdRegion currRegion = plugin.getRegionConfigManager().getRegionByName(regionName);
            if(currRegion != null){
                // get region object's level
                mcdLevel level = plugin.getLevelConfigManager().getLevelObject(currRegion.getLevel());
                if(level != null){
                    maxAttribValue = Math.max(maxAttribValue,(Double) level.getSettings().get(attributeName));
                }
            }
        }
        return maxAttribValue; 
    }

    private double getMinRegionDoubleAttribute(Entity entity, String attributeName){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        double minAttribValue = (Double) getWorldAttribute(attributeName);    

        ArrayList<String> regionNames = getRegions(entity);
        
        for(String regionName : regionNames) {
            // get region object
            mcdRegion currRegion = plugin.getRegionConfigManager().getRegionByName(regionName);
            if(currRegion != null){
                // get region object's level
                mcdLevel level = plugin.getLevelConfigManager().getLevelObject(currRegion.getLevel());
                if(level != null){
                    minAttribValue = Math.min(minAttribValue,(Double) level.getSettings().get(attributeName));
                }
            }
        }
        return minAttribValue; 
    }
}
