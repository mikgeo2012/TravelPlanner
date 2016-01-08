package planner.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import netscape.javascript.JSObject;
import planner.MainApp;
import planner.model.Stop;
import planner.util.LongLatService;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for the main Itinerary page
 *
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

        // If Stop in table is double clicked, Stop itinerary opens
        stopTable.setRowFactory(tv -> {
            TableRow<Stop> row = new TableRow<Stop>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Stop stop = row.getItem();
                    mainApp.showStopItinerary(stop);
                }
            });
            return row;
        });

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
     * Initializes the google map and add markers and path lines according to stop table
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

        // Create the map
        map = mapView.createMap(mapOptions);


        // Add Stop markers to map
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
        map.addUIEventHandler(UIEventType.rightclick, (JSObject obj) -> {
            LatLong coords = new LatLong((JSObject) obj.getMember("latLng"));
            handleNewStop(coords);
        });

    }

    /**
     * Called when the user clicks on the delete button. Deletes a stop and respective marker and path lines
     */
    @FXML
    private void handleDeleteStop() {
        int selectedIndex = stopTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0 && mainApp.getStopData().get(selectedIndex).getStopCoords() != null) {
            // Remove stop from table
            stopTable.getItems().remove(selectedIndex);

            // Remove stop marker from map and marker list
            Marker deletedStopMarker = mainApp.getStopMarkers().remove(selectedIndex);
            map.removeMarker(deletedStopMarker);

            // Remove path to deleted stop from map and path list if path exists
            if (!mainApp.getStopPath().isEmpty()) {
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


    /**
     * Delete a path line at a given index when a marker is deleted and create a new path line to the remaining
     * markers
     *
     * @param index
     */
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
     * Called when the user clicks the new button. Opens a dialog to add
     * details for a new stop.
     */
    @FXML
    private void handleNewStop() {
        // Index where to add new stop
        int index = stopTable.getSelectionModel().getSelectedIndex();

        // Open dialog box and add new stop
        Stop tempStop = new Stop();
        boolean okClicked = mainApp.showStopAddDialog(tempStop);
        if (okClicked) {
            mainApp.getStopData().add(index, tempStop);
            addMarker(tempStop, index);
            addPathLine(tempStop, index);
        }
    }

    /**
     * Called when the user clicks on a location on the map. Opens a dialog to add
     * details for a new stop.
     */
    @FXML
    private void handleNewStop(LatLong coords) {
        // Index where to add new stop
        int index = stopTable.getSelectionModel().getSelectedIndex();

        // Open dialog box and add new stop
        Stop tempStop = new Stop();
        boolean okClicked = mainApp.showStopAddDialog(tempStop);
        if (okClicked) {
            mainApp.getStopData().add(index, tempStop);
            addMarker(tempStop, index, coords);
            addPathLine(tempStop, index);
        }
    }


    /**
     * Adds a marker to the map and to the marker list for a respective stop s at index
     *
     * @param s
     * @param index
     */
    private void addMarker(Stop s, int index) {
        // Specify marker options
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(s.getStopCoords().makeLatLong())
                .visible(Boolean.TRUE)
                .title(s.getCityName());

        // Make marker
        Marker marker = new Marker( markerOptions );

        // Add marker to map and list
        map.addMarker(marker);
        mainApp.getStopMarkers().add(index, marker);

        // Select respective stop when marker is clicked
        map.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
            int markerIndex = mainApp.getStopMarkers().indexOf(marker);
            stopTable.getSelectionModel().select(markerIndex);
            //mainApp.getStopMarkers().get(markerIndex).setAnimation(Animation.BOUNCE);
        });
    }

    /**
     * Adds a marker to the map and to the marker list for a respective stop s at index with given LatLong cordinates
     *
     * @param s
     * @param index
     * @param coords
     */
    private void addMarker(Stop s, int index, LatLong coords) {
        // Set stop coordinates
        LongLatService tempLongLat = new LongLatService();
        s.setStopCoords(tempLongLat.getCoords(coords));

        // Specify marker options
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coords)
                .visible(Boolean.TRUE)
                .title(s.getCityName());

        // Make marker
        Marker marker = new Marker( markerOptions );

        // Add marker to map and list
        map.addMarker(marker);
        mainApp.getStopMarkers().add(index, marker);

        // Select respective stop when marker is clicked
        map.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
            int markerIndex = mainApp.getStopMarkers().indexOf(marker);
            stopTable.getSelectionModel().select(markerIndex);
            //mainApp.getStopMarkers().get(markerIndex).setAnimation(Animation.BOUNCE);
        });
    }

    /**
     * Adds a path line to the map and the path list for stops s1 and s2 at index
     *
     * @param s1
     * @param s2
     * @param index
     */
    private void addPathLine(Stop s1, Stop s2, int index) {
        LatLong[] ary = new LatLong[]{s1.getStopCoords().makeLatLong(), s2.getStopCoords().makeLatLong()};
        MVCArray mvc = new MVCArray(ary);

        PolylineOptions polyOpts = new PolylineOptions()
                .path(mvc)
                .strokeColor("red")
                .strokeWeight(3);

        Polyline poly = new Polyline(polyOpts);
        map.addMapShape(poly);
        mainApp.getStopPath().add(index, poly);
    }


    /**
     * Adds the path lines to the map neccessary to add one stop at a given index
     * Calls addPathLine(s1, s2) to add paths
     *
     * @param s
     * @param index
     */
    private void addPathLine(Stop s, int index) {
        int pathIndex = index - 1;

        // pathIndex > mainApp.getStopPath.size should work but doesn't
        if (index > mainApp.getStopPath().size() || pathIndex < 0) {    // Either end stop added
            boolean b = (index != 0);
            if (b) {    // Last stop added
                addPathLine(mainApp.getStopData().get(index - 1), s, pathIndex);
            } else {    // First stop deleted
                addPathLine(s, mainApp.getStopData().get(1), index);
            }
        } else { // Stop added in the middle
            // Remove current path
            Polyline deletedPath = mainApp.getStopPath().remove(pathIndex);
            map.removeMapShape(deletedPath);

            // Add updated path to new stop
            addPathLine(mainApp.getStopData().get(index - 1), s, pathIndex);
            addPathLine(s, mainApp.getStopData().get(index + 1),  index);
        }
    }
}
