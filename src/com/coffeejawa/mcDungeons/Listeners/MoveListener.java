package com.coffeejawa.mcDungeons.Listeners;

import org.bukkit.event.Listener;

import com.coffeejawa.mcDungeons.EntityHelper;
import com.coffeejawa.mcDungeons.mcDungeons;

public class MoveListener implements Listener {
 
    @SuppressWarnings("unused")
    private mcDungeons plugin;    
    @SuppressWarnings("unused")
    private EntityHelper entityHelper;
    
    
    public MoveListener(mcDungeons plugin){
        this.plugin = plugin;
        entityHelper = plugin.getEntityHelper();
    }
    
//    @EventHandler
//    public void onZombieMove(ZombieMoveEvent event) {
//                
//        plugin.logger.info("zombie move event");
//        
//        Zombie zombie = event.getZombie();
//        double zombieSpeed = entityHelper.getMaxSpeedX(event.getEntity());
//        double zombieActivationRange = entityHelper.getMinActivationRange(event.getEntity());
//        
//        if( zombie instanceof mcdZombie ){
//            mcdZombie myZombie = (mcdZombie) zombie;
//            
//            plugin.logger.info("speed = "+myZombie.getSpeed());
//        }
//        
//        EntityCreature ec = ((CraftCreature)zombie).getHandle();
//        if( ec instanceof mcdZombie ){
//            mcdZombie myZombie = (mcdZombie) ec;
//            
//            plugin.logger.info("speed = "+myZombie.getSpeed());
//        }
        
//        for (Player player : Bukkit.getOnlinePlayers()){
//            // due to a bug in .distance() we need to check that the points are in the same world
//            if(player.getWorld() != zombie.getWorld()){
//                continue;
//            }
//            
//            if(player.getLocation().distance(zombie.getLocation()) < zombieActivationRange){
//                // set zombie speed
//                EntityCreature ec = ((CraftCreature)zombie).getHandle();
//                
//                
//                Navigation nav = ec.getNavigation();
//                  nav.a((float)zombieSpeed);
//
//                break;
//            }
//        }
//    }
//    
//    @EventHandler
//    public void onSkeletonMove(SkeletonMoveEvent event) {
//        
//        Skeleton skeleton = event.getSkeleton();
//        double skeletonSpeed = entityHelper.getMaxSpeedX(event.getEntity());
//        double skeletonActivationRange = entityHelper.getMinActivationRange(event.getEntity());
//        
//        for (Player player : Bukkit.getOnlinePlayers()){
//            // due to a bug in .distance() we need to check that the points are in the same world
//            if(player.getWorld() != skeleton.getWorld()){
//                continue;
//            }
//            
//            if(player.getLocation().distance(skeleton.getLocation()) < skeletonActivationRange){
//                // set skeleton speed
//                EntityCreature ec = ((CraftCreature)skeleton).getHandle();
//                Navigation nav = ec.getNavigation();
//                  nav.a((float)skeletonSpeed);
//
//                break;
//            }
//        }
//    }
//    
//    @EventHandler
//    public void onCreeperMove(CreeperMoveEvent event) {
//        
//        Creeper creeper = event.getCreeper();
//        double creeperSpeed = entityHelper.getMaxSpeedX(event.getEntity());
//        double creeperActivationRange = entityHelper.getMinActivationRange(event.getEntity());
//        
//        for (Player player : Bukkit.getOnlinePlayers()){
//            // due to a bug in .distance() we need to check that the points are in the same world
//            if(player.getWorld() != creeper.getWorld()){
//                continue;
//            }
//            
//            if(player.getLocation().distance(creeper.getLocation()) < creeperActivationRange){
//                // set creeper speed
//                EntityCreature ec = ((CraftCreature)creeper).getHandle();
//                Navigation nav = ec.getNavigation();
//                  nav.a((float)creeperSpeed);
//                  
//                
//
//                break;
//            }
//        }
//    }
//    
//    @EventHandler
//    public void onSpiderMove(SpiderMoveEvent event) {
//
//        Spider spider = event.getSpider();
//        double spiderSpeed = entityHelper.getMaxSpeedX(event.getEntity());
//        double spiderActivationRange = entityHelper.getMinActivationRange(event.getEntity());
//        
//        for (Player player : Bukkit.getOnlinePlayers()){
//            // due to a bug in .distance() we need to check that the points are in the same world
//            if(player.getWorld() != spider.getWorld()){
//                continue;
//            }
//            
//            if(player.getLocation().distance(spider.getLocation()) < spiderActivationRange){
//                // set spider speed
//                EntityCreature ec = ((CraftCreature)spider).getHandle();
//                Navigation nav = ec.getNavigation();
//                  nav.a((float)spiderSpeed);
//
//                break;
//            }
//        }
//    }
}
