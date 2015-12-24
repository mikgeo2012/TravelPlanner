package planner.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import planner.model.Stop;



/**
 * Created by mikhailgeorge on 12/23/15.
 */
public class ItineraryController {
    @FXML
    private TableView<Stop> stopTable;
    @FXML
    private TableColumn<Stop, String> firstNameColumn;

}
