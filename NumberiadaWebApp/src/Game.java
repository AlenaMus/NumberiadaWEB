import GameEngine.logic.GameManager;

public class Game {

    public void foo(){
        GameManager manager = new GameManager();
        //Gson gson = new Gson();
        manager.startGame();

    }

    public static void main(String[] args){
        Game game = new Game();
        game.foo();

    }
}
