package com.unsilencedsins.checklist.inventories;

import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import com.unsilencedsins.checklist.Task;
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

public class TaskConfirm extends HartInventory {
    private ItemStack confirm;
    private ItemStack cancel;
    private Task task;
    private Player player;
    private Checklist list;
    private GUIType type;
    private boolean left = true;

    public enum GUIType {CREATE, EDIT}

    public TaskConfirm(Task task, Player player, GUIType type, Checklist list) {
        this.list = list;
        this.player = player;
        this.task = task;
        this.type = type;
    }

    @Override
    public Inventory getInventory() {
        String title = "";

        if (type == GUIType.CREATE) title = "Confirm Task Creation";
        else if (type == GUIType.EDIT) title = "Confirm Task Edit";

        Inventory inv = Bukkit.createInventory(this, 27, title);

        //create confirm item
        confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aConfirm task &e" +
                task.getName() + "&a?"));
        confirm.setItemMeta(confirmMeta);

        //create cancel item
        cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Cancel"));
        cancel.setItemMeta(cancelMeta);

        //set the item
        ItemStack item = task.getFace();
        ItemMeta taskMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GOLD + "Click on an item");
        lore.add(ChatColor.GOLD + "in your inventory");
        lore.add(ChatColor.GOLD + "to set that as");
        lore.add(ChatColor.GOLD + "the icon");
        taskMeta.setLore(lore);
        item.setItemMeta(taskMeta);
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
        if (left) player.sendMessage(ChatColor.RED + "Task not created");
    }

    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);

        if (e.getCurrentItem().isSimilar(confirm)) {
            left = false;

            if (type == GUIType.CREATE) {
                final int[] id = {0};

                //get the new id
                if (Task.listHasTasks(player, list)) {
                    Main.getInstance().getListsFile().getConfig().getConfigurationSection(list.getPath() + ".tasks").
                            getKeys(false).forEach(p -> id[0]++);
                }

                task.setUniqueId(id[0]);
                task.setPath(list.getPath() + ".tasks.task" + id[0]);

                //set loose params
                ItemStack item = task.getFace();
                task.setFace(item);
                task.setCompleted(false);

                //write the task to file
                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".taskName", task.getName()); //set task name
                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".taskItem", task.getFace()); //set the task face
                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".completed", false); //set the completed boolean
                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".taskId", id[0]); //set the task id
                Main.getInstance().getListsFile().saveConfig();

                //add the task to the list
                list.addTask(task);

                //done writing
                player.sendMessage(ChatColor.GREEN + "Task created successfully");
                player.openInventory(new TasksInventory(list, player).getInventory());
            } else if (type == GUIType.EDIT) {
            }
        } else if (e.getCurrentItem().isSimilar(cancel)) {
            left = false;

            player.closeInventory();

            String msg = "";

            if (type == GUIType.CREATE) msg = "Task creation cancelled";
            else if (type == GUIType.EDIT) msg = "Task edit cancelled";

            player.sendMessage(ChatColor.RED + msg);
        } else {
            left = false;

            ItemStack newFace = new ItemStack(e.getCurrentItem().getType());
            ItemMeta oldMeta = (task.getFace().hasItemMeta()) ? task.getFace().getItemMeta() : newFace.getItemMeta();
            newFace.setItemMeta(oldMeta);
            task.setFace(newFace);

            if (type == GUIType.CREATE)
                player.openInventory(new TaskConfirm(task, player, GUIType.CREATE, list).getInventory());
            else if (type == GUIType.EDIT)
                player.openInventory(new TaskConfirm(task, player, GUIType.EDIT, list).getInventory());
        }
    }


}