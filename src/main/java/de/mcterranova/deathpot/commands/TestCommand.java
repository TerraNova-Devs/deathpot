package de.mcterranova.deathpot.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
import de.mcterranova.deathpot.DeathPot;
import de.mcterranova.terranovaLib.violetPDC.violetDataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class TestCommand implements CommandExecutor {

    private final NamespacedKey itemKey;
    private final NamespacedKey userKey;
    private final NamespacedKey timeKey;
    private final NamespacedKey uuidKey;
    private final NamespacedKey dumm;
    private final DeathPot plugin;

    public TestCommand(DeathPot plugin) {
        this.itemKey = new NamespacedKey(plugin, "itemKey");
        this.userKey = new NamespacedKey(plugin, "userKey");
        this.timeKey = new NamespacedKey(plugin, "timeKey");
        this.uuidKey = new NamespacedKey(plugin, "uuidKey");
        this.dumm = new NamespacedKey(plugin, "uuid");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) return false;
        if (!p.hasPermission("deathpot.admin")) return false;

        if (p.getTargetBlockExact(20) == null || !(p.getTargetBlockExact(20).getState() instanceof DecoratedPot pot))return false;
        p.sendMessage("Item: " + Arrays.toString(pot.getPersistentDataContainer().get(itemKey, DataType.ITEM_STACK_ARRAY)));
        p.sendMessage("User: " +pot.getPersistentDataContainer().get(userKey, violetDataType.UUID));
        p.sendMessage("Time: " +pot.getPersistentDataContainer().get(timeKey, violetDataType.Instant));
        p.sendMessage("UUID: " +pot.getPersistentDataContainer().get(uuidKey, violetDataType.UUID));

        PersistentDataContainer pdc = p.getItemInHand().getItemMeta().getPersistentDataContainer();
        p.sendMessage("PDC: "+ pdc.get(dumm, violetDataType.UUID));
        return false;


    }
}
