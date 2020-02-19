package com.unsilencedsins.checklist;

import com.unsilencedsins.checklist.inventories.HartInventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private ConfigWrapper listsFile;

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        listsFile = new ConfigWrapper(this, "", "listsFile.yml");
        listsFile.saveConfig();

        Bukkit.getServer().getPluginManager().registerEvents(new com.unsilencedsins.checklist.inventories.HartInventoryListener(), this);
        this.getCommand("checklists").setExecutor(new CommandsClass());
        this.getCommand("clv").setExecutor(new CommandsClass());
    }

    public static Main getInstance() {return instance; }

    public ConfigWrapper getListsFile(){return listsFile; }

    @Override
    public void onDisable() {}
}
