package eu.maxpi.fiverr.blacksmith.utils;

import org.bukkit.ChatColor;

public class ColorTranslator {

    public static String translate(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
