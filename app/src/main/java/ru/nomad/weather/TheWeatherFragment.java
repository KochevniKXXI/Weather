package ru.nomad.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TheWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TheWeatherFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PARCEL = "parcel";

    public TheWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parcel Parameter 1.
     * @return A new instance of fragment TheWeatherFragment.
     */
    public static TheWeatherFragment newInstance(Parcel parcel) {
        TheWeatherFragment fragment = new TheWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARCEL, parcel);
        fragment.setArguments(args);
        return fragment;
    }

    public Parcel getParcel() {
        Parcel parcel = (Parcel) getArguments().getSerializable(PARCEL);
        return parcel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_the_weather, container, false);

        TextView textView = layout.findViewById(R.id.current_city);
        TextView wind = layout.findViewById(R.id.wind);
        TextView humidity = layout.findViewById(R.id.humidity);
        TextView pressure = layout.findViewById(R.id.pressure);
        TextView water = layout.findViewById(R.id.water);

        Parcel parcel = getParcel();

        textView.setText(parcel.getCity());
        if (parcel.isCheckWind()) {
            wind.setVisibility(View.VISIBLE);
        } else {
            wind.setVisibility(View.GONE);
        }
        if (parcel.isCheckHumidity()) {
            humidity.setVisibility(View.VISIBLE);
        } else {
            humidity.setVisibility(View.GONE);
        }
        if (parcel.isCheckPressure()) {
            pressure.setVisibility(View.VISIBLE);
        } else {
            pressure.setVisibility(View.GONE);
        }
        if (parcel.isCheckWater()) {
            water.setVisibility(View.VISIBLE);
        } else {
            water.setVisibility(View.GONE);
        }
        return layout;
    }
}