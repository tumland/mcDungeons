package com.coffeejawa.mcDungeons.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.coffeejawa.mcDungeons.mcDungeons;
import com.coffeejawa.mcDungeons.Dungeon.EditSession;

public class BlockListener implements Listener{
    mcDungeons plugin;
    
    
    public BlockListener(mcDungeons plugin){
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(!plugin.getEditSessionManager().isEditing(event.getPlayer())){
            // player is not in an edit session, ignore
            return;
        }
        EditSession sess = plugin.getEditSessionManager().getOpenEditSessions().get(event.getPlayer());
        sess.addBlock(event.getBlock());
    }
}
