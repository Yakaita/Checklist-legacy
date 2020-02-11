package com.unsilencedsins.checklist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ListsInventory extends HartInventory {
    Player player;
    ArrayList<Checklist> lists;
    ItemStack createList;
    //to be used later
    //ItemStack lastPage;
    //ItemStack nextPage;


    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);
    }

    public ListsInventory(Player player, ArrayList<Checklist> lists) {
        this.player = player;
        this.lists = lists;

        //create the new list item
        createList = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = createList.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GREEN + "Create new list");
        createList.setItemMeta(meta);

        //create the last page item
        //lastPage = new ItemStack(Material.)
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, this.defaultSize, player.getName() + "'s Lists");

        //set the bottom row of options
        inv.setItem(49, createList);
        //inv,setItem(51, nextPage) //to be used later
        //inv.setItem(47, lastPage) //to be used later

        //set the different lists
        for(Checklist list : lists){
            //limit to 45 lists until I do pages.
            if(list.getUniqueId() < 45) inv.setItem(list.getUniqueId(), list.getFace());
        }

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

    }
}
