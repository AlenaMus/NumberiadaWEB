package GameEngine.userInterface;

import GameEngine.gameObjects.GameColor;
import GameEngine.gameObjects.Marker;
import GameEngine.gameObjects.Point;
import GameEngine.gameObjects.Square;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoardButton extends Button {

    private Square boardButton;
    private IntegerProperty buttonColor;
    private String  styleId ="";
    private String filterStyle = "filter-button";

    DropShadow shadow = new DropShadow();

    public int getButtonColor() {
        return buttonColor.get();
    }

    public IntegerProperty buttonColorProperty() {
        return buttonColor;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor.set(buttonColor);
        String setColor = GameColor.setColor(buttonColor);
        this.setId(setColor);
        this.getStyleClass().add(setColor);
    }


    public void setBColor(int color)
    {
        String setColor = GameColor.setColor(color);
        this.setId(setColor);
        this.getStyleClass().add(setColor);
        buttonColor.set(color);

    }


    public BoardButton(Square buttonSquare) {

         buttonColor = new SimpleIntegerProperty();
         this.boardButton = buttonSquare;

    if(boardButton.getValue().isEmpty())
    {
        this.setButtonColor(GameColor.GRAY);
    }
    else if(boardButton.getValue().equals(Marker.markerSign)) {
          this.setText(boardButton.getValue());
          setButtonColor(buttonSquare.getColor());
    }
    else{
         this.setText(boardButton.getValue());
         setButtonColor(buttonSquare.getColor());
    }
}


public Point getLocation()
{
    return boardButton.getLocation();
}

public int getValue()
{
    return Integer.valueOf(boardButton.getValue());
}

public void setChosenButtonEffect(){


    this.setEffect(shadow);
    this.fontProperty().set(Font.font("System",FontWeight.BOLD,12));

}

public void removeChosenButtonEffect(){

    this.setEffect(null);
    this.fontProperty().set(Font.font("System",FontWeight.THIN,12));

}



}
