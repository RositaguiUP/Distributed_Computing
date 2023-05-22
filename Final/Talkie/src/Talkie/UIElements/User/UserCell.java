package Talkie.UIElements.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Talkie.Elements.User;

public class UserCell extends ListCell<User> implements Initializable {

    @FXML
    private Label lblUser;

    @FXML
    private Label lblMsg;

    @FXML
    private GridPane root;

    private User model;

   
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

    public static UserCell newInstance() {
        FXMLLoader loader = new FXMLLoader(UserCell.class.getResource("UserCell.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty); // <-- Important
        // make empty cell items invisible
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        // update valid cells with model data
        if (!empty && item != null && !item.equals(this.model)) {
            lblUser.setText(item.getUsername());
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

}
