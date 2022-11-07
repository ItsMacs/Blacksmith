package eu.maxpi.fiverr.blacksmith.utils;

import java.util.HashMap;

public class WeaponCompound {

    public HashMap<String, String> stringValues;
    public HashMap<String, Integer> intValues;
    public HashMap<String, Double> doubleValues;

    public WeaponCompound(){
        stringValues = new HashMap<>();
        doubleValues = new HashMap<>();
        intValues = new HashMap<>();
    }

}
