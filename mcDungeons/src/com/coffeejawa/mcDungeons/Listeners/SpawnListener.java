package com.coffeejawa.mcDungeons.Listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.coffeejawa.mcDungeons.Dungeon.Dungeon;
import com.coffeejawa.mcDungeons.Dungeon.PointSpawner;
import com.coffeejawa.mcDungeons.WorldGuardHelper;
import com.coffeejawa.mcDungeons.mcDungeons;

public class SpawnListener implements Listener {

    private mcDungeons plugin;
    private WorldGuardHelper wgHelper;

    public SpawnListener(mcDungeons plugin) {
        this.plugin = plugin;
        wgHelper = new WorldGuardHelper(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (event.isCancelled()) {
            return;
        }

        Entity newEntity = plugin.getEntityHelper().replaceCreature(
                event.getEntity());
        // if the entity is not replaced, we don't care
        if (entity == newEntity) {
            return;
        }

        Location loc = event.getLocation();
        ArrayList<String> regionNames = wgHelper.locationInRegionsNamed(loc);
        if (!regionNames.isEmpty()) {
            // add to entity registry
            plugin.getEntityRegistry().add(event.getEntity(), regionNames);
            if (plugin.getConfig().getBoolean("debug")) {
                plugin.logger.info(String.format(
                        "Added to entity registry: %s", event.getEntity()
                                .getType().toString()));
            }
        }

        // add the new entity to a PointSpawner if we can show that the
        // locations match
        for (Dungeon dungeon : plugin.getDungeonConfigManager().getDungeons()
                .values()) {
            PointSpawner spawner = dungeon.getPointSpawnerAtLocation(newEntity
                    .getLocation());
            if (spawner != null) {
                plugin.logger.info("entity added to pointspawner for dungeon "
                        + dungeon.getName());
                spawner.setEntity(newEntity);
            }
        }

        // if we replaced the entity, create a new event with the new entity
        event.setCancelled(true);
        CreatureSpawnEvent newEvent = new CreatureSpawnEvent(
                (LivingEntity) newEntity, SpawnReason.CUSTOM);
        newEntity.getServer().getPluginManager().callEvent(newEvent);

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        plugin.getEntityRegistry().remove(event.getEntity());
        if (plugin.getConfig().getBoolean("debug")) {
            plugin.logger.info(String.format(
                    "Removed from entity registry: %s", event.getEntity()
                            .getType().toString()));
        }

        Dungeon dungeon = plugin.getDungeonConfigManager().isEntityAssociated(
                event.getEntity());

        if (dungeon != null) {
            plugin.logger.info("Created spawn timer for dungeon "
                    + dungeon.getName());

            dungeon.createRespawnTimer(event.getEntity());
        }

        // if LootChests is enabled, use it to spawn loot
        if (plugin.bLootChestsEnabled) {
            if (event.getEntity().getKiller() instanceof Player) {
                plugin.lootchests.CreateChest(event.getEntity().getKiller(), event
                    .getEntity().getLocation(), new ArrayList<ItemStack>(), 30);
            }
        }
    }

    // @EventHandler
    // public void onChunkUnload(ChunkUnloadEvent event){
    // Entity[] entities = event.getChunk().getEntities();
    //
    // for( Entity entity : entities ){
    // entityRegistry.remove(entity);
    // if(plugin.getConfig().getBoolean("debug")){
    // plugin.logger.info(String.format("Removed from entity registry: %s",entity.getType().toString()));
    // }
    // }
    // }
    //
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Entity[] entities = event.getChunk().getEntities();

        for (Entity entity : entities) {

            plugin.getEntityHelper().replaceCreature(entity);

            // // add to entity registry
            // entityRegistry.add(entity, regionNames);
            // if(_plugin.getConfig().getBoolean("debug")){
            // _plugin.logger.info(String.format("Added to entity registry: %s",entity.getType().toString()));
            // }
        }
    }
}

// public SuperDuperFastZombie(World world) {
// super(world);
// this.bb = 10.0F;
// this.goalSelector.a(0, new PathfinderGoalFloat(this));
// this.goalSelector.a(1, new PathfinderGoalBreakDoor(this));
// this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class,
// this.bb, false));
// this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this,
// EntityVillager.class, this.bb, true));
// this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this,
// this.bb));
// this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bb,
// false));
// this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bb));
// this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this,
// EntityHuman.class, 8.0F));
// this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
// this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
// this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this,
// EntityHuman.class, 16.0F, 0, true));
// this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this,
// EntityVillager.class, 16.0F, 0, false));
// }
