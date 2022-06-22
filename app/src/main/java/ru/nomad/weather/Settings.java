package ru.nomad.weather;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Settings implements Serializable {
    private String city;
    private boolean isCheckWind;
    private boolean isCheckHumidity;
    private boolean isCheckPressure;
    private boolean isCheckWater;

    public Settings(String city, boolean isCheckWind, boolean isCheckHumidity, boolean isCheckPressure, boolean isCheckWater) {
        this.city = city;
        this.isCheckWind = isCheckWind;
        this.isCheckHumidity = isCheckHumidity;
        this.isCheckPressure = isCheckPressure;
        this.isCheckWater = isCheckWater;
    }

    public String getCity() {
        return city;
    }

    public boolean isCheckWind() {
        return isCheckWind;
    }

    public boolean isCheckHumidity() {
        return isCheckHumidity;
    }

    public boolean isCheckPressure() {
        return isCheckPressure;
    }

    public boolean isCheckWater() {
        return isCheckWater;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCheckWind(boolean checkWind) {
        isCheckWind = checkWind;
    }

    public void setCheckHumidity(boolean checkHumidity) {
        isCheckHumidity = checkHumidity;
    }

    public void setCheckPressure(boolean checkPressure) {
        isCheckPressure = checkPressure;
    }

    public void setCheckWater(boolean checkWater) {
        isCheckWater = checkWater;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Settings settings = (Settings) obj;
        return settings.getCity().equals(this.city)
                && settings.isCheckWind() == this.isCheckWind
                && settings.isCheckHumidity() == this.isCheckHumidity
                && settings.isCheckPressure() == this.isCheckPressure
                && settings.isCheckWater() == this.isCheckWater;
    }
}
