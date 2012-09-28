package com.coffeejawa.mcDungeons;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.coffeejawa.mcDungeons.Region.mcdRegion;

public class SpawnController {
    private mcDungeons plugin;
    private mcdRegion region;
    private mcdRegion triggerRegion;
    private Integer spawnAmount;
    private boolean bOnCooldown;
    private double cooldownTime;
    private List<String> spawnTypeList;
 
    
    public SpawnController(mcDungeons plugin, ConfigurationSection settings){
        this.plugin = plugin;
        
        
        spawnAmount = settings.getInt("spawnAmount");
        
        String strTriggerRegion = settings.getString("triggerRegion");
        this.triggerRegion = plugin.getRegionConfigManager().getRegionByName(strTriggerRegion);
        this.spawnTypeList = settings.getStringList("spawnTypes");
        this.cooldownTime = settings.getDouble("spawnCooldown");
    }
    
    
    public void trigger(){
        plugin.getLogger().info("Spawn controller triggered");
        
//        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,  new Runnable()
//        {
//            public void run()
//            {
//                plugin.resetCooldown(dungeon);
//            }
//        }, (long)(cooldownTime * 20));
    }


    public mcDungeons getPlugin() {
        return plugin;
    }


    public mcdRegion getRegion() {
        return region;
    }


    public mcdRegion getTriggerRegion() {
        return triggerRegion;
    }


    public Integer getSpawnAmount() {
        return spawnAmount;
    }


    public boolean isbOnCooldown() {
        return bOnCooldown;
    }


    public double getCooldownTime() {
        return cooldownTime;
    }


    public List<String> getSpawnTypeList() {
        return spawnTypeList;
    }
}
