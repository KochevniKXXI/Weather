package ru.nomad.weather;

import java.util.HashSet;

public class History {
    private HashSet<String> listCity;
    private static History instance;

    private History() {
        listCity = new HashSet<>();
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

    public HashSet<String> getListCity() {
        return listCity;
    }
}
