package org.example.testPlugin.testPlugin.listener;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.example.testPlugin.testPlugin.TestPlugin;

import java.util.HashMap;
import java.util.Objects;

public class DeathPotListener implements Listener {

    TestPlugin plugin;

    public DeathPotListener(TestPlugin plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Block, Inventory> deathPot = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        //DataType.ITEM_STACK_ARRAY
        Block blockpot = player.getWorld().getBlockAt(player.getLocation().add(0, 0.5,0));
        blockpot.setType(Material.DECORATED_POT);

        NamespacedKey key = new NamespacedKey(plugin, "deathPot");

        if (blockpot instanceof DecoratedPot p){
            p.getPersistentDataContainer().set(key, DataType.ITEM_STACK_ARRAY, player.getInventory().getContents());
            if(!p.getPersistentDataContainer().has(key)) return;
            Inventory inv = Bukkit.createInventory(null, 45);
            inv.setContents(Objects.requireNonNull(p.getPersistentDataContainer().get(key, DataType.ITEM_STACK_ARRAY)));
        }
        for (ItemStack itemStack2 : player.getInventory().getContents()){
            if(itemStack2 == null){
                player.sendMessage("null");
                continue;
            }
            player.sendMessage(itemStack2.displayName());
        }

        Inventory inv = Bukkit.createInventory(null, 45, "DeathPot");
        inv.setContents(player.getInventory().getContents());

        deathPot.put(blockpot, inv);



        event.getDrops().clear();
        player.sendMessage("worked");
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event){
        Player p = event.getPlayer();
        p.sendMessage("test");
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (event.getClickedBlock().getType() == Material.DECORATED_POT){
                Block block = event.getClickedBlock();

                for(Block blocks : deathPot.keySet()){
                    if (blocks.getLocation().equals(block.getLocation())){
                        event.setCancelled(true);
                        event.getPlayer().dropItem(true);
                    }
                }
            }
        }

    }


}
