package com.coffeejawa.mcDungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.coffeejawa.mcDungeons.Level.mcdLevel;
import com.coffeejawa.mcDungeons.Region.mcdRegion;

public class EntityRegistry {
    
    private HashMap<Entity,ArrayList<String>> entityTable;
    
    public EntityRegistry(){
        entityTable = new HashMap<Entity, ArrayList<String>>();
    }
    
    public void add(Entity entity, ArrayList<String> regionNames){
        if(!entityTable.containsKey(entity) && regionNames.size() > 0){
            entityTable.put(entity, regionNames);
        }
    }
    
    public void remove(Entity entity){
        if(entityTable.containsKey(entity)){
            entityTable.remove(entity);
        }
    }
    public ArrayList<String> getRegions(Entity entity){
        
        if(entityTable.containsKey(entity)){
            return entityTable.get(entity);
        }
        return new ArrayList<String>();
    }
    
    public boolean contains(Entity entity){
        if( entityTable.containsKey(entity) ){
            return true;
        }
        return false;
    }
}
