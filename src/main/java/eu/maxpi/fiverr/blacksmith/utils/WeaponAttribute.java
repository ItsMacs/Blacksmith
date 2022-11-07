package eu.maxpi.fiverr.blacksmith.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaponAttribute {

    public String name;
    public List<String> lore;
    public List<WeaponCompound> compounds;

    public WeaponAttribute(String name){
        this.name = ColorTranslator.translate(name);
        lore = new ArrayList<>();
        compounds = new ArrayList<>();
    }

}
