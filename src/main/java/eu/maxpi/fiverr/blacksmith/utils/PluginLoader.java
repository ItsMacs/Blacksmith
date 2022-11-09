package eu.maxpi.fiverr.blacksmith.utils;

import eu.maxpi.fiverr.blacksmith.Blacksmith;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PluginLoader {

    public static HashMap<String, String> lang = new HashMap<>();

    public static void load(){
        Blacksmith.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Blacksmith.getInstance().getDataFolder() + "/config.yml"));
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));

        loadDefaults();

        config.getConfigurationSection("variants").getKeys(false).forEach(s -> {
            WeaponAttribute w = new WeaponAttribute(config.getString("variants." + s + ".name"));

            List<String> s2 = config.getStringList("variants." + s + ".lore");

            s2.forEach(s3 -> w.lore.add(ColorTranslator.translate(s3)));
            config.getConfigurationSection("variants." + s + ".compounds").getKeys(false).forEach(c -> {
                WeaponCompound compound = new WeaponCompound();

                config.getStringList("variants." + s + ".compounds." + c + ".string-attributes").forEach(s1 -> {
                    compound.stringValues.put(s1.split(":")[0], s1.split(":")[1]);
                });
                config.getStringList("variants." + s + ".compounds." + c + ".int-attributes").forEach(s1 -> {
                    compound.intValues.put(s1.split(":")[0], Integer.valueOf(s1.split(":")[1]));
                });
                config.getStringList("variants." + s + ".compounds." + c + ".double-attributes").forEach(s1 -> {
                    compound.doubleValues.put(s1.split(":")[0], Double.valueOf(s1.split(":")[1]));
                });

                compound.intValues.put("UUIDLeast", 311019);
                compound.intValues.put("UUIDMost", 504709);

                w.compounds.add(compound);
            });

            Blacksmith.attributes.add(w);
        });
    }

    private static void loadDefaults(){
        WeaponAttribute woodSword = new WeaponAttribute("woodsword");
        WeaponAttribute stoneSword = new WeaponAttribute("woodsword");
        WeaponAttribute ironSword = new WeaponAttribute("woodsword");
        WeaponAttribute diamondSword = new WeaponAttribute("woodsword");

        WeaponCompound speed = new WeaponCompound();
        speed.intValues.put("Amount", 1);
        speed.intValues.put("Operation", 0);
        speed.intValues.put("UUIDLeast", 311019);
        speed.intValues.put("UUIDMost", 504709);
        speed.stringValues.put("AttributeName", "generic.attackSpeed");
        speed.stringValues.put("Name", "generic.attackSpeed");
        speed.stringValues.put("Slot", "mainhand");

        WeaponCompound wood = new WeaponCompound();
        wood.intValues.put("Amount", 4);
        wood.intValues.put("Operation", 0);
        wood.intValues.put("UUIDLeast", 311019);
        wood.intValues.put("UUIDMost", 504709);
        wood.stringValues.put("AttributeName", "generic.attackDamage");
        wood.stringValues.put("Name", "generic.attackDamage");
        wood.stringValues.put("Slot", "mainhand");

        WeaponCompound stone = new WeaponCompound();
        stone.intValues.put("Amount", 5);
        stone.intValues.put("Operation", 0);
        stone.intValues.put("UUIDLeast", 311019);
        stone.intValues.put("UUIDMost", 504709);
        stone.stringValues.put("AttributeName", "generic.attackDamage");
        stone.stringValues.put("Name", "generic.attackDamage");
        stone.stringValues.put("Slot", "mainhand");

        WeaponCompound iron = new WeaponCompound();
        iron.intValues.put("Amount", 6);
        iron.intValues.put("Operation", 0);
        iron.intValues.put("UUIDLeast", 311019);
        iron.intValues.put("UUIDMost", 504709);
        iron.stringValues.put("AttributeName", "generic.attackDamage");
        iron.stringValues.put("Name", "generic.attackDamage");
        iron.stringValues.put("Slot", "mainhand");

        WeaponCompound diamond = new WeaponCompound();
        diamond.intValues.put("Amount", 7);
        diamond.intValues.put("Operation", 0);
        diamond.intValues.put("UUIDLeast", 311019);
        diamond.intValues.put("UUIDMost", 504709);
        diamond.stringValues.put("AttributeName", "generic.attackDamage");
        diamond.stringValues.put("Name", "generic.attackDamage");
        diamond.stringValues.put("Slot", "mainhand");

        woodSword.compounds.add(wood);
        woodSword.compounds.add(speed);

        stoneSword.compounds.add(stone);
        stoneSword.compounds.add(speed);

        ironSword.compounds.add(iron);
        ironSword.compounds.add(speed);

        diamondSword.compounds.add(diamond);
        diamondSword.compounds.add(speed);

        Blacksmith.defaultAttributes.put(Material.WOOD_SWORD, woodSword);
        Blacksmith.defaultAttributes.put(Material.GOLD_SWORD, woodSword);
        Blacksmith.defaultAttributes.put(Material.STONE_SWORD, stoneSword);
        Blacksmith.defaultAttributes.put(Material.IRON_SWORD, ironSword);
        Blacksmith.defaultAttributes.put(Material.DIAMOND_SWORD, diamondSword);
    }

}
