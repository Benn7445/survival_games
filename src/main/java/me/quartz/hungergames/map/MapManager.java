package me.quartz.hungergames.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MapManager {
    private final List<Map> maps;

    public MapManager() {
        this.maps = new ArrayList<>();
    }

    public boolean isMinimum() {
        return maps.size() > 0;
    }

    public Map getMap(String name) {
        Optional<Map> map = maps.stream().filter(map1 -> map1.getName().equalsIgnoreCase(name)).findAny();
        return map.orElse(null);
    }

    public Map getRandomMap() {
        Random random = new Random();
        if(maps.size() > 0) maps.get(random.nextInt(maps.size()));
        return null;
    }

    public List<Map> getMaps() {
        return maps;
    }

    public boolean createMap(String name) {
        if(getMap(name) == null) {
            maps.add(new Map(name));
            return true;
        }
        return false;
    }

    public boolean removeMap(String name) {
        Optional<Map> map = maps.stream().filter(map1 -> map1.getName().equalsIgnoreCase(name)).findAny();
        if(map.isPresent()) {
            maps.remove(map.get());
            return true;
        }
        return false;
    }
}
