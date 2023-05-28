package Talkie.UIElements.Room;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Talkie.Controllers.MainController;
import Talkie.Elements.Room;
import Talkie.Functions.Client;

public class RoomCell extends ListCell<Object> implements Initializable {
    @FXML
    private Label lblGroup;

    @FXML
    private Label lblNotf;

    @FXML
    private Button btnEnter;
    
    @FXML
    private GridPane root;

    private Object model;
    private MainController mainController;

   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // set ListCell graphic
        setGraphic(root);
    }

    public GridPane getRoot() {
        return root;
    }

    public static RoomCell newInstance(MainController mainController) {
        FXMLLoader loader = new FXMLLoader(RoomCell.class.getResource("RoomCell.fxml"));
        try {
            loader.load();
            RoomCell cell = loader.getController();
            cell.mainController = mainController;
            return cell;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty); // <-- Important
        // make empty cell items invisible
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        // update valid cells with model data
        if (!empty && item != null && item instanceof Room) {
            Room group = (Room) item;
            lblGroup.setText(group.getGroupname());
            lblNotf.setVisible(false);
            
            if (group.isWaitingEnter()) {
                btnEnter.setDisable(true);
                lblNotf.setVisible(true);
            }
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @FXML
    private void enterGroups(ActionEvent event) {
        try {
            Client client = new Client();
            char response = client.addReq(mainController.getActiveUser().getUsername(),
                 lblGroup.getText(), mainController.getActiveUser().getUsername());
            
            if (response == '1') {
                mainController.getRooms();
                mainController.updateLists();
                System.out.println("Req added");
            } else {
                System.out.println("Error adding req");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
        
        // btnEnter.setDisable(true);
        // lblNotf.setVisible(true);
        
    }

}
