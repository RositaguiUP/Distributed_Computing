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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Talkie.Elements.Room;
import Talkie.Elements.Chat;
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
    private Chat activeChat;
    private ListOptions activeList;

    // Lists
    private ArrayList<User> users;
    private ArrayList<Chat> chats;
    private ArrayList<Room> rooms;
    private ArrayList<Message> messages;
    private ObservableList<User> usersItems;
    private ObservableList<Chat> chatsItems;
    private ObservableList<Room> roomsItems;
    private ObservableList<Message> messagesItems;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void initData(User user) throws IOException{
        activeUser = user;
        activeList = ListOptions.CHATS;
        getChats();

        updateActiveChat(chats.get(0));
        updateLists();
    }

    public void updateActiveChat(Chat chat) {
        activeChat = chat;
        lblActChat.setText(activeChat.getGroupname());

        // Enable option to delete group
        if (activeChat.getTotalUsers() == 1 && activeChat.isAdmin()) {
            btnDelete.setVisible(true);
        } else {
            btnDelete.setVisible(false);
        }
        updateMessages();
    }

    public void updateMessages() {
        getMessages();

        // Messages list View
        lstViewMsg.setCellFactory((lv) -> {
            return MsgCell.newInstance(activeUser);
        });
        lstViewMsg.setItems(messagesItems);
    }

    public void updateLists() {
        // Main list view
        switch (activeList) {
            case CHATS:
                lstView.setCellFactory((lv) -> {
                    return ChatCell.newInstance(this);
                });
                
                ObservableList<Object> objectItemsChats = FXCollections.observableArrayList();
                objectItemsChats.addAll(chatsItems);
                lstView.setItems(objectItemsChats);

                // Select the activeChat if it exists in the chats list
                if (activeChat != null && chats.contains(activeChat)) {
                    lstView.getSelectionModel().select(activeChat);
                } else {
                    System.out.println("Not active chat found in chats!!!");
                }

                break;
            case USERS:
                lstView.setCellFactory((lv) -> {
                    return UserCell.newInstance(activeChat);
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

    private void getUsers() {
        users = new ArrayList<>();
        usersItems = FXCollections.observableArrayList();
        
        users.add(activeUser);
        users.add(new User("User2", false));
        users.add(new User("User3", false, true));
        usersItems.addAll(users);
    }

    private void getChats() {
        chats = new ArrayList<>();
        chatsItems = FXCollections.observableArrayList();

        // Groups / Chats
        Chat chat1 = new Chat("Group", 1, 2, false);
        chat1.setNewMessages(true);
        chats.add(chat1);
        chats.add(new Chat("Group2", 2, 2, false));
        chatsItems.addAll(chats);
    }

    private void getRooms() {
        rooms = new ArrayList<>();
        roomsItems = FXCollections.observableArrayList();

        // Groups / Rooms
        rooms.add(new Room("Group3", false));
        rooms.add(new Room("Group4", true));
        roomsItems.addAll(rooms);
    }

    private void getMessages() {
        messages = new ArrayList<>();
        messagesItems = FXCollections.observableArrayList();

        try {
            Client client = new Client();
            JSONObject responseJson = client.getChat(activeUser.getUsername(), activeChat.getGroupname());
            
            char response = ((String) responseJson.get("result")).charAt(0);
        
            if (response == '1') {
                System.out.println("Chat is got!");
                JSONArray msgsJson = (JSONArray) responseJson.get("chats");

                for (Object obj : msgsJson) {
                    JSONObject msgObject = (JSONObject) obj;
                    String username = (String) msgObject.get("user");
                    String messageText = (String) msgObject.get("message");
        
                    User user = new User(username, true);
                    Message message = new Message(user, messageText);
                    messages.add(message);
                }

                messagesItems.addAll(messages);

            } else {
                System.out.println("Error getting chat");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }

    }

    @FXML
    private void refresh(ActionEvent event) {
        System.out.println("Updating...");
        getChats();
        getUsers();
        getRooms();
        getMessages();
        
        for (Chat chat : chats) {
            if (chat.getGroupname().equals(activeChat.getGroupname())) {
                activeChat = chat;
                break;
            }
        }

        updateActiveChat(activeChat);
        updateLists();
        updateMessages();
    }
    
    @FXML
    private void viewUsers(ActionEvent event) {
        activeList = ListOptions.USERS;
        getUsers();
        updateLists();
    }

    @FXML
    private void viewChats(ActionEvent event) {
        activeList = ListOptions.CHATS;
        getChats();

        for (Chat chat : chats) {
            if (chat.getGroupname().equals(activeChat.getGroupname())) {
                activeChat = chat;
                break;
            }
        }

        updateActiveChat(activeChat);
        updateLists();
    }

    @FXML
    private void viewRooms(ActionEvent event) {
        activeList = ListOptions.ROOMS;
        getRooms();
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
                        getChats();
                        updateLists();
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
        
        getChats();
        updateLists();
    }
    
    @FXML
    private void sendMessage(ActionEvent event) throws IOException, ParseException {
        String message = txtMessage.getText();

        if (message != ""){
            try {
                Client client = new Client();
                char response = client.sendMessage(activeUser.getUsername(), activeChat.getGroupname(), message);
            
                if (response == '1') {
                    txtMessage.setText("");
                    updateMessages();
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
