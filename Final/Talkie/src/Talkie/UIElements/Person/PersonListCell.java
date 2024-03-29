package Talkie.UIElements.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Talkie.Elements.Message;
import Talkie.Elements.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Nicholas Folse
 */
public class PersonListCell extends ListCell<Message> implements Initializable {

    
    @FXML
    private Label lblUser;

    @FXML
    private Label lblMsg;

    @FXML
    private Label id;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    //@FXML
    //private GridPane root;
    @FXML
    private GridPane root;
    private Message model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize a newly created cell to unselected status
        updateSelected(false);
        // add a un-focused listener to each child-item that triggers commitEdit(...)
        getRoot().getChildrenUnmodifiable().forEach(c -> {
            c.focusedProperty().addListener((obj, prev, curr) -> {
                if (!curr) {
                    commitEdit(model);
                }
            });
        });
        // set ListCell graphic
        setGraphic(root);
    }

    public GridPane getRoot() {
        return root;
    }

    private static final Logger LOG = Logger.getLogger(PersonListCell.class.getName());

    public static PersonListCell newInstance() {
        FXMLLoader loader = new FXMLLoader(PersonListCell.class.getResource("MsgCell.fxml")); //./../Msg/MsgCell.fxml Talkie.UIElements.Msg.
        try {
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
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
            lblUser.textProperty().set("User1");
            //id.textProperty().set(item.id().toString());
            // firstName.textProperty().set(item.firstName());
            // lastName.textProperty().set(item.lastName());
            // phoneNumber.textProperty().set(item.phoneNumber());
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @Override
    public void commitEdit(Message newValue) {
        // if newValue isn't defined, use this.model
        newValue = (newValue == null) ? this.model : newValue;
        super.commitEdit(newValue); // <-- important
        // update the model with values from the text fields
        //newValue.firstName(firstName.textProperty().get());
        //newValue.lastName(lastName.textProperty().get());
        //newValue.phoneNumber(phoneNumber.textProperty().get());
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        // update UI hints based on selected state
        getRoot().getChildrenUnmodifiable().forEach(c -> {
            // setting mouse-transparent to false ensure that
            // the cell will get selected we click on a field in
            // a non-selected cell
            c.setMouseTransparent(!selected);
            // focus-traversable prevents users from "tabbing"
            // out of the currently selected cell
            c.setFocusTraversable(selected);
        });
        if (selected) {
            // start editing when the cell is selected
            startEdit();
        } else {
            if (model != null) {
                // commit edits if the cell becomes unselected
                // we're not keeping track of "dirty" state
                // so this will commit changes even to unmodified cells
                commitEdit(model);
            }
        }
    }

}