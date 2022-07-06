package ru.nomad.weather;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.MalformedURLException;

import ru.nomad.weather.model.Connection;
import ru.nomad.weather.model.WeatherRequest;

public class WeatherFragment extends Fragment {

    public static final String SETTINGS = "settings";
    private static final String TAG = "WEATHER";

    private TextView city;
    private TextView temperature;
    private ImageView imageWeather;
    private TextView description;
    private TextView wind;
    private TextView humidity;
    private TextView pressure;
    private TextView water;
    private FrameLayout error;
    private LinearLayout working;

    public static WeatherFragment newInstance(Settings settings) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(SETTINGS, settings);
        fragment.setArguments(args);
        return fragment;
    }

    public Settings getSettings() {
        return (Settings) getArguments().getSerializable(SETTINGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather, container, false);

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

        try {
            Connection connection = new Connection(settings.getCity());
            new Thread(() -> {
                try {
                    final WeatherRequest weatherRequest = connection.getWeatherRequest();
                    connection.getHandler().post(() -> displayWeather(weatherRequest));
                    History.getInstance().addCity(settings.getCity());
                } catch (IOException e) {
                    connection.getHandler().post(() -> {
                        working.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    });
                    Log.e(TAG, "Fail connection!", e);
                    e.printStackTrace();
                } finally {
                    connection.closeConnection();
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
        if (isAdded()) {
            temperature.setText(getResources().getString(R.string.temperature, Math.round(weatherRequest.getMain().getTemp())));
            imageWeather.setImageResource(getResources().getIdentifier(String.format("ic_%s", weatherRequest.getWeather()[0].getIcon()), "drawable", getContext().getPackageName()));
            description.setText(weatherRequest.getWeather()[0].getDescription());
            pressure.setText(getResources().getString(R.string.pressure, Math.round(weatherRequest.getMain().getPressure() * 0.750064f)));
            humidity.setText(getResources().getString(R.string.humidity, weatherRequest.getMain().getHumidity()));
            wind.setText(getResources().getString(R.string.wind, Math.round(weatherRequest.getWind().getSpeed())));
        }
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