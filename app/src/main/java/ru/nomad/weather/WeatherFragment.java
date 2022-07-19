package ru.nomad.weather;

import static ru.nomad.weather.InputCityBottomSheetDialogFragment.INPUT_CITY;

import android.content.Context;
import android.content.SharedPreferences;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nomad.weather.model.WeatherRequest;

public class WeatherFragment extends Fragment {

    private static final String TAG = "WEATHER";
    public static final String SAVED_WEATHER_OPTIONS_FILE_NAME = "saved_weather_options";

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
    private ThermometerView thermometer;

    private Settings weatherOptions;
    private SharedPreferences savedWeatherOptions;

    public static WeatherFragment newInstance(String enteredCity) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(INPUT_CITY, enteredCity);
        fragment.setArguments(args);
        return fragment;
    }

    public String getCity() {
        return getArguments().getString(INPUT_CITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_weather, container, false);
        initView(layout);

        savedWeatherOptions = getContext().getSharedPreferences(SAVED_WEATHER_OPTIONS_FILE_NAME, Context.MODE_PRIVATE);
        loadWeatherOptions();

        city.setText(weatherOptions.getCity());
        if (weatherOptions.isCheckWind()) {
            wind.setVisibility(View.VISIBLE);
        } else {
            wind.setVisibility(View.GONE);
        }
        if (weatherOptions.isCheckHumidity()) {
            humidity.setVisibility(View.VISIBLE);
        } else {
            humidity.setVisibility(View.GONE);
        }
        if (weatherOptions.isCheckPressure()) {
            pressure.setVisibility(View.VISIBLE);
        } else {
            pressure.setVisibility(View.GONE);
        }
        if (weatherOptions.isCheckWater()) {
            water.setVisibility(View.VISIBLE);
        } else {
            water.setVisibility(View.GONE);
        }
        setHasOptionsMenu(true);

        if (!getCity().equals("")) {
            Connection.getInstance().getOpenWeather().loadWeather(getCity() + ", RU", BuildConfig.WEATHER_API_KEY, "metric", "ru")
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                            if (response.body() != null) {
                                History.getInstance().addCity(getCity());
                                displayWeather(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherRequest> call, Throwable t) {
                            working.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                            ErrorDialogFragment errorDialog = new ErrorDialogFragment("Fail Connection!");
                            errorDialog.show(getParentFragmentManager(), "errorDialog");
                            Log.e(TAG, "Fail connection!", t);
                            t.printStackTrace();
                        }
                    });
        }
        return layout;
    }

    private void loadWeatherOptions() {
        Boolean[] booleans = {
                savedWeatherOptions.getBoolean("visibility_wind", true),
                savedWeatherOptions.getBoolean("visibility_humidity", true),
                savedWeatherOptions.getBoolean("visibility_pressure", true),
                savedWeatherOptions.getBoolean("visibility_water", true)
        };
        weatherOptions = new Settings(getCity(), booleans[0], booleans[1], booleans[2], booleans[3]);
    }

    private void initView(View layout) {
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
        thermometer = layout.findViewById(R.id.thermometer);
    }

    private void displayWeather(WeatherRequest weatherRequest) {
        if (isAdded()) {
            temperature.setText(getResources().getString(R.string.temperature, Math.round(weatherRequest.getMain().getTemp())));
            imageWeather.setImageResource(getResources().getIdentifier(String.format("ic_%s", weatherRequest.getWeather()[0].getIcon()), "drawable", getContext().getPackageName()));
            description.setText(weatherRequest.getWeather()[0].getDescription());
            pressure.setText(getResources().getString(R.string.pressure, Math.round(weatherRequest.getMain().getPressure() * 0.750064f)));
            humidity.setText(getResources().getString(R.string.humidity, weatherRequest.getMain().getHumidity()));
            wind.setText(getResources().getString(R.string.wind, Math.round(weatherRequest.getWind().getSpeed())));
            thermometer.setMercuryLevel((int) weatherRequest.getMain().getTemp());
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

        wind.setChecked(weatherOptions.isCheckWind());
        humidity.setChecked(weatherOptions.isCheckHumidity());
        pressure.setChecked(weatherOptions.isCheckPressure());
        water.setChecked(weatherOptions.isCheckWater());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //FIXME: Не передаются настройки при повороте экрана
        item.setChecked(!item.isChecked());
        if (item.getItemId() == R.id.action_wind) {
            weatherOptions.setCheckWind(item.isChecked());
            if (weatherOptions.isCheckWind()) {
                wind.setVisibility(View.VISIBLE);
            } else {
                wind.setVisibility(View.GONE);
            }
            return true;
        } else if (item.getItemId() == R.id.action_humidity) {
            weatherOptions.setCheckHumidity(item.isChecked());
            if (weatherOptions.isCheckHumidity()) {
                humidity.setVisibility(View.VISIBLE);
            } else {
                humidity.setVisibility(View.GONE);
            }
            return true;
        } else if (item.getItemId() == R.id.action_pressure) {
            weatherOptions.setCheckPressure(item.isChecked());
            if (weatherOptions.isCheckPressure()) {
                pressure.setVisibility(View.VISIBLE);
            } else {
                pressure.setVisibility(View.GONE);
            }
            return true;
        } else if (item.getItemId() == R.id.action_water) {
            weatherOptions.setCheckWater(item.isChecked());
            if (weatherOptions.isCheckWater()) {
                water.setVisibility(View.VISIBLE);
            } else {
                water.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        saveWeatherOptions();
    }

    private void saveWeatherOptions() {
        SharedPreferences.Editor editor = savedWeatherOptions.edit();
        editor.putString("selected_city", weatherOptions.getCity());
        editor.putBoolean("visibility_wind", weatherOptions.isCheckWind());
        editor.putBoolean("visibility_humidity", weatherOptions.isCheckHumidity());
        editor.putBoolean("visibility_pressure", weatherOptions.isCheckPressure());
        editor.putBoolean("visibility_water", weatherOptions.isCheckWater());
        editor.apply();
    }
}