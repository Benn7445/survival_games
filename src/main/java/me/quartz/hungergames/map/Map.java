package me.quartz.hungergames.map;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.files.CustomFile;
import me.quartz.hungergames.utils.FileUtils;
import org.bukkit.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Map {

    private final String name;

    private Location center;
    private final List<Location> spawnLocations;

    public Map(String name) {
        this.name = name;
        this.center = null;
        this.spawnLocations = new ArrayList<>();
        Hungergames.getInstance().getMapManager().serialize(this);
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
        Hungergames.getInstance().getMapManager().serialize(this);
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public void addSpawnLocation(Location location) {
        spawnLocations.add(location);
        Hungergames.getInstance().getMapManager().serialize(this);
    }
}
