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
import javafx.scene.image.Image;
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
import Talkie.Functions.ListViewSetting.ListOptions;
import Talkie.UIElements.Msg.MsgCell;
import Talkie.UIElements.User.UserCell;
import Talkie.UIElements.Chat.ChatCell;
import Talkie.UIElements.Room.RoomCell;

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
    private Button btnDelete;
    @FXML
    private TextField txtMessage;
    @FXML
    private ListView<Message> lstViewMsg;
    @FXML
    private ListView<Object> lstView;

    // Active items
    private User activeUser;
    private Group activeGroup;
    private ListOptions activeList;

    // Lists
    private ArrayList<User> users;
    private ArrayList<Group> chats;
    private ArrayList<Group> rooms;
    private ArrayList<Message> messages;
    private ObservableList<User> usersItems;
    private ObservableList<Group> chatsItems;
    private ObservableList<Group> roomsItems;
    private ObservableList<Message> messagesItems;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void initData(User user) throws IOException{
        activeUser = user;
        activeList = ListOptions.USERS;

        updateLists();
    }

    public void updateLists() {
        getData();

        activeGroup = chats.get(0);
        lblActChat.setText(activeGroup.getGroupname());

        // Enable option to delete group
        if (activeGroup.getUsers().size() == 1) {
            btnDelete.setVisible(true);
        } else {
            btnDelete.setVisible(false);
        }

        // Messages list View
        lstViewMsg.setCellFactory((lv) -> {
            return MsgCell.newInstance(activeUser);
        });
        lstViewMsg.setItems(messagesItems);

        // Main list view
        switch (activeList) {
            case CHATS:
                lstView.setCellFactory((lv) -> {
                    return ChatCell.newInstance();
                });
                
                ObservableList<Object> objectItemsChats = FXCollections.observableArrayList();
                objectItemsChats.addAll(chatsItems);
                lstView.setItems(objectItemsChats);
                break;
            case USERS:
                lstView.setCellFactory((lv) -> {
                    return UserCell.newInstance();
                });

                ObservableList<Object> objectItemsUsers = FXCollections.observableArrayList();
                objectItemsUsers.addAll(usersItems);
                lstView.setItems(objectItemsUsers);
                break;
            case ROOMS:
                lstView.setCellFactory((lv) -> {
                    return RoomCell.newInstance();
                });

                ObservableList<Object> objectItemsRooms = FXCollections.observableArrayList();
                objectItemsRooms.addAll(roomsItems);
                lstView.setItems(objectItemsRooms);
                break;
        }
    }

    private void getData() {
        users = new ArrayList<>();
        chats = new ArrayList<>();
        rooms = new ArrayList<>();
        messages = new ArrayList<>();
        usersItems = FXCollections.observableArrayList();
        chatsItems = FXCollections.observableArrayList();
        roomsItems = FXCollections.observableArrayList();
        messagesItems = FXCollections.observableArrayList();

        // Users
        users.add(activeUser);
        users.add(new User("User2", false));
        users.add(new User("User3", false, true));
        usersItems.addAll(users);

        // Groups / Chats
        chats.add(new Group("Group", users, messages, false));
        chats.add(new Group("Group2", users, messages, true));
        chatsItems.addAll(chats);

        // Groups / Rooms
        rooms.add(new Group("Group3", users, true));
        rooms.add(new Group("Group4", users, true));
        roomsItems.addAll(rooms);
        
        // Messages
        messages.add(new Message(activeUser, "Hola"));
        messages.add(new Message(users.get(1), "Holi"));
        messagesItems.addAll(messages);


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
    private void refresh(ActionEvent event) {
        System.out.println("Updating...");
        updateLists();
    }
    
    @FXML
    private void viewUsers(ActionEvent event) {
        activeList = ListOptions.USERS;
        updateLists();
    }

    @FXML
    private void viewChats(ActionEvent event) {
        activeList = ListOptions.CHATS;
        updateLists();
    }

    @FXML
    private void viewRooms(ActionEvent event) {
        activeList = ListOptions.ROOMS;
        updateLists();
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
            // Set the icon for the dialog's stage
            stage.getIcons().add(new Image("file:./img/Icon.png"));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteGroup(ActionEvent event) {
        System.out.println("Deleting group...");
        updateLists();
        // Here I need to change the active group to the next one
    }
    

    @FXML
    private void sendMessage(ActionEvent event) throws IOException, ParseException {
        String message = txtMessage.getText();

        if (message != ""){
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
            }
        }
    }
}
