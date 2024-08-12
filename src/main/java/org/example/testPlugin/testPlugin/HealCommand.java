package org.example.testPlugin.testPlugin;

import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Object sender;
        if (commandSender instanceof Player){

            Player player = (Player) commandSender;
            player.sendMessage("You´re health got restored!");
            player.setHealth(20);
            player.spawnParticle(Particle.HEART, player.getLocation(), 2);
        }


        return false;
    }
}
