package com.coffeejawa.mcDungeons.Entities;

import net.minecraft.server.Navigation;

import org.bukkit.Location;
import org.bukkit.entity.Spider;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class SpiderMoveEvent extends EntityEvent{
    
    private static final HandlerList handlers = new HandlerList();
    private Spider spider;
    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
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
    
    public SpiderMoveEvent(Spider spider, Location from, Location to, Navigation nav) {
        // TODO Auto-generated constructor stub
        super(spider);
        this.spider = spider;
        this.from = from;
        this.to = to;
        this.nav = nav;
    }

    public boolean isCancelled() {
        // TODO Auto-generated method stub
        if(spider.isDead())
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