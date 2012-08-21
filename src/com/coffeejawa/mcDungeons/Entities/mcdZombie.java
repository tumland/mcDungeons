package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Zombie;

public class mcdZombie extends net.minecraft.server.EntityZombie{

    private float speed;
    
    public mcdZombie(World world) {
        super(world);
        speed = 1.0f;
    }
    
    public void d(){
        Zombie zombie = (Zombie) this.getBukkitEntity();
               
        Location from = new Location(zombie.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
        Location to = new Location(zombie.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        EntityCreature ec = ((CraftCreature)zombie).getHandle();
        Navigation nav = ec.getNavigation();
        nav.a(this.speed * 0.35f);
          
        ZombieMoveEvent event = new ZombieMoveEvent(zombie, from, to, nav);
        if (!event.isCancelled() && !zombie.isDead()){
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