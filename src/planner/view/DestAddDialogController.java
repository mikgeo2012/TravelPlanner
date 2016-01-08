package planner.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import planner.model.Coords;
import planner.model.Dest;
import planner.model.Stop;
import planner.util.LongLatService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for dialog to add destinations
 *
 * Created by mikhailgeorge on 1/8/16.
 */
public class DestAddDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;


    private Stage dialogStage;
    private Dest dest;
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
     * @param dest
     */
    public void setDest(Dest dest) {
        this.dest = dest;

        nameField.setText(dest.getDestName());
        descriptionField.setText(dest.getDescription());
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
            dest.setDestName(nameField.getText());
            dest.setDescription(descriptionField.getText());
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
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) { // Check if country entered
            errorMessage += "No valid name!\n";
        }
        if (descriptionField.getText() == null || descriptionField.getText().length() == 0) { // Check if city entered
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


    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
