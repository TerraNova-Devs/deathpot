package org.example.testPlugin.testPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandArguments implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player p)) return false;
        String Test2;

        if (args.length < 1) {
            String Test = "Test";
            p.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<red> Bitte nach %s noch was schreiben!",Test)));
            return false;
        }
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("hello")) {
                p.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<%s> Hello!", p.getName())));
            } else if (args[0].equalsIgnoreCase("hi")) {
                p.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<%s><%s> Hi!", p.getName())));
            } else{
                p.sendMessage(MiniMessage.miniMessage().deserialize("<" + p.getName() + "> <black>" + args[0]));
            }
        }



        if (args[0].equalsIgnoreCase("hello")) {
            p.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<%s><%s> Hello!", p.getName(), args[1])));
        } else if (args[0].equalsIgnoreCase("hi")) {
            p.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<%s><%s> Hi!", p.getName(), args[1])));
        } else{
            p.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<%s><%s> " + args[0], p.getName(), args[1])));
        }
        return false;
    }
}
