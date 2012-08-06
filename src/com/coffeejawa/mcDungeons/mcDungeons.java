package com.coffeejawa.mcDungeons;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.coffeejawa.mcDungeons.Level.LevelCommandHelper;
import com.coffeejawa.mcDungeons.Level.LevelConfigMgr;
import com.coffeejawa.mcDungeons.Listeners.DamageListener;
import com.coffeejawa.mcDungeons.Listeners.MoveListener;
import com.coffeejawa.mcDungeons.Listeners.SpawnListener;
import com.coffeejawa.mcDungeons.Region.RegionCommandHelper;
import com.coffeejawa.mcDungeons.Region.RegionConfigMgr;
import com.coffeejawa.mcDungeons.Entities.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class mcDungeons extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");

	public WorldGuardPlugin worldguard;
	public boolean bWorldGuardEnabled;
	
	private RegionConfigMgr regionConfigManager;
	private LevelConfigMgr levelConfigManager;
	
	private EntityRegistry entityRegistry;
	
	@Override
	public void onEnable() 
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		
		bWorldGuardEnabled = false;
		
		getConfiguration();
		
		regionConfigManager = new RegionConfigMgr(this);
		
		levelConfigManager = new LevelConfigMgr(this);
		
		entityRegistry = new EntityRegistry(this);
		
		this.logger.info(pdfFile.getName() + " v" + pdfFile.getVersion() + " Has Been Enabled!");
        getServer().getPluginManager().registerEvents(new DamageListener(this,entityRegistry), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(this,entityRegistry), this);
        getServer().getPluginManager().registerEvents(new MoveListener(this,entityRegistry), this);
        
        try{
            Class<?>[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;

            Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);

            a.setAccessible(true);

            a.invoke(a, mcdCreeper.class, "Creeper", 50);
            a.invoke(a, mcdSkeleton.class, "Skeleton", 51);
            a.invoke(a, mcdSpider.class, "Spider", 52);
            a.invoke(a, mcdZombie.class, "Zombie", 54);
//            a.invoke(a, BloodMoonEntityEnderman.class, "Enderman", 58);
        }catch (Exception e){
            e.printStackTrace();

            this.setEnabled(false);
            return;
        }
        
        List<World> worlds = this.getServer().getWorlds();
        
        for( World world : worlds){
            replaceCreatures(world);
        }
    }
	
	@Override
	public void onDisable(){
	    saveConfig();
	    regionConfigManager.saveConfig();
	    levelConfigManager.saveConfig();
	       
        getLogger().info("mcDungeons has been disabled.");

	}
	
	
	public void getConfiguration()
	{	
		this.reloadConfig();
		
		if(getConfig().getBoolean("useWorldGuard")){
			this.bWorldGuardEnabled = checkWorldGuard();
		}
		
		if (this.bWorldGuardEnabled == true && worldguard != null)
			logger.info("[" + this.getDescription().getName()
					+ "] WorldGuard support enabled.");
	}
	public RegionConfigMgr getRegionConfigManager(){
	    return this.regionConfigManager;
	}
    public LevelConfigMgr getLevelConfigManager(){
        return this.levelConfigManager;
    }

    private boolean checkWorldGuard()
	{
		if (getServer().getPluginManager().getPlugin("WorldGuard") == null)
		{
			getLogger().info("WorldGuard plugin not found");
			return false;
		}
		worldguard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		return true;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = cmd.getName();
		if (name.equalsIgnoreCase("mcd")){
			// check the first arg and call any appropriate nested parsers
			if(args.length == 0){
				sender.sendMessage("mcDungeons Command Interface");
				sender.sendMessage("Usage: /mcd <region|level|debug> [add|set|list|reset|remove]");
			}
			if (args.length > 0){
				String arg = args[0];

				if(arg.equalsIgnoreCase("level")){
				    LevelCommandHelper helper = new LevelCommandHelper(this);
				    helper.handleLevelCommand(sender, this, Arrays.copyOfRange(args, 1, args.length));
				}
				else if(arg.equalsIgnoreCase("region")){
                    RegionCommandHelper helper = new RegionCommandHelper();
                    helper.handleRegionCommand(sender, this, Arrays.copyOfRange(args, 1, args.length));
				}
				else if(arg.equalsIgnoreCase("debug")){
					if(getConfig().getBoolean("debug")){
						getConfig().set("debug", false);
						sender.sendMessage("Toggled DEBUG mode to OFF");
					}
					else{
						this.getConfig().set("debug", true);
						sender.sendMessage("Toggled DEBUG mode to ON");
					}
				}
				else{
				    return false;
				}
			}
	
		}
		return true;
	}
	
	public void replaceCreatures(World world){
	    List<Entity> entities = world.getEntities();
	    for( Entity entity : entities){
	        replaceCreature(entity);
	    }
	}
	public void replaceCreature(Entity entity){
	    if(entity.isDead()){
	        return;
	    }
	    
        Location location = entity.getLocation();
        EntityType entityType = entity.getType();
 
        net.minecraft.server.World mcWorld = ((CraftWorld) (location.getWorld())).getHandle();
        net.minecraft.server.Entity mcEntity = (((CraftEntity) entity).getHandle());
      
      if (entityType == EntityType.ZOMBIE && mcEntity instanceof mcdZombie == false){
          mcdZombie zombie = new mcdZombie(mcWorld);

          zombie.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntityZombie) mcEntity);
          mcWorld.addEntity(zombie, SpawnReason.CUSTOM);

          return;
      }
      
      if (entityType == EntityType.CREEPER && mcEntity instanceof mcdCreeper == false){
          mcdCreeper creeper = new mcdCreeper(mcWorld);

          creeper.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntityCreeper) mcEntity);
          mcWorld.addEntity(creeper, SpawnReason.CUSTOM);

          return;
      }
      
      if (entityType == EntityType.SKELETON && mcEntity instanceof mcdSkeleton == false){
          mcdSkeleton skeleton = new mcdSkeleton(mcWorld);

          skeleton.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntitySkeleton) mcEntity);
          mcWorld.addEntity(skeleton, SpawnReason.CUSTOM);

          return;
      }
      
      if (entityType == EntityType.SPIDER && mcEntity instanceof mcdSpider == false){
          mcdSpider spider = new mcdSpider(mcWorld);

          spider.setPosition(location.getX(), location.getY(), location.getZ());
          
          mcWorld.removeEntity((net.minecraft.server.EntitySpider) mcEntity);
          mcWorld.addEntity(spider, SpawnReason.CUSTOM);

          return;
      }
	    
	}
	
}
