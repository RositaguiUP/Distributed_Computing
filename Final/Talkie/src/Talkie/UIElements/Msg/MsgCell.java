package Talkie.UIElements.Msg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Talkie.Elements.Message;

public class MsgCell extends ListCell<Message> implements Initializable {

    @FXML
    private Label lblUser;

    @FXML
    private Label lblMsg;

    @FXML
    private GridPane root;

    private Message model;

   
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

    public static MsgCell newInstance() {
        FXMLLoader loader = new FXMLLoader(MsgCell.class.getResource("MsgCell.fxml"));
        try {
            loader.load();
            return loader.getController();
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
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

}
