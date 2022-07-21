package ru.nomad.weather;

import java.util.Iterator;
import java.util.LinkedHashSet;

import ru.nomad.weather.database.LocationDao;
import ru.nomad.weather.database.LocationEntity;

public class History {
    private final LinkedHashSet<String> listCity;
    private static History instance;
    private final LocationDao locationDao;

    private History() {
        listCity = new LinkedHashSet<>();
        locationDao = App.getInstance().getLocationDao();
    }

    public static History getInstance() {
        if (instance == null) {
            instance = new History();
        }
        return instance;
    }

    public void addCity(String city) {
        listCity.add(city);
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.city = city;
        locationDao.insetCity(locationEntity);
    }

    public void removeCity(String city) {
        listCity.remove(city);
        locationDao.deleteCityByName(city);
    }

    public LinkedHashSet<String> getListCity() {
        if (listCity.isEmpty()) {
            Iterator<LocationEntity> iterator = locationDao.getHistory().iterator();
            while (iterator.hasNext()) {
                listCity.add(iterator.next().city);
            }
        }
        return listCity;
    }
}
