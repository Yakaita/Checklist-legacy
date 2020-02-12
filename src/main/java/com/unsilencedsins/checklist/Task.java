package com.unsilencedsins.checklist;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Task {
    ItemStack face;
    String name;
    boolean completed;

    public Task(){
        face = new ItemStack(Material.COBBLESTONE);
        name = "";
        completed = false;
    }

    public Task(ItemStack face, String name, boolean completed) {
        this.face = face;
        this.name = name;
        this.completed = completed;
    }

    public ItemStack getFace() {return face;}

    public void setFace(ItemStack face) {this.face = face;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public boolean isCompleted() {return completed;}

    public void setCompleted(boolean completed) {this.completed = completed;}
}
