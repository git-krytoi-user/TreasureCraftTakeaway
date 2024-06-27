package dev.r1nex.treasurecrafttakeaway.data;

import java.util.HashMap;
import java.util.List;

public class ConfigData {

    private final HashMap<String, String> stringsHashMap;
    private final HashMap<String, List<String>> listHashMap;
    private final HashMap<String, Double> doubleHashMap;
    private final HashMap<String, Boolean> booleanHashMap;

    public ConfigData(HashMap<String, String> stringsHashMap, HashMap<String, List<String>> listHashMap, HashMap<String, Double> doubleHashMap, HashMap<String, Boolean> booleanHashMap) {
        this.stringsHashMap = stringsHashMap;
        this.listHashMap = listHashMap;
        this.doubleHashMap = doubleHashMap;
        this.booleanHashMap = booleanHashMap;
    }

    public String getStringFromMemory(String path) {
        return stringsHashMap.get(path);
    }

    public Double getADouble(String path) {
        return doubleHashMap.get(path);
    }

    public List<String> getStringListFromMemory(String path) {
        return listHashMap.get(path);
    }

    public boolean getBooleanFromMemory(String path) {
        return booleanHashMap.get(path);
    }
}
