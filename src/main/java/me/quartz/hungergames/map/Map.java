package me.quartz.hungergames.map;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private final String name;

    private Location center;
    private final List<Location> spawnLocations;

    public Map(String name) {
        this.name = name;
        this.center = null;
        this.spawnLocations = new ArrayList<>();
    }

    public Map(String name, Location center, List<Location> spawnLocations) {
        this.name = name;
        this.center = center;
        this.spawnLocations = spawnLocations;
    }

    public String getName() {
        return name;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public void addSpawnLocation(Location location) {
        spawnLocations.add(location);
    }
}
