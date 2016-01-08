package planner.model;

import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Stop in the itinerary which builds on the Location base
 * An itinerary is made up of multiple Stops
 *
 * Created by mikhailgeorge on 12/23/15.
 */
public class Stop extends Location {
    private StringProperty countryName;
    private StringProperty stateName;
    private StringProperty cityName;

    // List of destinations at the stop
    private ObservableList<Dest> destData = FXCollections.observableArrayList();


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
        super(stopCoords, description);
        this.countryName = new SimpleStringProperty(countryName);
        this.stateName = new SimpleStringProperty(stateName);
        this.cityName = new SimpleStringProperty(cityName);
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
        super(stopCoords, description);
        this.countryName = new SimpleStringProperty(countryName);
        this.stateName = new SimpleStringProperty("");
        this.cityName = new SimpleStringProperty(cityName);
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

    public ObservableList<Dest> getDestData() {
        return destData;
    }

    public void setDestData(ObservableList<Dest> destData) {
        this.destData = destData;
    }
}
