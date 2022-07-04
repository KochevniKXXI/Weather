package ru.nomad.weather.model;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.nomad.weather.BuildConfig;

public class Connection {
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=%s&lang=ru";
    private final URL uri;
    private final Handler handler = new Handler();
    private HttpsURLConnection urlConnection = null;

    public Connection(String city) throws MalformedURLException {
        this.uri = new URL(String.format(WEATHER_URL, city, BuildConfig.WEATHER_API_KEY));
    }

    public WeatherRequest getWeatherRequest() throws IOException {
        urlConnection = ((HttpsURLConnection) uri.openConnection());
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String result = getLines(in);
        Gson gson = new Gson();
        return gson.fromJson(result, WeatherRequest.class);
    }

    public Handler getHandler() {
        return handler;
    }

    public void closeConnection() {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }
}
