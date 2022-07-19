package ru.nomad.weather;

import static ru.nomad.weather.InputCityBottomSheetDialogFragment.INPUT_CITY;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = initToolbar();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            String enteredCity = getIntent().getExtras().getString(INPUT_CITY);
            WeatherFragment weather = WeatherFragment.newInstance(enteredCity);
            getSupportFragmentManager().beginTransaction().add(R.id.today, weather).commit();
            WeekWeatherFragment weekWeatherFragment = WeekWeatherFragment.newInstance(enteredCity);
            getSupportFragmentManager().beginTransaction().add(R.id.week, weekWeatherFragment).commit();
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }
}