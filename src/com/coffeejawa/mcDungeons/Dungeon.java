package com.coffeejawa.mcDungeons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public class Dungeon {
    private mcDungeons plugin;
    Set<PointSpawner> spawners;
    String name;
    Set<LivingEntity> entities;
    
    public Dungeon(mcDungeons plugin, String name){
        this.plugin = plugin;
        this.name = name;
        this.entities = new HashSet<LivingEntity>();
        spawners = new HashSet<PointSpawner>();
    }
    
    public Dungeon(mcDungeons plugin, ConfigurationSection map){
        this.plugin = plugin;
        spawners = new HashSet<PointSpawner>();
        this.name = map.getString("name");
        Set<String> keys = map.getConfigurationSection("spawners").getKeys(false);
        for( String key : keys ){
            spawners.add(new PointSpawner(map.getConfigurationSection("spawners."+key)));
        }
        this.entities = new HashSet<LivingEntity>();
    }
    
    public HashMap<String,Object> serialize(){
        HashMap<String,Object> settings = new HashMap<String, Object>();
        
        HashMap<String,HashMap<String,Object>> spawnerList = new HashMap<String, HashMap<String, Object>>();
        int count = 0;
        for( PointSpawner spawner : spawners){
            count++;
            spawnerList.put(Integer.toString(count), spawner.serialize());
        }
        settings.put("name", name);
        settings.put("spawners",spawnerList);
        return settings;
    }
    
    
    public void addSpawner(PointSpawner spawner){
        spawners.add(spawner);
    }
    
    public void triggerSpawns(){
        plugin.logger.info("Triggered spawns for dungeon: "+name);
        for( PointSpawner spawner : spawners ){
            entities.add(spawner.getLoc().getWorld().spawnCreature(spawner.getLoc(), spawner.getType()));
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEntityAssociated(LivingEntity entity){
        return entities.contains(entity);
    }
    

}