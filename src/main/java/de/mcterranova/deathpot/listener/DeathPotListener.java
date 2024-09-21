package de.mcterranova.deathpot.listener;

import com.jeff_media.morepersistentdatatypes.DataType;
import de.mcterranova.deathpot.DeathPot;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class DeathPotListener implements Listener {

    DeathPot plugin;

    NamespacedKey key;

    public DeathPotListener(DeathPot plugin) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "deathPot");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Block deathblock = p.getWorld().getBlockAt(p.getLocation().add(0, 0.5, 0));
        deathblock.setType(Material.DECORATED_POT);

        if (deathblock instanceof DecoratedPot pot) {
            pot.getPersistentDataContainer().set(key, DataType.ITEM_STACK_ARRAY, event.getDrops().toArray(new ItemStack[0]));
        }

        event.getDrops().clear();
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!(event.getClickedBlock() instanceof DecoratedPot pot)) return;
        if (!pot.getPersistentDataContainer().has(key)) return;
        ItemStack[] deathDrop = pot.getPersistentDataContainer().get(key, DataType.ITEM_STACK_ARRAY);
        if (deathDrop == null) return;
        Arrays.stream(deathDrop)
                .filter(Objects::nonNull)
                .forEach(itemStack -> p.getWorld().dropItem(p.getLocation(), itemStack));
    }
}