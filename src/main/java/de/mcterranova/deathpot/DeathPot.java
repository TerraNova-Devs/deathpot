package de.mcterranova.deathpot;

import com.jeff_media.customblockdata.CustomBlockData;
import de.mcterranova.deathpot.commands.TestCommand;
import de.mcterranova.deathpot.listener.DeathPotListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class DeathPot extends JavaPlugin {

    @Override
    public void onEnable (){
        CustomBlockData.registerListener(this);
        Objects.requireNonNull(getCommand("test")).setExecutor(new TestCommand());
        Bukkit.getPluginManager().registerEvents(new DeathPotListener(this), this);
    }

}