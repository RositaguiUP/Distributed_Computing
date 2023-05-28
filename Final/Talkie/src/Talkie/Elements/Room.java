package Talkie.Elements;


public class Room {
    private String groupname;
    private boolean waitingEnter;

    // Groups / rooms where the user doesn't belong
    public Room(String groupname, boolean waitingEnter) {
        this.groupname = groupname;
        this.waitingEnter =  waitingEnter;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public boolean isWaitingEnter() {
        return waitingEnter;
    }

    public void setWaitingEnter(boolean waitingEnter) {
        this.waitingEnter = waitingEnter;
    }
}
