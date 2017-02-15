/**
 * Created by Alona on 11/7/2016.
 */
package GameEngine.gameObjects;

import java.util.Random;

public class Board {

    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 50;

    private int boardSize;
    private eBoardType boardType;
    private Square[][] gameBoard;
    private BoardRange boardRange;
    private Marker marker;
    private int maxDigitsNum = 0;



    public Board(Board gameBoard){
        this.boardSize = gameBoard.GetBoardSize();
        this.boardType = gameBoard.GetBoardType();
        if(boardType.equals(eBoardType.Random)){
            this.boardRange = new BoardRange(gameBoard.getBoardRange().getFrom(),gameBoard.getBoardRange().getTo());
        }else{
            boardRange = null;
        }
        this.marker = new Marker(gameBoard.getMarker().getMarkerLocation().getRow(),gameBoard.getMarker().getMarkerLocation().getCol());
        this.gameBoard = new Square[boardSize][boardSize];
        copyBoard(gameBoard.getGameBoard());

    }

    public Board(int size, eBoardType type)
    {
        boardSize = size;
        boardType = type;
        gameBoard = new Square[size][size];
        marker = new Marker(0,0);
        boardRange = null;
        InitBoard();
    }

    public void clearBoard(){

        for (int i=0;i<boardSize;i++)
        {
            for(int j=0;j<boardSize;j++)
            {
                gameBoard[i][j] = null;
            }
        }
        gameBoard=null;
    }
    public int GetBoardSize()
    {
        return boardSize;
    }
    public eBoardType GetBoardType(){return boardType;}
    public void SetBoardType(eBoardType type){boardType = type; }

    public void setBoardRange(BoardRange range)
    {
        boardRange = new BoardRange(range.getFrom(),range.getTo());
    }
    public BoardRange getBoardRange(){return boardRange;}

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
    public Square[][] getGameBoard(){return gameBoard;}



    public void InitBoard()
    {

        for (int i=0;i<boardSize;i++)
        {
            for(int j=0;j<boardSize;j++)
            {
                gameBoard[i][j] = new Square(new Point(i,j));
                gameBoard[i][j].setColor(GameColor.GRAY);
            }
        }

    }


    private void copyBoard(Square[][] board){
        for (int i=0;i<boardSize;i++)
        {
            for(int j=0;j<boardSize;j++)
            {
                gameBoard[i][j] = new Square(board[i][j]);
            }
        }

    }


   public void shuffleArray(Square[][] matrix) {
        Random random = new Random();

        for (int i = matrix.length - 1; i > 0; i--) {
            for (int j = matrix[i].length - 1; j > 0; j--) {
                    int m = random.nextInt(i + 1);
                    int n = random.nextInt(j + 1);

                    String temp = matrix[i][j].getValue();
                    int color = matrix[i][j].getColor();
                    matrix[i][j].setValue(matrix[m][n].getValue());
                    matrix[i][j].setColor(matrix[m][n].getColor());
                    matrix[m][n].setValue(temp);
                    matrix[m][n].setColor(color);

            }
        }
    }


    private String setBoardRow(int size)
    {
        String row = "  ";

        for(int fr = 0 ; fr < size; fr++)
        {
           row+=("-");
        }
        row+="\n";

        return row;
    }

    private int digits(int num)
    {
        int digits;
        if(num != 0)
          digits = (int)(Math.log10(num)+1);
        else
            digits=1;

        return digits;

    }

    private String spaces(String value)
    {
        String spaces ="";
        int maxCell = sellSize();
        int size = value.length();

            for(int i=0;i<maxCell-size;i++)
            {
                spaces+=" ";
            }

        return spaces;
    }


    private int sellSize()
    {
        int size =0;
        if(boardType.equals(eBoardType.Random))
        {
            if(boardRange.getTo()<0 || boardRange.getFrom()<0){
                size++;
            }
            size+= digits(Math.max(Math.abs(boardRange.getFrom()),Math.abs(boardRange.getTo())));
        }
        else if(boardType.equals(eBoardType.Explicit))
        {
            size = maxDigitsNum;
        }

        return size;
    }

    @Override
    public String toString() {
        int j;
        int fr;
        String space;
        int maxDigits = sellSize();
        int size = maxDigits*boardSize + boardSize + 1;

        StringBuilder board = new StringBuilder();
        String boardRow = setBoardRow(size);

        space = spaces("");
        board.append(space+space);
        if(boardSize < 10)
        {
            board.append(" ");
        }

        for( fr = 1 ; fr <= boardSize; fr++)
        {
            space = spaces(Square.ConvertFromIntToStringValue(fr));

            board.append(fr);
            board.append(space+" ");


        }
        board.append("\n");
        board.append(boardRow);


        for(int i = 0; i<boardSize; i++)
        {
              space = spaces(Square.ConvertFromIntToStringValue(i+1));
              board.append(space);
              board.append(i+1);
              board.append(" |");

              space=spaces(gameBoard[i][0].getValue());
              board.append(space);


            for(j = 0; j<boardSize; j++)
            {
               if(j>0)
               {
                    space=spaces(gameBoard[i][j].getValue());
                    board.append(space);
                }
                board.append(gameBoard[i][j].getValue());
                board.append("|");

            }
            board.append("\n");
            board.append(boardRow);
        }

        return board.toString();
    }

    public int updateBoard(Point squareLocation)
    {
        int squareValue = 100;
        Point oldMarkerPoint = marker.getMarkerLocation();

        String squareStringValue = gameBoard[squareLocation.getRow()][squareLocation.getCol()].getValue();//get number
        if (squareStringValue.equals(marker.getMarkerSign()) || squareStringValue.isEmpty()) { //checks if wrong square-marker or empty
            return squareValue;
        }
            squareValue = Square.ConvertFromStringToIntValue(squareStringValue); //return number value

        gameBoard[oldMarkerPoint.getRow()-1][oldMarkerPoint.getCol()-1].setValue("");    //empty old marker location

        gameBoard[squareLocation.getRow()][squareLocation.getCol()].setValue(marker.markerSign); //update marker to square

        return squareValue;
    }

//     public boolean isGameOver(Point markerLocation)
//     {
//         int MarkerRow = markerLocation.getRow()-1;
//         int MarkerCol = markerLocation.getCol()-1;
//         for (int i=0; i < boardSize; i++)
//             if ((!gameBoard[MarkerRow][i].isEmpty()) && (!gameBoard[MarkerRow][i].getValue().equals(marker.getMarkerSign())))
//                 return false;
//             for (int i=0; i < boardSize; i++)
//                 if ((!gameBoard[i][MarkerCol].isEmpty() )&& (!gameBoard[i][MarkerCol].getValue().equals(marker.getMarkerSign())))
//                     return false;
//         return true;
//     }

     public boolean isRowPlayerHaveMoves(Point markerLocation)
     {
         int MarkerRow = markerLocation.getRow()-1;
         for (int i=0; i < boardSize; i++)
             if ((!gameBoard[MarkerRow][i].isEmpty()) && (!gameBoard[MarkerRow][i].getValue().equals(marker.getMarkerSign())))
                 return true;
         return false;
     }


    public boolean isColPlayerHaveMoves (Point markerLocation)
    {
        int MarkerCol = markerLocation.getCol()-1;
        for (int i=0; i < boardSize; i++)
            if ((!gameBoard[i][MarkerCol].isEmpty())&& (!gameBoard[i][MarkerCol].getValue().equals(marker.getMarkerSign())))
                return true;
        return false;
    }



}
