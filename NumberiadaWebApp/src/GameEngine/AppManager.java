package GameEngine;


import GameEngine.gameObjects.Game;
import GameEngine.gameObjects.Player;
import GameEngine.logic.GameLogic;
import GameEngine.validation.XmlNotValidException;

import java.io.InputStream;
import java.util.*;

public final class AppManager {

    private GameManager gameManager;

    public static Map<String,GameLogic> games = new HashMap<>();
    public static Map<String,Game> gamesInfo = new HashMap<>();
    public static UsersManager userManager = new UsersManager();
    public static int numOfGame = 0;

    private AppManager(){
        gameManager = new GameManager();
    }


    public static void AddNewGameInfo(Game game){

        gamesInfo.put(game.getGameTitle(),game);
    }
    public static void AddNewGame(GameLogic game,String title){

        games.put(title,game);
        numOfGame++;
    }

    public void CreateNewGame(Player user,InputStream filePath)throws XmlNotValidException{
        // gameManager.LoadGameFromXmlAndValidate(filePath);
         //games.put(user.getName(), gameManager.getGameLogic());

}

    public static boolean SignToGame(String gameTitle,Player player)
    {
        boolean signed = false;
        GameLogic game = games.get(gameTitle);
        if((!game.getPlayers().contains(player)) && (game.getPlayers().size() < game.getNumOfPlayers())){
            game.getPlayers().add(player);
            signed = true;
        }
        return signed;
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
        games.get(gameTitle).startGame();
    }


}
