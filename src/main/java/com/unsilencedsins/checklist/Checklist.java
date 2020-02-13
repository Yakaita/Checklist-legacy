package com.unsilencedsins.checklist;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Checklist {

    private ArrayList<Task> tasks;
    private ItemStack face;
    private String name;
    private int uniqueId;
    private String path;

    public Checklist() {
        tasks = new ArrayList<Task>();
        face = new ItemStack(Material.COBBLESTONE);
        name = "";
    }

    public Checklist(String name, ItemStack face, ArrayList<Task> tasks) {
        this.name = name;
        this.face = face;
        this.tasks = tasks;

        ItemMeta meta = face.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
        this.face.setItemMeta(meta);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ItemStack getFace() {
        return face;
    }

    public void setFace(ItemStack face) {
        this.face = face;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTask(Task newTask) {

        tasks.add(newTask);
        Main.getInstance().getServer().broadcastMessage("added to list");
    }
    public void removeTask(Task delTask) {tasks.remove(delTask);}

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPath() {return path;}

    public void setPath(String path) {this.path = path;}

    public static boolean playerHasLists(Player p) {
        ConfigurationSection sec = Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                p.getUniqueId().toString() + ".lists");

        if (sec == null || sec.getKeys(false).size() == 0) return false;
        else return true;
    }

    public static ArrayList<Checklist> getChecklists(Player p) {
        ArrayList<Checklist> lists = new ArrayList<Checklist>();

        if (playerHasLists(p)) {
            final int[] listCount = {0};
            final int[] taskCount = {0};

            Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                    p.getUniqueId().toString() + ".lists").getKeys(false).forEach(k -> {
                //for each list
                Checklist list = new Checklist();
                list.setPath("players." + p.getUniqueId().toString() + ".lists.list" + listCount[0]);

                list.setName(Main.getInstance().getListsFile().getConfig().getString(list.getPath() + ".listName")); //get the list name
                list.setFace(Main.getInstance().getListsFile().getConfig().getItemStack(list.getPath() + ".listItem")); //get the list item
                list.setUniqueId(Main.getInstance().getListsFile().getConfig().getInt(list.getPath() + ".listId")); //get the list id


                if (Task.listHasTasks(p, list)) {
                    //get the tasks
                    Main.getInstance().getListsFile().getConfig().getConfigurationSection("players." +
                            p.getUniqueId().toString() + ".lists.list" + listCount[0] + ".tasks").
                            getKeys(false).forEach(t -> {
                        //for each task
                        Task task = new Task();
                        task.setPath(list.getPath() + ".tasks.task" + taskCount[0]);

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


        return lists;
    }
}
