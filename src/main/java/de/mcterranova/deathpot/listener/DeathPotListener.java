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
import java.util.Arrays;
import java.util.Objects;

public class DeathPotListener implements Listener {

    private final NamespacedKey itemKey;
    private final NamespacedKey userKey;
    private final NamespacedKey timeKey;

    public DeathPotListener(DeathPot plugin) {
        this.itemKey = new NamespacedKey(plugin, "itemKeyDeathPot");
        this.userKey = new NamespacedKey(plugin, "userKeyDeathPot");
        this.timeKey = new NamespacedKey(plugin, "timeKeyDeathPot");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Block block = p.getLocation().add(0,0.5,0).getBlock();
        block.setType(Material.DECORATED_POT);
        DecoratedPot pot = (DecoratedPot) block.getState();
        pot.getPersistentDataContainer().set(itemKey, DataType.ITEM_STACK_ARRAY, p.getInventory().getContents());
        pot.getPersistentDataContainer().set(userKey, violetDataType.UUID, p.getUniqueId());
        pot.getPersistentDataContainer().set(timeKey, violetDataType.Instant, Instant.now());
        pot.update();
        p.getInventory().clear();
        event.getPlayer();
        ItemStack item = new RoseItem.Builder()
                .setCompass(p.getLocation())
                .displayName(Chat.cottonCandy(p.getName()))
                .addLore(prettyLocation(block.getLocation()),
                        "<red>User: <gray>" + p.getName()).build().stack;
        p.getInventory().addItem(item);
    }

    private String prettyLocation(Location loc) {
        return "<red>Koordinaten: <gray>" + (int) loc.x() + ", " + (int) loc.y() + ", " + (int) loc.z();
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!(Objects.requireNonNull(event.getClickedBlock()).getState() instanceof DecoratedPot pot)) return;
        if (!pot.getPersistentDataContainer().has(itemKey)) return;
        ItemStack[] deathDrop = pot.getPersistentDataContainer().get(itemKey, DataType.ITEM_STACK_ARRAY);
        if (deathDrop == null) return;
        Arrays.stream(deathDrop)
                .filter(Objects::nonNull)
                .forEach(itemStack -> p.getWorld().dropItem(p.getLocation(), itemStack));
        pot.getPersistentDataContainer().remove(itemKey);
        pot.update();
        pot.getBlock().setType(Material.AIR);
        p.sendMessage(MiniMessage.miniMessage().stripTags(prettyLocation(event.getClickedBlock().getLocation())));
        chargeStrict(p,"COMPASS",1,event.getClickedBlock().getLocation());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if (!(event.getBlock().getState() instanceof DecoratedPot pot)) return;
        if (pot.getPersistentDataContainer().has(itemKey)) event.setCancelled(true);

    }
    private Integer chargeStrict(Player p, String itemString, int amount, Location loc) {

        ItemStack item = new ItemStack(Material.valueOf(itemString));

        ItemStack[] stacks = p.getInventory().getContents();

        int total = amount;
        for (int i = 0; i < stacks.length; i++) {
            //if (stacks[i] == null || !stacks[i].isSimilar(item)) continue;
            p.sendMessage(MiniMessage.miniMessage().stripTags(MiniMessage.miniMessage().serialize(Objects.requireNonNull(stacks[i].getItemMeta().lore()).getFirst())));
            if (!MiniMessage.miniMessage().stripTags(MiniMessage.miniMessage().serialize(Objects.requireNonNull(stacks[i].getItemMeta().lore()).getFirst())).equals(MiniMessage.miniMessage().stripTags(prettyLocation(loc)))) continue;

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