package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.Navigation;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class mcdEntityMoveEvent extends EntityEvent{
    
    private static final HandlerList handlers = new HandlerList();
    private Creeper creeper;
    public Creeper getCreeper() {
        return creeper;
    }

    public void setCreeper(Creeper creeper) {
        this.creeper = creeper;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Navigation getNav() {
        return nav;
    }

    public void setNav(Navigation nav) {
        this.nav = nav;
    }

    private Location from;
    private Location to;
    private Navigation nav;
    
    public mcdEntityMoveEvent(Creeper creeper, Location from, Location to, Navigation nav) {
        // TODO Auto-generated constructor stub
        super(creeper);
        this.creeper = creeper;
        this.from = from;
        this.to = to;
        this.nav = nav;
    }

    public boolean isCancelled() {
        // TODO Auto-generated method stub
        if(creeper.isDead())
            return true;
        return false;
    }

    public HandlerList getHandlers() {
        // TODO Auto-generated method stub
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}