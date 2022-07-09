package ru.nomad.weather;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CitySelectionFragment citySelectionFragment;
    private HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initFab();
        if (savedInstanceState == null) {
            citySelectionFragment = CitySelectionFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.city_selection, citySelectionFragment, "CSF").commit();
        } else {
            citySelectionFragment = (CitySelectionFragment) getSupportFragmentManager().findFragmentByTag("CSF");
            historyFragment = (HistoryFragment) getSupportFragmentManager().findFragmentByTag("HF");
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFab() {
        FloatingActionButton inputCity = findViewById(R.id.input_city);
        inputCity.setOnClickListener(v -> {
            InputCityBottomSheetDialogFragment inputCityDialog = InputCityBottomSheetDialogFragment.newInstance();
            inputCityDialog.show(getSupportFragmentManager(), "input_city_dialog");
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_main) {
            if (citySelectionFragment == null) {
                citySelectionFragment = CitySelectionFragment.newInstance();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(historyFragment);
            transaction.add(R.id.city_selection, citySelectionFragment, "CSF");
            transaction.addToBackStack("");
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        } else if (item.getItemId() == R.id.nav_history) {
            if (historyFragment == null) {
                historyFragment = HistoryFragment.newInstance();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(citySelectionFragment);
            transaction.add(R.id.city_selection, historyFragment, "HF");
            transaction.addToBackStack("");
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}