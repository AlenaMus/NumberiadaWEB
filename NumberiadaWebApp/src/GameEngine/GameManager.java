package GameEngine;

import GameEngine.gameObjects.Game;
import GameEngine.gameObjects.Player;
import GameEngine.gameObjects.Point;
import GameEngine.gameObjects.ePlayerType;
import GameEngine.jaxb.schema.generated.GameDescriptor;
import GameEngine.logic.AdvancedGame;
import GameEngine.logic.BasicGame;
import GameEngine.logic.GameLogic;
import GameEngine.logic.eGameType;
import GameEngine.validation.ValidationResult;
import GameEngine.validation.XmlNotValidException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;


public class GameManager {

    public static int gameRound = 0;
    public boolean runningGame = false;
    public static boolean gameExists = false;
    private GameLogic gameLogic = null;
    private String gameTitle = "";
    private int gameVersion = 0;
    private int gameNumber = 0;
    private int[] playersVersions;

    public GameLogic getGameLogic() {
        return gameLogic;
    }


    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getGameNumber() {
        return this.gameNumber;
    }

    public int getGameVersion() {
        return gameVersion;
    }

    public void updateGameVersion() {
        this.gameVersion++;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public boolean SignToGame(String gameTitle, Player player) {
        boolean signed = false;
        if ((!gameLogic.getPlayers().contains(player)) && (gameLogic.getPlayers().size() < gameLogic.getNumOfPlayers())) {
            gameLogic.getPlayers().add(player);
            signed = true;
        }
        return signed;
    }

    public Game getNewGameData(String editorName) {
        Game game = new Game(editorName, gameLogic.getGameTitle(), gameLogic.getNumOfPlayers(), gameLogic.getGameBoard(), AppManager.numOfGame);
        AppManager.AddNewGameInfo(game);

        return game;
    }


    public GameDescriptor loadGameFromFile(InputStream file) throws XmlNotValidException {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xmlFileInputStream = BasicGame.class.getResourceAsStream("/GameEngine/xmlResources/Numberiada.xsd");
            Source schemaSource = new StreamSource(xmlFileInputStream);
            Schema schema = schemaFactory.newSchema(schemaSource);
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            GameDescriptor JaxbGame;
            JaxbGame = (GameDescriptor) unmarshaller.unmarshal(file);
            return JaxbGame;
        } catch (JAXBException e) {

            ValidationResult validationResult = new ValidationResult();
            validationResult.add(String.format("JAXBException: file %1$s xml is not in a valid GameDescriptor schema", "file"));
            throw new XmlNotValidException(validationResult);
        } catch (SAXException e) {
            ValidationResult validationResult = new ValidationResult();
            validationResult.add(String.format("SAXException: file %1$s xml is not in a valid GameDescriptor schema", "file"));
            throw new XmlNotValidException(validationResult);
        }

    }

    public void LoadGameFromXmlAndValidate(InputStream file) throws XmlNotValidException {
        String gameType = "";
        GameDescriptor loadedGame;

        try {
            loadedGame = loadGameFromFile(file);
        } catch (XmlNotValidException ex) {
            throw new XmlNotValidException(ex.getValidationResult());
        }

        if (loadedGame != null) {

            gameType = loadedGame.getGameType();

            //   if(gameRound == 0) {

            if (gameType.equals(String.valueOf(eGameType.AdvanceDynamic))) {
                gameLogic = new AdvancedGame();
            } else {
                ValidationResult valid = new ValidationResult();
                valid.add("Invalid Game Type - game type must be AdvancedDynamic!/n");
                throw new XmlNotValidException(valid);
            }
            // }

            gameTitle = loadedGame.getDynamicPlayers().getGameTitle();
            if (!AppManager.ExistsGame(gameTitle)) {
                gameExists = false;
                gameLogic.initGame();
                gameLogic.setGameType(eGameType.valueOf(gameType));
                gameLogic.checkXMLData(loadedGame);
                gameLogic.loadDataFromJaxbToGame(loadedGame, gameType);

                // AppManager.AddNewGame(this,gameTitle);

            } else {
                gameExists = true;
            }


        }
    }

    public void initPlayersGameVersions(){

        int size = gameLogic.getNumOfPlayers();
        for(int i = 0 ;i < size; i++){
           gameLogic.getPlayers().get(i).setPlayerIndex(0);
        }
    }


    public void updatePlayerVersion(int playerIndex){
        gameLogic.getPlayers().get(playerIndex).setPlayerVersion(this.gameVersion);
    }


    public boolean checkGamePlayersVersionUpToDate(){

        for(int i=0;i< gameLogic.getNumOfPlayers(); i++)
        {
            if(gameLogic.getPlayers().get(i).getPlayerVersion() != this.gameVersion){
                return false;
            }
        }
        return true;
    }

    public String findFirstPlayerToMove() {
        runningGame = true;
        String message = "";

        //AppManager.setPlayersGameVersionList(gameNumber, getGameLogic().getPlayers());
//        if(GameManager.gameRound > 0){
//                restartGame();
//            }else {
//                logic.setHistoryMoves();
//            }
//
        GameManager.gameRound++;
        gameLogic.setFirstPlayer();
        if (!gameLogic.InitMoveCheck()) //first player check
        {
            message = "No Possible moves for current player";
            message += findPlayerToNextMove();
        }
        return message;
    }


    public String AdvanceRetire() {
        String returnedString = "";
        getGameLogic().playerRetire();
        if (!GameLogic.isEndOfGame) {

            //NEED TO UPDATE PLAYER LIST IN CLIENTS AND BOARD!
            //builder.clearPlayersScoreView(PlayerScoreGridPane);
            //builder.setPlayersScore(PlayerScoreGridPane,logic.getPlayers());

            returnedString = findPlayerToNextMove();
        } else {//Gameover
            // returnedString = setGameOver();
        }
        return returnedString;
    }


    public void makeComputerMove() {

        //ComputerProgressBar.visibleProperty().set(true);
        //ComputerThinkingLabel.visibleProperty().set(true);
        //Task<Point> moveTask = new Task<Point>() {
        Point squareLocation = null;


        // @Override
        // protected Point call() throws Exception {
        //    int i;

        squareLocation = getGameLogic().makeComputerMove();

           /*     for(i=1;i < 10; i++){
                    updateProgress(i,10);
                    Thread.sleep(100);
                }
                    Thread.sleep(300);
*/
        //return squareLocation;
        //}
        /*    @Override
            protected void updateProgress(double workTodo,double max){
                updateMessage("Computer thinking...");
                super.updateProgress(workTodo,max);
            }
        };*/

       /* moveTask.setOnSucceeded(t -> {

            Point chosenPoint = moveTask.getValue();
                 ComputerThinkingLabel.textProperty().unbind();
                 ComputerProgressBar.visibleProperty().set(false);
                 ComputerThinkingLabel.visibleProperty().set(false);
                 System.out.println("Updating Board!");*/
        getGameLogic().updateDataMove(squareLocation);
        //  System.out.println("Finding next player after computer");
        findPlayerToNextMove();


       /* moveTask.setOnFailed(t -> {
            System.out.println("Failed to perform task !!");
            ComputerThinkingLabel.textProperty().unbind();
            ComputerProgressBar.visibleProperty().set(false);
            ComputerThinkingLabel.visibleProperty().set(false);
            System.out.println("Finding next player after computer");
            findPlayerToNextMove();
        });

        ComputerProgressBar.progressProperty().bind(moveTask.progressProperty());
        ComputerThinkingLabel.textProperty().bind(moveTask.messageProperty());

        moveTask.valueProperty().addListener((observable, oldValue, newValue) ->  {
            System.out.println(String.format("Value returned is %d %d",newValue.getRow(),newValue.getCol()));});
        Thread move = new Thread(moveTask);
        move.start();*/

    }


    // private void ExitGameButtonClicked(ActionEvent event){
//        Alert exitWindow = new Alert(Alert.AlertType.CONFIRMATION);
//        exitWindow.setTitle("Exit Game");
//        exitWindow.setHeaderText("Do you want to exit Numberiada Game?");
//
//        Optional<ButtonType> result = exitWindow.showAndWait();
//        if( result.get() == ButtonType.OK){
////            if(logic.getHistoryMoves()!=null){
////                logic.clearHistory();
////            }
//            Platform.exit();
//        }else{
//            exitWindow.hide();
//        }
//    }

    public String setGameOver() {
        String winnerMessage = getGameLogic().getWinner(); //BRING A STRING WITH WINNER NAMES
        String statistics = getGameLogic().gameOver(); //BRING STRING WITH OTHER PLAYER SCORE
        //NEED TO UPDATE CLIENTS WITH THOSE STRINGS !
        //clearGameWindow();
        //enableHistoryView();
        return (winnerMessage + statistics);
    }

    private String findPlayerToNextMove() { //return next player who have move
        String returnedString = " ";
        if (!getGameLogic().isGameOver()) {
            boolean hasMove = getGameLogic().switchPlayer();
            // setCurrentPlayer(logic.getCurrentPlayer());
            while (!hasMove) {
                returnedString = returnedString + "/n no possible moves for user" + getGameLogic().getCurrentPlayer();
                hasMove = getGameLogic().switchPlayer();
                //setCurrentPlayer(logic.getCurrentPlayer());
            }}
           /* if(logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))){
                makeComputerMove(); // I THINK NEED TO SPERATE (THIS IS ANOTHER MOVE) *NEW*THINK WE GET THIS IN INTERVAL
            }*/
//        } else {
//            returnedString = setGameOver();
//        }
        return returnedString;
    }

    public boolean isComputerTurn() {
        boolean isComputerTurn = false;
        if (getGameLogic().getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer)))
            return isComputerTurn = true;
        return isComputerTurn;
    }

    public String MoveAdvanceMove(Point userPoint) //needs to get user point from client
    {
        int pointStatus;
        String value = "";
        String errorString = "";
        if (userPoint != null) {
            pointStatus = getGameLogic().isValidPoint(userPoint);
            if (pointStatus == GameLogic.GOOD_POINT) {
                getGameLogic().updateDataMove(userPoint); // NEED TO UPDATE CLIENT WITH BOARD CHANGE
                errorString = findPlayerToNextMove(); //I THINK NEED TO SEPERATE (*new* seems ok china the same)
            } else if (pointStatus == GameLogic.NOT_IN_MARKER_ROW_AND_COLUMN) {
                // value = logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].getValue();
                // logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].setValue(value);
                errorString = "You choose illegal square -the square needs to be in the marker raw or column";
            } else if (pointStatus == GameLogic.NOT_PLAYER_COLOR) {
                //value = logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].getValue();
                //logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].setValue(value);
                errorString = ("You choose illegal square - the square is not in your color!");
            }
        } else {
            errorString = ("YOU DIDN'T CHOOSE A SQUARE!");
        }

        return errorString;
    }
}





