package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Creeper;
import com.coffeejawa.mcDungeons.Entities.CreeperMoveEvent;

public class mcdCreeper extends net.minecraft.server.EntityCreeper{

    private float speed;
    
    public mcdCreeper(World world) {
        super(world);
        speed = 1.0f;
    }
    
    public void d(){
        Creeper creeper = (Creeper) this.getBukkitEntity();
       
        Location from = new Location(creeper.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
        Location to = new Location(creeper.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        EntityCreature ec = ((CraftCreature)creeper).getHandle();
        Navigation nav = ec.getNavigation();
        nav.a(this.speed*0.25F);
          
        CreeperMoveEvent event = new CreeperMoveEvent(creeper, from, to, nav);
        if (!event.isCancelled() && !creeper.isDead()){
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