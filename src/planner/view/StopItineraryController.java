package planner.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import netscape.javascript.JSObject;
import planner.MainApp;
import planner.model.Dest;
import planner.model.Stop;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Stop itinerary page
 *
 * Created by mikhailgeorge on 1/8/16.
 */
public class StopItineraryController implements Initializable, MapComponentInitializedListener {
    @FXML
    private TableView<Dest> destTable;
    @FXML
    private TableColumn<Dest, String> destNameColumn;

    // Creates the map
    @FXML
    private GoogleMapView stopMapView;

    private GoogleMap stopMap;

    // Reference to the main application
    private MainApp mainApp;

    // Reference to the stop
    private Stop stop;


    /**
     * The constructor is called before the initialize() method.
     */
    public StopItineraryController() {}


    /**
     * Initializes the controller class
     * @param url
     * @param rb
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the person table with the column.
        destNameColumn.setCellValueFactory(cellData -> cellData.getValue().cityNameProperty());

        stopMapView.addMapInializedListener(this);
    }



    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        // Set mainApp and get Stops list
        this.mainApp = mainApp;
    }


    /**
     * Called by the main application to give a reference to the Stop clicked on
     *
     * @param stop
     */
    public void setStop(Stop stop) {
        // Set the Stop and get destination list
        this.stop = stop;

        // Add observable list data to the table
        destTable.setItems(stop.getDestData());
    }


    /**
     * Initializes the google map and add markers and path lines according to destination table
     */
    @Override
    public void mapInitialized() {
        // Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(stop.getStopCoords().makeLatLong())
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(9);

        // Create the map
        stopMap = stopMapView.createMap(mapOptions);


        // Add dest markers to map
        int index = 0;
        for (Stop s : mainApp.getStopData()) {
            if (s.getStopCoords() != null) {
                addMarker(s, index);
                index++;
            }
        }


        // Add travel lines to map
        for (int i = 0; i < mainApp.getStopData().size() - 1; i++) {
            Stop stop1 = mainApp.getStopData().get(i);
            Stop stop2 = mainApp.getStopData().get(i + 1);
            if (stop2.getStopCoords() != null) {
                addPathLine(stop1, stop2, i);
            }
        }

        //Add stop by right clicking on point on map
        stopMap.addUIEventHandler(UIEventType.rightclick, (JSObject obj) -> {
            LatLong coords = new LatLong((JSObject) obj.getMember("latLng"));
            handleNewStop(coords);
        });

    }
}
