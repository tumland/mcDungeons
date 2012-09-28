package com.coffeejawa.mcDungeons;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class TinyLocation implements ConfigurationSerializable {
 
    @SuppressWarnings("unused")
    private final static long serialVersionUID = 1L;
    private String world;
    private double x;
    private double y;
    private double z;
 
    public TinyLocation(World world, Double x, Double y, Double z) {
        this.world = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }
 
    public TinyLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }
    
    public TinyLocation(ConfigurationSection map) {
        this.world = map.getString("world");
        this.x = map.getDouble("x");
        this.y = map.getDouble("y");
        this.z = map.getDouble("z");                
    }
 
    public Location toLocation() {
        Location l = new Location(Bukkit.getWorld(world), x, y, z);
        return l;
    }
 
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
 
        map.put("world", world);
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
 
        return map;
    }
 
    public static TinyLocation deserialize(Map<String, Object> map) {
        Configuration conf = new MemoryConfiguration();
        for (Entry<String, Object> e : map.entrySet()) {
            conf.set(e.getKey(), e.getValue());
        }
        return new TinyLocation(conf);
    }
}