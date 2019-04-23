package running;

import animation.GameLevel;
import geometry.Point;
import objects.Alien;
import objects.Ball;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The alien collection got a list of aliens in specific structure.
 */
public class AlienCollection {
    private List<List<Alien>> alienList;
    private GameEnvironment gameEnvironment;
    private SpriteCollection spriteCollection;
    private GameLevel gameLevel;
    private boolean isAtBottomLimit;

    /**
     * @param game      the enviorment.
     * @param sprite    the sprite collection.
     * @param gameLevel the game level.
     *                  The constructor.
     */
    public AlienCollection(GameEnvironment game, SpriteCollection sprite, GameLevel gameLevel) {
        this.alienList = new ArrayList<>();
        this.gameEnvironment = game;
        this.spriteCollection = sprite;
        this.isAtBottomLimit = false;
        this.gameLevel = gameLevel;
    }

    /**
     * @return the speed of the aliens structure.
     */
    public double getAlinesSpeed() {
        return this.alienList.get(this.alienList.size() - 1).get(
                this.alienList.get(this.alienList.size() - 1).size() - 1).getLeftRightMovmenet();
    }

    /**
     * @param speed the speed we want to set.
     */
    public void speedReset(double speed) {
        for (int i = 0; i < this.alienList.size(); i++) {

            for (int j = 0; j < this.alienList.get(i).size(); j++) {

                this.alienList.get(i).get(j).setSpeedBlock(speed);
            }
        }
    }

    /**
     * @return bool val, if the structure is at his bottom limit.
     */
    public boolean isAtBottomLimit() {
        return this.isAtBottomLimit;
    }

    /**
     * This func will speed up the structure movement.
     */
    public void setTheSpeed() {
        for (int i = 0; i < alienList.size(); i++) {
            for (int j = 0; j < alienList.get(i).size(); j++) {
                this.alienList.get(i).get(j).setLeftRightMovement();
            }
        }
    }

    /**
     * This func will set the structure position.
     */
    public void setToStart() {
        int x = 40;
        int y = 40;
        int widthJump = 50;
        int heightJump = 40;

        for (int i = 0; i < alienList.size(); i++) {

            for (int j = 0; j < alienList.get(i).size(); j++) {
                this.alienList.get(i).get(j).getBlock().setBlock(new Point(x + widthJump * i,
                        y + j * heightJump));
            }
        }
    }

    /**
     * @param dt the speed. of the frame.
     *           This func will set alien by alien position,
     *           so that all the structure will move as one piece.
     *           Case we got to the limit we will change direction.
     */

    public void notifyAllTimePassed(double dt) {

        boolean rightLim = alienList.get(alienList.size() - 1).get(0).getRightLimit() >= 800;
        boolean leftLim = alienList.get(0).get(0).getLeftLimit() <= 0;
        this.isAtBottomLimit = false;

        for (int i = 0; i < this.alienList.size(); i++) {
            for (int j = 0; j < alienList.get(i).size(); j++) {
                if (rightLim) {
                    this.alienList.get(i).get(j).setMoveDown(true);
                    this.alienList.get(i).get(j).setMoveRight(false);
                    this.alienList.get(i).get(j).setLeftRightMovement();
                }
                if (leftLim) {
                    this.alienList.get(i).get(j).setMoveDown(true);
                    this.alienList.get(i).get(j).setMoveRight(true);
                    this.alienList.get(i).get(j).setLeftRightMovement();
                }
                if (this.alienList.get(i).get(j).getBlock().getNumOfHits() == 0) {
                    this.alienList.get(i).get(j).removeFromGame(this.gameLevel);
                    this.alienList.get(i).remove(alienList.get(i).get(j));
                    if (this.alienList.get(i).isEmpty()) {
                        this.alienList.remove(i);
                        break;
                    }
                    continue;
                }
                if (alienList.get(i).get(j).getLowestLimY() >= 500) {
                    this.isAtBottomLimit = true;
                }
            }
        }
    }

    /**
     * @return list of aliens.
     * This func creats the aliens list.
     */
    public List<List<Alien>> initialList() {
        int x = 40;
        int y = 40;
        int widthJump = 50;
        int heightJump = 40;


        for (int i = 0; i < 10; i++) {
            List<Alien> alienLinked = new LinkedList<Alien>();
            this.alienList.add(alienLinked);
            for (int j = 0; j < 5; j++) {
                alienLinked.add(new Alien(x + widthJump * i, y + j * heightJump));
            }
        }
        return this.alienList;
    }

    /**
     * @return ball.
     * This func will create ball and return it back.
     */
    public Ball oneShot() {
        Random rand = new Random();
        int randomInt = rand.nextInt(this.alienList.size());
        Point p = this.alienList.get(randomInt).get(
                this.alienList.get(randomInt).size() - 1).getBlock().getBlock().getUpperLeft();
        Point point = new Point(p.getX() + 20, p.getY() + 35);
        Ball ball = new Ball(point, 3, Color.red, this.gameEnvironment);
        ball.setVelocity(0, 200);
        return ball;
    }
}
