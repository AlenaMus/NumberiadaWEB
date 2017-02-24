package GameEngine;

import java.util.ArrayList;
import java.util.List;


public class UsersManager {
    
    private final List<String> players ;
    
    public UsersManager() { players = new ArrayList<String>() ;}
    
    public void addUser(String name) {
        players.add(name);
    }
    
    public void removeUser(String name) {
        players.remove(name);
    }
    
    public List<String> getUsers() {
        return players;
    }
    
    public boolean isUserExists(String username) {
        return players.contains(username);
    }

    public void RenameUsersName(String nameToDelete, String NewUserName) {
        removeUser(nameToDelete);
        addUser(NewUserName);
    }
}
