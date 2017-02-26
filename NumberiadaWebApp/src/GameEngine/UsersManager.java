package GameEngine;

import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.ePlayerType;
import GameEngine.logic.GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UsersManager {
    
    private final List<String> players;
    public Map<String,Player> users;

    public UsersManager() {
        players = new ArrayList<String>();
        users = new HashMap<>();
    }
    
    public void addUser(String name) {
        players.add(name);
    }

    public void addPlayer(String userName,String type){

        users.put(userName,new Player(userName, ePlayerType.valueOf(type)));
    }

    public Player getPlayer(String Name){
        return users.get(Name);
    }

    public void removePlayer(String name){
        users.remove(name);
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
