package ru.nomad.weather;

import java.util.LinkedHashSet;

public class History {
    private LinkedHashSet<String> listCity;
    private static History instance;

    private History() {
        listCity = new LinkedHashSet<>();
    }

    public static History getInstance() {
        if (instance == null) {
            instance = new History();
        }
        return instance;
    }

    public void addCity(String city) {
        listCity.add(city);
    }

    public void removeCity(String city) {
        listCity.remove(city);
    }

    public LinkedHashSet<String> getListCity() {
        return listCity;
    }

    public void setListCity(LinkedHashSet<String> listCity) {
        this.listCity = listCity;
    }
}
