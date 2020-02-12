package com.unsilencedsins.checklist;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Checklist {

    private ArrayList<Task> tasks;
    private ItemStack face;
    private String name;
    private int uniqueId;

    public Checklist(){
        tasks = new ArrayList<Task>();
        face = new ItemStack(Material.AIR);
        name = "";
        uniqueId = -1;
    }

    public Checklist(String name, ItemStack face, ArrayList<Task> tasks){
        this.name = name;
        this.face = face;
        this.tasks = tasks;

        ItemMeta meta = face.getItemMeta();
        meta.setDisplayName(name);
        this.face.setItemMeta(meta);
    }

    public ArrayList<Task> getTasks() {return tasks;}

    public void setTasks(ArrayList<Task> tasks) {this.tasks = tasks;}

    public ItemStack getFace() {return face;}

    public void setFace(ItemStack face) {this.face = face;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public void addTask(Task newTask){
        tasks.add(newTask);
    }

    public int getUniqueId() {return uniqueId;}

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}