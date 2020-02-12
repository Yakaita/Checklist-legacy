package com.unsilencedsins.checklist;

import com.unsilencedsins.checklist.inventories.ListsInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CommandsClass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("checklists")) {

            ArrayList<Checklist> lists = new ArrayList<Checklist>();

            if (Main.getInstance().getGuisFile().getConfig().contains("players." + player.getUniqueId().toString())) {
                //they have done the command before, get their info


                final int[] listIndex = {1};

                Main.getInstance().getGuisFile().getConfig().getConfigurationSection("players." + player.getUniqueId().toString()).getKeys(false).forEach(p -> {
                    //for each list for the user
                    Checklist list = new Checklist();
                    int taskIndex = 1;

                    //set the list name
                    list.setName(Main.getInstance().getGuisFile().getConfig().getString("players." + player.getUniqueId().toString() + ".list" + listIndex[0] + ".listName"));

                    //set the list face
                    ItemStack face = Main.getInstance().getGuisFile().getConfig().getItemStack("players." + player.getUniqueId().toString() + ".list" + listIndex[0] + ".listItem", new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
                    ItemMeta meta = face.getItemMeta();
                    meta.setDisplayName(ChatColor.DARK_AQUA + list.getName());
                    face.setItemMeta(meta);
                    list.setFace(face);

                    //set the list uniqueId
                    list.setUniqueId(Main.getInstance().getGuisFile().getConfig().getInt("players." + player.getUniqueId().toString() + ".list" + listIndex[0] + ".uniqueId"));

                    //get the tasks
                    for (int item = 3; item < Main.getInstance().getGuisFile().getConfig().getConfigurationSection("players." + player.getUniqueId().toString() + ".list" + listIndex[0]).getKeys(false).size(); item++) {
                        //for each task in current list
                        Task task = new Task();

                        //set the task name
                        task.setName(Main.getInstance().getGuisFile().getConfig().getString("players." + player.getUniqueId().toString() + ".list" + listIndex[0] + ".task" + taskIndex + ".name"));

                        //set the completed boolean
                        if(Main.getInstance().getGuisFile().getConfig().getBoolean("players." + player.getUniqueId().toString() + ".list" + listIndex[0] + ".task" + taskIndex + ".completed"))
                            task.setCompleted(true);
                        else task.setCompleted(false);

                        //set the task face
                        face = Main.getInstance().getGuisFile().getConfig().getItemStack("players." + player.getUniqueId().toString() + ".list" + listIndex[0] + ".task" + taskIndex + ".face",new ItemStack( Material.COBBLESTONE));
                        meta = face.getItemMeta();

                        if(!task.isCompleted()) {
                            meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GREEN + task.getName());

                            ArrayList<String> lore = new ArrayList<String>();
                            lore.add(ChatColor.GOLD + "Not completed");
                            meta.setLore(lore);
                        }
                        else {
                            meta.setDisplayName(ChatColor.STRIKETHROUGH + "" + ChatColor.GREEN + task.getName());

                            ArrayList<String> lore = new ArrayList<String>();
                            lore.add(ChatColor.GOLD + "Completed");
                            meta.setLore(lore);
                        }

                        face.setItemMeta(meta);
                        task.setFace(face);

                        list.addTask(task);

                        taskIndex++;
                    }

                    taskIndex = 0;
                    lists.add(list);
                    listIndex[0]++;
                });//end for each list

            } else {
                //they've never done the command before, write the user to the file

                Main.getInstance().getGuisFile().getConfig().set("players." + player.getUniqueId().toString(), player.getUniqueId().toString());
            }

            player.openInventory(new ListsInventory(player, lists).getInventory());
        }


        return true;
    }
}
