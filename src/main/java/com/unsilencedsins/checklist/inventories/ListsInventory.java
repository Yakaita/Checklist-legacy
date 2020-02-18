package com.unsilencedsins.checklist.inventories;

import com.google.common.collect.Lists;
import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ListsInventory extends HartInventory {
    private Player player;

    private ArrayList<Checklist> lists;
    private List<List<Checklist>> chunks;

    private ItemStack createList;

    private ItemStack lastPage;
    private ItemStack nextPage;

    public ListsInventory(Player player, ArrayList<Checklist> lists) {
        this.player = player;
        this.lists = lists;

        //create the create list item
        createList = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = createList.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GREEN + "Create new list");
        createList.setItemMeta(meta);

        // set default page
        this.page = 1;
        // divide chunks
        this.chunks = Lists.partition(lists, 45);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, this.defaultSize, player.getName() + "'s Lists");

        ItemMeta meta;
        //create the last page item
        lastPage = new ItemStack(Material.ARROW);
        meta = lastPage.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GRAY + "Previous Page");
        ArrayList<String> lore = new ArrayList<>();
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
        inv.setItem(49, createList);
        inv.setItem(51, nextPage);
        inv.setItem(47, lastPage);

        // display list depending on page
        try {chunks.get(this.page - 1).forEach(list -> inv.setItem(inv.firstEmpty(), list.getFace())); }
        catch(Exception e){}

        return inv;
    }

    @Override
    public void onClick(InventoryClickEvent e, int slot) {
        e.setCancelled(true);

        if (slot == 49) { //create list item
            final boolean[] left = {true};

            ItemStack listItem = new ItemStack(Material.BOOK);
            ItemMeta meta = listItem.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "Rename me!");
            meta.setLore(lore);
            listItem.setItemMeta(meta);

            new AnvilGUI.Builder()
                    .onComplete((player, text) -> {
                        left[0] = false;

                        if (text.trim().equals("")) {
                            player.sendMessage(ChatColor.RED + "You can't have an empty name. Try again");
                            left[0] = true;
                            return AnvilGUI.Response.close();
                        }

                        player.openInventory(new ListConfirm(new Checklist(text, new ItemStack(Material.BOOK),
                                new ArrayList<>()), player, ListConfirm.GUIType.CREATE).getInventory());

                        return AnvilGUI.Response.close();
                    })
                    .onClose(player -> {
                        if (left[0]) player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED +
                                "Checklist not created");
                    })
                    .text("New List")
                    .item(listItem)
                    .title("Set the list name")
                    .plugin(Main.getInstance())
                    .open(player);
        } else if (slot < 45) {//clicked on one of their lists.
            int id = slot;
            if (page > 1) id += 45 * (page - 1); //if they are on a different page

            Checklist clickedList = new Checklist();

            for (Checklist list : lists)
                if (list.getUniqueId() == id) {
                    clickedList.setPath(list.getPath());
                    clickedList.setName(list.getName());
                    clickedList.setUniqueId(list.getUniqueId());
                    clickedList.setFace(new ItemStack(list.getFace().getType()));
                    break;
                }

            if (!clickedList.getName().equals("")) {
                if (e.isLeftClick()) {//left clicked. Open the list
                    for (Checklist list : lists)
                        if (list.getUniqueId() == id) {
                            clickedList = list;
                            break;
                        }

                    player.openInventory(new TasksInventory(clickedList, player).getInventory());
                } else if (e.isRightClick()) {//right clicked. delete list
                    new DeleteConfim(clickedList, player, lists);
                }
            }
        }
        else if (slot == 51){ //next page
            if (this.page >= 1) player.openInventory(this.setPage(this.page + 1).getInventory());
        }
        else if (slot == 47){ //back page
            if (this.page > 1) player.openInventory(this.setPage(this.page - 1).getInventory());
        }
    }
}
