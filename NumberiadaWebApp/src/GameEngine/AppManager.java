package GameEngine;


import GameEngine.gameObjects.Board;
import GameEngine.gameObjects.Game;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.ePlayerType;
import GameEngine.logic.GameLogic;
import GameEngine.validation.UserMessageConfirmation;

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

   // public static List<ArrayList<Integer>> playersGameVersion = new ArrayList<>();
//    public static void initPlayersGameVersions(int gameNumber){ //init gameVersion for all players
//       int version = 0;
//        for(int i=0; i < playersGameVersion.get(gameNumber).size();i++){
//           playersGameVersion.get(gameNumber).set(i,version);
//        }
//    }
//
//    public static void createPlayersGameVersionList(int gameNumber){ //loadXml
//        playersGameVersion.add(gameNumber,new ArrayList<Integer>());
//    }
//
//    public static void setPlayersGameVersionList(int gameNumber, List<Player> players){ //start new Game
//
//        for(int i=0; i < players.size();i++){
//            playersGameVersion.get(gameNumber).add(players.get(i).getPlayerVersion());
//        }
//    }
//
//    public static boolean checkGamePlayersVersionUpToDate(int gameNumber,int gameVersion){
//
//        int version;
//        for(int i=0; i < playersGameVersion.get(gameNumber).size();i++){
//            version = playersGameVersion.get(gameNumber).get(i);
//            if(version != gameVersion){
//                return false;
//            }
//        }
//        return true;
//    }

//    public static void updatePlayersVersion(int gameNumber,int index,int gameVersion){
//        if(playersGameVersion.get(gameNumber).size() > index){
//            playersGameVersion.get(gameNumber).set(index,gameVersion);
//        }else{
//            playersGameVersion.get(gameNumber).add(gameVersion);
//        }
//
//    }
//
//    public static void initGamesVersion(){
//
//        for(int i = 0;i < playersGameVersion.size();i++){
//            initPlayersGameVersions(i);
//        }
//    }


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
        int playerIndex = 0;
        boolean signed = false;
        int numOfPlayersToGame;
        String message = "";  // can be 1. Success 2.Failed - the game is already full of players 3.the player is already signed to game 4.game is Running
        ePlayerType type ;
        if (isComputer)
            type =  ePlayerType.Computer;
        else
            type= ePlayerType.Human;
        // isComputer ? ePlayerType.Computer : ePlayerType.Human;
        Player player = new Player(username,type);

        if(!IsPlayerAlreadyInGame(player)) {
            GameLogic game = games.get(gameTitle).getGameLogic();
            int playerColor = game.getNumOfSignedPlayers();
            player.setColor(playerColor + 1);
            if (!games.get(gameTitle).runningGame) {
                if (game.getPlayers().size() < game.getNumOfPlayers()) {
                        playerIndex = game.getNumOfSignedPlayers();
                        player.setPlayerIndex(playerIndex);
                        game.getPlayers().add(player);
                        gamesInfo.get(gameNumber - 1).updateSignedPlayers();
                        game.setNumOfSignedPlayers(game.getNumOfSignedPlayers() + 1);
                        signedPlayersVersion++;
                        signed = true;
                        games.get(gameTitle).runningGame = game.getNumOfSignedPlayers() == game.getNumOfPlayers();
                        gamesInfo.get(gameNumber - 1).setRunningGame(games.get(gameTitle).runningGame);

                } else {

                    message = String.format("Game %s if full of players, try another time !", gameTitle);
                }
            } else {
                message = String.format("Game %s if already started, try another time !", gameTitle);
            }
        }else{
             message = String.format("Cannot sign in %s game ! The User %s is already playing another game",gameTitle,username);
        }

        numOfPlayersToGame = gamesInfo.get(gameNumber-1).getSignedPlayers();
        return new UserMessageConfirmation(signed,message,numOfPlayersToGame,playerIndex,games.get(gameTitle).runningGame);
    }


    public static boolean IsPlayerAlreadyInGame(Player player)
    {
        List<Player> gamePlayers;
        for (Map.Entry<String, GameManager> gameManager : games.entrySet())
        {
           gamePlayers = gameManager.getValue().getGameLogic().getPlayers();
            if(gamePlayers.contains(player)){
               return true;
            }
        }
        return false;
    }


    public static boolean ExistsGame(String gameTitle){
        boolean exists = false;
        if(games.containsKey(gameTitle)){
            exists = true;
        }
       return exists;
    }


    public void DeleteGame(String name){
        games.remove(name);
    }

    public void StartGame(String gameTitle){
        games.get(gameTitle).getGameLogic().startGame();
    }


}
