package de.mcterranova.deathpot.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
import de.mcterranova.deathpot.DeathPot;
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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class TestCommand implements CommandExecutor {

    NamespacedKey test;

    public TestCommand(DeathPot plugin) {
        test = new NamespacedKey(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("DeathPot")), "test");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) return false;
        Block block = p.getTargetBlockExact(20);
        assert block != null;
        if (!(block.getState() instanceof DecoratedPot pot)) return false;
        p.sendMessage(String.valueOf(pot.getPersistentDataContainer().has(new NamespacedKey(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("DeathPot")), "deathPot"))));
        /*
        if(!p.hasPermission("deathpot.admin")) return false;
        Location ploc = p.getLocation().add(0,0.5,0);
        Block block = ploc.getBlock();
        block.setType(Material.DECORATED_POT);
        if (!(block.getState() instanceof DecoratedPot pot)) return false;
        //DecoratedPot pot = (DecoratedPot) block.getState();
        pot.getPersistentDataContainer().set(test, DataType.ITEM_STACK_ARRAY, p.getInventory().getContents());
        p.sendMessage(String.valueOf(pot.getPersistentDataContainer().has(test)));
        Arrays.stream(pot.getPersistentDataContainer().get(test,DataType.ITEM_STACK_ARRAY)).filter(Objects::nonNull).forEach(itemStack -> p.sendMessage(itemStack.getType().name()));
        ItemStack[] deathDrop = pot.getPersistentDataContainer().get(test, DataType.ITEM_STACK_ARRAY);
        Arrays.stream(deathDrop).filter(Objects::nonNull).forEach(itemStack -> p.getLocation().getWorld().dropItem(ploc, itemStack));
        //pot.getPersistentDataContainer().set(test, DataType.STRING,"test");
        //p.sendMessage(pot.getPersistentDataContainer().get(test,DataType.STRING));
        //assert deathDrop != null;
        //Arrays.stream(deathDrop).filter(Objects::nonNull).forEach(itemStack -> p.sendMessage(itemStack.getType().toString()));
        */

        return false;


    }
}
