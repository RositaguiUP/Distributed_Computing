package Talkie.UIElements.Chat;

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
import Talkie.Elements.Chat;
import Talkie.Functions.Client;

public class ChatCell extends ListCell<Object> implements Initializable {
    @FXML
    private Label lblGroup;

    @FXML
    private Label lblNotf;

    @FXML
    private Button btnExit;
    
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

    public static ChatCell newInstance(MainController mainController) {
        FXMLLoader loader = new FXMLLoader(ChatCell.class.getResource("ChatCell.fxml"));
        try {
            loader.load();
            ChatCell cell = loader.getController();
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
        if (!empty && item != null && item instanceof Chat) {
            Chat group = (Chat) item;
            lblGroup.setText(group.getGroupname());

            if (!group.isNewMessages()) {
                lblNotf.setVisible(false);
            }

            if (group.getTotalUsers() == 1) {
                btnExit.setVisible(false);
            }

            // Set the background color based on the selected state
            if (isSelected()) {
                getRoot().setStyle("-fx-background-color: #ec82b357; -fx-background-radius: 25px;");
                lblNotf.setVisible(false);
                group.setNewMessages(false);
                mainController.updateActiveChat(group); // Update the activeChat in the main controller
            } else {
                getRoot().setStyle(""); // Reset the background color
            }
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @FXML
    private void exitGroup(ActionEvent event) {
        try {
            Client client = new Client();
            char response = client.deleteUser(lblGroup.getText(), mainController.getActiveUser().getUsername());
            
            if (response == '1') {
                mainController.getUsers();
                if (mainController.getArrayChats().isEmpty()) {
                    mainController.updateActiveChat(null);
                } else {
                    mainController.updateActiveChat(mainController.getArrayChats().get(0));
                }
                mainController.updateLists();
                System.out.println("Exit group...");
            } else {
                System.out.println("Error deleting user");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }


}
