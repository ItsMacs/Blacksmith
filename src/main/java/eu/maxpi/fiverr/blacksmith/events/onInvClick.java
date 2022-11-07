package eu.maxpi.fiverr.blacksmith.events;

import eu.maxpi.fiverr.blacksmith.Blacksmith;
import eu.maxpi.fiverr.blacksmith.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class onInvClick implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent event){
        if(!event.getView().getTitle().equalsIgnoreCase(PluginLoader.lang.get("gui-title"))) return;

        event.setCancelled(event.getSlot() != 13 || event.getInventory().getItem(22).getType() == Material.SLIME_BLOCK);
        if(event.getCurrentItem() == null) return;

        if(event.getSlot() == 13 && event.getInventory().getItem(22).getType() != Material.SLIME_BLOCK){
            event.getWhoClicked().getInventory().addItem(event.getCurrentItem().clone());
            event.getWhoClicked().closeInventory();
            return;
        }

        if(event.getSlot() == 22 && event.getInventory().getItem(22).getType() == Material.SLIME_BLOCK){
            event.getInventory().setItem(13, Blacksmith.smith(event.getInventory().getItem(13)));
            event.getInventory().setItem(22, Blacksmith.getGlass());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Blacksmith.getInstance(), () -> ((Player)event.getWhoClicked()).updateInventory());
            return;
        }
    }

}
