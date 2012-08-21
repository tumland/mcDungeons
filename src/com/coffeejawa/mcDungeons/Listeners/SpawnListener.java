package com.coffeejawa.mcDungeons.Listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.coffeejawa.mcDungeons.Dungeon;
import com.coffeejawa.mcDungeons.WorldGuardHelper;
import com.coffeejawa.mcDungeons.mcDungeons;

public class SpawnListener implements Listener {
    
    private mcDungeons plugin;
    private WorldGuardHelper wgHelper;
    
    public SpawnListener(mcDungeons plugin){
        this.plugin = plugin;
        wgHelper = new WorldGuardHelper(plugin);
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event){
        Location loc = event.getLocation();
//        Boolean isDungeonPresent = plugin.getDungeonHelper().isLocationInDungeon(loc);
//        
//        if(isDungeonPresent){
//            event.setCancelled(true);
//        }
        

        ArrayList<String> regionNames = wgHelper.locationInRegionsNamed(loc);
        if(!regionNames.isEmpty()){
            // add to entity registry
            plugin.getEntityRegistry().add(event.getEntity(), regionNames);
            if(plugin.getConfig().getBoolean("debug")){
                plugin.logger.info(String.format("Added to entity registry: %s",event.getEntity().getType().toString()));
            }
        }
         
        plugin.getEntityHelper().replaceCreature(event.getEntity());
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        plugin.getEntityRegistry().remove(event.getEntity());
        if(plugin.getConfig().getBoolean("debug")){
            plugin.logger.info(String.format("Removed from entity registry: %s",event.getEntity().getType().toString()));
        }
        
        Dungeon dungeon = plugin.getDungeonConfigManager().isEntityAssociated(event.getEntity());
        
        if(dungeon != null){
            final String dungeonName = dungeon.getName();
            
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,  new Runnable()
            {
                public void run()
                {
                    plugin.triggerDungeonSpawns(dungeonName);
                }
            }, (long)200.0);
        }
    }
    
//  @EventHandler
//  public void onChunkUnload(ChunkUnloadEvent event){
//      Entity[] entities = event.getChunk().getEntities();
//      
//      for( Entity entity : entities ){
//          entityRegistry.remove(entity);
//          if(plugin.getConfig().getBoolean("debug")){
//              plugin.logger.info(String.format("Removed from entity registry: %s",entity.getType().toString()));
//          }
//      }
//  }
//  
  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event){
      Entity[] entities = event.getChunk().getEntities();
      
      for( Entity entity : entities ){
          
          plugin.getEntityHelper().replaceCreature(entity);   
          
//          // add to entity registry
//          entityRegistry.add(entity, regionNames);
//          if(_plugin.getConfig().getBoolean("debug")){
//              _plugin.logger.info(String.format("Added to entity registry: %s",entity.getType().toString()));
//          }
      }
  }
}


//public SuperDuperFastZombie(World world) {
//super(world);
//this.bb = 10.0F;
//this.goalSelector.a(0, new PathfinderGoalFloat(this));
//this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
//this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bb, false));
//this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, this.bb, true));
//this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, this.bb));
//this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bb, false));
//this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bb));
//this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
//this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
//this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
//this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
//}
