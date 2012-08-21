package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Enderman;

public class mcdEnderman extends net.minecraft.server.EntityEnderman{

    private float speed;
    
    public mcdEnderman(World world) {
        super(world);
        speed = 0.1f;
    }
    
    public void d(){
        Enderman enderman = (Enderman) this.getBukkitEntity();
       
        Location from = new Location(enderman.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
        Location to = new Location(enderman.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        EntityCreature ec = ((CraftCreature)enderman).getHandle();
        Navigation nav = ec.getNavigation();
          nav.a(this.speed);
          
        EndermanMoveEvent event = new EndermanMoveEvent(enderman, from, to, nav);
        
        if (!event.isCancelled() && !enderman.isDead()){
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