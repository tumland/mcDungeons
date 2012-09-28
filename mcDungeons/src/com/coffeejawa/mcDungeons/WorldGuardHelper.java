package com.coffeejawa.mcDungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardHelper {

    mcDungeons plugin;
    WorldGuardPlugin wg;
    
    public WorldGuardHelper(mcDungeons plugin){
        this.plugin = plugin;
        wg = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
    }

    public ArrayList<String> locationInRegionsNamed(Location location)
    {
        if(wg == null){
            return new ArrayList<String>();
        }
        
        Map<String,ProtectedRegion> regionMap = getRegions(location.getWorld());
               
        
        ArrayList<String> regionNames = new ArrayList<String>();
        
        for(ProtectedRegion pr : regionMap.values()) {
            int depth = 1;
            ProtectedRegion p = pr;
            while(p.getParent() != null) {
                depth++;
                p = p.getParent();
            }
            if(depth > 16)
                continue;
            if( isInRegion(location, pr) ){
                String name = pr.getId();
                regionNames.add(name);
            }
        
        }
        return regionNames;
    }

    public boolean isInRegion(Location location, ProtectedRegion region) {

        String tn = region.getTypeName();
        com.sk89q.worldedit.BlockVector l0 = region.getMinimumPoint();      
        com.sk89q.worldedit.BlockVector l1 = region.getMaximumPoint();

        if(tn.equalsIgnoreCase("cuboid")) { 
  
            Location l = location;
            
            // Is player's X within the region?
            if((l0.getBlockX() < l.getBlockX() && l.getBlockX() < l1.getBlockX()) || (l1.getBlockX() < l.getBlockX() && l.getBlockX() < l0.getBlockX())) {
                // Is player's Z within the region?
                if((l0.getBlockZ() < l.getBlockZ() && l.getBlockZ() < l1.getBlockZ()) || (l1.getBlockZ() < l.getBlockZ() && l.getBlockZ() < l0.getBlockZ())){
                    return true;
                }
            }
                
        }
//        else if(tn.equalsIgnoreCase("polygon")) {
//TODO: add polygon support

        else {  
            return false;
        }
        return false;
    }
    public boolean isInRegion(Location location, String regionName) {
        ProtectedRegion region = this.getRegion(regionName, location.getWorld());
        if(region != null){
            isInRegion(location, region);
            return true;
        }
        return false;
        
    }
    
    
    public Map<String,ProtectedRegion> getRegions(World world) {
        Map<String,ProtectedRegion> regionsMap = new HashMap<String,ProtectedRegion>();
        if(wg == null) {
            // return empty map
            return regionsMap;
        }
        RegionManager rm = wg.getRegionManager(world); 
        if(rm == null) {
            // return empty map
            return regionsMap;
        }
            
      return rm.getRegions();
    }
    public ProtectedRegion getRegion(String regionName, World world) {
        if(wg == null) {
            // return empty map
            return null;
        }
        RegionManager rm = wg.getRegionManager(world); 
        if(rm == null) {
            // return empty map
            return null;
        }
            
      return rm.getRegion(regionName);
    }
    
    public ArrayList<String> getAllRegionNames(World world){
        Map<String,ProtectedRegion> regions = getRegions(world);       
        ArrayList<String> names = new ArrayList<String>();
        names.addAll(regions.keySet());
        return names;
    }
    
    public boolean isRegion(String regionName, World world){
        if (getAllRegionNames(world).contains(regionName)){
            return true;
        }
        return false;
    }
    
    public boolean isRegionAnyWorld(String regionName){
        List<World> worlds = plugin.getServer().getWorlds();
        for (World world : worlds){
            if (getAllRegionNames(world).contains(regionName)){
                return true;
            }
        }
        return false;
    }
    
    public String getRegionType(String regionName, World world){
        ProtectedRegion pr = getRegion(regionName, world);
        
        if( pr != null )
            return pr.getTypeName();
        else
            return "";
    }
    
    public boolean addRegion(String id, World world, BlockVector min, BlockVector max){
        RegionManager mgr = wg.getGlobalRegionManager().get(world);
        if (mgr.hasRegion(id)) {
            return false;
        }
        ProtectedRegion region;
        region = new ProtectedCuboidRegion(id, min, max);
        mgr.addRegion(region);
        return true;
    }
    
    public boolean isRegionTypeSupported(String regionName){
        WorldGuardHelper wgHelper = new WorldGuardHelper(plugin);
        
        if(!isRegionAnyWorld(regionName)){
            return false;
        }
        
        for( World world : plugin.getServer().getWorlds() ){
            if(wgHelper.isRegion(regionName, world)){
                String regionType = wgHelper.getRegionType(regionName,world);
                if(!regionType.equalsIgnoreCase("cuboid")){
                    return false;
                }
            }
        }
        return true;
    }
}
