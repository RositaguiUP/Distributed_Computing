package Talkie.UIElements.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Talkie.Elements.User;

public class UserCell extends ListCell<Object> implements Initializable {
    @FXML
    private Label lblUser;

    @FXML
    private Label lblNotf;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnIcon;
    
    @FXML
    ImageView imgViewIcon;;
    
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
    protected void updateItem(Object item, boolean empty) {

        super.updateItem(item, empty); // <-- Important
        // make empty cell items invisible
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        // update valid cells with model data
        if (!empty && item != null && item instanceof User && !item.equals(this.model)) {
            User user = (User) item;
            lblUser.setText(user.getUsername());
            lblNotf.setVisible(false);
            btnAccept.setVisible(false);

            if (user.isBelongGroup()) {
                // Create a new Image with the updated URL
                Image newImage = new Image("file:./img/add.png");
                // Set the new Image to the ImageView
                imgViewIcon.setImage(newImage);
                btnIcon.setOnAction(this::addUser);

            } else if (user.isRequestingEnter()) {
                lblNotf.setVisible(true);
                btnAccept.setVisible(true);

                Image newImage = new Image("file:./img/remove.png");
                imgViewIcon.setImage(newImage);
                btnIcon.setOnAction(this::denyUser);
            }
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @FXML
    private void acceptUser(ActionEvent event) {
        System.out.println("User accepted");
        // Update list

        lblNotf.setVisible(false);
        btnAccept.setVisible(false);
        Image newImage = new Image("file:./img/remove.png");
        imgViewIcon.setImage(newImage);
    }

    @FXML
    private void denyUser(ActionEvent event) {
        System.out.println("User denied");
        // Update list

        lblNotf.setVisible(false);
        btnAccept.setVisible(false);
        Image newImage = new Image("file:./img/add.png");
        imgViewIcon.setImage(newImage);
        btnIcon.setOnAction(this::addUser);
    }


    @FXML
    private void addUser(ActionEvent event) {
        System.out.println("User added");
        // Update list

        // SIMULATION (Delete)
        Image newImage = new Image("file:./img/remove.png");
        imgViewIcon.setImage(newImage);
    }

    @FXML
    private void removeUser(ActionEvent event) {
        System.out.println("User removed");
        // Update list

        // SIMULATION (Delete)
        Image newImage = new Image("file:./img/add.png");
        imgViewIcon.setImage(newImage);
    }

}
