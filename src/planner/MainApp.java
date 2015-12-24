package planner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import planner.view.ItineraryController;


import java.io.IOException;

/**
 * Created by mikhailgeorge on 12/23/15.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Constructor
     */
    public MainApp() {
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

    public void showItinerary() {
        try {
            // Load root layout from fxml file.
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

    public static void main(String[] args) {
        launch(args);
    }
}
