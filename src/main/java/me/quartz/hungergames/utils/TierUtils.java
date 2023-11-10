package me.quartz.hungergames.utils;

import me.quartz.hungergames.Hungergames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class TierUtils {

    public static void fillChests(Block start, int radius){
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block block = start.getRelative(x, y, z);
                        if(block.getType() == Material.CHEST) {
                            Chest chest = (Chest) block.getState();
                            Inventory inv = chest.getBlockInventory();
                            inv.clear();

                            int items = 0;
                            int amount = new Random().nextInt(6-2) + 2;

                            while (items != amount) {
                                List<String> list = Hungergames.getInstance().getFileManager().getTiersFile().getCustomConfig().getStringList("items");
                                String s = list.get(new Random().nextInt(list.size()));
                                Material material = Material.getMaterial(s.split(",")[0]);
                                int rndNumber = (int) (Math.random() * 100 + 1);
                                if(Integer.parseInt(s.split(",")[1]) >= rndNumber) {
                                    int perRandom = 27 / amount;
                                    inv.setItem(new Random().nextInt(((items*perRandom)+perRandom) - (items*perRandom)) + (items*perRandom), new ItemStack(material));
                                    items++;
                                }
                            }
                        }
                    }
                }
            }
    }

}
