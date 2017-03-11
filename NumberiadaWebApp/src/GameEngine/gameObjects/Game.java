package GameEngine.gameObjects;


public class Game{

        private String userName;
        private String gameTitle;
        private Board board;
        private int numOfPlayers;
        private int boardSize;
        private int gameNumber;
        private int signedPlayers = 0;
        private boolean isRunningGame = false;


        public Game(String editorName,String gameTitle, int numOfPlayers,Board board, int gameNumber){

            this.userName = editorName;
            this.gameTitle = gameTitle;
            this.numOfPlayers = numOfPlayers;
            this.board = new Board(board);
            this.gameNumber = gameNumber;
            boardSize = board.GetBoardSize();
          //  this.gameBoard = board.getGameBoard();
        }

    public boolean isRunningGame() {
        return isRunningGame;
    }

    public void setRunningGame(boolean runningGame) {
        isRunningGame = runningGame;
    }

    public void updateSignedPlayers(){signedPlayers++;}
    public void setSignedPlayers(int players){
        signedPlayers = players;
    }

    public int getSignedPlayers(){return signedPlayers;}

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


