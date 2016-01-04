package planner;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.*;
import com.lynden.gmapsfx.shapes.Polyline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import planner.model.Coords;
import planner.model.Stop;
import planner.view.ItineraryController;
import planner.view.StopAddDialogController;


import java.io.IOException;

/**
 * Created by mikhailgeorge on 12/23/15.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Stops.
     */
    private ObservableList<Stop> stopData = FXCollections.observableArrayList();

    /**
     * The data as an observable list of Markers of stops
     */
    private ObservableList<Marker> stopMarkers = FXCollections.observableArrayList();

    /**
     * The data as an observable list of polylines of the travel path
     */
    private ObservableList<Polyline> stopPath = FXCollections.observableArrayList();


    public MainApp() {
        // Sample data
        stopData.add(new Stop("USA", "NY", "New York City", new Coords(40.7122, -74.0052), "Starting city"));
        stopData.add(new Stop("USA", "IL","Chicago", new Coords(41.8782, -87.6292), "Second city"));
        stopData.add(new Stop("USA", "TX","Austin", new Coords(30.2672, -97.7432), "Third city"));
        stopData.add(new Stop("USA", "NV","Las Vegas", new Coords(36.1692, -115.1392), "Fourth city"));
        stopData.add(new Stop("USA", "WA","Seattle", new Coords(47.6062, -122.3322), "Ending city"));
        stopData.add(new Stop());
    }

    /**
     * Returns the data as an observable list of Stops.
     * @return
     */
    public ObservableList<Stop> getStopData() {
        return stopData;
    }

    /**
     * Returns the data as an observable list of Markers.
     * @return
     */
    public ObservableList<Marker> getStopMarkers() {
        return stopMarkers;
    }

    /**
     * Returns the data as an observable list of Paths.
     * @return
     */
    public ObservableList<Polyline> getStopPath() {
        return stopPath;
    }

    @Override
    public void start(Stage primaryStage) {
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

    /**
     * Show Itinerary view inside root layout
     */
    public void showItinerary() {
        try {
            // Load itinerary layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/Itinerary.fxml"));
            AnchorPane itinerary = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(itinerary);

            // Give the controller access to the main app.
            ItineraryController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean showStopAddDialog(Stop stop) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StopAddDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Stop");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            StopAddDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStop(stop);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
