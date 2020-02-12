package com.unsilencedsins.checklist.inventories;

import com.sun.tools.javac.comp.Check;
import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Set;

public class ListConfirm extends HartInventory {
    ItemStack confirm;
    ItemStack cancel;
    Checklist list;
    Player player;

    public ListConfirm(Checklist list, Player player) {
        this.list = list;
        this.player = player;
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 27, "Confirm List Creation");

        //create confirm item
        confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = confirm.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aConfirm list &e" + list.getName() + "&a?"));
        confirm.setItemMeta(meta);

        //create cancel item
        cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        meta = cancel.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Cancel"));
        cancel.setItemMeta(meta);

        //set the list
        ItemStack item = list.getFace();
        meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "Click on an item");
        lore.add(ChatColor.GOLD + "in your inventory");
        lore.add(ChatColor.GOLD + "to set that as");
        lore.add(ChatColor.GOLD + "the icon");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(13, item);

        //set the confirm items
        for (int i = 0; i <= 20; i += 9) {
            inv.setItem(i, confirm);

            if (i == 18) i = -8;
            else if (i == 19) i = -7;
        }

        //set the cancel items
        for (int i = 6; i <= 26; i += 9) {
            inv.setItem(i, cancel);

            if (i == 24) i = -2;
            else if (i == 25) i = -1;
        }

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);

        if (e.getCurrentItem().isSimilar(confirm)) {
            final int[] id = {1};

            //get the new id
            Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." + player.getUniqueId().toString() + ".lists").getKeys(false).forEach(l -> {
                id[0]++;
            });

            //write the list to file
            Main.getInstance().getListsFile().getConfig().set("players." + player.getUniqueId().toString() + ".lists.list" + id[0] + ".listName", list.getName());
            Main.getInstance().getListsFile().getConfig().set("players." + player.getUniqueId().toString() + ".lists.list" + id[0] + ".listItem", list.getFace());
            Main.getInstance().getListsFile().getConfig().set("players." + player.getUniqueId().toString() + ".lists.list" + id[0] + ".listId", id[0]);
            Main.getInstance().getListsFile().saveConfig();

            //done writing
            player.sendMessage(ChatColor.GREEN + "List created successfully");
            player.closeInventory();
        }
    }
}
