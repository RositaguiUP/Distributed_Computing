package Talkie.Elements;

import java.util.ArrayList;

public class Group {
    private String groupname;
    private ArrayList<User> users;
    private ArrayList<Message> messages;
    private boolean newMessages;
    private boolean waitingEnter;

    public Group(String groupname, ArrayList<User> users, ArrayList<Message> messages, boolean newMessages) {
        this.groupname = groupname;
        this.users = users;
        this.messages = messages;
        this.newMessages = newMessages;
        this.waitingEnter = false;
    }

    public Group(String groupname, ArrayList<User> users, boolean waitingEnter) {
        this.groupname = groupname;
        this.users = users;
        this.messages = new ArrayList<>();
        this.newMessages = false;
        this.waitingEnter =  waitingEnter;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public boolean isNewMessages() {
        return newMessages;
    }

    public void setNewMessages(boolean newMessages) {
        this.newMessages = newMessages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
    
    

    public boolean isWaitingEnter() {
        return waitingEnter;
    }

    public void setWaitingEnter(boolean waitingEnter) {
        this.waitingEnter = waitingEnter;
    }
}
