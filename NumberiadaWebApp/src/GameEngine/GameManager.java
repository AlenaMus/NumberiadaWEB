package GameEngine;

import GameEngine.gameObjects.Game;
import GameEngine.gameObjects.Player;
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
    public static boolean gameExists = false;
    private GameLogic gameLogic = null;
    private String gameTitle = "";
    public GameLogic getGameLogic(){return gameLogic;}

    public boolean SignToGame(String gameTitle,Player player)
    {
        boolean signed = false;
        if((!gameLogic.getPlayers().contains(player)) && (gameLogic.getPlayers().size() < gameLogic.getNumOfPlayers())){
            gameLogic.getPlayers().add(player);
            signed = true;
        }
        return signed;
    }

  public Game getNewGameData(String editorName){
      Game game = new Game(editorName,gameLogic.getGameTitle(),gameLogic.getNumOfPlayers(),gameLogic.getGameBoard(),AppManager.numOfGame);
      AppManager.AddNewGameInfo(game);

      return game;
  }


    public void addPlayer(String playerName,String type){

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
        }
        catch (JAXBException e) {

            ValidationResult validationResult = new ValidationResult();
            validationResult.add(String.format("JAXBException: file %1$s xml is not in a valid GameDescriptor schema", "file"));
            throw new XmlNotValidException(validationResult);
        }

        catch (SAXException e) {
            ValidationResult validationResult = new ValidationResult();
            validationResult.add(String.format("SAXException: file %1$s xml is not in a valid GameDescriptor schema", "file"));
            throw new XmlNotValidException(validationResult);
        }

    }

    public void LoadGameFromXmlAndValidate(InputStream file) throws XmlNotValidException
    {
        String gameType ="";
        GameDescriptor loadedGame;

        try{
            loadedGame = loadGameFromFile(file);
        }
        catch (XmlNotValidException ex)
        {
            throw new XmlNotValidException(ex.getValidationResult());
        }

        if(loadedGame!= null) {

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
            if(!AppManager.ExistsGame(gameTitle)){
                gameExists = false;
                gameLogic.initGame();
                gameLogic.setGameType(eGameType.valueOf(gameType));
                gameLogic.checkXMLData(loadedGame);
                gameLogic.loadDataFromJaxbToGame(loadedGame,gameType);
                AppManager.AddNewGame(gameLogic,gameTitle);

            }else{
                gameExists = true;
            }


        }
    }






    //    void StartGameButtonClicked(ActionEvent event) {
//
//        builder.removeButtonsEffect();
//        if(GameManager.gameRound > 0){
//                restartGame();
//            }else {
//                logic.setHistoryMoves();
//            }
//
//        MakeAMoveButton.getStyleClass().clear();
//        LeaveGameButton.disableProperty().setValue(false);
//        LoadXmlFileButton.disableProperty().setValue(true);
//        MakeAMoveButton.disableProperty().setValue(false);
//        StartGameButton.disableProperty().setValue(true);
//
//        setStartGame();
//        GameManager.gameRound++;
//        if (logic.getGameType().equals(eGameType.Advance)) {
//            if (!logic.InitMoveCheck()) //first player check
//            {
//                noPossibleMovesAlert();
//                findPlayerToNextMove();
//
//            } else {
//                if (logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))) {
//                    makeComputerMove();
//                }
//            }
//        }else{
//            if (!logic.InitMoveCheck())
//            {
//                setGameOver();
//            }
//        }
//    }
//
//



    //    private void makeComputerMove() {
//
//        ComputerProgressBar.visibleProperty().set(true);
//        ComputerThinkingLabel.visibleProperty().set(true);
//        Task<Point> moveTask = new Task<Point>() {
//            Point squareLocation = null;
//
//
//            @Override
//            protected Point call() throws Exception {
//                int i;
//
//                squareLocation = logic.makeComputerMove();
//
//                for(i=1;i < 10; i++){
//                    updateProgress(i,10);
//                    Thread.sleep(100);
//                }
//                    Thread.sleep(300);
//
//                return squareLocation;
//            }
//            @Override
//            protected void updateProgress(double workTodo,double max){
//                updateMessage("Computer thinking...");
//                super.updateProgress(workTodo,max);
//            }
//        };
//
//        moveTask.setOnSucceeded(t -> {
//
//            Point chosenPoint = moveTask.getValue();
//                 ComputerThinkingLabel.textProperty().unbind();
//                 ComputerProgressBar.visibleProperty().set(false);
//                 ComputerThinkingLabel.visibleProperty().set(false);
//                 System.out.println("Updating Board!");
//                 logic.updateDataMove(chosenPoint);
//                 System.out.println("Finding next player after computer");
//                 findPlayerToNextMove();
//        });
//
//        moveTask.setOnFailed(t -> {
//            System.out.println("Failed to perform task !!");
//            ComputerThinkingLabel.textProperty().unbind();
//            ComputerProgressBar.visibleProperty().set(false);
//            ComputerThinkingLabel.visibleProperty().set(false);
//            System.out.println("Finding next player after computer");
//            findPlayerToNextMove();
//        });
//
//        ComputerProgressBar.progressProperty().bind(moveTask.progressProperty());
//        ComputerThinkingLabel.textProperty().bind(moveTask.messageProperty());
//
//        moveTask.valueProperty().addListener((observable, oldValue, newValue) ->  {
//            System.out.println(String.format("Value returned is %d %d",newValue.getRow(),newValue.getCol()));});
//        Thread move = new Thread(moveTask);
//        move.start();
//
//    }
//




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

// private String setGameOver()
//    {
//        String winnerMessage = logic.getWinner(); //BRING A STRING WITH WINNER NAMES
//        String statistics = logic.gameOver(); //BRING STRING WITH OTHER PLAYER SCORE
   //NEED TO UPDATE CLIENTS WITH THOSE STRINGS !
//       // Alert alert = new Alert(Alert.AlertType.INFORMATION);
//       // alert.setTitle("GAME OVER !!!");
//        //alert.setHeaderText(winnerMessage);
//        //alert.setContentText(String.join(System.lineSeparator(),statistics));
//        // alert.showAndWait();
//
//        //LoadXmlFileButton.disableProperty().setValue(false);
//        MakeAMoveButton.disableProperty().setValue(true);
//        LeaveGameButton.disableProperty().setValue(true);
//        StartGameButton.disableProperty().setValue(false);
//
//        clearGameWindow();
//        //enableHistoryView();
//    return (winnerMessage + statistics)
//    }

//    private String findPlayerToNextMove() {
//        String noMovesString = "" ;
//        if (!logic.isGameOver()) {
//            boolean hasMove = logic.switchPlayer();
//           // setCurrentPlayer(logic.getCurrentPlayer());
//            while (!hasMove) {
//                 noMovesString = noMovesString + "/n no possible moves for user"  + logic.getCurrentPlayer();
//                  hasMove = logic.switchPlayer();
//                //setCurrentPlayer(logic.getCurrentPlayer());
//            }
//            if(logic.getCurrentPlayer().getPlayerType().equals(String.valueOf(ePlayerType.Computer))){
//                makeComputerMove(); // I THINK NEED TO SPERATE (THIS IS ANOTHER MOVE)
//            }
//        } else {
//            setGameOver();
//        }
//        return noMovesString;
//
//    }
//
//    private String MoveAdvanceMove()
//    {
//        int pointStatus;
//        String value="";
//        String errorString = "";
//        //Point userPoint = builder.getChosenPoint();
//        if (userPoint != null) {
//            pointStatus = logic.isValidPoint(userPoint);
//                if (pointStatus == GameLogic.GOOD_POINT) {
//                    logic.updateDataMove(userPoint); // NEED TO UPDATE CLIENT WITH BOARD CHANGE
//                    errorString = findPlayerToNextMove(); //I THINK NEED TO SEPERATE
//            }
//            else if (pointStatus == GameLogic.NOT_IN_MARKER_ROW_AND_COLUMN)
//            {
//               // value = logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].getValue();
//               // logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].setValue(value);
//                errorString = "You choose illegal square -the square needs to be in the marker raw or column";
//            }
//            else if (pointStatus == GameLogic.NOT_PLAYER_COLOR)
//            {
//                //value = logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].getValue();
//                //logic.getGameBoard().getGameBoard()[userPoint.getRow()][userPoint.getCol()].setValue(value);
//                errorString = ("You choose illegal square - the square is not in your color!");
//            }
//        }
//        else
//        {
//            errorString = ("YOU DIDN'T CHOOSE A SQUARE!");
//        }
//
//        return errorString;
//    }

//

   // private void AdvanceRetire()
//    {
//        logic.playerRetire();
//        if(!GameLogic.isEndOfGame){
    //NEED TO UPDATE PLAYER LIST IN CLIENTS AND BOARD!
//            //builder.clearPlayersScoreView(PlayerScoreGridPane);
//            //builder.setPlayersScore(PlayerScoreGridPane,logic.getPlayers());
//            findPlayerToNextMove();
//        }else{
//            setGameOver();
//        }
//    }



}