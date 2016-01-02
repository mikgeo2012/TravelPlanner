package planner.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
        stopNameColumn.setCellValueFactory(cellData -> cellData.getValue().cityNameProperty());

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

        // Add observable list data to the table
        stopTable.setItems(mainApp.getStopData());
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
        for (Stop s : mainApp.getStopData()) {
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(s.getStopCoords().makeLatLong())
                    .visible(Boolean.TRUE)
                    .title(s.getCityName());

            Marker marker = new Marker( markerOptions );

            map.addMarker(marker);
            mainApp.getStopMarkers().add(marker);
        }


        // Add travel lines to map
        for (int i = 1; i < mainApp.getStopData().size(); i++) {
            Stop stop1 = mainApp.getStopData().get(i - 1);
            Stop stop2 = mainApp.getStopData().get(i);
            LatLong[] ary = new LatLong[]{stop1.getStopCoords().makeLatLong(), stop2.getStopCoords().makeLatLong()};
            MVCArray mvc = new MVCArray(ary);

            PolylineOptions polyOpts = new PolylineOptions()
                    .path(mvc)
                    .strokeColor("red")
                    .strokeWeight(3);

            Polyline poly = new Polyline(polyOpts);
            map.addMapShape(poly);
            mainApp.getStopPath().add(poly);
        }

    }

    /**
     * Called when the user clicks on the delete button
     */
    @FXML
    private void handleDeleteStop() {
        int selectedIndex = stopTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Remove stop from table
            stopTable.getItems().remove(selectedIndex);

            // Remove stop marker from map
            Marker deletedStopMarker = mainApp.getStopMarkers().remove(selectedIndex);
            map.removeMarker(deletedStopMarker);

            if (!mainApp.getStopPath().isEmpty()) {  // Remove path to deleted stop if path exists
                deletePath(selectedIndex);
            }
        } else {
            // Nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No stop selected");
            alert.setContentText("Please select a stop to delete");

            alert.showAndWait();
        }
    }

    private void deletePath(int index) {
        if (index == mainApp.getStopMarkers().size() || index == 0) {    // Either end stop deleted
            boolean b = (index != 0);
            if (b) {    // Last stop deleted
                Polyline deletedPath = mainApp.getStopPath().remove(index - 1);
                map.removeMapShape(deletedPath);
            } else {    // First stop deleted
                Polyline deletedPath = mainApp.getStopPath().remove(index);
                map.removeMapShape(deletedPath);
            }
        } else {    // Middle stop deleted
            // Delete path lines
            Polyline deletedPath = mainApp.getStopPath().remove(index);
            Polyline deletedPath2 = mainApp.getStopPath().remove(index - 1);
            map.removeMapShape(deletedPath);
            map.removeMapShape(deletedPath2);

            // Draw new path line
            Stop stop1 = mainApp.getStopData().get(index - 1);
            Stop stop2 = mainApp.getStopData().get(index);
            LatLong[] ary = new LatLong[]{stop1.getStopCoords().makeLatLong(), stop2.getStopCoords().makeLatLong()};
            MVCArray mvc = new MVCArray(ary);

            PolylineOptions polyOpts = new PolylineOptions()
                    .path(mvc)
                    .strokeColor("red")
                    .strokeWeight(3);

            Polyline poly = new Polyline(polyOpts);
            map.addMapShape(poly);
            mainApp.getStopPath().add(index - 1, poly);
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewStop() {
        Stop tempStop = new Stop();
        boolean okClicked = mainApp.showStopAddDialog(tempStop);
        if (okClicked) {
            mainApp.getStopData().add(tempStop);
        }
    }


}
