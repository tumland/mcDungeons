package com.coffeejawa.mcDungeons.Dungeon;

public class RespawnTimer {
    
    private PointSpawner spawner;
    private long time;
    
    
    public RespawnTimer(PointSpawner spawner){
        this.spawner = spawner;
        this.time = System.currentTimeMillis() + spawner.getRespawnTime() + 1000 * 60;
    }
    
    public boolean check(){
        if(System.currentTimeMillis() > time)
            return true;
        return false;
    }
    
    public PointSpawner getSpawner(){
        return spawner;
    }
}
