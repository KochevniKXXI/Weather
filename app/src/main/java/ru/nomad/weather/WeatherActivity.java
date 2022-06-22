package ru.nomad.weather;

import static ru.nomad.weather.WeatherFragment.SETTINGS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            Settings settings = (Settings) getIntent().getExtras().getSerializable(SETTINGS);
            WeatherFragment weather = WeatherFragment.newInstance(settings);
            getSupportFragmentManager().beginTransaction().add(R.id.today, weather).commit();
            WeekWeatherFragment weekWeatherFragment = WeekWeatherFragment.newInstance(settings);
            getSupportFragmentManager().beginTransaction().add(R.id.week, weekWeatherFragment).commit();
        }
    }
}