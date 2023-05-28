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
    private Label lblWlcm;
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
    private ArrayList<Chat> chats;
    private ObservableList<User> usersItems;
    private ObservableList<Chat> chatsItems;
    private ObservableList<Room> roomsItems;
    private ObservableList<Message> messagesItems;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    

    public ArrayList<Chat> getArrayChats() {
        return chats;
    }



    public User getActiveUser() {
        return activeUser;
    }

    public Chat getActiveChat() {
        return activeChat;
    }

    public void initData(User user) throws IOException{
        usersItems = FXCollections.observableArrayList();
        chatsItems = FXCollections.observableArrayList();
        roomsItems = FXCollections.observableArrayList();
        messagesItems = FXCollections.observableArrayList();

        activeUser = user;
        lblWlcm.setText("Welcome to Talkie " + activeUser.getUsername() + "!");

        activeList = ListOptions.CHATS;
        getChats();

        if (chats.isEmpty()) {
            updateActiveChat(null);
        } else {
            updateActiveChat(chats.get(0));
        }
        updateLists();
    }

    public void updateActiveChat(Chat chat) {
        if (chat != null) {
            activeChat = chat;
            lblActChat.setText(activeChat.getGroupname());

            // Enable option to delete group
            if (activeChat.getTotalUsers() == 1 && activeChat.isAdmin()) {
                btnDelete.setVisible(true);
            } else {
                btnDelete.setVisible(false);
            }
            updateMessages();
        } else {
            lblActChat.setText("No chats yet!");
            btnDelete.setVisible(false);
        }
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
                objectItemsChats.clear();
                objectItemsChats.addAll(chatsItems);
                lstView.setItems(objectItemsChats);
                lstView.refresh();

                // Select the activeChat if it exists in the chats list
                if (activeChat != null && chats.contains(activeChat)) {
                    lstView.getSelectionModel().select(activeChat);
                } else {
                    System.out.println("Not active chat found in chats!!!");
                }

                break;
            case USERS:
                lstView.setCellFactory((lv) -> {
                    return UserCell.newInstance(this);
                });

                ObservableList<Object> objectItemsUsers = FXCollections.observableArrayList();
                objectItemsUsers.clear();
                objectItemsUsers.addAll(usersItems);
                lstView.setItems(objectItemsUsers);
                lstView.refresh();
                break;
            case ROOMS:
                lstView.setCellFactory((lv) -> {
                    return RoomCell.newInstance(this);
                });

                ObservableList<Object> objectItemsRooms = FXCollections.observableArrayList();
                objectItemsRooms.clear();
                objectItemsRooms.addAll(roomsItems);
                lstView.setItems(objectItemsRooms);
                //lstView.setItems(roomsItems);
                lstView.refresh();
                break;
        }
    }

    public void getUsers() {
        ArrayList<User> users = new ArrayList<>();
        usersItems.clear();

        try {
            Client client = new Client();
            JSONObject responseJson = client.getUsers(activeUser.getUsername(), activeChat.getGroupname(), activeChat.isAdmin());
            
            JSONArray arrayJson = (JSONArray) responseJson.get("users");

            for (Object obj : arrayJson) {
                JSONObject chatObject = (JSONObject) obj;
                String userName = (String) chatObject.get("username");

                if (!userName.equals(activeUser.getUsername())) {
                    User user = null;
                    if (activeChat.isAdmin()) {
                        long belongGroupLong = (long) chatObject.get("belongGroup");
                        boolean belongGroup = belongGroupLong == 1L;
                        long requestEnterLong = (long) chatObject.get("requestEnter");
                        boolean requestEnter = requestEnterLong == 1L;
                        user = new User(userName, belongGroup, requestEnter);
                    } else {
                        user = new User(userName, true);
                    }
                    users.add(user);
                }
            }

            usersItems.addAll(users);
            System.out.println("Got users");
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }

    private void getChats() {
        chats = new ArrayList<>();
        chatsItems.clear();

        try {
            Client client = new Client();
            JSONObject responseJson = client.getChats(activeUser.getUsername());
            
            JSONArray arrayJson = (JSONArray) responseJson.get("groups");

            for (Object obj : arrayJson) {
                JSONObject chatObject = (JSONObject) obj;
                String groupName = (String) chatObject.get("groupName");
                long totUsers = (long) chatObject.get("totUsers");
                long totMsg = (long) chatObject.get("totMsg");
                long isAdminLong = (long) chatObject.get("isAdmin");
                boolean isAdmin = isAdminLong == 1L;

                Chat chat = new Chat(groupName, (int) totUsers, (int) totMsg, isAdmin);
                chats.add(chat);
            }

            chatsItems.addAll(chats);
            System.out.println("Got chats");
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }

    private void getRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        roomsItems.clear();

        try {
            Client client = new Client();
            JSONObject responseJson = client.getRooms(activeUser.getUsername());
            
            JSONArray arrayJson = new JSONArray();
            arrayJson.clear();
            arrayJson = (JSONArray) responseJson.get("groups");

            for (Object obj : arrayJson) {
                JSONObject chatObject = (JSONObject) obj;
                String groupName = (String) chatObject.get("groupName");
                long isWaitingLong = (long) chatObject.get("userWaiting");
                boolean isWaiting = isWaitingLong == 1L;
                
                Room room = new Room(groupName, isWaiting);
                rooms.add(room);
            }
            roomsItems.addAll(rooms);
            System.out.println("Got rooms");
        } catch (Exception e) {
            System.out.println("Server not available");
        }
    }

    private void getMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        messagesItems.clear();

        try {
            Client client = new Client();
            JSONObject responseJson = client.getChat(activeUser.getUsername(), activeChat.getGroupname());
            
            char response = ((String) responseJson.get("result")).charAt(0);
        
            if (response == '1') {
                JSONArray arrayJson = (JSONArray) responseJson.get("chats");

                for (Object obj : arrayJson) {
                    JSONObject msgObject = (JSONObject) obj;
                    String username = (String) msgObject.get("user");
                    String messageText = (String) msgObject.get("message");
        
                    User user = new User(username, true);
                    Message message = new Message(user, messageText);
                    messages.add(message);
                }

                messagesItems.addAll(messages);

            } else {
                System.out.println("Error getting messages");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }

    }

    @FXML
    private void refresh(ActionEvent event) {
        getChats();
        getUsers();
        getRooms();
        
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
        if (activeChat != null) {
            activeList = ListOptions.CHATS;
            getChats();

            if (chats.isEmpty()) {
                updateActiveChat(null);
            } else {
                for (Chat chat : chats) {
                    if (chat.getGroupname().equals(activeChat.getGroupname())) {
                        activeChat = chat;
                        break;
                    }
                }
                updateActiveChat(chats.get(0));
            }
    
            updateActiveChat(activeChat);
            updateLists();
        }
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
        try {
            Client client = new Client();
            char response = client.deleteGroup(activeChat.getGroupname());
        
            if (response == '1') {
                getChats();
                if (chats.isEmpty()) {
                    updateActiveChat(null);
                } else {
                    updateActiveChat(chats.get(0));
                }
                updateLists();
                System.out.println("Deleting group...");
            } else {
                System.out.println("Error sending message");
            }
        } catch (Exception e) {
            System.out.println("Server not available");
        }
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
                } else {
                    System.out.println("Error sending message");
                }
            } catch (Exception e) {
                System.out.println("Server not available");
            }
        }
    }
}
