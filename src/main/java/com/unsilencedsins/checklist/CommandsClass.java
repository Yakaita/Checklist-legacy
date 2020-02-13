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
               lists = Checklist.getChecklists(player);
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
