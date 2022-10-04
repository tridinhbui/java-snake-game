package snake;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.events.Key;
import edu.macalester.graphics.ui.Button;

/**
 * Snake Game›
 * 
 * @author: Long Truong, Tri Bui, Thu Dang
 */
public class snakeGame {
    // CANVAS SCREEN SIZE AND AN UNIT GRID SIZE
    public static final int SCREEN_HEIGHT = 600;
    public static final int SCREEN_WIDTH = 600;
    public static final int SCORE_PANEL_WIDTH = 200;
    public static final int TOTAL_PROGRAM_WIDTH = SCORE_PANEL_WIDTH + SCREEN_WIDTH;
    public static final int UNIT_SIZE = 25;

    // Direction variable of the snake heading
    private static char direction = 'O';


    private static int delay_time = 115; // speed between each iteration animation

    // Food variables
    private static Food food = new Food(0, 0);
    private static int xPos = 0;
    private static int yPos = 0;

    // Snake head & snakes
    private static Snake snakeHead;
    private static int snakeBodyCount = 1;
    private static ArrayList<Snake> snakeBody = new ArrayList<>();

    // Player Score
    private static Integer foodEaten = 0;
    private static GraphicsText scoreValText, highScoreValText;
    private static Integer highScoreValue = 0;
    public static CanvasWindow canvas;
    public static boolean lose;

    // Screen Notifier
    private static GraphicsText notifier;

    /**
     * Main method
     */
    public static void main(String[] args) {
        openMenu();
    }

    /**
     * Load the game after the play button is hit
     */
    public static void playGame() {
        setBackground();
        addScorePanel();
        objectsPlaced();
        giveOutDirection();
        gameAnimate();
    }

    /** Open menu with button */
    public static void openMenu() {
        canvas = new CanvasWindow("Snake Game", SCREEN_WIDTH + SCORE_PANEL_WIDTH, SCREEN_HEIGHT);
        canvas.setBackground(Color.CYAN);
        GraphicsText gameTitle = new GraphicsText("SNAKE GAME");
        GraphicsText author = new GraphicsText("Presented by Long, Tri, and Thu");
        Button playButton = new Button("Play Game");
        Button quitButton = new Button("Quit Game");
        canvas.add(gameTitle);
        canvas.add(author);
        canvas.add(playButton, 200, 200);
        canvas.add(quitButton, 200, 300);
        gameTitle.setCenter(TOTAL_PROGRAM_WIDTH / 2, SCREEN_HEIGHT / 8);
        author.setCenter(TOTAL_PROGRAM_WIDTH / 2, SCREEN_HEIGHT / 8 + SCREEN_HEIGHT / 20);
        playButton.setCenter(TOTAL_PROGRAM_WIDTH / 2, SCREEN_HEIGHT / 2);
        quitButton.setCenter(TOTAL_PROGRAM_WIDTH / 2, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 12);
        canvas.draw();
        quitButton.onClick(() -> canvas.closeWindow());
        playButton.onClick(() -> playGame());
    }

    /**
     * Set Background with gridlines
     */
    private static void setBackground() {
        canvas.removeAll();
        canvas = new CanvasWindow("Snake Game", SCREEN_WIDTH + SCORE_PANEL_WIDTH, SCREEN_HEIGHT);
        canvas.setBackground(Color.gray);

        for (int i = 0; i <= SCREEN_WIDTH / UNIT_SIZE; i += 1) {
            Line line = new Line(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            line.setStrokeWidth(0.5);
            canvas.add(line);
        }
        for (int i = 0; i <= SCREEN_HEIGHT / UNIT_SIZE; i += 1) {
            Line line = new Line(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            line.setStrokeWidth(0.5);
            canvas.add(line);
        }

        // Add the notifier for Game Over
        notifier = new GraphicsText("GAME OVER");
        notifier.setCenter(SCREEN_WIDTH * 0.3, SCREEN_HEIGHT * 0.5);
        notifier.setFont(FontStyle.BOLD, 80);
        notifier.setFillColor(Color.RED);
    }

    /**
     * Score Panel for the game Consists of rectangles and textboxes
     */
    private static void addScorePanel() {
        // Score Panel is the overall interface on the rightside
        // scoreText just contains the word "YOUR SCORE".
        // scoreValText is the text displaying the value (how many foods was eaten)

        // Declare all graphicsObject things
        GraphicsGroup scorePanel = new GraphicsGroup(SCREEN_WIDTH, 0);
        Rectangle panel = new Rectangle(0, 0, SCORE_PANEL_WIDTH, SCREEN_HEIGHT);
        GraphicsText scoreText = new GraphicsText("YOUR SCORE:", 0, 0);
        scoreValText = new GraphicsText("0", 0, 0);
        GraphicsText highScoreText = new GraphicsText("HIGH:", 0, 0);
        highScoreValText = new GraphicsText("0");
        // Add them to scorePanel
        scorePanel.add(panel);
        scorePanel.add(scoreText);
        scorePanel.add(scoreValText);
        scorePanel.add(highScoreText);
        scorePanel.add(highScoreValText);
        // Set properties of each object

        panel.setFillColor(Color.LIGHT_GRAY);

        highScoreText.setCenter(SCORE_PANEL_WIDTH * 0.25, SCREEN_HEIGHT * 0.1);
        highScoreText.setFont(FontStyle.BOLD, 15);

        highScoreValText.setCenter(SCORE_PANEL_WIDTH * 0.5, SCREEN_HEIGHT * 0.1);
        highScoreValText.setFont(FontStyle.PLAIN, 15);


        scoreText.setFont(FontStyle.BOLD, 20);
        scoreText.setCenter(SCORE_PANEL_WIDTH / 2, SCREEN_HEIGHT * 1 / 3);

        scoreValText.setFont(FontStyle.PLAIN, 40);
        scoreValText.setFillColor(Color.GRAY);
        scoreValText.setCenter(SCORE_PANEL_WIDTH / 2, SCREEN_HEIGHT * 0.45);

        // Button to aid play again action
        Button playAgain = new Button("Play Again");
        scorePanel.add(playAgain);
        playAgain.setCenter(SCORE_PANEL_WIDTH / 2, SCREEN_HEIGHT * 0.7);
        playAgain.onClick(() -> {
            playAgainAction();
        });

        canvas.add(scorePanel);
    }

    /** play Again sequence after player loses */
    private static void playAgainAction() {
        canvas.remove(notifier);
        canvas.draw();
        lose = false;
        direction = 'O';
        // unhide snake head
        snakeHead.setFillColor(new Color(25, 51, 0));
        snakeHead.setStrokeColor(Color.black);

        for (int i = snakeBodyCount - 1; i > 0; i--) {
            canvas.remove(snakeBody.get(i));
            snakeBody.remove(i);
        }
        snakeBodyCount = 1;
        foodEaten = 0;
        scoreValText.setText("0");
        snakeHead.setPosition(250, 250);
        spawnFood();
    }

    /**
     * Place food and snake head initially
     */
    private static void objectsPlaced() {
        snakeHead = new Snake(250, 250);
        // snakeHead.setStrokeColor(new Color(25, 51, 0));
        snakeHead.setFillColor(new Color(25, 51, 0));
        snakeBody.add(snakeHead);
        food = new Food(0, 0);
        spawnFood();
        canvas.add(snakeHead);
        canvas.add(food);
    }

    /**
     * Capture key input from user into variable direction. Supporting function
     */
    private static void giveOutDirection() {
        canvas.onKeyDown(event -> {
            if (event.getKey() == Key.RIGHT_ARROW && direction != 'L') {
                direction = 'R';
            }
            if (event.getKey() == Key.LEFT_ARROW && direction != 'R') {
                direction = 'L';
            }
            if (event.getKey() == Key.UP_ARROW && direction != 'D') {
                direction = 'U';
            }
            if (event.getKey() == Key.DOWN_ARROW && direction != 'U') {
                direction = 'D';
            }
        });
    }

    /**
     * Relocate food after being eaten to a random location. Supporting function
     */
    private static void spawnFood() {
        Random rand = new Random();
        xPos = rand.nextInt((SCREEN_WIDTH / UNIT_SIZE - 0) + 0) + 0;
        yPos = rand.nextInt((SCREEN_HEIGHT / UNIT_SIZE - 0) + 0) + 0;
        food.setPosition(xPos * UNIT_SIZE, yPos * UNIT_SIZE);

    }

    /**
     * Sprout a new body segment. Supporting function
     */
    private static void sproutBodySegment() {
        double lastX = snakeBody.get(snakeBodyCount - 1).getPosition().getX();
        double lastY = snakeBody.get(snakeBodyCount - 1).getPosition().getY();
        switch (direction) {
            case 'U':
                snakeBody.add(new Snake(lastX, lastY + UNIT_SIZE));
                break;
            case 'D':
                snakeBody.add(new Snake(lastX, lastY - UNIT_SIZE));
                break;
            case 'R':
                snakeBody.add(new Snake(lastX - UNIT_SIZE, lastY));
                break;
            case 'L':
                snakeBody.add(new Snake(lastX + UNIT_SIZE, lastY));
                break;
        }
        snakeBodyCount += 1;
        canvas.add(snakeBody.get(snakeBodyCount - 1));
    }

    /**
     * Check if the snake hits the wall
     * 
     * @return true if the snake hits the wall, false if not
     */
    private static boolean checkWallCollision() {
        if (snakeHead.getPosition().getX() >= SCREEN_WIDTH ||
            snakeHead.getPosition().getX() < 0 ||
            snakeHead.getPosition().getY() >= SCREEN_HEIGHT ||
            snakeHead.getPosition().getY() < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the snake hits its own body segment
     * 
     * @return true if it hits, false if not
     */
    private static boolean checkBodyCollision() {
        for (int i = 1; i < snakeBodyCount; i++) {
            if (snakeBody.get(i).getPosition().getX() == snakeHead.getPosition().getX() &&
                snakeBody.get(i).getPosition().getY() == snakeHead.getPosition().getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Animate the snake movement scene. Calls spawnFood(), giveOutDirection(), and sproutBodySegment()
     * to work. If the player loses. Close the Canvas Window Main-role function
     */
    private static void gameAnimate() {

        canvas.animate(() -> {

            // Check if player loses
            if (checkWallCollision() || checkBodyCollision()) {
                lose = true;

                // Add GAME OVER notifier
                canvas.add(notifier);

                // Nice trick to hide snakeHead
                snakeHead.setFillColor(Color.LIGHT_GRAY);
                snakeHead.setStrokeColor(Color.LIGHT_GRAY);
            }

            if (!lose) {
                // Update snakeBody segment position
                int oldX = (int) snakeHead.getPosition().getX();
                int oldY = (int) snakeHead.getPosition().getY();
                canvas.pause(delay_time);
                for (int i = snakeBodyCount - 1; i > 0; i--) {
                    snakeBody.get(i).setPosition(snakeBody.get(i - 1).getPosition());
                }
                snakeHead.setPastPositionX(oldX);
                snakeHead.setPastPositionY(oldY);
                // Direct the snake head
                switch (direction) {
                    case 'U':
                        snakeHead.setPosition(oldX, oldY - UNIT_SIZE);
                        break;
                    case 'D':
                        snakeHead.setPosition(oldX, oldY + UNIT_SIZE);
                        break;
                    case 'R':
                        snakeHead.setPosition(oldX + UNIT_SIZE, oldY);
                        break;
                    case 'L':
                        snakeHead.setPosition(oldX - UNIT_SIZE, oldY);
                        break;
                }
                // if snake eats food then spawn new food.
                // Relocate food to a random position. Sprout a new body segment. Update the text for score value
                if (snakeHead.getPosition().getX() == food.getPosition().getX()
                    && snakeHead.getPosition().getY() == food.getPosition().getY()) {
                    foodEaten += 1;
                    highScoreValue = Math.max(foodEaten, highScoreValue);
                    spawnFood();
                    sproutBodySegment();
                    scoreValText.setText(foodEaten.toString());
                    highScoreValText.setText(highScoreValue.toString());
                }

            }
        });
    }

    /**
     * A delay between animation time
     * 
     * @param ms Time input measured in ms
     */
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

