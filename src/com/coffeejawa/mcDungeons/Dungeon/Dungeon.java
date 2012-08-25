package com.coffeejawa.mcDungeons.Dungeon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.coffeejawa.mcDungeons.mcDungeons;

public class Dungeon {
    private mcDungeons plugin;
    Set<PointSpawner> spawners;
    String name;
    Set<RespawnTimer> respawnTimers;
    
    public Dungeon(mcDungeons plugin, String name){
        this.plugin = plugin;
        this.name = name;
        spawners = new HashSet<PointSpawner>();
        respawnTimers = new HashSet<RespawnTimer>();
        
        
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            public void run() {
                Reset();
            }
         }, 1L);
    }
    
    public Dungeon(mcDungeons plugin, ConfigurationSection map){
        this.plugin = plugin;
        spawners = new HashSet<PointSpawner>();
        this.name = map.getString("name");
        Set<String> keys = map.getConfigurationSection("spawners").getKeys(false);
        for( String key : keys ){
            spawners.add(new PointSpawner(map.getConfigurationSection("spawners."+key)));
        }
        respawnTimers = new HashSet<RespawnTimer>();


        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
        {
            public void run() {
                Reset();
            }
         }, 1L);
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
        for( RespawnTimer respawnTimer : respawnTimers ){
            if(respawnTimer.check()){
                respawnTimer.getSpawner().triggerSpawn();
                respawnTimers.remove(respawnTimer);
            }
            else{
                plugin.logger.info("Spawner timer not yet expired.");
            }
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEntityAssociated(Entity entity){
        for( PointSpawner spawner : spawners ){
            // direct comparison of entity doesn't seem to work
            if(spawner.getEntity() == entity)
                return true;
        }
        plugin.logger.info("Entity is not associated");
        return false;
    }
    
    public void Reset(){
        for( PointSpawner spawner : spawners ){
            spawner.triggerSpawn();
        }
    }
    
    public void createRespawnTimer(Entity entity){
        if(!isEntityAssociated(entity)){
            return;
        }
        PointSpawner spnr = null;
        for( PointSpawner spawner : spawners ){
            if(spawner.getEntity() == entity)
                spnr = spawner;
        }
        if(spnr != null){
            respawnTimers.add(new RespawnTimer(spnr));
        }
    }
    
    
    
    public PointSpawner getPointSpawnerAtLocation(Location location){
        for(PointSpawner spawner: this.spawners){
            Location spawnLoc = spawner.getLoc();
            if(spawnLoc.getBlockX() == location.getBlockX() && spawnLoc.getBlockY() == location.getBlockY() && spawnLoc.getBlockZ() == location.getBlockZ()){
                return spawner;
            }
        }
        return null;
    }
    
}