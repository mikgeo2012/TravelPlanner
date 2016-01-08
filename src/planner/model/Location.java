package planner.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Base model Location class which all other places will build off of
 * Contains only a latitude/longtitude location and a description
 *
 * Created by mikhailgeorge on 1/8/16.
 */
public class Location {
    private ObjectProperty<Coords> stopCoords;
    private StringProperty description;


    public Location() { this(null, null); }


    /**
     * Constructor for Location class
     * @param stopCoords
     * @param description
     */
    public Location (Coords stopCoords, String description) {
        this.stopCoords = new SimpleObjectProperty<Coords>(stopCoords);
        this.description = new SimpleStringProperty(description);
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
