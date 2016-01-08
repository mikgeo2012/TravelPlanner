package planner.model;

import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a destination at a stop which builds off the Location base
 * A stop has one or more destinations
 *
 * Created by mikhailgeorge on 1/8/16.
 */
public class Dest extends Location {
    private StringProperty destName;


    public Dest() { this(null, null, null); }


    /**
     * Constructor for a destination
     * @param destName
     * @param stopCoords
     * @param description
     */
    public Dest(String destName, Coords stopCoords, String description) {
        super(stopCoords, description);
        this.destName = new SimpleStringProperty(destName);
    }


    public String getDestName() {
        return destName.get();
    }

    public StringProperty destNameProperty() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName.set(destName);
    }
}
