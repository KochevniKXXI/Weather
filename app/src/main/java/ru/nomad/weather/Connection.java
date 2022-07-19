package ru.nomad.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nomad.weather.interfaces.OpenWeather;

public class Connection {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static Connection instance;
    private final OpenWeather openWeather;

    private Connection() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public OpenWeather getOpenWeather() {
        return openWeather;
    }
}
