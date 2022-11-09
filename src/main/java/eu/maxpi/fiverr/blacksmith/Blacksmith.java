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
import eu.maxpi.fiverr.blacksmith.utils.WeaponCompound;
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

    public static HashMap<Material, WeaponAttribute> defaultAttributes = new HashMap<>();

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

        //WeaponAttribute wOld = attributes.stream().filter(w1 -> w1.name.equals(meta.getDisplayName()) && w1 != w).findAny().orElse(null);
        //if(wOld != null) return orig;

        meta.setDisplayName(w.name);
        meta.setLore(w.lore);
        orig.setItemMeta(meta);

        NBTItem i = new NBTItem(orig);
        NBTCompoundList attribute = i.getCompoundList("AttributeModifiers");
        attribute.clear();

        ItemStack finalOrig = orig;
        w.compounds.forEach(compound -> {
            NBTListCompound c = attribute.addCompound();
            compound.intValues.forEach((k, v) -> {
                //c.setInteger(k, v);
                if(!k.equals("Amount")){
                    c.setInteger(k, v);
                    return;
                }

                NBTListCompound compound1 = i.getCompoundList("StatsOriginal").stream().filter(c1 -> c1.hasKey("Attack")).findAny().orElse(null);

                int opt = 0;
                if(defaultAttributes.containsKey(finalOrig.getType())){
                    WeaponCompound a = defaultAttributes.get(finalOrig.getType()).compounds.stream().filter(c1 -> c1.stringValues.get("Name").equals(compound.stringValues.get("Name"))).findAny().orElse(null);
                    opt = a == null ? 0 : a.intValues.get("Amount");
                }

                if(compound1 == null){
                    c.setInteger(k, v + opt);
                    return;
                }

                c.setInteger(k, v + Math.round(compound1.getFloat("Attack")) + opt);
            });
            compound.stringValues.forEach(c::setString);
            compound.doubleValues.forEach((k, v) -> {
                //c.setDouble(k, v);
                if(!k.equals("Amount")){
                    c.setDouble(k, v);
                    return;
                }

                int opt = 0;
                if(defaultAttributes.containsKey(finalOrig.getType())){
                    WeaponCompound a = defaultAttributes.get(finalOrig.getType()).compounds.stream().filter(c1 -> c1.stringValues.get("Name").equals(compound.stringValues.get("Name"))).findAny().orElse(null);
                    opt = a == null ? 0 : a.intValues.get("Amount");
                }

                NBTListCompound compound1 = i.getCompoundList("StatsOriginal").stream().filter(c1 -> c1.hasKey("MiningSpeed")).findAny().orElse(null);
                if(compound1 == null){
                    c.setDouble(k, v + opt);
                    return;
                }

                c.setDouble(k, v + compound1.getFloat("MiningSpeed") + opt);
            });
        });

        defaultAttributes.get(finalOrig.getType()).compounds.stream().filter(weaponCompound -> attribute.stream().noneMatch(nbtListCompound -> nbtListCompound.getString("Name").equals(weaponCompound.stringValues.get("Name")))).forEach(weaponCompound -> {
            NBTListCompound c = attribute.addCompound();
            weaponCompound.stringValues.forEach(c::setString);
            weaponCompound.doubleValues.forEach(c::setDouble);
            weaponCompound.intValues.forEach(c::setInteger);
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
