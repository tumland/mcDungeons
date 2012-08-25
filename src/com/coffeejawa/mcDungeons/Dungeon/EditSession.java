package com.coffeejawa.mcDungeons.Dungeon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.coffeejawa.mcDungeons.WorldGuardHelper;
import com.coffeejawa.mcDungeons.mcDungeons;
import com.google.common.collect.Iterables;

public class EditSession {
    mcDungeons plugin;
    Player player;
    Set<Block> placedBlocks;
    HashMap<Material,EntityType> materialMap;
    String regionName;
    long respawnTime;

    WorldGuardHelper wgHelper;
    
    public EditSession(mcDungeons plugin, Player player, String regionName, long respawnTime){
        this.plugin = plugin;
        this.player = player;
        placedBlocks = new HashSet<Block>();
        materialMap = new HashMap<Material,EntityType>();
        this.regionName = regionName;
        wgHelper = new WorldGuardHelper(plugin);
        this.respawnTime = respawnTime;
    }
    
    public void close(){

        Dungeon newDungeon = new Dungeon(plugin, regionName);
        
        World world = player.getWorld();
        
        if(placedBlocks.size() < 1){
            if(plugin.getConfig().getBoolean("debug")){
                plugin.logger.info("Error: Dungeon has no spawners!");
            }
            return;
        }
        else{
            // get the first block in the set
            Block firstBlock = Iterables.get(placedBlocks, 0);
                    
            world = firstBlock.getWorld();
            
        }
        // create point spawners from blocks
        for(Block block : placedBlocks){
            // all blocks must be in the same world
            if( !world.getName().equals(block.getWorld().getName()) ){
                // error player and blocks are not all in the same world.
                if(plugin.getConfig().getBoolean("debug")){
                    plugin.logger.info("Error: Dungeon blocks and player not all in the same world!");
                }
                return;
            }
            
            Material blockType = block.getType();
            block.setTypeId(0);
            
            EntityType entityType = materialMap.get(blockType);
            
            PointSpawner spawner = new PointSpawner(entityType, block.getLocation(), respawnTime);
            
            newDungeon.addSpawner(spawner);
            
        }
               
        // autoadd missing region to mcd regions
        if(!plugin.getRegionConfigManager().isRegionPresent(regionName)){
            plugin.getRegionConfigManager().addRegion(regionName);
        }
        plugin.getDungeonConfigManager().addDungeon(regionName, newDungeon);
        plugin.getDungeonConfigManager().saveConfig();
        
        
        
    }
    
    public void addBlock(Block block){
        
        if(!wgHelper.isInRegion(block.getLocation(), regionName)){
            player.sendMessage("Warning: block not in dungeon region!");
            return;
        }
        
        if(!materialMap.containsKey(block.getType())){
            return;
        }
        
        placedBlocks.add(block);
    }
    
    public boolean addMaterial(Material material, EntityType entityType){
        if(!material.isBlock()){
            return false;
        }
        
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
