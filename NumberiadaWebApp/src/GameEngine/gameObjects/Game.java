package GameEngine.gameObjects;

/**
 * Created by Alona on 2/26/2017.
 */

public class Game{

        private String userName;
        private String gameTitle;
        private Board board;
        private int numOfPlayers;
        private int boardSize;
        private int gameNumber;


        public Game(String editorName,String gameTitle, int numOfPlayers,Board board, int gameNumber){

            this.userName = editorName;
            this.gameTitle = gameTitle;
            this.numOfPlayers = numOfPlayers;
            this.board = new Board(board);
            this.gameNumber = gameNumber;
            boardSize = board.GetBoardSize();
        }

    public String getUserName() {
        return userName;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public Board getBoard() {
        return board;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}


