package planner.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import planner.model.Coords;
import planner.model.Stop;
import planner.util.LongLatService;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to add stop
 *
 * Created by mikhailgeorge on 1/1/16.
 */
public class StopAddDialogController {
    @FXML
    private TextField countryField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField descriptionField;

    private Stage dialogStage;
    private Stop stop;
    private boolean okClicked = false;

    /**
     * Initializes the controller class
     */
    private void initialize() {}

    /**
     * Sets the stage of this dialog
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Sets the fields to be edited
     * @param stop
     */
    public void setStop(Stop stop) {
        this.stop = stop;

        countryField.setText(stop.getCountryName());
        stateField.setText(stop.getStateName());
        cityField.setText(stop.getCityName());
        descriptionField.setText(stop.getDescription());

    }


    /**
     * Returns true if the user has clicked OK, false otherwise
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }


    /**
     * Call when the user clicks OK
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            stop.setCountryName(countryField.getText());
            stop.setCityName(cityField.getText());
            stop.setStateName(stateField.getText());
            stop.setDescription(descriptionField.getText());
            Coords tempCoords = getCoords(countryField.getText(), stateField.getText(), cityField.getText());
            stop.setStopCoords(tempCoords);
            okClicked = true;
            dialogStage.close();
        }
    }


    /**
     * Check if country name is entered, if state name is entered if country is USA, if city name is entered, and
     * if description is entered
     *
     * @return
     */
    private boolean isInputValid() {
        // List of possible names of USA
        List<String> usaNames = new ArrayList<String>(5);
        usaNames.add("United States of America");
        usaNames.add("United States");
        usaNames.add("USA");
        usaNames.add("US");
        usaNames.add("America");

        String errorMessage = "";

        if (countryField.getText() == null || countryField.getText().length() == 0) { // Check if country entered
            errorMessage += "No valid country!\n";
        }
        if (usaNames.contains(countryField.getText()) && (stateField.getText().length() == 0 ||
                stateField.getText() == null)) { // Check if state entered if needed
            errorMessage += "No valid state!\n";
        }
        if (cityField.getText() == null || cityField.getText().length() == 0) { // Check if city entered
            errorMessage += "No valid city!\n";
        }
        if (descriptionField.getText() == null || descriptionField.getText().length() == 0) { // Check if description entered
            errorMessage += "No valid description!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private Coords getCoords(String country, String state, String city) {
        String address = null;

        if (state == null) { // Outside US
            address = city + ", " + country;
        } else { // Inside US
            address = city + ", " + state + ", " + country;
        }

        LongLatService coords = new LongLatService();
        coords.getLongitudeLatitude(address);

        double latitude = coords.getLatitude();
        double longtitude = coords.getLongtitude();

        return new Coords(latitude, longtitude);
    }


    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

}
