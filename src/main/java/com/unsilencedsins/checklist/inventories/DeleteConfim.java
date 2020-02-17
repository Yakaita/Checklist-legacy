package com.unsilencedsins.checklist.inventories;

import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import com.unsilencedsins.checklist.Task;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DeleteConfim {
    private boolean left = true;

    public DeleteConfim(Task task, Checklist list, Player player) { //deleting a task
        new AnvilGUI.Builder()
                .onComplete((p, text) -> {
                    left = false;

                    if (text.equals(task.getName())) {//entered the correct value
                        //delete the task from the file
                        Main.getInstance().getListsFile().getConfig().set(task.getPath(), null);
                        list.removeTask(task.getUniqueId());

                        int id = 0;
                        //remove the task from the list and rewrite the file
                        for (Task tsk : list.getTasks()) {
                            tsk.setPath(list.getPath() + ".tasks.task" + id);

                            //write the task to file
                            Main.getInstance().getListsFile().getConfig().set(tsk.getPath() + ".taskName", tsk.getName()); //set task name
                            Main.getInstance().getListsFile().getConfig().set(tsk.getPath() + ".taskItem", tsk.getFace()); //set the task face
                            Main.getInstance().getListsFile().getConfig().set(tsk.getPath() + ".completed", tsk.isCompleted()); //set the completed boolean
                            Main.getInstance().getListsFile().getConfig().set(tsk.getPath() + ".taskId", id); //set the task id
                            Main.getInstance().getListsFile().saveConfig();
                            id++;
                        }

                        player.sendMessage(ChatColor.GREEN + "Task successfully deleted");
                        player.openInventory(new TasksInventory(list, player).getInventory());
                    } else player.sendMessage(ChatColor.RED +
                            "The name you entered does not match the task. The task was not deleted.");

                    return AnvilGUI.Response.close();
                })
                .onClose(p -> {
                    if (left) player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "Task not deleted");
                })
                .text("Enter the task name")
                .item(task.getFace())
                .title("Confirm Task Deletion")
                .plugin(Main.getInstance())
                .open(player);
    }

    public DeleteConfim(Checklist list, Player player, ArrayList<Checklist> lists) {//delete checklist
        new AnvilGUI.Builder()
                .onComplete((p, text) -> {
                    left = false;

                    if (text.equals(list.getName())) {//entered the correct value
                        //delete the list from the file
                        Main.getInstance().getListsFile().getConfig().set(list.getPath(), null);
                        Main.getInstance().getListsFile().saveConfig();

                        int listId = 0;
                        //rewrite the file
                        int listToDelete = list.getUniqueId();
                        for (Checklist lst : lists) {

                            if (lst.getUniqueId() == list.getUniqueId()) {
                                continue;
                            }

                            int taskId = 0;
                            lst.setPath("players." + player.getUniqueId().toString() + ".lists.list" + listId);

                            //write the list to file
                            Main.getInstance().getListsFile().getConfig().set(lst.getPath() + ".listName", lst.getName()); //set list name
                            Main.getInstance().getListsFile().getConfig().set(lst.getPath() + ".listItem", lst.getFace()); //set the list face
                            Main.getInstance().getListsFile().getConfig().set(lst.getPath() + ".listId", listId); //set the list id
                            Main.getInstance().getListsFile().saveConfig();

                            //write the tasks
                            for (Task task : lst.getTasks()) {
                                task.setPath(lst.getPath() + ".tasks.task" + taskId);

                                //write the task to file
                                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".taskName", task.getName()); //set task name
                                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".taskItem", task.getFace()); //set the task face
                                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".completed", task.isCompleted()); //set the completed boolean
                                Main.getInstance().getListsFile().getConfig().set(task.getPath() + ".taskId", taskId); //set the task id
                                Main.getInstance().getListsFile().saveConfig();

                                taskId++;
                            }
                            listId++;
                        }

                        lists.remove(listToDelete);

                        player.sendMessage(ChatColor.GREEN + "Checklist successfully deleted");
                        player.openInventory(new ListsInventory(player, lists).getInventory());
                    } else
                        player.sendMessage(ChatColor.RED +
                                "The name you entered does not match the checklist. The checklist was not deleted.");

                        return AnvilGUI.Response.close();
                })
                .onClose(p -> {
                    if (left) player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "Checklist not deleted");
                })
                .text("Enter the checklist name")
                .item(list.getFace())
                .title("Confirm Checklist Deletion")
                .plugin(Main.getInstance())
                .open(player);
    }
}
