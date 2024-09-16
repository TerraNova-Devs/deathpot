package de.mcterranova.deathpot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class  CC implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String Test2 = "Test2";

        if (sender instanceof Player k) return false;
        else {
            System.out.println("Hello, you have messaged the consol!");
        }
        return false;
    }
}