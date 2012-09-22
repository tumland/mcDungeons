package com.coffeejawa.mcDungeons;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.coffeejawa.mcDungeons.Dungeon.DungeonConfigMgr;
import com.coffeejawa.mcDungeons.Dungeon.EditSession;
import com.coffeejawa.mcDungeons.Dungeon.EditSessionManager;
import com.coffeejawa.mcDungeons.Entities.mcdCreeper;
import com.coffeejawa.mcDungeons.Entities.mcdEnderman;
import com.coffeejawa.mcDungeons.Entities.mcdSkeleton;
import com.coffeejawa.mcDungeons.Entities.mcdSpider;
import com.coffeejawa.mcDungeons.Entities.mcdZombie;
import com.coffeejawa.mcDungeons.Level.LevelConfigMgr;
import com.coffeejawa.mcDungeons.Listeners.BlockListener;
import com.coffeejawa.mcDungeons.Listeners.DamageListener;
import com.coffeejawa.mcDungeons.Listeners.MoveListener;
import com.coffeejawa.mcDungeons.Listeners.SpawnListener;
import com.coffeejawa.mcDungeons.Region.RegionConfigMgr;
import com.google.common.base.Joiner;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class mcDungeons extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");

	public WorldGuardPlugin worldguard;
	public boolean bWorldGuardEnabled;
	
	private RegionConfigMgr regionConfigManager;
	private LevelConfigMgr levelConfigManager;
	private DungeonConfigMgr dungeonConfigManager;
	
	private EditSessionManager editSessionManager;
	
	private EntityRegistry entityRegistry;
    private EntityHelper entityHelper;
    

    
    public EntityRegistry getEntityRegistry() {
        return entityRegistry;
    }

    public EntityHelper getEntityHelper() {
        return entityHelper;
    }

		
	@Override
	public void onEnable() 
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		
		bWorldGuardEnabled = false;
		
        this.reloadConfig();
        
        // check for worldguard
        if(getConfig().getBoolean("useWorldGuard")){
            if (getServer().getPluginManager().getPlugin("WorldGuard") == null)
            {
                getLogger().info("WorldGuard plugin not found");
                bWorldGuardEnabled =  false;
            }
            worldguard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            if( worldguard != null )
                bWorldGuardEnabled = true;
        }
        
        if (bWorldGuardEnabled)
            logger.info("[" + this.getDescription().getName() +  "] WorldGuard support enabled.");
	
		regionConfigManager = new RegionConfigMgr(this);
		levelConfigManager = new LevelConfigMgr(this);
		editSessionManager = new EditSessionManager(this);
		dungeonConfigManager = new DungeonConfigMgr(this);
		
		entityRegistry = new EntityRegistry();
		
		entityHelper = new EntityHelper(this);
		
		this.logger.info(pdfFile.getName() + " v" + pdfFile.getVersion() + " Has Been Enabled!");
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        
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
            a.invoke(a, mcdEnderman.class, "Enderman", 58);
        }catch (Exception e){
            e.printStackTrace();
            this.setEnabled(false);
            return;
        }
        
        List<World> worlds = this.getServer().getWorlds();
        for( World world : worlds){
            entityHelper.replaceCreatures(world);
        }
        
    }
	
	public DungeonConfigMgr getDungeonConfigManager() {
	    return dungeonConfigManager;
	}
	
	public EditSessionManager getEditSessionManager() {
        return editSessionManager;
    }

    @Override
	public void onDisable(){
	    saveConfig();
	    regionConfigManager.saveConfig();
	    levelConfigManager.saveConfig();
	    dungeonConfigManager.saveConfig();
	       
        getLogger().info("mcDungeons has been disabled.");
	}

    public RegionConfigMgr getRegionConfigManager(){
        return this.regionConfigManager;
    }
    public LevelConfigMgr getLevelConfigManager(){
        return this.levelConfigManager;
    }
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = cmd.getName();
		if (name.equalsIgnoreCase("mcd")){
			// check the first arg and call any appropriate nested parsers
			if(args.length == 0){
			    if(!checkPermission(sender,"mcd.help")){
			        return true;
			    }
			    
				sender.sendMessage("mcDungeons Command Interface");
				sender.sendMessage("Usage: /mcd <region|level|debug> [add|set|list|reset|remove]");
			}
			if (args.length > 0){
				String arg = args[0];

				if(arg.equalsIgnoreCase("level")){
				    handleLevelCommand(sender, Arrays.copyOfRange(args, 1, args.length));
				}
				else if(arg.equalsIgnoreCase("region")){
                    handleRegionCommand(sender, Arrays.copyOfRange(args, 1, args.length));
				}
				else if(arg.equalsIgnoreCase("dungeon")){
				    handleDungeonCommand(sender, Arrays.copyOfRange(args, 1, args.length));
				}
				else if(arg.equalsIgnoreCase("debug")){
				    if(!checkPermission(sender,"mcd.debug")){
				        return true;
				    }
				    
					if(getConfig().getBoolean("debug")){
						getConfig().set("debug", false);
						sender.sendMessage("Toggled DEBUG mode to OFF");
					}
					else{
						this.getConfig().set("debug", true);
						sender.sendMessage("Toggled DEBUG mode to ON");
					}
				}

			}
	
		}
		return true;
	}
	
	public int getLevel(Entity entity){
	    return this.getEntityHelper().getMaxLevel(entity);
	}
	
	
	private boolean handleDungeonCommand(CommandSender sender, String[] args){
	
       if(args.length < 1){
            // display help
            sender.sendMessage("Usage: /mcd dungeon [edit|selectBlock|finish|remove|list]");
            return true;
        }
    
       if(!checkPermission(sender,"mcd.dungeons")){
           return true;
       }
	    
	    String arg = args[0];
        if(arg.equalsIgnoreCase("edit")){
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd dungeon edit <WorldGuard region name> <respawn time>");
                sender.sendMessage("Description: Start editing dungeon.");
                return true;
            }

            String regionName = args[1];
            String strRespawnTime = args[2];
            
            if(!getEditSessionManager().startEditSession((Player) sender, regionName, strRespawnTime)){
                // failed
                sender.sendMessage("Failed to start editing.");
                return true;
            }
            sender.sendMessage("Begin editing " + regionName + " Dungeon");
            return true;
        }
        else if(arg.equalsIgnoreCase("selectBlock")){
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd dungeon selectBlock <creature type>");
                sender.sendMessage("Description: Select a block to represent a creature. Uses currently selected block. Any block of this type placed will spawn the creature as indicated.");
                return true;
            }
            if(!this.getEditSessionManager().isEditing((Player) sender)){
                sender.sendMessage("Error: you aren't in edit mode!");
                return true;
            }
            
            EditSession editSession = this.getEditSessionManager().getSession((Player) sender);
            
            
            EntityType entityType = EntityType.fromName(args[1]);
            
            org.bukkit.Material materialType = ((Player) sender).getItemInHand().getType();
            
            if(!editSession.addMaterial(materialType, entityType)){
                sender.sendMessage("Failed to add material. Try a different material.");
                return true;
            }
            sender.sendMessage("mcDungeons: " + materialType.toString() + " blocks will spawn " + entityType.toString());
            return true;
        }
        else if(arg.equalsIgnoreCase("finish")){
            if(args.length != 1){
                sender.sendMessage("Usage: /mcd dungeon finish");
                sender.sendMessage("Description: Finish editing dungeon, save it under name provided.");
                return true;
            }    
            if(!getEditSessionManager().endEditSession((Player) sender)){
                sender.sendMessage("Error: cannot end edit session. Are you editing?");
            }
            sender.sendMessage("Finished editing.");
        }
        else if(arg.equalsIgnoreCase("remove")){
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd dungeon remove <dungeon name>");
                sender.sendMessage("Description: Remove dungeon.");
                return true;
            }
            String dungeonName = args[1];
            
            if(!getDungeonConfigManager().removeDungeon(dungeonName)){
                sender.sendMessage("Failed to remove dungeon. Does it exist?");
                return true;
            }
            sender.sendMessage("Removed dungeon: " + dungeonName);
            return true;
        }
        else if(arg.equalsIgnoreCase("list")){
            if(args.length != 1 && args.length != 2){
                sender.sendMessage("Usage: /mcd dungeon list [dungeon name]");
                sender.sendMessage("Description: List dungeon settings.");
                return true;
            }
            
            if(args.length == 1){
                sender.sendMessage(getDungeonConfigManager().getDungeons().keySet().toString());
                return true;
            }
            else{
                String dungeonName = args[1];
                sender.sendMessage(getDungeonConfigManager().getDungeonConfig(dungeonName).toString());
            }
            return true;  
        }
        else{
            return false;
        }
	    return true;
	}
	
    
    private boolean handleRegionCommand(CommandSender sender, String[] args){

                       
        RegionConfigMgr configMgr = getRegionConfigManager();
        ConfigurationSection config = getRegionConfigManager().getConfig();
        
        if(args.length < 1){
            // display help
            sender.sendMessage("Usage: /mcd region [add|set|list|reset|remove]");
            return true;
        }
        
        if(!checkPermission(sender,"mcd.regions")){
            return true;
        }
        
        HashMap<String,Object> defaultRegionOptions = this.getRegionConfigManager().getDefaultRegionOptions();
        
        
        String arg = args[0];
        if (arg.equalsIgnoreCase("add") )
        {
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd region add <regionID>");
                sender.sendMessage("Description: adds region to regions config");
                sender.sendMessage("with default values. Must be same as worldguard id.");
                return true;
            }

            String regionName = args[1];

            // make sure the region exists in worldguard
            WorldGuardHelper wgHelper = new WorldGuardHelper(this);
            if(!wgHelper.isRegionAnyWorld(args[1])){
                sender.sendMessage("Add failed: Add region in WorldGuard first!");
                return true;
            }
            if(!wgHelper.isRegionTypeSupported(regionName)){
                sender.sendMessage("Error: unsupported region type");
            }
             
            // add new region define
            // set up defaults
            if(configMgr.addRegion(regionName)){
                sender.sendMessage("Region "+args[1]+" added");
                return true;
            }
            else{
                sender.sendMessage("Error: region already exists in config.");
                return true;
            }
        }
        else if (arg.equalsIgnoreCase("set")){
            if(args.length != 4){
                sender.sendMessage("Usage: /mcd set <regionID> <property name> <value>");
                sender.sendMessage("Description: Sets region property values");
                return true;
            }
            String regionName = args[1];
            String sectionName = "regions."+regionName;
            String propName = args[2];
            String value = args[3];
            
            // Is the property Name in list of known args?
            if(!defaultRegionOptions.containsKey(propName)){
                sender.sendMessage("Unrecognized property.");
                return true;
            }
            if(!config.isConfigurationSection(sectionName)){
                sender.sendMessage("Error: region does not exist");
//                config.createSection(sectionName, defaultRegionOptions);
                return true;
            }
            

            
            if(defaultRegionOptions.get(propName) instanceof Boolean){
                config.set(sectionName+"."+propName, Boolean.parseBoolean(value));
            }
            else if(defaultRegionOptions.get(propName) instanceof Double){
                config.set(sectionName+"."+propName, Double.parseDouble(value));
            }
            else if(defaultRegionOptions.get(propName) instanceof Integer){
                if(propName.equalsIgnoreCase("level")){
                    if(!this.getLevelConfigManager().isLevel(Integer.parseInt(value))){
                        sender.sendMessage("Error: Level doesn't exist!");
                        return true;
                    }
                }
                config.set(sectionName+"."+propName, Integer.parseInt(value));
            }
            else{
                sender.sendMessage("Failed to set "+regionName+"'s property \""+propName+"\" to "+value);
                return true;
            }
            
            sender.sendMessage("Set "+regionName+"'s property \""+propName+"\" to "+value);
            
            configMgr.saveConfig();
            return true;
        }   
        else if (arg.equalsIgnoreCase("list")){
            if(args.length != 2 && args.length != 1){
                sender.sendMessage("Usage: /mcd region list [regionID]");
                sender.sendMessage("Description: Lists all properties and values for a given region");
                return false;
            }
            if(args.length == 1){
                ConfigurationSection regionsSection = config.getConfigurationSection("regions");
                if(regionsSection == null){
                    sender.sendMessage("Error: Regions section missing from regions.yml");
                    return false;
                }

                Set<String> sections = regionsSection.getKeys(false);
                String msgString = "Configured regions: ";
                msgString += StringUtils.join(sections,", ");
                sender.sendMessage(msgString);
                return true;
            }

            sender.sendMessage("List of properties for region "+args[1]+":");
            String sectionName = "regions."+args[1];
            ConfigurationSection section = config.getConfigurationSection(sectionName);
            
            if( section == null ){
                sender.sendMessage("Error: region not present");
                return true;
            }
            
            Set<String> keys = section.getKeys(false);

            sender.sendMessage("Region "+args[1]+" properties: ");
            for( String key : keys ){
                sender.sendMessage(key+" : "+section.get(key));
            }
            return true;
        }
        else if(arg.equalsIgnoreCase("remove")){
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd region remove <regionID>");
                sender.sendMessage("Description: Removes region from database.");
                return false;
            }
            String sectionName = "regions."+args[1];
            config.set(sectionName, null);
            configMgr.saveConfig();
            configMgr.reloadConfig();
            return true;
        }
        else if(arg.equalsIgnoreCase("reset")){
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd region reset <regionID>");
                sender.sendMessage("Description: Resets region to default values.");
                return false;
            }
            String sectionName = "regions."+args[1];
            config.createSection(sectionName, defaultRegionOptions);
            sender.sendMessage("Region "+args[1]+" configuration reset to default options");
            return true;
        }
        return true;
    }

    private boolean handleLevelCommand(CommandSender sender, String[] args){
        
        HashMap<String,Object> defaultOptions;
        defaultOptions = new HashMap<String,Object>();
        
        defaultOptions.put("damageX", 1.0);
        defaultOptions.put("healthX", 1.0);
        defaultOptions.put("speedX", 0.1);
        defaultOptions.put("activationRange", 20.0);
        
        LevelConfigMgr configMgr = getLevelConfigManager();
        ConfigurationSection config = configMgr.getConfig();
        
        if(args.length < 1){
            // display help
            sender.sendMessage("Usage: /mcd level [add|set|list|reset|remove]");
            return true;
        }
        
        if(!checkPermission(sender,"mcd.levels")){
            return true;
        }
        
        String arg = args[0];
        if (arg.equalsIgnoreCase("add") )
        {
            if(args.length != 1){
                sender.sendMessage("Usage: /mcd level add");
                sender.sendMessage("Description: adds a new level");
                sender.sendMessage("with default configuration values");
                return false;
            }

            // get the highest current level number
            Set<String> levelNumbers = config.getConfigurationSection("levels").getKeys(false);
            String max = Collections.max(levelNumbers);
            int newMax = Integer.parseInt(max)+1;
            String strNewMax = Integer.toString(newMax);
            String sectionName = "levels."+strNewMax;
            
            // set up defaults
            if(!config.isConfigurationSection(sectionName)){
                config.createSection(sectionName, defaultOptions);
                configMgr.saveConfig();
                configMgr.reloadConfig();
                sender.sendMessage("Level "+strNewMax+" added");
            }
            else{
                // shouldn't be here.
                sender.sendMessage("Error: level already exists. ");
                return false;
            }
        }
        else if (arg.equalsIgnoreCase("set")){
            if(args.length != 4){
                sender.sendMessage("Usage: /mcd level set <number> <property> <value> ");
                sender.sendMessage("Description: Sets level property values");
                return false;
            }
            String sectionName = "levels."+args[1];
            String propName = args[2];
            String value = args[3];

            sender.sendMessage("Warning: only double types currently allowed for property values.");

            // Is the property Name in list of known args?
            if(!defaultOptions.containsKey(propName)){
                sender.sendMessage("Unrecognized property.");
                return false;
            }

            if(!config.isConfigurationSection(sectionName)){
                config.createSection(sectionName, defaultOptions);
            }
            config.set(sectionName+"."+propName, Double.parseDouble(value));
            sender.sendMessage("Set "+args[1]+"'s property "+propName+" to "+value);

            configMgr.saveConfig();
            return true;
        }   
        else if (arg.equalsIgnoreCase("list")){
            if(args.length < 1){
                sender.sendMessage("Usage: /mcd levels list [level num]");
                sender.sendMessage("Description: Lists all levels or level properties");
                return false;
            }


            ConfigurationSection levelsSection = config.getConfigurationSection("levels");
            if(levelsSection == null){
                sender.sendMessage("Error: Levels section missing from levels.yml");
                return false;
            }

            if(args.length == 1){
                // list all levels
                Set<String> levelsNumbers = config.getConfigurationSection("levels").getKeys(false);
                sender.sendMessage(Joiner.on(", ").join(levelsNumbers));
            }
            else if(args.length == 2){
                String strLevelNum = args[1];
                sender.sendMessage(String.format("List of properties for level %s:",strLevelNum));
                Map<String,Object> levelSettings = config.getConfigurationSection("levels."+strLevelNum).getValues(false);
                for( String settingName : levelSettings.keySet()){
                    sender.sendMessage(settingName + " : " + levelSettings.get(settingName).toString());
                }
            }
            else{
                // error
                sender.sendMessage("Something has gone very wrong");
                return false;
            }
            return true;
        }
        else if(arg.equalsIgnoreCase("remove")){
            if(args.length != 1){
                sender.sendMessage("Usage: /mcd level remove");
                sender.sendMessage("Description: Removes the highest level from config.");
                return false;
            }
            // get the highest current level number
            ConfigurationSection levelsConfig = config.getConfigurationSection("levels");
            if(levelsConfig != null){
                Set<String> levelNumbers = levelsConfig.getKeys(false);
                String max = Collections.max(levelNumbers);
                
                String sectionName = "levels." + max;
                config.set(sectionName, null);
                configMgr.saveConfig();
                configMgr.reloadConfig();
                sender.sendMessage("Removed level: " + max);
            }
            
            return true;
        }
        else if(arg.equalsIgnoreCase("reset")){
            if(args.length != 2){
                sender.sendMessage("Usage: /mcd reset <level number>");
                sender.sendMessage("Description: Resets level config to default values.");
                return false;
            }
            String sectionName = "levels."+args[1];
            config.createSection(sectionName, defaultOptions);
            sender.sendMessage("Level "+args[1]+" configuration reset to default options");
            return true;
        }

        return true;
    }
	
    private boolean checkPermission(CommandSender sender, String permission){
        if(!sender.hasPermission(permission)){
            sender.sendMessage("Command failed: requires permission " + permission);
            return false;
        }  
        return true;
    }
   
}
