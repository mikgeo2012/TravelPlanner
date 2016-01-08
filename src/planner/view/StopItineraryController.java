package planner.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.javascript.object.Polyline;
import com.lynden.gmapsfx.javascript.object.PolylineOptions;
import com.lynden.gmapsfx.shapes.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import netscape.javascript.JSObject;
import planner.MainApp;
import planner.model.Dest;
import planner.model.Stop;
import planner.util.LongLatService;

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
        destNameColumn.setCellValueFactory(cellData -> cellData.getValue().destNameProperty());

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
                .zoom(12);

        // Create the map
        stopMap = stopMapView.createMap(mapOptions);


        // Add dest markers to map
        int index = 0;
        for (Dest d : stop.getDestData()) {
            if (d.getStopCoords() != null) {
                addMarker(d, index);
                index++;
            }
        }


        // Add travel lines to map
        for (int i = 0; i < stop.getDestData().size() - 1; i++) {
            Dest dest1 = stop.getDestData().get(i);
            Dest dest2 = stop.getDestData().get(i + 1);
            if (dest2.getStopCoords() != null) {
                addPathLine(dest1, dest2, i);
            }
        }

        //Add stop by right clicking on point on map
        stopMap.addUIEventHandler(UIEventType.rightclick, (JSObject obj) -> {
            LatLong coords = new LatLong((JSObject) obj.getMember("latLng"));
            handleNewDest(coords);
        });

    }


    /**
     * Adds a marker to the map and to the marker list for a respective dest d at index
     *
     * @param d
     * @param index
     */
    private void addMarker(Dest d, int index) {
        // Specify marker options
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(d.getStopCoords().makeLatLong())
                .visible(Boolean.TRUE)
                .title(d.getDestName());

        // Make marker
        Marker marker = new Marker( markerOptions );

        // Add marker to map and list
        stopMap.addMarker(marker);
        stop.getDestMarkers().add(index, marker);

        // Select respective stop when marker is clicked
        stopMap.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
            int markerIndex = stop.getDestData().indexOf(marker);
            destTable.getSelectionModel().select(markerIndex);
            //mainApp.getStopMarkers().get(markerIndex).setAnimation(Animation.BOUNCE);
        });
    }

    /**
     * Adds a marker to the map and to the marker list for a respective destination d at index with given LatLong cordinates
     *
     * @param d
     * @param index
     * @param coords
     */
    private void addMarker(Dest d, int index, LatLong coords) {
        // Set destination coordinates
        LongLatService tempLongLat = new LongLatService();
        d.setStopCoords(tempLongLat.getCoords(coords));

        // Specify marker options
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coords)
                .visible(Boolean.TRUE)
                .title(d.getDestName());

        // Make marker
        Marker marker = new Marker( markerOptions );

        // Add marker to map and list
        stopMap.addMarker(marker);
        stop.getDestMarkers().add(index, marker);

        // Select respective stop when marker is clicked
        stopMap.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
            int markerIndex = mainApp.getStopMarkers().indexOf(marker);
            destTable.getSelectionModel().select(markerIndex);
            //mainApp.getStopMarkers().get(markerIndex).setAnimation(Animation.BOUNCE);
        });
    }

    /**
     * Adds a path line to the map and the path list for destination2 d1 and d2 at index
     *
     * @param d1
     * @param d2
     * @param index
     */
    private void addPathLine(Dest d1, Dest d2, int index) {
        LatLong[] ary = new LatLong[]{d1.getStopCoords().makeLatLong(), d2.getStopCoords().makeLatLong()};
        MVCArray mvc = new MVCArray(ary);

        com.lynden.gmapsfx.shapes.PolylineOptions polyOpts = new com.lynden.gmapsfx.shapes.PolylineOptions()
                .path(mvc)
                .strokeColor("blue")
                .strokeWeight(3);

        com.lynden.gmapsfx.shapes.Polyline poly = new com.lynden.gmapsfx.shapes.Polyline(polyOpts);
        stopMap.addMapShape(poly);
        stop.getDestPath().add(index, poly);
    }


    /**
     * Adds the path lines to the map neccessary to add one destination at a given index
     * Calls addPathLine(d1, d2) to add destinations
     *
     * @param d
     * @param index
     */
    private void addPathLine(Dest d, int index) {
        int pathIndex = index - 1;

        // pathIndex > mainApp.getStopPath.size should work but doesn't
        if (index > stop.getDestPath().size() || pathIndex < 0) {    // Either end destination added
            boolean b = (index != 0);
            if (b) {    // Last stop added
                addPathLine(stop.getDestData().get(index - 1), d, pathIndex);
            } else {    // First stop deleted
                addPathLine(d, stop.getDestData().get(1), index);
            }
        } else { // Stop added in the middle
            // Remove current path
            com.lynden.gmapsfx.shapes.Polyline deletedPath = stop.getDestPath().remove(pathIndex);
            stopMap.removeMapShape(deletedPath);

            // Add updated path to new stop
            addPathLine(stop.getDestData().get(index - 1), d, pathIndex);
            addPathLine(d, stop.getDestData().get(index + 1),  index);
        }
    }


    /**
     * Called when the user clicks on the delete button. Deletes a destination and respective marker and path lines
     */
    @FXML
    private void handleDeleteDest() {
        int selectedIndex = destTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0 && stop.getDestData().get(selectedIndex).getStopCoords() != null) {
            // Remove stop from table
            destTable.getItems().remove(selectedIndex);

            // Remove stop marker from map and marker list
            Marker deletedStopMarker = stop.getDestMarkers().remove(selectedIndex);
            stopMap.removeMarker(deletedStopMarker);

            // Remove path to deleted stop from map and path list if path exists
            if (!stop.getDestPath().isEmpty()) {
                deletePath(selectedIndex);
            }
        } else {
            // Nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No destination selected");
            alert.setContentText("Please select a destination to delete");

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
        if (index == stop.getDestMarkers().size() || index == 0) {    // Either end stop deleted
            boolean b = (index != 0);
            if (b) {    // Last stop deleted
                com.lynden.gmapsfx.shapes.Polyline deletedPath = stop.getDestPath().remove(index - 1);
                stopMap.removeMapShape(deletedPath);
            } else {    // First stop deleted
                com.lynden.gmapsfx.shapes.Polyline deletedPath = stop.getDestPath().remove(index);
                stopMap.removeMapShape(deletedPath);
            }
        } else {    // Middle stop deleted
            // Delete path lines
            com.lynden.gmapsfx.shapes.Polyline deletedPath = stop.getDestPath().remove(index);
            com.lynden.gmapsfx.shapes.Polyline deletedPath2 = stop.getDestPath().remove(index - 1);
            stopMap.removeMapShape(deletedPath);
            stopMap.removeMapShape(deletedPath2);

            // Draw new path line
            Dest dest1 = stop.getDestData().get(index - 1);
            Dest dest2 = stop.getDestData().get(index);
            LatLong[] ary = new LatLong[]{dest1.getStopCoords().makeLatLong(), dest2.getStopCoords().makeLatLong()};
            MVCArray mvc = new MVCArray(ary);

            com.lynden.gmapsfx.shapes.PolylineOptions polyOpts = new com.lynden.gmapsfx.shapes.PolylineOptions()
                    .path(mvc)
                    .strokeColor("blue")
                    .strokeWeight(3);

            com.lynden.gmapsfx.shapes.Polyline poly = new com.lynden.gmapsfx.shapes.Polyline(polyOpts);
            stopMap.addMapShape(poly);
            stop.getDestPath().add(index - 1, poly);
        }
    }


    /**
     * Called when the user clicks on a location on the map. Opens a dialog to add
     * details for a new person.
     */
    @FXML
    private void handleNewDest(LatLong coords) {
        // Index where to add new stop
        int index = destTable.getSelectionModel().getSelectedIndex();

        // Open dialog box and add new stop
        Dest tempDest = new Dest();
        boolean okClicked = mainApp.showDestAddDialog(tempDest);
        if (okClicked) {
            stop.getDestData().add(index, tempDest);
            addMarker(tempDest, index, coords);
            addPathLine(tempDest, index);
        }
    }

}
