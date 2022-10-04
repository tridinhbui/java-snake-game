package snake;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;
import java.awt.Color;

public class Snake extends Rectangle {
    private double pastPositionX;
    private double pastPositionY;
    public Snake(double xPos, double yPos) {
        super(xPos, yPos, SnakeGame.UNIT_SIZE, SnakeGame.UNIT_SIZE);
        this.setFillColor(new Color(51,102,0));
        // this.setStrokeColor(new Color(51,102,0));
        this.setStrokeWidth(2);
    }

    /**
     * Set the x position of the snake on the gameboard before moving 
     */

    public void setPastPositionX(double xPos) {
        pastPositionX = xPos;
    }

    /**
     * Set the y position of the snake on the gameboard before moving 
     */

    public void setPastPositionY(double yPos) {
        pastPositionY = yPos;
    }

    /**
     * Obtain the x, y positions of the snake on the gameboard before moving 
     */

    public Point getPastPosition() {
        return new Point(pastPositionX, pastPositionY);
    }
    
    public void removeFromCanvas(CanvasWindow canvas){
        canvas.remove(this);
    }
}
