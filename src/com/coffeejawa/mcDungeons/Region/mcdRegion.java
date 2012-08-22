package com.coffeejawa.mcDungeons.Region;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

import com.coffeejawa.mcDungeons.mcDungeons;
import com.sk89q.worldedit.BlockVector;

public class mcdRegion {
    private mcDungeons plugin;
    private String name;
    private int level;
    private boolean enableSpawnControl;
    
    BlockVector min;
    BlockVector max;
    
    mcdRegion(mcDungeons plugin, String name, ConfigurationSection settings){
        this.plugin = plugin;
        
        this.name = name;
        this.level = settings.getInt("level");
        this.enableSpawnControl = settings.getBoolean("enableSpawnControl");
        
//        //create dungeons
//        if(enableSpawnControl){
//            this.Dungeon = new Dungeon(plugin,settings);
//        }
        
    }
    
    public boolean isEnableSpawnControl() {
        return enableSpawnControl;
    }

    public void setEnableSpawnControl(boolean enableSpawnControl) {
        this.enableSpawnControl = enableSpawnControl;
    }


    public HashMap<String, Object> getSettings(){
        HashMap<String,Object> tempMap = new HashMap<String,Object>();
        tempMap.put("name", getName());
        tempMap.put("level", getLevel());
        return tempMap;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getLevel() {
        return level;
    }
    
    
    
}
