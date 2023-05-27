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

import Talkie.Elements.Group;

public class RoomCell extends ListCell<Group> implements Initializable {
    @FXML
    private Label lblGroup;

    @FXML
    private Label lblNotf;

    @FXML
    private Button btnIcon;
    
    @FXML
    private GridPane root;

    private Group model;

   
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

    public static RoomCell newInstance() {
        FXMLLoader loader = new FXMLLoader(RoomCell.class.getResource("RoomCell.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void updateItem(Group item, boolean empty) {
        super.updateItem(item, empty); // <-- Important
        // make empty cell items invisible
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        // update valid cells with model data
        if (!empty && item != null && !item.equals(this.model)) {
            lblGroup.setText(item.getGroupname());
            btnIcon.setDisable(false);
            lblNotf.setVisible(false);
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @FXML
    private void enterGroups(ActionEvent event) {
        btnIcon.setDisable(true);
        lblNotf.setVisible(true);
    }

}
