package eu.maxpi.fiverr.blacksmith;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import eu.maxpi.fiverr.blacksmith.commands.BlacksmithCMD;
import eu.maxpi.fiverr.blacksmith.commands.ReloadBSCMD;
import eu.maxpi.fiverr.blacksmith.events.onInvClick;
import eu.maxpi.fiverr.blacksmith.utils.PluginLoader;
import eu.maxpi.fiverr.blacksmith.utils.WeaponAttribute;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Blacksmith extends JavaPlugin {

    private static Blacksmith instance = null;

    public static Blacksmith getInstance() { return Blacksmith.instance; }

    private static void setInstance(Blacksmith in) { Blacksmith.instance = in; }

    public static HashMap<Player, Inventory> activeInvs = new HashMap<>();

    public static List<WeaponAttribute> attributes = new ArrayList<>();

    @Override
    public void onEnable() {
        setInstance(this);

        PluginLoader.load();

        loadCommands();
        loadEvents();
        loadTasks();

        Bukkit.getLogger().info("Blacksmith by fiverr.com/macslolz was enabled successfully");
    }

    private void loadCommands(){
        getCommand("blacksmith").setExecutor(new BlacksmithCMD());
        getCommand("reloadbs").setExecutor(new ReloadBSCMD());
    }

    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new onInvClick(), this);
    }

    private void loadTasks(){
        //Bukkit.getScheduler().runTaskTimer(this, () -> activeInvs.keySet().removeIf(Objects::isNull), 0L, 40L);
    }

    public static ItemStack smith(ItemStack orig){
        orig = orig.clone();

        WeaponAttribute w = attributes.get(new Random().nextInt(attributes.size()));
        if(w == null) return orig;

        ItemMeta meta = orig.getItemMeta();
        meta.setDisplayName(w.name);
        meta.setLore(w.lore);
        orig.setItemMeta(meta);

        NBTItem i = new NBTItem(orig);
        NBTCompoundList attribute = i.getCompoundList("AttributeModifiers");
        w.compounds.forEach(compound -> {
            NBTListCompound c = attribute.addCompound();
            compound.intValues.forEach(c::setInteger);
            compound.stringValues.forEach(c::setString);
            compound.doubleValues.forEach(c::setDouble);
        });

        return i.getItem();
    }

    public static ItemStack getGlass(){
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(PluginLoader.lang.get("glass-name"));
        glass.setItemMeta(glassMeta);
        return glass;
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Blacksmith by fiverr.com/macslolz was disabled successfully");
    }
}
