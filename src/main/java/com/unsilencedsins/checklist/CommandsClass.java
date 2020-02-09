package com.unsilencedsins.checklist;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsClass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("checklists") || command.getName().equalsIgnoreCase("cl") ){
if (sender instanceof Player){

}
        }

        return true;
    }
}
