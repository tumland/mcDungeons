package com.coffeejawa.mcDungeons;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Entity;

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
