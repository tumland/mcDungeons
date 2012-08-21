package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalBreakDoor;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
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
        nav.a(this.speed * 0.23f);
          
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
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, this.bw * speed, false));
        this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, EntityVillager.class, this.bw * speed, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, this.bw * speed));
        this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bw * speed, false));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bw * speed));
    }
    
    
    
}