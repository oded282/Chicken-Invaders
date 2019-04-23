package animation;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import geometry.DrawRectangle;
import geometry.Point;
import geometry.Rectangle;
import hitlistener.BallRemover;
import hitlistener.BlockRemover;
import hitlistener.ScoreTrackingListener;
import indicators.LevelIndicator;
import indicators.LivesIndicator;
import indicators.ScoreIndicator;
import inerfaces.Animation;
import inerfaces.Collidable;
import inerfaces.Sprite;
import levels.AnimationRunner;
import objects.Ball;
import objects.Block;
import objects.Paddle;
import objects.Shield;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import objects.Alien;
import running.ColorBackground;
import running.Counter;
import running.GameEnvironment;
import running.SpriteCollection;
import running.AlienCollection;

/**
 * The game class intialize the game itself. It creates a screen,
 * and draw all the blocks, paddle and balls on the screen. Using two functions runs and  intialize.
 * initialize in charge of creating all object and run in charge of starting the game.
 */
public class GameLevel implements Animation {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int PADDLE_SPEED = 400;
    public static final Point PADDLE_LOCATION = new Point(375, 550);
    public static final int PADDLE_HEIGHT = 20;
    public static final int PADDLE_WIDTH = 80;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private GUI gui;
    private Counter counterBlocks;
    private Counter counterScore;
    private Counter counterLives;
    private AnimationRunner runner;
    private boolean running;
    private KeyboardSensor keyboard;
    private AlienCollection collectionOfAliens;
    private Paddle paddle;
    private Shield shield;
    private double coolDownEnemy;
    private double coolDownPlayer;
    private int countLevelString;
    private List<Ball> allBalls;

    /**
     * @param ks    the keyboard sensor.
     * @param ar    the class who run's the animation.
     * @param gui   the gui sent to the func.
     * @param score the counter of the score.
     * @param lives the counter of the lives.
     * @param countLevelString - counts the level.
     *              initialize all the methods in the game class.
     *              the sprite collection, GameEnvironment, gui and the counters.
     */
    public GameLevel(KeyboardSensor ks, AnimationRunner ar,
                     GUI gui, Counter score, Counter lives, int countLevelString) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.gui = gui;
        //this.counterBalls = new Counter(level.numberOfBalls());
        this.counterScore = score;
        this.counterLives = lives;
        this.runner = ar;
        this.keyboard = ks;
        this.collectionOfAliens = new AlienCollection(this.environment, this.sprites, this);
        this.shield = new Shield(50, 500);
        this.counterBlocks = new Counter(50);
        this.paddle = new Paddle(new Rectangle(PADDLE_LOCATION, PADDLE_WIDTH, PADDLE_HEIGHT), gui.getKeyboardSensor(),
                Color.YELLOW, PADDLE_SPEED);
        this.countLevelString = countLevelString;
        this.allBalls = new ArrayList<Ball>();
    }

    /**
     * @return Collection of aliens.
     */

    public AlienCollection getCollectionOfAliens() {
        return collectionOfAliens;
    }

    /**
     * @return the paddle itself.
     */
    public Paddle getPaddle() {
        return this.paddle;
    }

    /**
     * @param c the collidable object.
     *          Add the collidable to the environment.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * @param s the sprite object
     *          Add the sprite to sprite member.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * The func creates the all the listeners, and add all the blocks to the game.
     * furthermore it creates all the indicators of the game, like score and lives.
     */
    public void initialize() {
        int a = this.countLevelString + 1;
        DrawRectangle levelbg = new DrawRectangle(new Point(0, 0),
                SCREEN_WIDTH, SCREEN_HEIGHT, new ColorBackground(Color.BLACK));

        sprites.addSprite(levelbg);

        LivesIndicator indicateLives = new LivesIndicator(this);
        indicateLives.addToGame(this);
        ScoreIndicator indicateScore = new ScoreIndicator(this);
        indicateScore.addToGame(this);
        LevelIndicator indicateLevel = new LevelIndicator("Battle no." + "" + a + "");
        sprites.addSprite(indicateLevel);

        List<List<Alien>> listOfAliens = this.collectionOfAliens.initialList();

        BallRemover removeBall = new BallRemover(this);
        BlockRemover b = new BlockRemover(this, this.counterBlocks);
        ScoreTrackingListener scoreTracking = new ScoreTrackingListener(counterScore, this);

        Block ballUPCleaner = new Block(new Point(0, 20), 800, 5, 0);
        Block ballDownCleaner = new Block(new Point(0, 600), 800, 5, 0);
        ballDownCleaner.addHitListener(removeBall);
        ballDownCleaner.addToGame(this);
        ballUPCleaner.addHitListener(removeBall);
        ballUPCleaner.addToGame(this);
        this.paddle.addHitListener(removeBall);
        paddle.addToGame(this);
        this.shield.setTheShield();
        for (int i = 0; i < this.shield.getShieldSize(); i++) {
            this.shield.getShieldList().get(i).addToGame(this);
            this.shield.getShieldList().get(i).addHitListener(removeBall);
            this.shield.getShieldList().get(i).addHitListener(b);
        }
        for (int i = 0; i < listOfAliens.size(); i++) {
            for (int j = 0; j < listOfAliens.get(i).size(); j++) {
                listOfAliens.get(i).get(j).addToGame(this);
                listOfAliens.get(i).get(j).getBlock().addHitListener(removeBall);
                listOfAliens.get(i).get(j).getBlock().addHitListener(b);
                listOfAliens.get(i).get(j).getBlock().addHitListener(scoreTracking);
            }
        }
    }

    /**
     * This func will play one turn of the specific level.
     * Until there is no more blocks or lives.
     */
    public void playOneTurn() {
        this.collectionOfAliens.setToStart();
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        this.running = true;
        this.runner.run(this);
    }

    /**
     * @param d  the surface upon we draw.
     * @param dt frame speed.
     *           This func will draw all the sprites on the frame, as well as checking if it
     *           time to shoot a some balls.
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.coolDownEnemy = this.coolDownEnemy - dt;
        this.coolDownPlayer = this.coolDownPlayer - dt;
        if (this.coolDownEnemy < 0) {
            Ball alienBall = collectionOfAliens.oneShot();
            allBalls.add(alienBall);
            sprites.addSprite(alienBall);
            this.coolDownEnemy = 0.5;
        }
        if (this.coolDownPlayer < 0 && this.keyboard.isPressed("space")) {
            Ball playerBall = this.paddle.oneShoot(this.environment);
            allBalls.add(playerBall);
            sprites.addSprite(playerBall);
            this.coolDownPlayer = 0.35;
        }
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        this.collectionOfAliens.notifyAllTimePassed(dt);
        if (this.keyboard.isPressed("p")) {
            Animation pause = new PauseScreen(this.keyboard);
            Animation p = new KeyPressStoppableAnimation(this.keyboard, "space", pause);
            this.runner.run(p);
        }
        if (this.getCounterBlocks().getValue() == 0) {
            this.running = false;
            cleanBalls();
        }
        if (this.getCollectionOfAliens().isAtBottomLimit() || this.paddle.getIsHit()) {
            this.running = false;
            this.counterLives.decrease(1);
            this.paddle.setIsHit(false);
            cleanBalls();
        }
    }

    /**
     * This func clean's the balls from the screen.
     */
    public void cleanBalls() {
        for (int i = 0; i < this.allBalls.size(); i++) {
            this.sprites.getSpriteList().remove(this.allBalls.get(i));
        }
    }

    /**
     * @return boolean val, end the turn or not.
     * This func will be in charge of stopping this turn conditions.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * @param c the collidable object.
     *          remove the specific collidable.
     */
    public void removeCollidable(Collidable c) {
        this.environment.getCollidableList().remove(c);
    }

    /**
     * @param s the spritee sent to the func.
     *          Remove tthe spriite.
     */
    public void removeSprite(Sprite s) {
        this.sprites.getSpriteList().remove(s);
    }

    /**
     * @return the counter of the score.
     */
    public Counter getCounterScore() {
        return this.counterScore;
    }

    /**
     * @return the counter of the blocks.
     */
    public Counter getCounterBlocks() {
        return this.counterBlocks;
    }

    /**
     * @return the counter of the lives.
     */
    public Counter getCounterLives() {
        return this.counterLives;
    }

}
