package GameEngine;


import GameEngine.gameObjects.Game;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.ePlayerType;
import GameEngine.logic.GameLogic;
import GameEngine.validation.UserMessageConfirmation;
import GameEngine.validation.XmlNotValidException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppManager {

    private GameManager gameManager;

    public static int signedPlayersVersion =0;
    public static Map<String,GameManager> games = new HashMap<>();
    //public static Map<String,Game> gamesInfo = new HashMap<>();
    public static List<Game> gamesInfo = new ArrayList<>();
    public static UsersManager userManager = new UsersManager();
    public static int numOfGame = 0;

    private AppManager(){
        gameManager = new GameManager();
    }


    public static void AddNewGameInfo(Game game){

        //gamesInfo.put(game.getGameTitle(),game);
        gamesInfo.add(game);
    }
    public static void AddNewGame(GameManager gameManager,String title){

        games.put(title,gameManager);
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

    public void CreateNewGame(Player user,InputStream filePath)throws XmlNotValidException{
        // gameManager.LoadGameFromXmlAndValidate(filePath);
         //games.put(user.getName(), gameManager.getGameLogic());

}

    public static UserMessageConfirmation SignToGame(int gameNumber,String gameTitle,String username,Boolean isComputer)
    {
        boolean signed = false;
        int numOfPlayersToGame;
        String message = "";  // can be 1. Success 2.Failed - the game is already full of players 3.the player is already signed to game 4.game is Running
        ePlayerType type = isComputer ? ePlayerType.Computer : ePlayerType.Human;
        Player player = new Player(username,type);
        GameLogic game = games.get(gameTitle).getGameLogic();

        if(!games.get(gameTitle).runningGame) {

            if (game.getPlayers().size() < game.getNumOfPlayers()) {
                if (!game.getPlayers().contains(player)) {
                    game.getPlayers().add(player);
                    System.out.println(player.toString());
                    gamesInfo.get(gameNumber-1).updateSignedPlayers();
                    game.setNumOfSignedPlayers(game.getNumOfSignedPlayers() + 1);
                    signedPlayersVersion++;
                    System.out.println(signedPlayersVersion);
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
        return new UserMessageConfirmation(signed,message,numOfPlayersToGame);
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
