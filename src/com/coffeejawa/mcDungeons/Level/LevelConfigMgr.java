package com.coffeejawa.mcDungeons.Level;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.coffeejawa.mcDungeons.mcDungeons;

public class LevelConfigMgr {
    private FileConfiguration levelsConfig = null;
    private File configFile = null;
    
    private mcDungeons plugin;
    
    HashMap<Integer,mcdLevel> levels;
    
    public LevelConfigMgr(mcDungeons plugin){
        this.plugin = plugin;
        
        levels = new HashMap<Integer, mcdLevel>();
        
        reloadConfig();
        
        // TODO: set up async task to save periodically save config to disk
        // avoid problems with server crashing
    }
    
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "levels.yml");
        }
        levelsConfig = YamlConfiguration.loadConfiguration(configFile);
     
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("levels.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            levelsConfig.setDefaults(defConfig);
        }
        // if config file empty, copy defaults in
        if(getConfig().getConfigurationSection("levels").getKeys(false).size() == 0){
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        // Deserialize config
        Deserialize();
    }
     
    public FileConfiguration getConfig() {
        if (levelsConfig == null) {
            this.reloadConfig();
        }
        return levelsConfig;
    }
    
    public void saveConfig() {
        if (levelsConfig == null || configFile == null) {
            return;
        }
        try {
            // serialize (convert Level objects into yml)
            //Serialize();
            
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
        
        // refresh level objects
        Deserialize();
    }
    
    private void Deserialize(){
        levels = new HashMap<Integer, mcdLevel>();
        
        // Get list of levels
        Set<String> levelNames = getConfig().getConfigurationSection("levels").getKeys(false);
        plugin.logger.info("Loaded levels: " + levelNames.toString());
        
        // Push settings for each level into our HashMap
        for( String levelName : levelNames ){
            ConfigurationSection settings = getConfig().getConfigurationSection("levels."+levelName);
            levels.put(Integer.parseInt(levelName),new mcdLevel(Integer.parseInt(levelName),settings.getDouble("damageX"),
                    settings.getDouble("healthX"),
                    settings.getDouble("speedX"),
                    settings.getDouble("activationRange"),
                    settings.getBoolean("knockback")));
        }
        
    }
    
//    public HashMap<Integer, mcdLevel> getLevels() {
//        return levels;
//    }
//
//    public void setLevels(HashMap<Integer, mcdLevel> levels) {
//        this.levels = levels;
//    }
    // serialize is not working properly.
//    private void Serialize(){
//        for( int levelName : levels.keySet() ){
//                getConfig().set("levels."+Integer.toString(levelName),levels.get(levelName).getSettings());
//        }
//    }
    
    public mcdLevel getLevelObject(int levelIndex){
        mcdLevel ret = levels.get(levelIndex);
        if(ret == null){
            plugin.logger.warning(String.format("Invalid level accessed: %d",levelIndex));
        }
        return ret;
    }
    
    public boolean isLevel(int level){
        if(this.getLevelObject(level) == null){
            plugin.logger.info("Level "+ level + " doesn't exist!");
            return false;
        }
        plugin.logger.info("Level "+ level + " exists!");
        return true;
    }
    
   
}
