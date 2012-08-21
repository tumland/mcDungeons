package com.coffeejawa.mcDungeons.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.coffeejawa.mcDungeons.EntityRegistry;
import com.coffeejawa.mcDungeons.WorldGuardHelper;
import com.coffeejawa.mcDungeons.mcDungeons;

public class DamageListener implements Listener 
{
	private final mcDungeons plugin;
	private WorldGuardHelper wgHelper;
    
	public DamageListener(mcDungeons plugin) {
		super();

		this.plugin = plugin;
		wgHelper = new WorldGuardHelper(plugin);
	}
    
	@EventHandler
	public void onEntityDamage	( EntityDamageEvent event )	
	{
		// Damage cause by player
		if( event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player){
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			Player playerDamager = null; 
			
			// Direct damage
			if( damageEvent.getDamager().getType() == EntityType.PLAYER )
			{
				playerDamager = (Player) damageEvent.getDamager();
			}
			// Projectile damage
			else if(damageEvent.getDamager() instanceof Projectile ) {
				Projectile projectile = (Projectile) damageEvent.getDamager();
				if (projectile.getShooter().getType() == EntityType.PLAYER ) {
					playerDamager = (Player) projectile.getShooter();
				}
			}

			if(playerDamager == null){
			    return;
			}
						
			double healthFactor = plugin.getEntityHelper().getMaxHealthX(event.getEntity());
			double damage = Math.ceil(event.getDamage() / healthFactor);
						
			if(plugin.getConfig().getBoolean("debug")){
				plugin.logger.info(String.format("YOU DAMAGED AN ENTITY: AMT %d", event.getDamage()));	
				plugin.logger.info(String.format("maxMobHealthMult %f", healthFactor));
				plugin.logger.info(String.format("Adjusted Damage: AMT %f", damage));
				playerDamager.sendMessage(String.format("Adjusted Damage: AMT %f", damage));
			}
			
			event.setDamage((int) damage);
		}
		
		// Player being damaged
		if( event instanceof EntityDamageByEntityEvent && event.getEntity() instanceof Player ){
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			Entity damager = null;
            Player player = (Player) event.getEntity();
			
			// Direct damage
			if( damageEvent.getEntityType() == EntityType.PLAYER )
			{
			    damager = damageEvent.getDamager();
			}
	        // Projectile damage
			else if(damageEvent.getDamager() instanceof Projectile ) {
                Projectile projectile = (Projectile) damageEvent.getDamager();
                damager = projectile.getShooter();
            }

	        if(damager == null){
	            return;
	        }
		    
		    double damageFactor = plugin.getEntityHelper().getMaxDamageX(damager);
		    
			if(plugin.getConfig().getBoolean("debug")){
				plugin.logger.info(String.format("you were hit by an entity: amt %d", event.getDamage()));	
				plugin.logger.info(String.format("mobDifficultyLevel %f", damageFactor));
			}
			
			double damage = Math.ceil(event.getDamage() * damageFactor);
						
			if(plugin.getConfig().getBoolean("debug")){
				plugin.logger.info(String.format("Adjusted Damage: AMT %f", damage));
				player.sendMessage(String.format("Adjusted Damage: AMT %f", damage));
			}
			
			event.setDamage((int) damage);
		}
	}
	
	// Deprecated. maxLevel does not imply max damage, etc.
	
//	private int getMaxLevel(Entity entity){
//	    // where did this guy spawn?
//	    ArrayList<String> regionNames = entityRegistry.getRegions(entity);
//	    int maxLevel = 0;
//	    for(String regionName : regionNames) {
//	        mcdRegion currRegion = plugin.getRegionConfigManager().getRegionByName(regionName);
//	        mcdLevel level = plugin.getLevelsConfigManager().getLevelObject(currRegion.getLevel());
//	        int levelNo = level.getNumber();
//	        if(levelNo > maxLevel){
//	            maxLevel = levelNo;
//	        }
//	    }
//	    return maxLevel;
//	}

    
    
}



