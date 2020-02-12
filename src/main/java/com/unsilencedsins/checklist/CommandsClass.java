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


            } else {
                //they've never done the command before, write the user to the file

                Main.getInstance().getGuisFile().getConfig().set("players", player.getUniqueId().toString());
            }

            player.openInventory(new ListsInventory(player, lists).getInventory());
        }


        return true;
    }
}
