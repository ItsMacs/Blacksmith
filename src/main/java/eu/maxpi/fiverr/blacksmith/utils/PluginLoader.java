package eu.maxpi.fiverr.blacksmith.utils;

import eu.maxpi.fiverr.blacksmith.Blacksmith;
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

}
