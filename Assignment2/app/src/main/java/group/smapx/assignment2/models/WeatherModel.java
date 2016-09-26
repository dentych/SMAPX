package group.smapx.assignment2.models;

/**
 * Created by benla on 9/26/2016.
 */


/**
 * Model for weather data.
 * Contains getters and constructor for all parameters
 */
public class WeatherModel {
    public WeatherModel(double temperature, double humidity, double pressure, double temp_min, double temp_max, double windspeed, double winddirection, String clouds) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.windspeed = windspeed;
        this.winddirection = winddirection;
        this.clouds = clouds;
    }

    /* Members */
    double temperature;
    double humidity;
    double pressure;
    double temp_min;
    double temp_max;
    double windspeed;
    double winddirection;
    String clouds;

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public double getWinddirection() {
        return winddirection;
    }

    public String getClouds() {
        return clouds;
    }
}
