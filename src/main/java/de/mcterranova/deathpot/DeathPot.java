package de.mcterranova.deathpot;

import de.mcterranova.deathpot.listener.DeathPotListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathPot extends JavaPlugin {

    @Override
    public void onEnable (){
        Bukkit.getPluginManager().registerEvents(new DeathPotListener(this), this);
    }
}