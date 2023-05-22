package Talkie.Elements;

import java.util.ArrayList;

public class Group {
    private String groupname;
    private ArrayList<User> users;
    private ArrayList<Message> messages;

    public Group(String groupname, ArrayList<User> users, ArrayList<Message> messages) {
        this.groupname = groupname;
        this.users = users;
        this.messages = messages;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
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
    
}
