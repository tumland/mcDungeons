package com.coffeejawa.mcDungeons.Dungeon;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.coffeejawa.mcDungeons.TinyLocation;

public class PointSpawner {
    private EntityType type;
    private Location loc;
    private final long respawnTime;
    private Entity entity;
        
    public PointSpawner( EntityType type, Location loc, long respawnTime){
        this.type = type;
        this.loc = loc;
        this.respawnTime = respawnTime; // minutes     
    }
    
    public PointSpawner(ConfigurationSection config){;
        this.type = EntityType.fromName(config.getString("type"));
        this.respawnTime = config.getLong("respawnTime");
        TinyLocation tempLoc = new TinyLocation(config.getConfigurationSection("location"));
        this.loc = tempLoc.toLocation();   
    }

    public HashMap<String,Object> serialize(){
        HashMap<String,Object> settings = new HashMap<String,Object>();
        settings.put("type", type.toString());
        settings.put("location", new TinyLocation(loc).serialize());
        settings.put("respawnTime", respawnTime);
        return settings;
    }
    
    // this function is expected to be called once per minute
    public void triggerSpawn(){
        loc.getWorld().spawnEntity(this.getLoc(), this.getType());
    }
    
    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public long getRespawnTime() {
        return respawnTime;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
