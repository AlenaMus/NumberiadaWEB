package GameEngine.logic;

import GameEngine.gameObjects.*;
import GameEngine.jaxb.schema.generated.GameDescriptor;
import GameEngine.jaxb.schema.generated.Range;
import GameEngine.jaxb.schema.generated.Squares;
import GameEngine.validation.ValidationResult;
import GameEngine.validation.XmlNotValidException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;


public abstract class GameLogic {


    public static final int GOOD_POINT = 1000;
    public static final int NOT_IN_MARKER_ROW_AND_COLUMN = 1001;
    public static final int NOT_PLAYER_COLOR =1002;
    public static final int NOT_IN_MARKER_ROW_BASIC =1003;
    public static final int NOT_IN_MARKER_COL_BASIC =1004;
    public static final int MARKER_SQUARE_BASIC=1005;
    public static final int EMPTY_SQUARE_BASIC=1006;

    public boolean isEndOfGame = false;
    private Board originalBoard;
    protected String gameTitle = "";
    protected IntegerProperty gameMoves = new SimpleIntegerProperty(0);
    public static ValidationResult validationResult  = new ValidationResult();
    protected List<Square> explicitSquares;
    protected List<GameMove> historyMoves;
    protected List<Player>  players;
    protected List<Player> winners;
    protected GameDescriptor loadedGame;
    protected  int numOfPlayers;
    public int numOfSignedPlayers = 0;
    protected Board gameBoard;
    protected Player currentPlayer = new Player();
    private eGameType gameType;
    protected int currentPlayerIndex;
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(Player currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
    public List<GameMove> getHistoryMoves(){return historyMoves;}
    public void setHistoryMoves(){
        historyMoves = new ArrayList<>();
    }
    public GameDescriptor getLoadedGame() {
        return loadedGame;
    }
    public void setLoadedGame(GameDescriptor loadedGame) {
        this.loadedGame = loadedGame;
    }
    public void setGameType(eGameType type){gameType = type;}
    public eGameType getGameType() {
        return gameType;
    }
    public int getNumOfSignedPlayers () {return numOfSignedPlayers;}
    public void setNumOfSignedPlayers(int num) { numOfSignedPlayers = num; }
    public int getNumOfPlayers () {return numOfPlayers;}
    public void setNumOfPlayers(int num) { numOfPlayers = num; }
    public Board getGameBoard() {return gameBoard;}
    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }
    public int getGameMoves() {
        return gameMoves.get();
    }
    public IntegerProperty gameMovesProperty() {
        return gameMoves;
    }
    public void setGameMoves(int gameMoves) {
        this.gameMoves.set(gameMoves);
    }
    public abstract Point makeComputerMove();
    //public abstract String playerRetire();
    public abstract int isValidPoint(Point squareLocation);
    public abstract boolean InitMoveCheck();
    public abstract void FillRandomBoard();
    public abstract void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException;
    public abstract void checkRandomBoardValidity(Range boardRange, int boardSize)throws XmlNotValidException;
    public abstract String gameOver();
    public abstract boolean switchPlayer();
 //  protected void checkAndSetPlayersXML(GameEngine.jaxb.schema.generated.Players players)throws XmlNotValidException{}
    public abstract boolean isGameOver();
    protected List<Square> cellsToUpdate = new ArrayList<>();

    public Board getOriginalBoard(){
        return originalBoard;
    }

public void clearCellsToUpdate(){
     cellsToUpdate.clear();
}
    public String getGameTitle(){
        return gameTitle;
    }

    public void gameLogicClear(){
        players.clear();
        winners.clear();
        explicitSquares.clear();
        gameBoard.clearBoard();
        currentPlayer = null;
        setNumOfPlayers(0);
        setGameMoves(0);
    }

    protected void updateUserData(int squareValue) // in Player?
    {
        int newScore = currentPlayer.getScore()+squareValue;
        currentPlayer.setNumOfMoves(currentPlayer.getNumOfMoves()+1); //maybe do totalmoves var in gameManager
        currentPlayer.scoreProperty().set(newScore);
        //currentPlayer.scoreStringProperty().set(String.valueOf(newScore));
    }

    public List<Player> getPlayers(){return players;}

    public List<Player> getActivePlayers(){
        List<Player> activePlayers = new ArrayList<>();
        for (Player player:players) {
            if(player.isActive()){
                activePlayers.add(player);
            }
        }
        return activePlayers;
    }

//    public void updateHistory(Point chosenSquare){
//        GameMove move = new GameMove(gameType,gameBoard,currentPlayer,players,gameMoves.get());
//        if(chosenSquare!= null){
//            Square chosenSq = new Square(gameBoard.getGameBoard()[chosenSquare.getRow()][chosenSquare.getCol()]);
//            move.setChosenMove(chosenSq);
//        }
//        historyMoves.add(move);
//    }

//    public void clearHistory(){
//        if(historyMoves != null){
//            for (GameMove move:historyMoves) {
//                move.clear();
//            }
//            historyMoves.clear();
//        }
//    }
    public void setFirstPlayer()
    {
        int i=0;
        Player player = players.get(i);
        while(!player.isActive()){
            i++;
            player = players.get(i);
        }
        setCurrentPlayer(player);
        currentPlayerIndex = 0;
    }

    public String getWinner(){
        String winnerMessage = "";
        setWinners();
        if(winners.size()> 1){
            winnerMessage = "It's a TIE!\n The Winners are :\n";
            for (Player player:winners) {
                winnerMessage+=String.format(" %s -> score :%d \n",player.getName(),player.getScore());
            }
        }else{
            for (Player player:winners) {
                if(player!=null){
                    winnerMessage = "The Winner is:\n" +
                            String.format("%s -> score : %d \n", player.getName(), player.getScore());
                    break;
                }
            }
        }
        return winnerMessage;
    }

    public void startGame(){
        //to implement !!
    }

    public void initGame()
    {
        explicitSquares = new ArrayList<Square>();
        players = new ArrayList<>();
        setNumOfPlayers(0);
        winners = new ArrayList<>();
    }

    public void setWinners(){
        int maxScore=-10000;
        for (Player player:players) {
            if(player.isActive()){
                maxScore = player.getScore();
            }
            break;
        }
        for (Player player:players) {
            if(player!=null && player.isActive())
            {
                if(player.getScore()> maxScore){
                    maxScore = player.getScore();
                }
            }
        }
        for (Player player:players) {
            if(player!=null && player.isActive())
            {
                if(player.getScore()== maxScore){
                    winners.add(player);
                }
            }
        }
    }


    public List<Square> getSquaresToUpdate(){
        return cellsToUpdate;
    }

    public void updateDataMove(Point squareLocation){
        int squareValue;
        //updateHistory(squareLocation);
        //gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getRow()].setSetEffect(false);
        squareValue = updateBoard(squareLocation);
        updateUserData(squareValue);
        gameBoard.getMarker().setMarkerLocation(squareLocation.getRow() + 1, squareLocation.getCol() + 1);

    }


    protected void checkBoardXML(GameEngine.jaxb.schema.generated.Board board)throws XmlNotValidException
    {
        eBoardType boardType;
        int size = board.getSize().intValue();

        if((size >= Board.MIN_SIZE )&&(size <= Board.MAX_SIZE)) {
            boardType = eBoardType.valueOf(board.getStructure().getType());

            switch (boardType) {
                case Random:
                    Range range = board.getStructure().getRange();
                    try {
                        checkRandomBoardValidity(range,size);
                    }
                    catch (XmlNotValidException ex)
                    {
                        validationResult.add("Random Board Validation Failed!");
                        throw new XmlNotValidException(validationResult);
                    }
                    break;
                case Explicit:
                    explicitSquares.clear();
                    Squares square = board.getStructure().getSquares();
                    try {
                        checkExplicitBoard(square.getSquare(), square.getMarker(), size);
                    }
                    catch (XmlNotValidException ex)
                    {
                        validationResult.add("Random Board Validation Failed!");
                        throw new XmlNotValidException(validationResult);
                    }
                    break;
            }
        }
    }

    public int updateBoard(Point squareLocation)
    {
        int squareValue;

        Point oldMarkerPoint = gameBoard.getMarker().getMarkerLocation();
        String squareStringValue = gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getValue();//get number
        squareValue = Square.ConvertFromStringToIntValue(squareStringValue); //return number value

        gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setValue("");    //empty old marker location
        gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setColor(GameColor.GRAY);

        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setValue(Marker.markerSign); //update marker to square
        gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].setColor(GameColor.MARKER);

        Square newMarkerLocation =  gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()];
        Square oldMarkerLocatoion = gameBoard.getGameBoard()[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1];

        cellsToUpdate.add(oldMarkerLocatoion);
        cellsToUpdate.add(newMarkerLocation);


        return squareValue;
    }

    public boolean playerRetire () {
        for (int i = 0; i < gameBoard.GetBoardSize(); i++)
            for (int j = 0; j < gameBoard.GetBoardSize(); j++)
                if (gameBoard.getGameBoard()[i][j].getColor() == currentPlayer.getColor())
                {
                    gameBoard.getGameBoard()[i][j].setColor(GameColor.GRAY);
                    gameBoard.getGameBoard()[i][j].setDisabled(true);
                    gameBoard.getGameBoard()[i][j].setValue(" ");
                    gameBoard.getGameBoard()[i][j].setEmpty(true);
                    cellsToUpdate.add(gameBoard.getGameBoard()[i][j]);

                }
        currentPlayer.setActive(false);
      //  players.remove(currentPlayer);
        numOfPlayers--;
        numOfSignedPlayers--;
        if(numOfPlayers == 1) {
             this.isEndOfGame = true;
            System.out.print("End Of game true !");
        }
        return isEndOfGame;
    }

    protected void checkExplicitBoard(List<GameEngine.jaxb.schema.generated.Square> squares, GameEngine.jaxb.schema.generated.Marker marker, int boardSize) throws XmlNotValidException
    {
        Square newSquare;
        int row,col,val,color;
        explicitSquares.clear();

        if(!isInBoardRange(marker.getRow().intValue(),boardSize))
        {
            validationResult.add(String.format("Explicit Board validation : invalid row of marker location ! Must be in range  from %d  to %d",1,boardSize));
            throw new XmlNotValidException(validationResult);
        }
        if(!isInBoardRange(marker.getColumn().intValue(),boardSize))
        {
            validationResult.add(String.format("Explicit Board validation error: invalid column of marker location ! Must be in range  from %d  to %d",1,boardSize));
            throw new XmlNotValidException(validationResult);
        }

        if(squares.size() > 0) {

            for (GameEngine.jaxb.schema.generated.Square square : squares) {
                row = square.getRow().intValue();
                col = square.getColumn().intValue();
                val = square.getValue().intValue();
                color = square.getColor();

                if ((val < BoardRange.MIN_BOARD_RANGE) || (val > BoardRange.MAX_BOARD_RANGE)) {
                    validationResult.add("Explicit Board Validation Error: squares values must be in between -99 and 99");
                    throw new XmlNotValidException(validationResult);
                }

                if(color > numOfPlayers){
                    validationResult.add(String.format("Explicit Board Validation Error: There are more colors than players exist! Color must be in range from 1 to %d",numOfPlayers));
                    throw new XmlNotValidException(validationResult);
                }

                if (!(row == marker.getRow().intValue() && col == marker.getColumn().intValue())) {

                    if (isInBoardRange(row, boardSize) && isInBoardRange(col, boardSize)) //location is ok
                    {
                        newSquare = new Square(new Point(row, col), String.valueOf(val), color);
                        if (explicitSquares.contains(newSquare)) {
                            validationResult.add(String.format("Explicit Board validation error: square double location [%d,%d] existance!",
                                    square.getRow().intValue(), square.getColumn().intValue()));
                            throw new XmlNotValidException(validationResult);

                        } else {
                            explicitSquares.add(newSquare);
                        }
                    } else {
                        explicitSquares.clear();
                        validationResult.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d outside board size :%d", row, col, boardSize));
                        throw new XmlNotValidException(validationResult);
                    }
                } else {
                    validationResult.add(String.format("Explicit Board Validation Error: Square row :%d,column:%d is both @ marker location and play square location!",
                            row, col));
                    throw new XmlNotValidException(validationResult);
                }
            }
        }else{
               validationResult.add("Explicit Board Validation Error: The Board cannot be set cause it is empty!");
               throw new XmlNotValidException(validationResult);
        }
    }


    public void loadDataFromJaxbToGame(GameDescriptor loadedGame,String gameType) {
        setGameType(eGameType.valueOf(gameType));
        GameEngine.jaxb.schema.generated.Board loadedBoard = loadedGame.getBoard();
        setBoard(loadedBoard);
    }

    public void setBoard(GameEngine.jaxb.schema.generated.Board board)
    {
        eBoardType boardType = eBoardType.valueOf(board.getStructure().getType());
        gameBoard = new Board(board.getSize().intValue(),boardType);
        switch (boardType) {
            case Explicit: Point markerLocation = new Point(board.getStructure().getSquares().getMarker().getRow().intValue(),board.getStructure().getSquares().getMarker().getColumn().intValue());
                FillExplicitBoard(explicitSquares,markerLocation);
                    gameBoard.getGameBoard()[markerLocation.getRow()-1][markerLocation.getCol()-1].setColor(GameColor.MARKER);
                break;
            case Random:
                BoardRange range = new BoardRange(board.getStructure().getRange().getFrom(),board.getStructure().getRange().getTo());
                gameBoard.setBoardRange(range);
                FillRandomBoard();
//                if(GameManager.gameRound > 0){
//                    gameBoard = new Board(historyMoves.get(0).getGameBoard());
//                }else{
//
//                }
                break;
        }

        originalBoard = new Board(gameBoard);
    }


    public void FillExplicitBoard(List<Square> xmlBoardList,Point markerLocation)
    {
        int col ,row,color;
        String val;
        Square[][] board = gameBoard.getGameBoard();

        for(Square square:xmlBoardList)
        {
            col = square.getLocation().getCol();
            row = square.getLocation().getRow();
            val = square.getValue();
            color = square.getColor();

            board[row-1][col-1].setColor(color);
            board[row-1][col-1].setValue(val);
        }

        gameBoard.getMarker().setMarkerLocation(markerLocation.getRow(),markerLocation.getCol());
        board[markerLocation.getRow()-1][markerLocation.getCol()-1].setValue(gameBoard.getMarker().getMarkerSign());

    }

    public boolean isInBoardRange(int num, int size)
    {
        boolean isValid = true;
        if(num < 0 || num > size)
        {
            isValid = false;
        }
        return isValid;
    }



}
