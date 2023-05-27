package Talkie.Elements;

public class User {
    private String username;
    private boolean belongGroup;
    private boolean requestingEnter;

    public User(String username, boolean belongGroup) {
        this.username = username;
        this.belongGroup = belongGroup;
        this.requestingEnter = false;
    }

    public User(String username, boolean belongGroup, boolean requestingEnter) {
        this.username = username;
        this.belongGroup = belongGroup;
        this.requestingEnter = requestingEnter;
    }

    public String getUsername() {
        return username;
    }

    public boolean isBelongGroup() {
        return belongGroup;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setBelongGroup(boolean belongGroup) {
        this.belongGroup = belongGroup;
    }

    public boolean isRequestingEnter() {
        return requestingEnter;
    }

    public void setRequestingEnter(boolean requestingEnter) {
        this.requestingEnter = requestingEnter;
    }
}
