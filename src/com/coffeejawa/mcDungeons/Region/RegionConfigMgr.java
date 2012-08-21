package com.coffeejawa.mcDungeons.Region;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.coffeejawa.mcDungeons.mcDungeons;

public class RegionConfigMgr {
    private FileConfiguration regionConfig = null;
    private File regionConfigFile = null;
    
    private mcDungeons plugin;
    
    private HashMap<String,mcdRegion> regions;
    
    public RegionConfigMgr(mcDungeons plugin){
        this.plugin = plugin;
        
        regions = new HashMap<String,mcdRegion>();
        
        // TODO: set up async task to save periodically save config to disk
        // avoid problems with server crashing
        Deserialize();
    }
    
    public void reloadConfig() {
        if (regionConfigFile == null) {
            regionConfigFile = new File(plugin.getDataFolder(), "regions.yml");
        }
        regionConfig = YamlConfiguration.loadConfiguration(regionConfigFile);
     
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("regions.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            regionConfig.setDefaults(defConfig);
        }
        
        Deserialize();
    }
    
    public FileConfiguration getConfig() {
        if (regionConfig == null) {
            this.reloadConfig();
        }
        return regionConfig;
    }
    
    public void saveConfig() {
        if (regionConfig == null || regionConfigFile == null) {
            return;
        }
        try {
            //Serialize();
            
            getConfig().save(regionConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + regionConfigFile, ex);
        }
        // refresh list of region objects
        Deserialize();
        
    }
    
    public Map<String, Object> getRegionConfig(String regionName){
        Map<String,Object> regionValues = this.getConfig().getConfigurationSection("regions."+regionName).getValues(false);
        return regionValues; // could be empty / null
    }
    
    public boolean isRegionPresent(String regionName){
        List<String> regionNames = this.getConfig().getStringList("regions");
        if(regionNames.contains(regionName)){
            return true;
        }
        return false;
    }
    
    
    private void Deserialize(){
        regions = new HashMap<String,mcdRegion>();
        // Get list of regions
        Set<String> regionNames = getConfig().getConfigurationSection("regions").getKeys(false); 
        plugin.logger.info("Loaded regions: " + regionNames.toString());
        
        // Push settings for each level into our HashMap
        for( String regionName : regionNames ){
            ConfigurationSection settings = getConfig().getConfigurationSection("regions."+regionName);
            regions.put(regionName,new mcdRegion(plugin, regionName, settings));
        }
    }
    // Serialize is not working properly.
//    private void Serialize(){
//        for( String regionName : regions.keySet() ){
//            getConfig().set("levels."+regionName,regions.get(regionName).getSettings());
//        }
//    }
    
    public HashMap<String, mcdRegion> getRegions() {
        return regions;
    }

    public void setRegions(HashMap<String, mcdRegion> regions) {
        this.regions = regions;
    }

    public mcdRegion getRegionByName(String regionName){
        return regions.get(regionName);
    }
    
}
