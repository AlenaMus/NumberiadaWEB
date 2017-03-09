package GameEngine;


import GameEngine.gameObjects.Game;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.ePlayerType;
import GameEngine.logic.GameLogic;
import GameEngine.validation.UserMessageConfirmation;
import GameEngine.validation.XmlNotValidException;
import Servlets.SessionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.InputStream;
import java.util.*;

public final class AppManager {

    private GameManager gameManager;

    public static int signedPlayersVersion =0;
    public static Map<String,GameManager> games = new HashMap<>();
    //public static Map<String,Game> gamesInfo = new HashMap<>();
    public static List<Game> gamesInfo = new ArrayList<>();
    public static UsersManager userManager = new UsersManager();
    public static int numOfGame = 0;
    public static List<ArrayList<Integer>> playersGameVersion = new ArrayList<>();


    public static void initPlayersGameVersions(int gameNumber){ //init gameVersion for all players
       int version = 0;
        for(int i=0; i < playersGameVersion.get(gameNumber).size();i++){
           playersGameVersion.get(gameNumber).set(i,version);
        }
    }

    public static void createPlayersGameVersionList(int gameNumber){ //loadXml
        playersGameVersion.add(gameNumber,new ArrayList<Integer>());
    }

    public static void setPlayersGameVersionList(int gameNumber, List<Player> players){ //start new Game

        for(int i=0; i < players.size();i++){
            playersGameVersion.get(gameNumber).add(players.get(i).getPlayerVersion());
        }
    }

    public static boolean checkGamePlayersVersionUpToDate(int gameNumber,int gameVersion){
        int version;
        for(int i=0; i < playersGameVersion.get(gameNumber).size();i++){
            version = playersGameVersion.get(gameNumber).get(i);
            if(version != gameVersion){
                return false;
            }
        }
        return true;
    }

    public static void updatePlayersVersion(int gameNumber,int index,int gameVersion){
        if(playersGameVersion.get(gameNumber).size() > index){
            playersGameVersion.get(gameNumber).set(index,gameVersion);
        }else{
            playersGameVersion.get(gameNumber).add(gameVersion);
        }

    }


    public static void initGamesVersion(){

        for(int i = 0;i < playersGameVersion.size();i++){
            initPlayersGameVersions(i);
        }
    }


    private AppManager(){
        gameManager = new GameManager();
    }
    public static void AddNewGameInfo(Game game){

        //gamesInfo.put(game.getGameTitle(),game);
        gamesInfo.add(game);
    }
    public static void AddNewGame(GameManager gameManager,String title){

        games.put(title,gameManager);
        gameManager.setGameNumber(numOfGame);
        numOfGame++;

    }

    public static GameLogic getGameForUser(Player player){

       for(GameManager gameManager : games.values()){
           if(gameManager.getGameLogic().getPlayers().contains(player)){
               return gameManager.getGameLogic();
           }
        }
        return null;
    }


    public static UserMessageConfirmation SignToGame(int gameNumber,String gameTitle,String username,Boolean isComputer)
    {
        boolean signed = false;
        int numOfPlayersToGame;
        String message = "";  // can be 1. Success 2.Failed - the game is already full of players 3.the player is already signed to game 4.game is Running
        ePlayerType type = isComputer ? ePlayerType.Computer : ePlayerType.Human;
        Player player = new Player(username,type);
        int playerIndex = 0;
        GameLogic game = games.get(gameTitle).getGameLogic();
        int playerColor = game.getNumOfSignedPlayers();
        player.setColor(playerColor+1);
        if(!games.get(gameTitle).runningGame) {

            if (game.getPlayers().size() < game.getNumOfPlayers()) {
                if (!game.getPlayers().contains(player)) {
                    playerIndex = game.getNumOfSignedPlayers();
                    player.setPlayerIndex(playerIndex);
                    game.getPlayers().add(player);
                    gamesInfo.get(gameNumber-1).updateSignedPlayers();
                    game.setNumOfSignedPlayers(game.getNumOfSignedPlayers() + 1);
                    signedPlayersVersion++;
                    signed = true;

                } else {

                      message = String.format("User %s is already signed to %s game !", username, gameTitle);
                }
            } else {

                     message = String.format("Game %s if full of players, try another time !", gameTitle);
            }
        }else{
                    message = String.format("Game %s if already started, try another time !", gameTitle);
        }

        numOfPlayersToGame = gamesInfo.get(gameNumber-1).getSignedPlayers();
        return new UserMessageConfirmation(signed,message,numOfPlayersToGame,playerIndex);
    }


    public static boolean ExistsGame(String gameTitle){
        boolean exists = false;
        if(games.containsKey(gameTitle)){
            exists = true;
        }
       return exists;
    }

    public void LeaveGame(Player player,String gameTitle)
    {

    }

    public void DeleteGame(String name){
        games.remove(name);
    }

    public void StartGame(String gameTitle){
        games.get(gameTitle).getGameLogic().startGame();
    }


}
