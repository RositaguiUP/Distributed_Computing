package Talkie.UIElements.Msg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Talkie.Elements.Message;
import Talkie.Elements.User;

public class MsgCell extends ListCell<Message> implements Initializable {

    @FXML
    private Label lblUser;

    @FXML
    private Label lblMsg;

    @FXML
    private VBox container;

    @FXML
    private AnchorPane root;

    private Message model;

   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // set ListCell graphic
        setGraphic(root);
    }

    public AnchorPane getRoot() {
        return root;
    }

    private User activeUser;

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public static MsgCell newInstance(User activeUser) {
        FXMLLoader loader = new FXMLLoader(MsgCell.class.getResource("MsgCell.fxml"));
        try {
            loader.load();
            MsgCell controller = loader.getController();
            controller.setActiveUser(activeUser);
            return controller;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty); // <-- Important
        // make empty cell items invisible
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        // update valid cells with model data
        if (!empty && item != null && !item.equals(this.model)) {
            lblUser.setText(item.getUser().getUsername());
            lblMsg.setText(item.getMessage());
            
            // Customize alignment based on active user
            if (item.getUser().getUsername().equals(activeUser.getUsername())) {
                container.setAlignment(Pos.CENTER_RIGHT);
                lblMsg.getStyleClass().add("soft-orange-bckgrnd");
                lblUser.setText("You");
            }
        }

        // keep a reference to the model item in the ListCell
        this.model = item;
    }

}
