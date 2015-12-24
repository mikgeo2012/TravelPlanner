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
    private StringProperty name;
    private ObjectProperty<LatLong> stopCoords;
    private StringProperty description;

    public Stop() {
        this(null, null, null);
    }

    public Stop(String sName, LatLong stopCoords, String sDescription) {
        this.name = new SimpleStringProperty(sName);
        this.description = new SimpleStringProperty(sDescription);
        this.stopCoords = new SimpleObjectProperty<LatLong>(stopCoords);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LatLong getStopCoords() {
        return stopCoords.get();
    }

    public ObjectProperty<LatLong> stopCoordsProperty() {
        return stopCoords;
    }

    public void setStopCoords(LatLong stopCoords) {
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
