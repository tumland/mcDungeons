package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Skeleton;

public class mcdSkeleton extends net.minecraft.server.EntitySkeleton{

    private float speed;
    
    public mcdSkeleton(World world) {
        super(world);
        speed = 1.0f;
    }
    
    public void d(){
        Skeleton skeleton = (Skeleton) this.getBukkitEntity();
       
        Location from = new Location(skeleton.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
        Location to = new Location(skeleton.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        EntityCreature ec = ((CraftCreature)skeleton).getHandle();
        Navigation nav = ec.getNavigation();
          nav.a(this.speed * 0.25f);
          
        SkeletonMoveEvent event = new SkeletonMoveEvent(skeleton, from, to, nav);
        
        if (!event.isCancelled() && !skeleton.isDead()){
            this.world.getServer().getPluginManager().callEvent(event);
        }
        
        super.d();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
}