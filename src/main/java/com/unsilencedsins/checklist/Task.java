package com.unsilencedsins.checklist;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Task {
    private ItemStack face;
    private String name;
    private boolean completed;
    private int uniqueId;
    private String path;

    public Task(){
        face = new ItemStack(Material.COBBLESTONE);
        name = "";
        completed = false;
    }

    public Task(ItemStack face, String name, boolean completed) {
        this.face = face;
        this.name = name;
        setCompleted(completed);

        ItemMeta meta = face.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.face.setItemMeta(meta);
    }

    public ItemStack getFace() {return face;}

    public void setFace(ItemStack face) {this.face = face;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public boolean isCompleted() {return completed;}

    public void setCompleted(boolean completed) {
        this.completed = completed;

        //set the lore
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Completed: " + completed);
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lLeft click&r to toggle complete"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lMiddle click&r to edit"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lRight click&r to delete"));
        ItemMeta meta = face.getItemMeta();
        meta.setLore(lore);

        if (completed)
            if (!meta.hasEnchants()) meta.addEnchant(Enchantment.DAMAGE_ALL, 5, false);
            else meta.removeEnchant(Enchantment.DAMAGE_ALL);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        face.setItemMeta(meta);
    }

    public int getUniqueId() {return uniqueId;}

    public void setUniqueId(int uniqueId) {this.uniqueId = uniqueId;}

    public String getPath() {return path;}

    public void setPath(String path) {this.path = path;}

    public static boolean listHasTasks(Player p, Checklist l) {
        ConfigurationSection sec = Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                p.getUniqueId().toString() + ".lists.list" + l.getUniqueId() + ".tasks");

        if (sec == null || sec.getKeys(false).size() == 0) return false;
        return true;
    }
}
