package ru.nomad.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.nomad.weather.model.WeatherRequest;

public class WeatherFragment extends Fragment {

    public static final String SETTINGS = "settings";
    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s,RU&appid=%s&lang=ru";

    TextView city;
    TextView temperature;
    ImageView imageWeather;
    TextView description;
    TextView wind;
    TextView humidity;
    TextView pressure;
    TextView water;
    FrameLayout error;
    LinearLayout working;

    public static WeatherFragment newInstance(Settings settings) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(SETTINGS, settings);
        fragment.setArguments(args);
        return fragment;
    }

    public Settings getSettings() {
        Settings settings = (Settings) getArguments().getSerializable(SETTINGS);
        return settings;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_the_weather, container, false);

        city = layout.findViewById(R.id.current_city);
        temperature = layout.findViewById(R.id.temperature);
        imageWeather = layout.findViewById(R.id.image_weather);
        description = layout.findViewById(R.id.description_weather);
        wind = layout.findViewById(R.id.wind);
        humidity = layout.findViewById(R.id.humidity);
        pressure = layout.findViewById(R.id.pressure);
        water = layout.findViewById(R.id.water);
        error = layout.findViewById(R.id.error);
        working = layout.findViewById(R.id.working);

        Settings settings = getSettings();

        city.setText(settings.getCity());
        if (settings.isCheckWind()) {
            wind.setVisibility(View.VISIBLE);
        } else {
            wind.setVisibility(View.GONE);
        }
        if (settings.isCheckHumidity()) {
            humidity.setVisibility(View.VISIBLE);
        } else {
            humidity.setVisibility(View.GONE);
        }
        if (settings.isCheckPressure()) {
            pressure.setVisibility(View.VISIBLE);
        } else {
            pressure.setVisibility(View.GONE);
        }
        if (settings.isCheckWater()) {
            water.setVisibility(View.VISIBLE);
        } else {
            water.setVisibility(View.GONE);
        }
        setHasOptionsMenu(true);

        // Получаем перевод города для запроса
        int index = Arrays.asList(getResources().getStringArray(R.array.cities)).indexOf(settings.getCity());
        String translateCity = "unnamed";
        if (index >= 0) {
            translateCity = getResources().getStringArray(R.array.translateCities)[index];
        }

        try {
            final URL uri = new URL(String.format(WEATHER_URL, translateCity, BuildConfig.WEATHER_API_KEY));
            final Handler handler = new Handler();

            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = ((HttpsURLConnection) uri.openConnection());
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = getLines(in);
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                    handler.post(() -> displayWeather(weatherRequest));
                } catch (IOException e) {
                    handler.post(() -> {
                        working.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    });
                    Log.e(TAG, "Fail connection!", e);
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            working.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
            Log.e(TAG, "Fail URI!", e);
            e.printStackTrace();
        }
        return layout;
    }

    private void displayWeather(WeatherRequest weatherRequest) {
        temperature.setText(getResources().getString(R.string.temperature, Math.round(weatherRequest.getMain().getTemp() - 273.15f)));
        imageWeather.setImageResource(getResources().getIdentifier(String.format("ic_%s", weatherRequest.getWeather()[0].getIcon()), "drawable", getContext().getPackageName()));
        description.setText(weatherRequest.getWeather()[0].getDescription());
        pressure.setText(getResources().getString(R.string.pressure, Math.round(weatherRequest.getMain().getPressure() * 0.750064f)));
        humidity.setText(getResources().getString(R.string.humidity, weatherRequest.getMain().getHumidity()));
        wind.setText(getResources().getString(R.string.wind, Math.round(weatherRequest.getWind().getSpeed())));
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem wind = menu.findItem(R.id.action_wind);
        MenuItem humidity = menu.findItem(R.id.action_humidity);
        MenuItem pressure = menu.findItem(R.id.action_pressure);
        MenuItem water = menu.findItem(R.id.action_water);

        wind.setChecked(getSettings().isCheckWind());
        humidity.setChecked(getSettings().isCheckHumidity());
        pressure.setChecked(getSettings().isCheckPressure());
        water.setChecked(getSettings().isCheckWater());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //FIXME: Не передаются настройки при повороте экрана
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.action_wind) {
            getSettings().setCheckWind(item.isChecked());
            if (getSettings().isCheckWind()) {
                wind.setVisibility(View.VISIBLE);
            } else {
                wind.setVisibility(View.GONE);
            }
            return true;
        } else if (item.getItemId() == R.id.action_humidity) {
            getSettings().setCheckHumidity(item.isChecked());
            if (getSettings().isCheckHumidity()) {
                humidity.setVisibility(View.VISIBLE);
            } else {
                humidity.setVisibility(View.GONE);
            }
            return true;
        } else if (item.getItemId() == R.id.action_pressure) {
            getSettings().setCheckPressure(item.isChecked());
            if (getSettings().isCheckPressure()) {
                pressure.setVisibility(View.VISIBLE);
            } else {
                pressure.setVisibility(View.GONE);
            }
            return true;
        } else if (item.getItemId() == R.id.action_water) {
            getSettings().setCheckWater(item.isChecked());
            if (getSettings().isCheckWater()) {
                water.setVisibility(View.VISIBLE);
            } else {
                water.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}