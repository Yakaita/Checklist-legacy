package com.unsilencedsins.checklist.inventories;

import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ListConfirm extends HartInventory {
    private ItemStack confirm;
    private ItemStack cancel;
    private Checklist list;
    private Player player;
    private GUIType type;
    private boolean left = true;

    public enum GUIType {CREATE, EDIT}

    public ListConfirm(Checklist list, Player player, GUIType type) {
        this.list = list;
        this.player = player;
        this.type = type;
    }

    @Override
    public Inventory getInventory() {
        String title = "";

        if (type == GUIType.CREATE) title = "Confirm List Creation";
        else if (type == GUIType.EDIT) title = "Confirm List Edit";

        Inventory inv = Bukkit.createInventory(this, 27, title);

        //create confirm item
        confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aConfirm list &e" +
                list.getName() + "&a?"));
        confirm.setItemMeta(confirmMeta);

        //create cancel item
        cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Cancel"));
        cancel.setItemMeta(cancelMeta);

        //set the list
        ItemStack item = list.getFace();
        ItemMeta listMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "Click on an item");
        lore.add(ChatColor.GOLD + "in your inventory");
        lore.add(ChatColor.GOLD + "to set that as");
        lore.add(ChatColor.GOLD + "the icon");
        listMeta.setLore(lore);
        item.setItemMeta(listMeta);
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
    public void onClose(InventoryCloseEvent e) {
        if (left) player.sendMessage(ChatColor.RED + "List not created");
    }

    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);

        if (e.getCurrentItem().isSimilar(confirm)) {
            left = false;

            if (type == GUIType.CREATE) {
                final int[] id = {0};

                //get the new id
                if (Checklist.playerHasLists(player)) {
                    Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                            player.getUniqueId().toString() + ".lists").getKeys(false).forEach(p -> id[0]++);
                }

                list.setUniqueId(id[0]);
                list.setPath("players." + player.getUniqueId().toString() + ".lists.list" + id[0]);

                //remove lore
                ItemMeta meta = list.getFace().getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(" ");
                meta.setLore(lore);
                ItemStack item = list.getFace();
                item.setItemMeta(meta);
                list.setFace(item);

                //write the list to file
                Main.getInstance().getListsFile().getConfig().set(list.getPath() + ".listName", list.getName());//set the list Name
                Main.getInstance().getListsFile().getConfig().set(list.getPath() + ".listItem", list.getFace());//set the list Item
                Main.getInstance().getListsFile().getConfig().set(list.getPath() + ".listId", id[0]);//set the list id
                Main.getInstance().getListsFile().saveConfig();

                //done writing
                player.sendMessage(ChatColor.GREEN + "List created successfully");
                player.openInventory(new TasksInventory(list, player).getInventory());
            }
            else if(type == GUIType.EDIT){}
        } else if (e.getCurrentItem().isSimilar(cancel)) {
            left = false;

            player.closeInventory();

            String msg = "";

            if (type == GUIType.CREATE) msg = "List creation cancelled";
            else if (type == GUIType.EDIT) msg = "List edit cancelled";

            player.sendMessage(ChatColor.RED + msg);
        } else {
            left = false;

            ItemStack newFace = new ItemStack(e.getCurrentItem().getType());
            ItemMeta oldMeta = list.getFace().getItemMeta();
            newFace.setItemMeta(oldMeta);
            list.setFace(newFace);

            if (type == GUIType.CREATE) player.openInventory(new ListConfirm(list, player, GUIType.CREATE).getInventory());
            else if (type == GUIType.EDIT) player.openInventory(new ListConfirm(list, player, GUIType.EDIT).getInventory());
        }
    }



}
