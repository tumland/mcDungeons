package com.coffeejawa.mcDungeons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.coffeejawa.mcDungeons.Entities.mcdCreeper;
import com.coffeejawa.mcDungeons.Entities.mcdSkeleton;
import com.coffeejawa.mcDungeons.Entities.mcdSpider;
import com.coffeejawa.mcDungeons.Entities.mcdZombie;
import com.coffeejawa.mcDungeons.Level.mcdLevel;
import com.coffeejawa.mcDungeons.Region.mcdRegion;

public class EntityHelper {

    private mcDungeons plugin;
    
    public EntityHelper(mcDungeons plugin){
        this.plugin = plugin;
    }
    
    public void replaceCreatures(World world){
        List<Entity> entities = world.getEntities();
        for( Entity entity : entities){
            replaceCreature(entity);
        }
    }
    public void replaceCreature(Entity entity){
        if(entity.isDead()){
            return;
        }
        
        Location location = entity.getLocation();
        EntityType entityType = entity.getType();
 
        net.minecraft.server.World mcWorld = ((CraftWorld) (location.getWorld())).getHandle();
        net.minecraft.server.Entity mcEntity = (((CraftEntity) entity).getHandle());
      
      if (entityType == EntityType.ZOMBIE && mcEntity instanceof mcdZombie == false){
        
          mcdZombie zombie = new mcdZombie(mcWorld);

          zombie.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntityZombie) mcEntity);
          mcWorld.addEntity(zombie, SpawnReason.CUSTOM);

          return;
      }
      
      if (entityType == EntityType.CREEPER && mcEntity instanceof mcdCreeper == false){
          mcdCreeper creeper = new mcdCreeper(mcWorld);

          creeper.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntityCreeper) mcEntity);
          mcWorld.addEntity(creeper, SpawnReason.CUSTOM);

          return;
      }
      
      if (entityType == EntityType.SKELETON && mcEntity instanceof mcdSkeleton == false){
          mcdSkeleton skeleton = new mcdSkeleton(mcWorld);

          skeleton.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntitySkeleton) mcEntity);
          mcWorld.addEntity(skeleton, SpawnReason.CUSTOM);

          return;
      }
      
      if (entityType == EntityType.SPIDER && mcEntity instanceof mcdSpider == false){
          mcdSpider spider = new mcdSpider(mcWorld);

          spider.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntitySpider) mcEntity);
          mcWorld.addEntity(spider, SpawnReason.CUSTOM);

          return;
      }
        
    }
    
    

    
    public double getMaxHealthX(Entity entity)
    {
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        return (Double) getMaxRegionDoubleAttribute(entity, "healthX");
    }
    public double getMaxDamageX(Entity entity){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        return (Double) getMaxRegionDoubleAttribute(entity, "damageX"); 
    }
    
    public double getMaxSpeedX(Entity entity){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        return getMaxRegionDoubleAttribute(entity, "speedX");
    }
    
    public double getMinActivationRange(Entity entity) { 
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return (Double) getWorldAttribute("activationRange");
        }
        
        return (Double) getMinRegionDoubleAttribute(entity, "activationRange");
    }
    
    public Object getWorldAttribute(String attributeName){
        // get default level
        int serverDefaultLevel = plugin.getConfig().getInt("defaultLevel");
        mcdLevel level = plugin.getLevelConfigManager().getLevelObject(serverDefaultLevel);
        return level.getSettings().get(attributeName);
    }
    
    private double getMaxRegionDoubleAttribute(Entity entity, String attributeName){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        double maxAttribValue = (Double) getWorldAttribute(attributeName);    

        ArrayList<String> regionNames = plugin.getEntityRegistry().getRegions(entity);
        
        for(String regionName : regionNames) {
            // get region object
            mcdRegion currRegion = plugin.getRegionConfigManager().getRegionByName(regionName);
            if(currRegion != null){
                // get region object's level
                mcdLevel level = plugin.getLevelConfigManager().getLevelObject(currRegion.getLevel());
                if(level != null){
                    maxAttribValue = Math.max(maxAttribValue,(Double) level.getSettings().get(attributeName));
                }
            }
        }
        return maxAttribValue; 
    }

    private double getMinRegionDoubleAttribute(Entity entity, String attributeName){
        if (plugin.worldguard == null || !plugin.bWorldGuardEnabled){
            return 1.0;
        }
        double minAttribValue = (Double) getWorldAttribute(attributeName);    

        ArrayList<String> regionNames = plugin.getEntityRegistry().getRegions(entity);
        
        for(String regionName : regionNames) {
            // get region object
            mcdRegion currRegion = plugin.getRegionConfigManager().getRegionByName(regionName);
            if(currRegion != null){
                // get region object's level
                mcdLevel level = plugin.getLevelConfigManager().getLevelObject(currRegion.getLevel());
                if(level != null){
                    minAttribValue = Math.min(minAttribValue,(Double) level.getSettings().get(attributeName));
                }
            }
        }
        return minAttribValue; 
    }
    
//    public boolean isSpawnControlEnabledInAnyRegion(ArrayList<String> regionNames){
//        for( String regionName : regionNames ){
//            mcdRegion region = plugin.getRegionConfigManager().getRegionByName(regionName);
//            if(region.isEnableSpawnControl()){
//                return true;
//            }
//            // keep looking
//        }
//        return false;
//    }
//    
//    public mcdRegion getFirstSpawnControlEnabledRegion(ArrayList<String> regionNames){
//        for( String regionName : regionNames ){
//            mcdRegion region = plugin.getRegionConfigManager().getRegionByName(regionName);
//            if(region.isEnableSpawnControl()){
//                return region;
//            }
//            // keep looking
//        }
//        return null;
//    }
//    
//    public List<mcdRegion> getSpawnControlledRegions(World world){
//        WorldGuardHelper wgHelper = new WorldGuardHelper(plugin);
//        List<String> regionNames = wgHelper.getAllRegionNames(world);
//        
//        List<mcdRegion> spawnControlledRegions = new ArrayList<mcdRegion>();
//        
//        for( String regionName : regionNames ){
//            mcdRegion region = plugin.getRegionConfigManager().getRegionByName(regionName);
//            if(region == null){
//                continue;
//            }
//            if(region.isEnableSpawnControl()){
//                spawnControlledRegions.add(region);
//            }
//        }
//        return spawnControlledRegions;
//    }
}
