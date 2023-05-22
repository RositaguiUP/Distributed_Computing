/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Talkie.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.simple.parser.ParseException;

import Talkie.Elements.Group;
import Talkie.Elements.Message;
import Talkie.Elements.User;
import Talkie.Functions.Client;
import Talkie.UIElements.Msg.MsgCell;
import Talkie.UIElements.Person.PersonListCell;
import Talkie.UIElements.User.UserCell;

/**
 * FXML Controller class
 *
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label lblActChat;
    @FXML
    private TextField txtMessage;
    @FXML
    private ListView<Message> lstViewMsg;

    @FXML
    private ListView<User> lstView;


    private User activeUser;
    private Group activeGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void initData(User user) throws IOException{
        activeUser = user;
        updateChat();
    }

    public void updateChat() {

        ArrayList<User> users = new ArrayList<>();
        users.add(activeUser);

        ArrayList<Message> chat = new ArrayList<>();
        chat.add(new Message(activeUser, "Hola"));
        chat.add(new Message(new User("User2"), "Holi"));

        activeGroup = new Group("Group", users, chat);
        lblActChat.setText(activeGroup.getGroupname());

        // Chat
        ObservableList<Message> messagesItems = FXCollections.observableArrayList();
        messagesItems.add(new Message(activeUser, "Hola")); //chat.get(0));
        messagesItems.add(new Message(new User("User2"), "Holi"));
        
        lstViewMsg.setCellFactory((lv) -> {
            return MsgCell.newInstance();
        });
        lstViewMsg.setItems(messagesItems);

        // Users
        ObservableList<User> usersItems = FXCollections.observableArrayList();
        usersItems.addAll(users);

        lstView.setCellFactory((lv) -> {
            return UserCell.newInstance();
        });
        lstView.setItems(usersItems);
        

        // try {
        //     Client client = new Client();
        //     JSONObject responseJson = client.getChat(activeUser.getUsername(), activeGroup.getGroupname());
            
        //     char response = ((String) responseJson.get("result")).charAt(0);
        
        //     if (response == '1') {
        //         System.out.println("Chat is got!");
        //     } else {
        //         System.out.println("Error getting chat");
        //     }
        // } catch (Exception e) {
        //     System.out.println("Server not available");
        // }
    }
    
    @FXML
    private void viewUsers(ActionEvent event) {
        //contextMenu.show(btnShowMenu, Side.BOTTOM, 0, 0);
    }

    @FXML
    private void viewChats(ActionEvent event) {
        //updateChat();
    }

    @FXML
    private void viewRooms(ActionEvent event) {
        //contextMenu.show(btnShowMenu, Side.BOTTOM, 0, 0);
    }
     
    @FXML
    private void createGroup(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./../UI/createGroup.fxml"));
            Parent root = fxmlLoader.load();

            CreateGroupController createGroupController = fxmlLoader.getController();

            // Create a new dialog
            Dialog<Void> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setResizable(false);
            // dialog.getIcons().add(new Image("file:./img/Icon.png"));

            // Set the content of the dialog
            dialog.getDialogPane().setContent(root);

            // Add the necessary buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Load the CSS file
            String cssFile = getClass().getResource("./../UI/Components.css").toExternalForm();
            dialog.getDialogPane().getScene().getStylesheets().add(cssFile);

            // Define the action to be performed when the OK button is clicked
            Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.getStyleClass().add("ok-button");

            Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.getStyleClass().add("cancel-button");

            okButton.addEventFilter(ActionEvent.ACTION, ev -> {
                try {
                    int response = createGroupController.createGroup(activeUser.getUsername());
                    System.out.println("Res = " + response);
                    if (response == 1) {
                        // Allow the dialog to close
                        dialog.close();
                    } else {
                        // Consume the event to prevent the dialog from closing
                        ev.consume();
                    }
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            });
            
            // Show the dialog and wait for user input
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage(ActionEvent event) throws IOException, ParseException {
        String message = txtMessage.getText();
        
        try {
            Client client = new Client();
            char response = client.sendMessage(activeUser.getUsername(), activeGroup.getGroupname(), message);
        
            if (response == '1') {
                txtMessage.setText("");
                System.out.println("Message send!");
            } else {
                System.out.println("Error sending message");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
            //lblError.setText("Server not available");
        }
    }
}
