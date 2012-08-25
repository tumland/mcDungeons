package com.coffeejawa.mcDungeons.Dungeon;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.coffeejawa.mcDungeons.mcDungeons;

public class EditSessionManager {
    private mcDungeons plugin;
    private Map<Player,EditSession> openEditSessions;

    public EditSessionManager(mcDungeons plugin) {
        // TODO Auto-generated constructor stub
        this.plugin = plugin;
        openEditSessions = new HashMap<Player,EditSession>();
    }

    public boolean startEditSession(Player player, String regionName, String strRespawnTime){
        // does dungeon named regionName already exist?
        if(plugin.getDungeonConfigManager().isDungeon(regionName)){
            return false;
        }
        // convert string to long, hope this works!
        long respawnTime = Integer.parseInt(strRespawnTime);
        
        // is the player already editing?
        if(!isEditing(player)){
            EditSession session = new EditSession(plugin, player, regionName, respawnTime);
            openEditSessions.put(player,session);
            return true;
        }
        return false;
    }
    public boolean endEditSession(Player player){
        if(!openEditSessions.containsKey(player)){
            return false;
        }
        
        EditSession sess = openEditSessions.get(player);
        sess.close();
        openEditSessions.remove(player);
        return true;
    }
    
    public Map<Player, EditSession> getOpenEditSessions() {
        return openEditSessions;
    }
    
    public boolean isEditing(Player player){
        return openEditSessions.containsKey(player);
    }
    public EditSession getSession(Player player){
        return openEditSessions.get(player);
    }

}
