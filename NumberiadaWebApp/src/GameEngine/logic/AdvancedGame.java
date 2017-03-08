package GameEngine.logic;

import GameEngine.gameObjects.*;
import GameEngine.jaxb.schema.generated.DynamicPlayers;
import GameEngine.jaxb.schema.generated.GameDescriptor;
import GameEngine.jaxb.schema.generated.Range;
import GameEngine.validation.XmlNotValidException;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class AdvancedGame extends GameLogic{

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 6;


  //  @Override
//    public void initGame()
//    {
//        explicitSquares = new ArrayList<Square>();
//        players = new ArrayList<>();
//        winners = new ArrayList<>();
//    }


    @Override
    public String gameOver()
    {
        int i=0;
     //   updateHistory(null);
        GameEngine.gameObjects.Player player;
        String winnerStatistics="";
        Collections.sort(players);
        Collections.reverse(players);
        while(i < players.size())
        {
            player = players.get(i);
            if(!winners.contains(player)){
                winnerStatistics += (String.format("player %s id: %d with score %d\n",
                        player.getName(),player.getId(),player.getScore()));
            }
            i++;
        }

        gameLogicClear();
        return winnerStatistics;
    }



    @Override
    public boolean InitMoveCheck()
    {
        return isPlayerHaveMove(gameBoard.getMarker().getMarkerLocation(),currentPlayer);
    }


    @Override
    public boolean switchPlayer()
    {
       boolean isSwitchSucceed = true;
       GameEngine.gameObjects.Player nextPlayer;

            currentPlayerIndex++;
            nextPlayer = players.get(currentPlayerIndex % numOfPlayers);
            super.setCurrentPlayer(nextPlayer);
        if (!(isPlayerHaveMove(gameBoard.getMarker().getMarkerLocation(),nextPlayer)))
            isSwitchSucceed = false;
        else {
            setGameMoves(getGameMoves()+1);
        }

        return isSwitchSucceed;
    }

    private boolean isPlayerHaveMove(Point markerLocation, GameEngine.gameObjects.Player player)
    {
        int MarkerRow = markerLocation.getRow()-1;
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < gameBoard.GetBoardSize();i++)
            if ((!gameBoard.getGameBoard()[MarkerRow][i].isDisabled()) &&(!gameBoard.getGameBoard()[MarkerRow][i].isEmpty())
                    && (!gameBoard.getGameBoard()[MarkerRow][i].getValue().equals(Marker.markerSign))
                    && ((gameBoard.getGameBoard()[MarkerRow][i].getColor()  == (player.getColor()))))
                return true;
        for (int i=0; i < gameBoard.GetBoardSize(); i++)
            if ((!gameBoard.getGameBoard()[i][MarkerCol].isDisabled() )&& (!gameBoard.getGameBoard()[i][MarkerCol].isEmpty() )
                    && (!gameBoard.getGameBoard()[i][MarkerCol].getValue().equals(Marker.markerSign))
                    && ((gameBoard.getGameBoard()[i][MarkerCol].getColor()  == (player.getColor()))))
                return true;
        return false;
    }

    @Override
    public boolean isGameOver()
    {
        boolean gameOver = true;
        Point markerLocation = gameBoard.getMarker().getMarkerLocation();
        int MarkerRow = markerLocation.getRow()-1;
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < gameBoard.GetBoardSize();i++)
            if ((!gameBoard.getGameBoard()[MarkerRow][i].isDisabled()) &&(!gameBoard.getGameBoard()[MarkerRow][i].isEmpty())
                    && (!gameBoard.getGameBoard()[MarkerRow][i].getValue().equals(gameBoard.getMarker().getMarkerSign())))
               gameOver = false;
        for (int i=0; i < gameBoard.GetBoardSize(); i++)
            if ((!gameBoard.getGameBoard()[i][MarkerCol].isDisabled() )&& (!gameBoard.getGameBoard()[i][MarkerCol].isEmpty() )
                    && (!gameBoard.getGameBoard()[i][MarkerCol].getValue().equals(gameBoard.getMarker().getMarkerSign())))
               gameOver = false;

        return gameOver;
    }

    public String playerRetire () {
        for (int i = 0; i < gameBoard.GetBoardSize(); i++)
            for (int j = 0; j < gameBoard.GetBoardSize(); j++)
                if (gameBoard.getGameBoard()[i][j].getColor() == currentPlayer.getColor())
                {
                    gameBoard.getGameBoard()[i][j].setColor(GameColor.GRAY);
                    gameBoard.getGameBoard()[i][j].setDisabled(true);
                    gameBoard.getGameBoard()[i][j].setValue(" ");
                    gameBoard.getGameBoard()[i][j].setEmpty(true);
                }
        currentPlayer.setActive(false);
        players.remove(currentPlayer);
        numOfPlayers--;
       if(numOfPlayers==1) {
           isEndOfGame = true;
       }
       return "";
    }

    private static int ComputerMove(int boardSize) {
        return (ThreadLocalRandom.current().nextInt(0, boardSize));
    }

    public Point makeComputerMove()
    {
            boolean foundSquare = false;
            Point squareLocation = null;

                int MarkerRow = gameBoard.getMarker().getMarkerLocation().getRow()-1;
                int MarkerCol = gameBoard.getMarker().getMarkerLocation().getCol()-1;


                while (!foundSquare) {
                    int random = ComputerMove(gameBoard.GetBoardSize());
                    if (gameBoard.getGameBoard()[MarkerRow][random].getColor() == (currentPlayer.getColor())) {
                        squareLocation = new Point(MarkerRow, random);
                        foundSquare = true;
                    }
                    else if (gameBoard.getGameBoard()[random][MarkerCol].getColor() == (currentPlayer.getColor())) {
                        squareLocation = new Point(random, MarkerCol);
                        foundSquare = true;
                    }
                }

                return squareLocation;
    }












//
//    public void makeHumanMove(Point userPoint)
//    {
//        int squareValue;
//        squareValue = updateBoard(userPoint); //update 2 squares
//        updateUserData(squareValue);
//        gameBoard.getMarker().setMarkerLocation(userPoint.getRow()+1, userPoint.getCol()+1);
//
//    }

    public int isValidPoint(Point squareLocation)
    {
        int returnPointStatus = GOOD_POINT;
        Point markerPoint = gameBoard.getMarker().getMarkerLocation();
        if (squareLocation.getRow() != (markerPoint.getRow()-1) && squareLocation.getCol() != (markerPoint.getCol()-1)  )
            returnPointStatus = NOT_IN_MARKER_ROW_AND_COLUMN;
        else if  (currentPlayer.getColor() != gameBoard.getGameBoard()[squareLocation.getRow()][squareLocation.getCol()].getColor())
            returnPointStatus = NOT_PLAYER_COLOR;
        return returnPointStatus;
    }



    @Override
    public void checkXMLData(GameDescriptor loadedGame)throws XmlNotValidException
    {
        //validationResult = new ValidationResult();
      //  int numOfPlayers = loadedGame.getPlayers().getPlayer().size();

//        if(numOfPlayers< MIN_PLAYERS || numOfPlayers> MAX_PLAYERS)
//        {
//            validationResult.add(String.format("XML Load error: %d is invalid number of players,must be minimum 3 - 6 players",getNumOfPlayers()));
//            throw new XmlNotValidException(validationResult);
//        }
           // setNumOfPlayers(numOfPlayers);
        // checkAndSetPlayersXML(loadedGame.getPlayers());

            setDynamicPlayers(loadedGame.getDynamicPlayers());
            super.checkBoardXML(loadedGame.getBoard());

    }

    private void setDynamicPlayers(DynamicPlayers gamePlayers)throws XmlNotValidException {
        gameTitle = gamePlayers.getGameTitle();
        numOfPlayers = gamePlayers.getTotalPlayers();
        if(numOfPlayers < MIN_PLAYERS || numOfPlayers > MAX_PLAYERS){
            validationResult.add(String.format("%d - Invalid number of players! Players Numbers must be from 3 to 6",numOfPlayers));
            numOfPlayers = 0;
            throw new XmlNotValidException(validationResult);
        }
    }

    @Override
    public void checkRandomBoardValidity(Range boardRange, int boardSize) throws XmlNotValidException
    {
        int range;
        if(!(boardRange.getFrom() >= BoardRange.MIN_BOARD_RANGE &&  boardRange.getTo() <= BoardRange.MAX_BOARD_RANGE))
        {
            validationResult.add(String.format("Random Board Validation Error: board range have to be in [-99,99] range,range [%d,%d] is invalid ",
                    boardRange.getFrom(),boardRange.getTo()));
            throw new XmlNotValidException(validationResult);
        }
        if(boardRange.getFrom() <= boardRange.getTo())
        {
            range = boardRange.getTo() - boardRange.getFrom() +1;

            if(((boardSize*boardSize -1) / (range*getNumOfPlayers()) == 0))
            {
               validationResult.add(String.format("Random Board Validation Error: Board Size %d < Board Range %d for %d players!",
                   boardSize*boardSize,range,getNumOfPlayers()));
                throw new XmlNotValidException(validationResult);
            }

        } else {
                    validationResult.add(String.format("Random Board Validation Error: Board Range numbers invalid from %d > to %d",
                     boardRange.getFrom(),boardRange.getTo()));
                     throw new XmlNotValidException(validationResult);
        }
    }





//    @Override
//    public void checkAndSetPlayersXML(GameEngine.jaxb.schema.generated.Players players)throws XmlNotValidException {
//        List<Player> gamePlayers = players.getPlayer();
//        GameEngine.gameObjects.Player newPlayer;
//
//
//        for (Player player : gamePlayers) {
//             newPlayer = new GameEngine.gameObjects.Player(ePlayerType.valueOf(player.getType()), player.getName(), player.getId().intValue(), player.getColor());
//            if(getPlayers()!=null)
//            {
//                if (getPlayers().contains(newPlayer)) {
//                    validationResult.add(String.format("Player Validation Error: name = %s ,id = %d, color = %s already exists !",
//                            player.getName(),player.getId(),player.getColor()));
//                    getPlayers().clear();
//                    throw new XmlNotValidException(validationResult);
//                } else {
//                    getPlayers().add(newPlayer);
//                }
//            }
//            else
//            {
//                getPlayers().add(newPlayer);
//            }
//        }
//    }


    @Override
    public void FillRandomBoard() {
        int i ;
        int j ;
        int row =0;
        int col=0;
        int color = GameColor.RED;
        int boardSize = gameBoard.GetBoardSize();
        BoardRange boardRange = gameBoard.getBoardRange();
        Square[][] board = gameBoard.getGameBoard();
        // filling our numbers in given range
        int rangeSize = boardRange.RangeSize();
        int printNumCount = (boardSize * boardSize -1) / (rangeSize*numOfPlayers);
        int rangeNumToPrint = boardRange.getFrom();

            for (int k = 0; k <numOfPlayers ; k++) {
                for(int m = 0;m < rangeSize ;m++) {
                    for(int n=0;n < printNumCount;n++){
                        board[row][col].setValue(Square.ConvertFromIntToStringValue(rangeNumToPrint));
                        board[row][col].setColor(color);

                        if(col == boardSize-1)
                        {
                            col = -1;
                            row++;
                        }
                        col++;
                    }

                    rangeNumToPrint++;
                    if(rangeNumToPrint>boardRange.getTo()){
                        rangeNumToPrint = boardRange.getFrom();
                    }
            }
               // color=players.get((k+1)%numOfPlayers).getColor();
               color++;

        }

        if (col == boardSize) {
            col = 0;
        }

        for (int m = row; m < boardSize; m++) {
            for (int n = col; n < boardSize; n++) {
                board[m][n].setValue("");
                board[m][n].setColor(GameColor.GRAY);
            }
        }

        board[boardSize - 1][boardSize - 1].setValue(Marker.markerSign);
        board[boardSize - 1][boardSize - 1].setColor(GameColor.MARKER);
        gameBoard.shuffleArray(board);

        String MarkerSign = gameBoard.getMarker().getMarkerSign();
        for(i =0 ;i<boardSize;i++)          //////FOR MARKER CONTROL IN INIT
        {
            for(j=0;j<boardSize;j++)
                if  (board[i][j].getValue().equals(MarkerSign)) {
                     gameBoard.getMarker().setMarkerLocation(i + 1, j + 1);
                    break;
                }
        }
    }


}
