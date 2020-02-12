package com.unsilencedsins.checklist.inventories;

import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.inventories.HartInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TasksInventory extends HartInventory {
    Checklist list;
    Player player;

    public TasksInventory(Checklist list, Player player){

    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, this.defaultSize, list.getName());

        return inv;
    }
}
