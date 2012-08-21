package com.coffeejawa.mcDungeons;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EditSessionManager {
    private mcDungeons plugin;
    private Map<Player,EditSession> openEditSessions;

    public EditSessionManager(mcDungeons plugin) {
        // TODO Auto-generated constructor stub
        this.plugin = plugin;
        openEditSessions = new HashMap<Player,EditSession>();
    }

    public boolean startEditSession(Player player, Material material, EntityType entityType){
        // is the player already editing?
        if(!isEditing(player)){
            EditSession session = new EditSession(player, material, entityType);
            openEditSessions.put(player,session);
        }
        else{
            return getSession(player).addMaterial(material, entityType);
        }
        return true;
    }
    public void endEditSession(Player player, String dungeonName){
        EditSession sess = openEditSessions.get(player);
        sess.close(plugin, dungeonName);
        openEditSessions.remove(player);
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
