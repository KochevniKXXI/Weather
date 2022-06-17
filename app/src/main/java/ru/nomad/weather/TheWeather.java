package ru.nomad.weather;

import static ru.nomad.weather.TheWeatherFragment.PARCEL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

public class TheWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            Parcel parcel = (Parcel) getIntent().getExtras().getSerializable(PARCEL);
            TheWeatherFragment weather = TheWeatherFragment.newInstance(parcel);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, weather).commit();
        }
    }
}