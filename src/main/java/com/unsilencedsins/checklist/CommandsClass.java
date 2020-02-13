package com.unsilencedsins.checklist;

import com.unsilencedsins.checklist.inventories.ListsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandsClass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("checklists")) {

            ArrayList<Checklist> lists = new ArrayList<Checklist>();

            if (Main.getInstance().getListsFile().getConfig().contains("players." + player.getUniqueId().toString())) {
                //they have done the command before, get their info

                if (Checklist.playerHasLists(player)) {
                    final int[] listCount = {0};
                    final int[] taskCount = {0};

                    Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                            player.getUniqueId().toString() + ".lists").getKeys(false).forEach(p -> {
                        //for each list
                        Checklist list = new Checklist();
                        list.setPath("players." + player.getUniqueId().toString() + ".lists.list" + listCount[0]);//set the path of the list
                        list.setName(Main.getInstance().getListsFile().getConfig().getString(list.getPath() +
                                ".listName")); //get the list name
                        list.setFace(Main.getInstance().getListsFile().getConfig().getItemStack(list.getPath() +
                                ".listItem")); //get the list item
                        list.setUniqueId(Main.getInstance().getListsFile().getConfig().getInt(list.getPath() +
                                ".listId")); //get the list id

                        if (Task.listHasTasks(player, list)) {
                            //get the tasks
                            Main.getInstance().getListsFile().getConfig().getConfigurationSection(list.getPath() +
                                    ".tasks").getKeys(false).forEach(t -> {
                                //for each task
                                Task task = new Task();
                                task.setPath(list.getPath() + ".tasks.task" + taskCount[0]); //set the task path
                                task.setName(Main.getInstance().getListsFile().getConfig().getString(task.getPath() +
                                        ".taskName")); //get the task name
                                task.setFace(Main.getInstance().getListsFile().getConfig().getItemStack(task.getPath() +
                                        ".taskItem")); //get the task Item
                                task.setCompleted(Main.getInstance().getListsFile().getConfig().getBoolean(task.getPath() +
                                        ".completed")); //get the completed boolean
                                task.setUniqueId(Main.getInstance().getListsFile().getConfig().getInt(task.getPath() +
                                        ".taskId")); //get the task id

                                list.addTask(task);
                                taskCount[0]++;
                            });
                            taskCount[0] = 0;
                        }
                        lists.add(list);
                        listCount[0]++;
                    });
                }
            } else {
                //they've never done the command before, write the user to the file
                Main.getInstance().getListsFile().getConfig().set("players", player.getUniqueId().toString());
            }
            //open the inventory
            player.openInventory(new ListsInventory(player, lists).getInventory());
        }
        return true;
    }
}
