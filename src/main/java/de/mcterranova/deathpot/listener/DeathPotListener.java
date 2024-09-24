package de.mcterranova.deathpot.listener;

import com.jeff_media.morepersistentdatatypes.DataType;
import de.mcterranova.deathpot.DeathPot;
import de.mcterranova.terranovaLib.roseGUI.RoseItem;
import de.mcterranova.terranovaLib.utils.Chat;
import de.mcterranova.terranovaLib.violetPDC.violetDataType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;
import java.util.UUID;

public class DeathPotListener implements Listener {

    private final NamespacedKey itemKey;
    private final NamespacedKey userKey;
    private final NamespacedKey timeKey;
    private final NamespacedKey uuidKey;
    private final NamespacedKey cuuidKey;
    private final DeathPot plugin;

    public DeathPotListener(DeathPot plugin) {
        this.itemKey = new NamespacedKey(plugin, "itemKey");
        this.userKey = new NamespacedKey(plugin, "userKey");
        this.timeKey = new NamespacedKey(plugin, "timeKey");
        this.uuidKey = new NamespacedKey(plugin, "uuidKey");
        this.cuuidKey = new NamespacedKey(plugin, "uuid");
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Block block = p.getLocation().add(0,0.5,0).getBlock();
        block.setType(Material.DECORATED_POT);
        DecoratedPot pot = (DecoratedPot) block.getState();

        Instant time = Instant.now();
        RoseItem item = new RoseItem.Builder()
                .setCompass(p.getLocation())
                .displayName(Chat.cottonCandy(p.getName()))
                .addLore("<red>Koordinaten: <gray>" + Chat.prettyLocation(block.getLocation()),
                        "<red>Todeszeitpunkt: <gray>" + Chat.prettyInstant(time))
                .generateUUID(plugin).build();
        pot.getPersistentDataContainer().set(itemKey, DataType.ITEM_STACK_ARRAY, p.getInventory().getContents());
        pot.getPersistentDataContainer().set(userKey, violetDataType.UUID, p.getUniqueId());
        pot.getPersistentDataContainer().set(timeKey, violetDataType.Instant, time);
        pot.getPersistentDataContainer().set(uuidKey, violetDataType.UUID, item.getUUID());

        pot.update();
        p.getInventory().clear();

        p.getInventory().addItem(item.stack);
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!(Objects.requireNonNull(event.getClickedBlock()).getState() instanceof DecoratedPot pot)) return;
        //if (!pot.getPersistentDataContainer().has(timeKey) == timeKey.equals(0) || !pot.getPersistentDataContainer().has(userKey) == p.getUniqueId())
        if (!pot.getPersistentDataContainer().has(itemKey)) return;
        ItemStack[] deathDrop = pot.getPersistentDataContainer().get(itemKey, DataType.ITEM_STACK_ARRAY);
        if (deathDrop == null) return;
        Arrays.stream(deathDrop)
                .filter(Objects::nonNull)
                .forEach(itemStack -> p.getWorld().dropItem(p.getLocation(), itemStack));
        chargeStrict(p,"COMPASS",1,event.getClickedBlock().getLocation(),pot.getPersistentDataContainer().get(uuidKey, violetDataType.UUID));
        pot.getPersistentDataContainer().remove(itemKey);
        pot.getPersistentDataContainer().remove(uuidKey);
        pot.getPersistentDataContainer().remove(userKey);
        pot.getPersistentDataContainer().remove(timeKey);
        pot.update();
        pot.getBlock().setType(Material.AIR);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event, Player p){
        if (!(event.getBlock().getState() instanceof DecoratedPot pot)) return;
        if (pot.getPersistentDataContainer().has(itemKey)) event.setCancelled(true);

    }
    private Integer chargeStrict(Player p, String itemString, int amount, Location loc, UUID uuid) {

        ItemStack item = new ItemStack(Material.valueOf(itemString));

        ItemStack[] stacks = p.getInventory().getContents();

        int total = amount;
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] == null || !stacks[i].getType().equals(Material.COMPASS)) continue;
            UUID itemUUID = stacks[i].getItemMeta().getPersistentDataContainer().get(cuuidKey, violetDataType.UUID);
            if (itemUUID == null || !itemUUID.equals(uuid)) continue;


            int stackAmount = stacks[i].getAmount();
            if (stackAmount < total) {
                stacks[i] = null;
                total -= stackAmount;
            } else {
                stacks[i].setAmount(stackAmount - total);
                total -= total;
                break;
            }
        }

        p.getInventory().setContents(stacks);
        p.updateInventory();
        return amount - total;
    }

}