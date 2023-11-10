package me.quartz.hungergames.map;

import me.quartz.hungergames.game.Game;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WorldManager {

    private void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unloadWorld(World world) {
        Bukkit.getServer().unloadWorld(world, true);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public World copyWorld(Game game) {
        World world = Bukkit.getWorld(game.getMap().getName());
        if(world == null) world = new WorldCreator(game.getId().toString()).createWorld();
        copyFileStructure(world.getWorldFolder(), new File(Bukkit.getWorldContainer(), game.getId().toString()));
        return new WorldCreator(game.getId().toString()).createWorld();
    }
}
