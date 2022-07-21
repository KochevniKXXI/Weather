package ru.nomad.weather.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = LocationEntity.class, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationDao getLocationDao();
}
