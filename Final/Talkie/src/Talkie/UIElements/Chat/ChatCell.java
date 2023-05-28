package Talkie.UIElements.Chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Talkie.Elements.Group;

public class ChatCell extends ListCell<Object> implements Initializable {
    @FXML
    private Label lblGroup;

    @FXML
    private Label lblNotf;
    
    @FXML
    private GridPane root;

    private Object model;

   
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

    public static ChatCell newInstance() {
        FXMLLoader loader = new FXMLLoader(ChatCell.class.getResource("ChatCell.fxml"));
        try {
            loader.load();
            return loader.getController();
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
        if (!empty && item != null && item instanceof Group && !item.equals(this.model)) {
            Group group = (Group) item;
            lblGroup.setText(group.getGroupname());
            lblNotf.setVisible(false);

            if (group.isNewMessages()) {
                lblNotf.setVisible(true);
            }
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @FXML
    private void exitGroup(ActionEvent event) {
        System.out.println("Exit group...");
    }


}
