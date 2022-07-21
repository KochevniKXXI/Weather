package ru.nomad.weather;

import android.app.Application;

import androidx.room.Room;

import ru.nomad.weather.database.LocationDao;
import ru.nomad.weather.database.LocationDatabase;

public class App extends Application {
    private static App instance;
    //База данных
    private LocationDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                LocationDatabase.class,
                "request_history")
                .allowMainThreadQueries()   // ТОЛЬКО для примеров и тестирования!
                .build();
    }

    public LocationDao getLocationDao() {
        return db.getLocationDao();
    }
}
