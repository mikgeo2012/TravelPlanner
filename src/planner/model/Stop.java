package planner.model;

import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Stop
 *
 * Created by mikhailgeorge on 12/23/15.
 */
public class Stop {
    private StringProperty countryName;
    private StringProperty stateName;
    private StringProperty cityName;
    private ObjectProperty<Coords> stopCoords;
    private StringProperty description;

    public Stop() {
        this(null, null, null, null, null);
    }

    /**
     * Constructor for stop located in US
     * @param countryName
     * @param stateName
     * @param cityName
     * @param stopCoords
     * @param description
     */
    public Stop(String countryName, String stateName, String cityName, Coords stopCoords,
                String description) {
        this.countryName = new SimpleStringProperty(countryName);
        this.stateName = new SimpleStringProperty(stateName);
        this.cityName = new SimpleStringProperty(cityName);
        this.stopCoords = new SimpleObjectProperty<Coords>(stopCoords);
        this.description = new SimpleStringProperty(description);
    }

    /**
     * Constructor for stop located outside of US
     * @param countryName
     * @param cityName
     * @param stopCoords
     * @param description
     */
    public Stop(String countryName, String cityName, Coords stopCoords,
                String description) {
        this.countryName = new SimpleStringProperty(countryName);
        this.stateName = new SimpleStringProperty("");
        this.cityName = new SimpleStringProperty(cityName);
        this.stopCoords = new SimpleObjectProperty<Coords>(stopCoords);
        this.description = new SimpleStringProperty(description);
    }

    public String getCountryName() {
        return countryName.get();
    }

    public StringProperty countryNameProperty() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName.set(countryName);
    }

    public String getStateName() {
        return stateName.get();
    }

    public StringProperty stateNameProperty() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName.set(stateName);
    }

    public String getCityName() {
        return cityName.get();
    }

    public StringProperty cityNameProperty() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName.set(cityName);
    }

    public Coords getStopCoords() {
        return stopCoords.get();
    }

    public ObjectProperty<Coords> stopCoordsProperty() {
        return stopCoords;
    }

    public void setStopCoords(Coords stopCoords) {
        this.stopCoords.set(stopCoords);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
