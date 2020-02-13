package com.unsilencedsins.checklist.inventories;

import com.sun.tools.javac.jvm.Items;
import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import com.unsilencedsins.checklist.Task;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListsInventory extends HartInventory {
    Player player;

    ArrayList<Checklist> lists;

    ItemStack createList;

    //to be used later
    ItemStack lastPage;
    ItemStack nextPage;

    public ListsInventory(Player player, ArrayList<Checklist> lists) {
        this.player = player;
        this.lists = lists;

        //create the create list item
        createList = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = createList.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GREEN + "Create new list");
        createList.setItemMeta(meta);

        //create the last page item //to be used later
        lastPage = new ItemStack(Material.ARROW);
        meta = lastPage.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GRAY + "Previous Page");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.LIGHT_PURPLE + "Current Page: " + page);
        meta.setLore(lore);
        lastPage.setItemMeta(meta);

        //create the next page item //to be used later
        nextPage = new ItemStack(Material.ARROW);
        meta = nextPage.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GRAY + "Next Page");
        meta.setLore(lore);
        nextPage.setItemMeta(meta);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, this.defaultSize, player.getName() + "'s Lists");

        //set the bottom row of options
        inv.setItem(49, createList);
        inv.setItem(51, nextPage); //to be used later
        inv.setItem(47, lastPage); //to be used later

        //set the different lists
        for (Checklist list : lists) {
            //limit to 45 lists until I do pages.
            if (list.getUniqueId() < 45) inv.setItem(list.getUniqueId(), list.getFace());
        }

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);


        if (slot == 49) { //create list item
            final boolean[] left = {true};

            ItemStack listItem = new ItemStack(Material.BOOK);
            ItemMeta meta = listItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GREEN + "Rename me!");
            meta.setLore(lore);
            listItem.setItemMeta(meta);

            new AnvilGUI.Builder()
                    .onComplete((player, text) -> {
                        left[0] = false;

                        if (text.trim().equals("")){
                            player.sendMessage(ChatColor.RED + "You can't have an empty name. Try again");
                            left[0] = true;
                            return AnvilGUI.Response.close();
                        }

                        player.openInventory(new ListConfirm(new Checklist(text, new ItemStack(Material.BOOK),
                                new ArrayList<Task>()), player, ListConfirm.GUIType.CREATE).getInventory());

                        return AnvilGUI.Response.close();
                    })
                    .onClose(player -> {if (left[0]) player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED +
                            "Checklist not created");})
                    .text("New List")
                    .item(listItem)
                    .title("Set the list name")
                    .plugin(Main.getInstance())
                    .open(player);
        }
        else if (slot < 45){//clicked on one of their lists.
           //get the id
            int id = slot;
            if (page > 1) id += 45 * (page - 1); //if they are on a different page

            //open that list
            for (Checklist list : lists){
                if(list.getUniqueId() == id) player.openInventory(new TasksInventory(list, player).getInventory());
            }
        }
    }
}
