package snake;

import java.awt.Color;
import edu.macalester.graphics.Ellipse;

/**
 * Set the position and color of the food on the game board
 */

public class Food extends Ellipse{
    public Food(int xPos, int yPos) {
        super(xPos, yPos, SnakeGame.UNIT_SIZE, SnakeGame.UNIT_SIZE);
        this.setFillColor(Color.RED);
        this.setStrokeWidth(2);
    }
}
