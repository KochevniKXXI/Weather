package ru.nomad.weather.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insetCity(LocationEntity city);

    @Delete
    void deleteCity(LocationEntity city);

    @Query("SELECT * FROM cities")
    List<LocationEntity> getHistory();

    @Query("DELETE FROM cities WHERE city = :city")
    void deleteCityByName(String city);
}
