package GameEngine.validation;

/**
 * Created by Alona on 3/3/2017.
 */
public class UserMessageConfirmation {
    private boolean success = false;
    private String message = "";
    private int playersUpdateNum;
    private int playersIndex;
    private boolean isGameStarted = false;

    public UserMessageConfirmation(boolean success,String message,int playersUpdate,int index,boolean isGameStarted){
        this.success = success;
        this.message = message;
       this.playersUpdateNum = playersUpdate;
        this.playersIndex = index;
        this.isGameStarted = isGameStarted;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public int getPlayersIndex() {
        return playersIndex;
    }

    public void setPlayersIndex(int playersIndex) {
        this.playersIndex = playersIndex;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPlayersUpdateNum() {
        return playersUpdateNum;
    }

    public void setPlayersUpdateNum(int playersUpdateNum) {
        this.playersUpdateNum = playersUpdateNum;
    }
}
