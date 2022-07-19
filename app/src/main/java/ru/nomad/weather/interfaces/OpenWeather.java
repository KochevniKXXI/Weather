package ru.nomad.weather.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nomad.weather.model.WeatherRequest;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String cityCountry,
                                     @Query("appid") String keyApi,
                                     @Query("units") String units,
                                     @Query("lang") String lang);
}
