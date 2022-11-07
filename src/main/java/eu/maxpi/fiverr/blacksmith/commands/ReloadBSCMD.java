package eu.maxpi.fiverr.blacksmith.commands;

import eu.maxpi.fiverr.blacksmith.Blacksmith;
import eu.maxpi.fiverr.blacksmith.utils.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadBSCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("blacksmith.reload")){
            sender.sendMessage(PluginLoader.lang.get("no-permission"));
            return true;
        }

        PluginLoader.load();
        sender.sendMessage(PluginLoader.lang.get("reloaded"));
        return true;
    }
}
