package GameEngine.validation;

/**
 * Created by Alona on 3/3/2017.
 */
public class UserMessageConfirmation {
    private boolean success = false;
    private String message = "";

    public UserMessageConfirmation(boolean success,String message){
        this.success = success;
        this.message = message;
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
}
