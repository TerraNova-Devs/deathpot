package org.example.testPlugin.testPlugin;

import com.jeff_media.morepersistentdatatypes.DataType;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;


public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        commandRegistry();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void commandRegistry() {
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("test").setExecutor(new CommandArguments());
        getCommand("test2").setExecutor(new CC());
    }

    @EventHandler

    public void onPlayerEggThrow(PlayerEggThrowEvent e) {

        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<blue> YEET"));

    }

    @EventHandler

    public void onPlayerDeath(PlayerDeathEvent e) {

        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<yellow> Ha Ha!"));
    }

    public static HashMap<Block, Inventory> deathPot = new HashMap<>();

    @EventHandler

    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        //DataType.ITEM_STACK_ARRAY
        Block blockpot = player.getWorld().getBlockAt(player.getLocation().add(0, 0.5,0));
        blockpot.setType(Material.DECORATED_POT);

        NamespacedKey key = new NamespacedKey(this, "deathPot");

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
    @Override
    public void onDisable() {

    }

}
