package me.quartz.hungergames.map;

import me.quartz.hungergames.Hungergames;
import me.quartz.hungergames.files.CustomFile;
import me.quartz.hungergames.utils.FileUtils;
import org.bukkit.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class MapManager {
    private final List<Map> maps;

    public MapManager() {
        this.maps = new ArrayList<>();
        deserialize();
    }

    public boolean isMinimum() {
        return maps.stream().filter(map -> map.getSpawnLocations().size() >= getMinimum() && map.getCenter() != null).toArray().length > 0;
    }

    public Map getMap(String name) {
        Optional<Map> map = maps.stream().filter(map1 -> map1.getName().equalsIgnoreCase(name)).findAny();
        return map.orElse(null);
    }

    public Map getRandomMap() {
        Random random = new Random();
        List<Map> maps1 = maps.stream().filter(map -> map.getSpawnLocations().size() >= getMinimum() && map.getCenter() != null).collect(Collectors.toList());
        if(maps1.size() > 0) return maps1.get(random.nextInt(maps1.size()));
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

    public int getMinimum() {
        return 2;
    }

    public void serialize(Map map) {
        CustomFile file = Hungergames.getInstance().getFileManager().getMapsFile();
        file.getCustomConfig().set(map.getName() + ".locations", map.getSpawnLocations().stream().map(FileUtils::locationToString).collect(Collectors.toList()));
        file.getCustomConfig().set(map.getName() + ".center", FileUtils.locationToString(map.getCenter()));
        try {
            file.getCustomConfig().save(file.getCustomConfigFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserialize() {
        CustomFile file = Hungergames.getInstance().getFileManager().getMapsFile();
        for(String name : file.getCustomConfig().getKeys(false)) {
            Location center = FileUtils.stringToLocation(file.getCustomConfig().getString(name + ".center"));
            List<Location> locations = file.getCustomConfig().getStringList(name + ".locations").stream().map(FileUtils::stringToLocation).collect(Collectors.toList());
            Map map = new Map(name, center, locations);
            maps.add(map);
        }
    }
}
