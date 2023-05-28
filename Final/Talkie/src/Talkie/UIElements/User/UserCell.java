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

import Talkie.Controllers.MainController;
import Talkie.Elements.Chat;
import Talkie.Elements.User;
import Talkie.Functions.Client;

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

    public static UserCell newInstance(MainController mainController) {
        FXMLLoader loader = new FXMLLoader(UserCell.class.getResource("UserCell.fxml"));
        try {
            loader.load();
            UserCell cell = loader.getController();
            cell.mainController = mainController;
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
        if (!empty && item != null && item instanceof User) {
            User user = (User) item;
            lblUser.setText(user.getUsername());
            
            if (mainController.getActiveChat().isAdmin()) {
                lblNotf.setVisible(false);
                btnAccept.setVisible(false);

                if (!user.isBelongGroup()) {
                    // Create a new Image with the updated URL
                    Image newImage = new Image("file:./img/add.png");
                    // Set the new Image to the ImageView
                    imgViewIcon.setImage(newImage);
                    btnIcon.setOnAction(this::addUser);
                }
                
                if (user.isRequestingEnter()) {
                    lblNotf.setVisible(true);
                    btnAccept.setVisible(true);
                    
                    Image newImage = new Image("file:./img/remove.png");
                    imgViewIcon.setImage(newImage);
                    btnIcon.setOnAction(this::denyUser);
                }
            } else {
                lblNotf.setVisible(false);
                btnAccept.setVisible(false);
                btnIcon.setVisible(false);
            }
            
        }
        // keep a reference to the model item in the ListCell
        this.model = item;
    }

    @FXML
    private void acceptUser(ActionEvent event) {
        try {
            Client client = new Client();
            char response = client.addUser(mainController.getActiveUser().getUsername(),
            mainController.getActiveChat().getGroupname(), lblUser.getText());
            
            if (response == '1') {
                System.out.println("User accepted");
                response = client.deleteReq(mainController.getActiveChat().getGroupname(), lblUser.getText());
            
                if (response == '1') {
                    mainController.getUsers();
                    mainController.updateLists();
                    
                    // Notify on the group
                    String message = "User added to the group";
                    response = client.sendMessage(lblUser.getText(),
                    mainController.getActiveChat().getGroupname(), message);
                    
                    if (response == '1') {
                        mainController.updateMessages();
                        System.out.println("User added message send");
                    } else {
                        System.out.println("Error sending message");
                    }

                    System.out.println("Request removed");
                } else {
                    System.out.println("Error removing request");
                }
            } else {
                System.out.println("Error accepting user");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }

    @FXML
    private void denyUser(ActionEvent event) {
        try {
            Client client = new Client();
            char response = client.deleteReq(mainController.getActiveChat().getGroupname(), lblUser.getText());
            
            if (response == '1') {
                mainController.getUsers();
                mainController.updateLists();
                
                System.out.println("Request denied");
            } else {
                System.out.println("Error denied user");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }


    @FXML
    private void addUser(ActionEvent event) {
        try {

            Client client = new Client();

            char response = client.addUser(mainController.getActiveUser().getUsername(),
                    mainController.getActiveChat().getGroupname(), lblUser.getText());
            
            if (response == '1') {
                mainController.getUsers();
                mainController.updateLists();
                
                // Notify on the group
                String message = "User added to the group";
                response = client.sendMessage(lblUser.getText(),
                mainController.getActiveChat().getGroupname(), message);
                
                if (response == '1') {
                    mainController.updateMessages();
                    System.out.println("User added");
                } else {
                    System.out.println("Error sending message");
                }
            } else {
                System.out.println("Error adding user");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }

    @FXML
    private void removeUser(ActionEvent event) {
        try {
            Client client = new Client();

            // Notify on the group
            String message = "User removed from the group";
            char response = client.sendMessage(lblUser.getText(),
                       mainController.getActiveChat().getGroupname(), message);
                       
            if (response == '1') {
                System.out.println("User removed");
                mainController.updateMessages();

                response = client.deleteUser(mainController.getActiveChat().getGroupname(), lblUser.getText());
                if (response == '1') {
                    mainController.getUsers();
                    mainController.updateLists();
                } else {
                    System.out.println("Error deleting user");
                }
            } else {
                System.out.println("Error sending message");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }

}
