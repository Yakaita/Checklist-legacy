package com.unsilencedsins.checklist;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Task {
    private ItemStack face;
    private String name;
    private boolean completed;
    private int uniqueId;

    public Task(){
        face = new ItemStack(Material.COBBLESTONE);
        name = "";
        completed = false;
    }

    public Task(ItemStack face, String name, boolean completed) {
        this.face = face;
        this.name = name;
        this.completed = completed;

        ItemMeta meta = face.getItemMeta();
        meta.setDisplayName(name);
        this.face.setItemMeta(meta);
    }

    public ItemStack getFace() {return face;}

    public void setFace(ItemStack face) {this.face = face;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public boolean isCompleted() {return completed;}

    public void setCompleted(boolean completed) {this.completed = completed;}

    public int getUniqueId() {return uniqueId;}

    public void setUniqueId(int uniqueId) {this.uniqueId = uniqueId;}

    public static boolean listHasTasks(Player p, Checklist l) {
        ConfigurationSection sec = Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                p.getUniqueId().toString() + ".lists.list" + l.getUniqueId() + ".tasks");

        if (sec == null || sec.getKeys(false).size() == 0) return false;
        return true;
    }
}
