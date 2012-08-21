package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.Navigation;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageEvent;

public class mcdSpider extends net.minecraft.server.EntitySpider{

    private float speed;
    private boolean knockback;
    
    public mcdSpider(World world) {
        super(world);
        speed = 1.0f;
        knockback = true;
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

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.world.isStatic) {
            return false;
        } else {
            this.bq = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.k() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.aZ = 1.5F;
                boolean flag = true;
                
                // CraftBukkit start
                if (damagesource instanceof EntityDamageSource) {
                    EntityDamageEvent event = CraftEventFactory.handleEntityDamageEvent(this, damagesource, i);
                    if (event.isCancelled()) {
                        return false;
                    }
                    i = event.getDamage();
                }
                // CraftBukkit end

                if ((float) this.noDamageTicks > (float) this.maxNoDamageTicks / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.d(damagesource, i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.aL = this.health;
                    this.noDamageTicks = this.maxNoDamageTicks;
                    this.d(damagesource, i);
                    this.hurtTicks = this.aO = 10;
                }

                this.aP = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.c((EntityLiving) entity);
                    }

                    if (entity instanceof EntityHuman) {
                        this.lastDamageByPlayerTime = 60;
                        this.killer = (EntityHuman) entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity;

                        if (entitywolf.isTamed()) {
                            this.lastDamageByPlayerTime = 60;
                            this.killer = null;
                        }
                    }
                }

                if (flag) {
                    this.world.broadcastEntityEffect(this, (byte) 2);
                    if (damagesource != DamageSource.DROWN && damagesource != DamageSource.EXPLOSION2) {
                        this.K();
                    }

                    if(knockback)
                    {
	                    if (entity != null) {
	                        double d0 = entity.locX - this.locX;
	
	                        double d1;
	
	                        for (d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
	                            d0 = (Math.random() - Math.random()) * 0.01D;
	                        }
	
	                        this.aP = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.yaw;
	                        this.a(entity, i, d0, d1);
	                    } else {
	                        this.aP = (float) ((int) (Math.random() * 2.0D) * 180);
	                    }
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.world.makeSound((Entity)this, this.aS(), this.aP(), this.iI());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.world.makeSound((Entity)this, this.aR(), this.aP(), this.iI());
                }

                return true;
            }
        }
    }    
    
    private float iI() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public boolean getKnockback() {
    	return knockback;
    }
    
    public void setKnockback(boolean b) {
    	knockback = b;
    }
    
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
    
}