package com.coffeejawa.mcDungeons.Entities;


import net.minecraft.server.Navigation;

import org.bukkit.Location;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class SkeletonMoveEvent extends EntityEvent{
    
    private static final HandlerList handlers = new HandlerList();
    private Skeleton skeleton;
    public Skeleton getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
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
    
    public SkeletonMoveEvent(Skeleton skeleton, Location from, Location to, Navigation nav) {
        // TODO Auto-generated constructor stub
        super(skeleton);
        this.skeleton = skeleton;
        this.from = from;
        this.to = to;
        this.nav = nav;
    }

    public boolean isCancelled() {
        // TODO Auto-generated method stub
        if(skeleton.isDead())
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