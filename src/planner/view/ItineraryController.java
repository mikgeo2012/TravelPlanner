package planner.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import planner.MainApp;
import planner.model.Stop;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by mikhailgeorge on 12/23/15.
 */
public class ItineraryController implements Initializable, MapComponentInitializedListener {

    @FXML
    private TableView<Stop> stopTable;
    @FXML
    private TableColumn<Stop, String> stopNameColumn;

    // Creates the map
    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    // Reference to the main application
    private MainApp mainApp;

    private ObservableList<Stop> stopData;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ItineraryController() {}



    /**
     * Initializes the controller class
     * @param url
     * @param rb
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the person table with the column.
        stopNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        mapView.addMapInializedListener(this);
    }



    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        // Set mainApp and get Stops list
        this.mainApp = mainApp;
        stopData = this.mainApp.getStopData();

        // Add observable list data to the table
        stopTable.setItems(stopData);
    }

    /**
     * Initializes the google maps
     */
    @Override
    public void mapInitialized() {
        // Get list of stops
        // ObservableList<Stop> stops = mainApp.getStopData();
        // Get starting stop
        // Stop start = stops.get(0);



        // Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(39.8282, -98.5795))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(4);

        map = mapView.createMap(mapOptions);


        // Add Stop markers to map
        for (Stop s : stopData) {
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position( s.getStopCoords().makeLatLong() )
                    .visible(Boolean.TRUE)
                    .title(s.getName());

            Marker marker = new Marker( markerOptions );

            map.addMarker(marker);
        }


    }


}
