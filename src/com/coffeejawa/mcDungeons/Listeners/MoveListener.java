package com.coffeejawa.mcDungeons.Listeners;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.coffeejawa.mcDungeons.EntityHelper;
import com.coffeejawa.mcDungeons.EntityRegistry;
import com.coffeejawa.mcDungeons.mcDungeons;
import com.coffeejawa.mcDungeons.Entities.CreeperMoveEvent;
import com.coffeejawa.mcDungeons.Entities.SkeletonMoveEvent;
import com.coffeejawa.mcDungeons.Entities.SpiderMoveEvent;
import com.coffeejawa.mcDungeons.Entities.ZombieMoveEvent;

public class MoveListener implements Listener {
 
    private mcDungeons plugin;    
    private EntityHelper entityHelper;
    
    
    public MoveListener(mcDungeons plugin){
        this.plugin = plugin;
        entityHelper = plugin.getEntityHelper();
    }
    
    @EventHandler
    public void onZombieMove(ZombieMoveEvent event) {
                
        Zombie zombie = event.getZombie();
        double zombieSpeed = entityHelper.getMaxSpeedX(event.getEntity());
        double zombieActivationRange = entityHelper.getMinActivationRange(event.getEntity());
        
        for (Player player : Bukkit.getOnlinePlayers()){
            // due to a bug in .distance() we need to check that the points are in the same world
            if(player.getWorld() != zombie.getWorld()){
                continue;
            }
            
            if(player.getLocation().distance(zombie.getLocation()) < zombieActivationRange){
                // set zombie speed
                EntityCreature ec = ((CraftCreature)zombie).getHandle();
                Navigation nav = ec.getNavigation();
                  nav.a((float)zombieSpeed);

                break;
            }
        }
    }
    
    @EventHandler
    public void onSkeletonMove(SkeletonMoveEvent event) {
        
        Skeleton skeleton = event.getSkeleton();
        double skeletonSpeed = entityHelper.getMaxSpeedX(event.getEntity());
        double skeletonActivationRange = entityHelper.getMinActivationRange(event.getEntity());
        
        for (Player player : Bukkit.getOnlinePlayers()){
            // due to a bug in .distance() we need to check that the points are in the same world
            if(player.getWorld() != skeleton.getWorld()){
                continue;
            }
            
            if(player.getLocation().distance(skeleton.getLocation()) < skeletonActivationRange){
                // set skeleton speed
                EntityCreature ec = ((CraftCreature)skeleton).getHandle();
                Navigation nav = ec.getNavigation();
                  nav.a((float)skeletonSpeed);

                break;
            }
        }
    }
    
    @EventHandler
    public void onCreeperMove(CreeperMoveEvent event) {
        
        Creeper creeper = event.getCreeper();
        double creeperSpeed = entityHelper.getMaxSpeedX(event.getEntity());
        double creeperActivationRange = entityHelper.getMinActivationRange(event.getEntity());
        
        for (Player player : Bukkit.getOnlinePlayers()){
            // due to a bug in .distance() we need to check that the points are in the same world
            if(player.getWorld() != creeper.getWorld()){
                continue;
            }
            
            if(player.getLocation().distance(creeper.getLocation()) < creeperActivationRange){
                // set creeper speed
                EntityCreature ec = ((CraftCreature)creeper).getHandle();
                Navigation nav = ec.getNavigation();
                  nav.a((float)creeperSpeed);
                  
                

                break;
            }
        }
    }
    
    @EventHandler
    public void onSpiderMove(SpiderMoveEvent event) {

        Spider spider = event.getSpider();
        double spiderSpeed = entityHelper.getMaxSpeedX(event.getEntity());
        double spiderActivationRange = entityHelper.getMinActivationRange(event.getEntity());
        
        for (Player player : Bukkit.getOnlinePlayers()){
            // due to a bug in .distance() we need to check that the points are in the same world
            if(player.getWorld() != spider.getWorld()){
                continue;
            }
            
            if(player.getLocation().distance(spider.getLocation()) < spiderActivationRange){
                // set spider speed
                EntityCreature ec = ((CraftCreature)spider).getHandle();
                Navigation nav = ec.getNavigation();
                  nav.a((float)spiderSpeed);

                break;
            }
        }
    }
}
