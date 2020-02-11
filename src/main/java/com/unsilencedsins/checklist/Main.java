package com.unsilencedsins.checklist;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private ConfigWrapper guisFile;

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getServer().getPluginManager().registerEvents(new HartInventoryListener(), this);
        this.getCommand("checklists").setExecutor(new CommandsClass());

        guisFile = new ConfigWrapper(this, "", "Guis.yml");
        guisFile.saveConfig();
    }

    public static Main getInstance() {return instance; }

    public ConfigWrapper getGuisFile(){return guisFile; }

    @Override
    public void onDisable() {}
}
