package planner;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import planner.model.Coords;
import planner.model.Stop;
import planner.view.ItineraryController;


import java.io.IOException;

/**
 * Created by mikhailgeorge on 12/23/15.
 */
public class MainApp extends Application {

    private GoogleMapView mapView;
    private GoogleMap map;

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Stop> stopData = FXCollections.observableArrayList();

    /**
     * Initialize the google map
     */
    /*@Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(47.6097, -122.3331))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

    }*/

    /**
     * Constructor
     */
    public MainApp() {
        /*// Sample data
        stopData.add(new Stop("New York", new LatLong(40.7122, -74.0052), "Starting city"));
        stopData.add(new Stop("Chicago", new LatLong(41.8782, -87.6292), "Second city"));
        stopData.add(new Stop("Austin", new LatLong(30.2672, -97.7432), "Third city"));
        stopData.add(new Stop("Las Vegas", new LatLong(36.1692, -115.1392), "Fourth city"));
        stopData.add(new Stop("Seattle", new LatLong(47.6062, -122.3322), "Ending city"));*/
    }

    /**
     * Returns the data as an observable list of Stops.
     * @return
     */
    public ObservableList<Stop> getStopData() {
        return stopData;
    }

    @Override
    public void start(Stage primaryStage) {
        mapView = new GoogleMapView();
        //mapView.addMapInializedListener(this);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TravelPlannerApp");

        initRootLayout();

        showItinerary();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showItinerary() {
        try {
            // Load itinerary layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/Itinerary.fxml"));
            AnchorPane itinerary = (AnchorPane) loader.load();

//            ObservableList<Node> children = itinerary.getChildren();
//            children.add(mapView);

            // Set person overview into the center of root layout.
            rootLayout.setCenter(itinerary);

            // Give the controller access to the main app.
            ItineraryController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
