package com.unsilencedsins.checklist.inventories;

import com.unsilencedsins.checklist.Checklist;
import com.unsilencedsins.checklist.Main;
import com.unsilencedsins.checklist.Task;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeleteConfim {
    private boolean left = true;

    public DeleteConfim(Task task, Checklist list, Player player) {
        new AnvilGUI.Builder()
                .onComplete((p, text) -> {
                    left = false;

                    if (text.equals(task.getName())){//entered the correct value
                        //delete the task from the file
                        Main.getInstance().getListsFile().getConfig().set(task.getPath(), null);
                        list.removeTask(task.getUniqueId());

                        int id = 0;
                        //remove the task from the list and rewrite the file
                        for (Task tsk: list.getTasks()) {
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
                    }
                    else player.sendMessage(ChatColor.RED +
                            "The name you entered does not match the list. The task was not deleted.");

                    return AnvilGUI.Response.close();
                })
                .onClose(p -> {
                    if (left) player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "Task not deleted");})
                .text("Enter the task name")
                .item(task.getFace())
                .title("Confirm Task Deletion")
                .plugin(Main.getInstance())
                .open(player);
    }
}
