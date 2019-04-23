package objects;

import animation.GameLevel;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Point;
import geometry.Rectangle;
import inerfaces.Collidable;
import inerfaces.HitListener;
import inerfaces.HitNotifier;
import inerfaces.Sprite;
import running.GameEnvironment;
import running.Velocity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The paddle will be collidable, so each ball who will collide with him
 * will bounce back. further more the block will implement the sprite interface,
 * so he can be drawn on the screen. The ball can move and will return balls the hits him
 * different from regular block.
 */
public class Paddle implements Sprite, Collidable, HitNotifier {
    public static final int PADDLE_LEFT_BORDER = 0;
    public static final int PADDLE_RIGHT_BORDER = 800;

    private Rectangle paddle;
    private Block block;
    private biuoop.KeyboardSensor keyboard;
    private java.awt.Color color;
    private int paddleMovement;
    private ArrayList<HitListener> hitListeners;
    private boolean isHit;


    /**
     * @param paddleMovement the movment of the paddle.
     * @param rectangle      the shape of the paddle.
     * @param keyboard       the paddle option to move by the keyboard.
     * @param color          the paddle's color.
     *                       All the paddle's members.
     */
    public Paddle(Rectangle rectangle, biuoop.KeyboardSensor keyboard, java.awt.Color color, int paddleMovement) {
        this.paddle = new Rectangle(rectangle.getUpperLeft(), rectangle.getWidth(), rectangle.getHeight());
        this.block = new Block(this.paddle.getUpperLeft(), this.paddle.getWidth(), this.paddle.getHeight(), 1);
        this.keyboard = keyboard;
        this.color = color;
        this.paddleMovement = paddleMovement;
        this.isHit = false;
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
     * @param val set to true or false.
     */
    public void setIsHit(boolean val) {
        this.isHit = val;
    }

    /**
     * @param p new point.
     *          This func reset the paddle position.
     */
    public void resetPaddle(Point p) {
        this.paddle.setRectangle(p);
        this.block.setBlock(p);
    }

    /**
     * @return if paddle got hit.
     */
    public boolean getIsHit() {
        return this.isHit;
    }

    /**
     * @param dt frame speed.
     *           The func will move the paddle to the left.
     */
    public void moveLeft(double dt) {
        this.paddle.setRectangle(new Point(paddle.getUpperLeft().getX()
                - paddleMovement * dt, paddle.getUpperLeft().getY()));
        this.block.setBlock(new Point(paddle.getUpperLeft().getX()
                - paddleMovement * dt, paddle.getUpperLeft().getY()));
    }

    /**
     * @param dt frame speed.
     *           The func will move the paddle to the right.
     */
    public void moveRight(double dt) {
        this.paddle.setRectangle(new Point(paddle.getUpperLeft().getX()
                + paddleMovement * dt, paddle.getUpperLeft().getY()));
        this.block.setBlock(new Point(paddle.getUpperLeft().getX()
                + paddleMovement * dt, paddle.getUpperLeft().getY()));
    }

    /**
     * @param dt frame speed.
     *           The func will put limits to the paddle.
     *           if he get's to the left border of the screen,
     *           we will cannot move him anymore.
     */
    public void timePassed(double dt) {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) { // Left limit.
            if (paddle.getUpperLeft().getX() > PADDLE_LEFT_BORDER) {
                moveLeft(dt);
            }
        } else if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) { // Right limit.
            if (paddle.getUpperLeft().getX() + paddle.getWidth() < PADDLE_RIGHT_BORDER) {
                moveRight(dt);
            }
        }
    }

    /**
     * @param d the drawn surface.
     *          The func will draw the paddle on screen.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle((int) this.paddle.getUpperLeft().getX(), (int) this.paddle.getUpperLeft().getY(),
                (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
        d.setColor(Color.BLACK);
        d.drawRectangle((int) this.paddle.getUpperLeft().getX(), (int) this.paddle.getUpperLeft().getY(),
                (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
    }


    /**
     * @param hitter          the actual hitting object.
     * @param collisionPoint  collision with the object.
     * @param currentVelocity the velocity of some object before the object have been collided.
     * @return Velocity of the vall.
     * Set the bool val to true - means the paddle was hit.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.isHit = true;
        notifyHit(hitter);
        return currentVelocity;
    }

    /**
     * @param g the surface, the blocks will be drawn on.
     *          The func will add the block to the collidable
     *          and the sprite in the game class.
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * @return the paddle.
     * Returns the paddle itself.
     */
    public Rectangle getCollisionRectangle() {
        return this.paddle;
    }

    /**
     * @param game the game level.
     * @return ball.
     * This func creates a ball, located on top of the paddle and return the ball.
     */
    public Ball oneShoot(GameEnvironment game) {
        Ball ball = new Ball(new Point(this.paddle.getUpperLeft().getX() + 40,
                this.paddle.getUpperLeft().getY() - 5), 2, Color.white, game);
        ball.setVelocity(Velocity.fromAngleAndSpeed(270, 500));
        return ball;
    }

    /**
     * @param hl the one who will "hear" hits.
     *           Add new listener.
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * @param hl the one who will "hear" hits.
     *           Remove the listener.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }


    /**
     * @param hitter the ball who hit's this block.
     *               This func will notify all the hit listeners that was a hit.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this.block, hitter);
        }
    }
}
