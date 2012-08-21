package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Spider;

public class mcdSpider extends net.minecraft.server.EntitySpider{

    private float speed;
    
    public mcdSpider(World world) {
        super(world);
        speed = 1.0f;
    }
    
    public void d(){
        Spider spider = (Spider) this.getBukkitEntity();
       
        Location from = new Location(spider.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
        Location to = new Location(spider.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        EntityCreature ec = ((CraftCreature)spider).getHandle();
        Navigation nav = ec.getNavigation();
        nav.a(this.speed * 0.8f);
          
        SpiderMoveEvent event = new SpiderMoveEvent(spider, from, to, nav);
        if (!event.isCancelled() && !spider.isDead()){
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