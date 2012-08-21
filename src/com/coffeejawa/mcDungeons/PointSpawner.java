package com.coffeejawa.mcDungeons;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class PointSpawner {
    private EntityType type;
    private Location loc;
    
    public PointSpawner(EntityType type, Location loc){
        this.type = type;
        this.loc = loc;
           
    }
    
    public PointSpawner(ConfigurationSection config){
        this.type = EntityType.fromName(config.getString("type"));
        TinyLocation tempLoc = new TinyLocation(config.getConfigurationSection("location"));
        this.loc = tempLoc.toLocation();
    }

    public HashMap<String,Object> serialize(){
        HashMap<String,Object> settings = new HashMap<String,Object>();
        settings.put("type", type.toString());
        settings.put("location", new TinyLocation(loc).serialize());
        return settings;
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
    
    

}
