package eu.maxpi.fiverr.blacksmith.commands;

import eu.maxpi.fiverr.blacksmith.Blacksmith;
import eu.maxpi.fiverr.blacksmith.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlacksmithCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1 && sender.hasPermission("blacksmith.others")){
            Player p = Bukkit.getPlayer(args[0]);
            if(p == null){
                sender.sendMessage(PluginLoader.lang.get("no-player"));
                return true;
            }

            openMenu(p);
            return true;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage(PluginLoader.lang.get("no-player"));
            return true;
        }

        if(!sender.hasPermission("blacksmith.self")) return true;
        openMenu((Player)sender);
        return true;
    }

    public static void openMenu(Player p){
        ItemStack item = p.getInventory().getItemInMainHand();
        if(item == null){
            p.sendMessage(PluginLoader.lang.get("no-weapon"));
            return;
        }

        /*if(!item.getType().name().contains("SWORD") && !item.getType().name().contains("AXE")){
            p.sendMessage(PluginLoader.lang.get("no-weapon"));
            return;
        }*/

        Inventory inv = Bukkit.createInventory(null, 27, PluginLoader.lang.get("gui-title"));

        //Set the glass in all the slots, the needed ones will get overwritten
        ItemStack glass = Blacksmith.getGlass();
        for(int i = 0; i < 27; i++){
            inv.setItem(i, glass.clone());
        }

        ItemStack confirm = new ItemStack(Material.SLIME_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(PluginLoader.lang.get("confirm-name"));
        confirm.setItemMeta(confirmMeta);
        inv.setItem(22, confirm);

        inv.setItem(13, item.clone());

        //p.getInventory().setItemInMainHand(null);
        p.openInventory(inv);
    }
}
