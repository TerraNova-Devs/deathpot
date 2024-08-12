package org.example.testPlugin.testPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class DeathPot extends JavaPlugin implements Listener {

    public static HashMap<Block, Inventory> DeathPot = new HashMap<Block, Inventory>();

    @EventHandler

    public void onDeath(PlayerDeathEvent event){
        Player player = (Player) event.getEntity();

        Block blockpot = player.getWorld().getBlockAt(player.getLocation().add(0, 0.5,0));
        blockpot.setType(Material.DECORATED_POT);

        Inventory inv = Bukkit.createInventory(null, 36, "DeathPot");
        inv.clear();
        inv.setContents(player.getInventory().getContents());

        DeathPot.put(blockpot, inv);

        event.getDrops().clear();
        player.sendMessage("worked");
    }


    @EventHandler

    public void onOpen(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (event.getClickedBlock().getType() == Material.DECORATED_POT){
                Block block = event.getClickedBlock();

                for(Block blocks : DeathPot.keySet()){
                    if (blocks.getLocation().equals(block.getLocation())){
                        event.setCancelled(true);
                        event.getPlayer().dropItem(true);
                    }
                }
            }
        }

    }
}
