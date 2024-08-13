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
import org.example.testPlugin.testPlugin.listener.DeathPotListener;

import java.util.HashMap;
import java.util.Objects;


public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        commandRegistry();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new DeathPotListener(this), this);
    }

    private void commandRegistry() {
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("test").setExecutor(new CommandArguments());
        getCommand("test2").setExecutor(new CC());
    }

    @EventHandler

    public void onInteract(PlayerInteractEvent event2) {

        ItemStack itemHand = event2.getItem();
        Player player2 = event2.getPlayer();
        Action action = event2.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
            player2.sendMessage("WORKING");
            if (itemHand != null && itemHand.getType() == Material.SNOWBALL){
                player2.sendMessage("working");
            }
    }

    @EventHandler

    public void onPlayerEggThrow(PlayerEggThrowEvent e) {

        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<blue> YEET"));

    }

    @EventHandler

    public void onPlayerDeath(PlayerDeathEvent e) {

        e.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<yellow> Ha Ha!"));
    }




    @Override
    public void onDisable() {

    }

}
