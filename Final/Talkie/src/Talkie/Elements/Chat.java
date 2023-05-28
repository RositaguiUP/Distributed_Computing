package Talkie.Elements;

public class Chat {
    private String groupname;
    private int totalUsers;
    private int totalMessages;
    private boolean isAdmin;
    private boolean newMessages;

    // Groups / Chats where the user belongs
    public Chat(String groupname, int totalUsers, int totalMessages, boolean isAdmin) {
        this.groupname = groupname;
        this.totalUsers = totalUsers;
        this.totalMessages = totalMessages;
        this.isAdmin = isAdmin;
        this.newMessages = false;
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

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
