package com.coffeejawa.mcDungeons.Dungeon;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;

import com.coffeejawa.mcDungeons.mcDungeons;

public class DungeonConfigMgr {
    private FileConfiguration dungeonConfig = null;
    private File dungeonConfigFile = null;
    
    private final mcDungeons plugin;
    
    private HashMap<String, Dungeon> dungeons;
    
    public DungeonConfigMgr(final mcDungeons plugin){
        this.plugin = plugin;
        
        dungeons = new HashMap<String,Dungeon>();
        
        reloadConfig();

        
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() 
        {
            public void run() {
                plugin.logger.info("Triggering spawns");
                for( Dungeon dungeon : dungeons.values() ){
                    dungeon.triggerSpawns();
                }
            }
         }, 60L, 1200L);
    }
    
    public void reloadConfig() {
        if (dungeonConfigFile == null) {
            dungeonConfigFile = new File(plugin.getDataFolder(), "dungeons.yml");
        }
        dungeonConfig = YamlConfiguration.loadConfiguration(dungeonConfigFile);
     
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("dungeons.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            dungeonConfig.setDefaults(defConfig);
        }
        
        Deserialize();
    }
    
    public FileConfiguration getConfig() {
        if (dungeonConfig == null) {
            this.reloadConfig();
        }
        return dungeonConfig;
    }
    
    public void saveConfig() {
        Serialize();
        if (dungeonConfig == null || dungeonConfigFile == null) {
            return;
        }
        try {
            
            getConfig().save(dungeonConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + dungeonConfigFile, ex);
        }        
    }
    
    public Map<String, Object> getDungeonConfig(String dungeonName){
        Map<String,Object> dungeonValues = this.getConfig().getConfigurationSection("dungeons."+dungeonName).getValues(false);
        return dungeonValues; // could be empty / null
    }
    
    public HashMap<String, Dungeon> getDungeons() {
        return dungeons;
    }


    public Dungeon getDungeonByName(String dungeonName){
        return dungeons.get(dungeonName);
    }
    
    
    public void addDungeon(String dungeonName, Dungeon dungeon){
        dungeons.put(dungeonName, dungeon);
    }
    
    public boolean removeDungeon(String dungeonName){
        if(!isDungeon(dungeonName)){
            return false;
        }
        dungeons.remove(dungeonName);
        return true;
    }
    
    private void Serialize(){
        for(String dungeonName : dungeons.keySet()){
            getConfig().createSection("dungeons."+dungeonName,dungeons.get(dungeonName).serialize());
        }
    }
    
    private void Deserialize(){
        ConfigurationSection dungeonsConfig = getConfig().getConfigurationSection("dungeons");
        if(dungeonsConfig == null)
            return;
        Set<String> dungeonNames = dungeonsConfig.getKeys(false);
        for( String dungeonName : dungeonNames ){
            addDungeon(dungeonName, new Dungeon(plugin,getConfig().getConfigurationSection("dungeons."+dungeonName)));
        }
    }
    
    public Dungeon isEntityAssociated(LivingEntity entity){
        for( String dungeonName : dungeons.keySet()){
            if(dungeons.get(dungeonName).isEntityAssociated(entity)){
                return dungeons.get(dungeonName);
            }
        }
        return null;
    }
    
    public boolean isDungeon(String dungeonName){
        return dungeons.containsKey(dungeonName);
    }
    
}
