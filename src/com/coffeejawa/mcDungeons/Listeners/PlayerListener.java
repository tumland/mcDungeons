package com.coffeejawa.mcDungeons.Listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.coffeejawa.mcDungeons.*;
import com.coffeejawa.mcDungeons.Region.mcdRegion;

public class PlayerListener implements Listener {
    
    mcDungeons plugin;
    EntityRegistry entityRegistry;
    
    
    public PlayerListener(mcDungeons plugin, EntityRegistry entityRegistry){
        this.plugin = plugin;
        this.entityRegistry = entityRegistry;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        
        // check for spawn controlled regions in trigger range
        //List<Dungeon> dungeons = plugin.getDungeons();
        
        // get player regions
        WorldGuardHelper wgHelper = new WorldGuardHelper(plugin);
        List<String> regions = wgHelper.locationInRegionsNamed(event.getPlayer().getLocation());
        
//        List<Dungeon> dungeons = plugin.getDungeons();
//        
//        for( Dungeon dungeon : dungeons ){
//            // if player is in a trigger region
//            if(regions.contains(dungeon.getTriggerRegion().getName())){
//                // trigger dungeon
//                dungeon.trigger();
//            }
//        }
        
        
        

        
        
    }
}
