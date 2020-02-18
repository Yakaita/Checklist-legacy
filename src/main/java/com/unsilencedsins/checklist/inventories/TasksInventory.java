package com.unsilencedsins.checklist.inventories;

import com.google.common.collect.Lists;
import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import com.unsilencedsins.checklist.Task;
import com.unsilencedsins.checklist.inventories.HartInventory;
import net.md_5.bungee.chat.SelectorComponentSerializer;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TasksInventory extends HartInventory {
    private Checklist list;
    private Player player;

    private List<List<Task>> chunks;

    private ItemStack createTask;
    private ItemStack back;
    private ItemStack lastPage;
    private ItemStack nextPage;

    public TasksInventory(Checklist list, Player player) {
        this.list = list;
        this.player = player;

        //create the create task item
        createTask = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = createTask.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GREEN + "Create new task");
        createTask.setItemMeta(meta);

        //create the create task item
        back = new ItemStack(Material.BOOK);
        meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GREEN + "Back to lists");
        back.setItemMeta(meta);

        // set default page
        this.page = 1;
        // divide chunks
        this.chunks = Lists.partition(list.getTasks(), 45);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, this.defaultSize, list.getName());

        ItemMeta meta;
        //create the last page item
        lastPage = new ItemStack(Material.ARROW);
        meta = lastPage.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GRAY + "Previous Page");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.LIGHT_PURPLE + "Current Page: " + page);
        meta.setLore(lore);
        lastPage.setItemMeta(meta);

        //create the next page item
        nextPage = new ItemStack(Material.ARROW);
        meta = nextPage.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GRAY + "Next Page");
        meta.setLore(lore);
        nextPage.setItemMeta(meta);
        //set the bottom row of options
        inv.setItem(45, back);
        inv.setItem(47, lastPage);
        inv.setItem(49, createTask);
        inv.setItem(51, nextPage);

        // display tasks depending on page
       try {chunks.get(this.page - 1).forEach(task -> inv.setItem(inv.firstEmpty(), task.getFace())); }
       catch(Exception e){}

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);

        if (slot == 45) {//back button
            e.getWhoClicked().openInventory(new ListsInventory((Player) e.getWhoClicked(),
                    Checklist.getChecklists((Player) e.getWhoClicked())).getInventory());
        } else if (slot == 49) { //create task item
            final boolean[] left = {true};

            ItemStack taskItem = new ItemStack(Material.PAPER);
            ItemMeta meta = taskItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GREEN + "Rename me!");
            meta.setLore(lore);
            taskItem.setItemMeta(meta);

            new AnvilGUI.Builder()
                    .onComplete((player, text) -> {
                        left[0] = false;

                        player.openInventory(new TaskConfirm(new Task(new ItemStack(Material.PAPER), text,
                                false), player, TaskConfirm.GUIType.CREATE, list).getInventory());

                        return AnvilGUI.Response.close();
                    })
                    .onClose(player -> {
                        if (left[0]) player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED +
                                "Task not created");
                    })
                    .text("New Task")
                    .item(taskItem)
                    .title("Set the task name")
                    .plugin(Main.getInstance())
                    .open(player);
        } else if (slot < 45) {//they clicked on on of the tasks
            int id = slot;
            if (page > 1) id += 45 * (page - 1); //if they are on a different page

            Task clickedTask = new Task();

            for (Task task : list.getTasks())
                if (task.getUniqueId() == id) {
                    clickedTask.setPath(task.getPath());
                    clickedTask.setName(task.getName());
                    clickedTask.setUniqueId(task.getUniqueId());
                    clickedTask.setFace(new ItemStack(task.getFace().getType()));
                    break;
                }

            if (!clickedTask.getName().equals("")) {
                if (e.isLeftClick()) {//left clicked

                    for (Task task : list.getTasks())
                        if (task.getUniqueId() == id) {
                            clickedTask = task;
                            break;
                        }

                    if (clickedTask.isCompleted()) clickedTask.setCompleted(false);
                    else clickedTask.setCompleted(true);

                    //write the task to file
                    Main.getInstance().getListsFile().getConfig().set(clickedTask.getPath() + ".taskName", clickedTask.getName()); //set task name
                    Main.getInstance().getListsFile().getConfig().set(clickedTask.getPath() + ".taskItem", clickedTask.getFace()); //set the task face
                    Main.getInstance().getListsFile().getConfig().set(clickedTask.getPath() + ".completed", clickedTask.isCompleted()); //set the completed boolean
                    Main.getInstance().getListsFile().getConfig().set(clickedTask.getPath() + ".taskId", clickedTask.getUniqueId()); //set the task id
                    Main.getInstance().getListsFile().saveConfig();

                    player.openInventory(new TasksInventory(list, player).getInventory());
                } else if (e.getClick().equals(ClickType.MIDDLE)) {//middle clicked
                } else {//right clicked
                    new DeleteConfim(clickedTask, list, player);
                }
            }
        }
        else if(slot == 47){//last page
            if (this.page > 1) player.openInventory(this.setPage(this.page - 1).getInventory());
        }
        else if(slot == 51){//next page
            if (this.page >= 1) player.openInventory(this.setPage(this.page + 1).getInventory());
        }
    }
}
