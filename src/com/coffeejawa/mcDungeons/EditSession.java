package com.coffeejawa.mcDungeons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;

public class EditSession {

    Player player;
    Set<Block> placedBlocks;
    HashMap<Material,EntityType> materialMap;
    
    
    public EditSession(Player player, Material material, EntityType entityType){
        this.player = player;
        placedBlocks = new HashSet<Block>();
        materialMap = new HashMap<Material,EntityType>();
        materialMap.put(material,entityType);
    }
    
    public void close(mcDungeons plugin, String dungeonName){
        // create dungeon region
        Dungeon newDungeon = new Dungeon(plugin, dungeonName);
        
        // create point spawners from blocks
        for(Block block : placedBlocks){
            Material blockType = block.getType();
            block.setTypeId(0);
            
            EntityType entityType = materialMap.get(blockType);
            
            PointSpawner spawner = new PointSpawner(entityType, block.getLocation());
            
            newDungeon.addSpawner(spawner);
        }
        
        plugin.getDungeonConfigManager().addDungeon(dungeonName, newDungeon);
        plugin.getDungeonConfigManager().saveConfig();
    }
    
    public void addBlock(Block block){
        placedBlocks.add(block);
    }
    
    public boolean addMaterial(Material material, EntityType entityType){
        if(materialMap.containsKey(material)){
            // error. 
            return false;
        }
        else{
            materialMap.put(material, entityType);
            return true;
        }
    }
    
}
