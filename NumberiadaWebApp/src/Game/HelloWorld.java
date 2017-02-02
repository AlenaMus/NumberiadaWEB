package Game;

import GameEngine.logic.GameManager;

public class HelloWorld {

    public void foo(){
        GameManager manager = new GameManager();
         manager.startGame();

    }

    public static String getMessage() {
        return "Hello, world";
    }

    public static void main(String[] args){
        HelloWorld game = new HelloWorld();
        game.foo();

    }
}
