package objects;

import animation.GameLevel;
import biuoop.DrawSurface;
import geometry.Point;
import inerfaces.Sprite;
import running.ImageBackground;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The alien class hides a field of block, and can be move by his default speed.
 */
public class Alien implements Sprite {

    public static final int NUMBER_HITS = 1;
    public static final double DEF_SPEED = 100;
    private double downwardMovement;
    private double leftRightMovement;
    private boolean moveRight;
    private boolean moveDown;
    private Block block;

    /**
     *
     * @param x alien pox.
     * @param y alien pox.
     *          The constructor of the alien.
     */
    public Alien(double x, double y) {
        String s = "enemy.png";
        this.block = new Block(new Point(x, y), 40, 30, NUMBER_HITS);
        String path = "enemy.png";
        File file = new File(path);
        Image img = null;
        try {
            if (file.exists()) {
                img = ImageIO.read(file);
            } else {
                InputStream inputStream = ClassLoader.getSystemResourceAsStream(path);
                img = ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            System.out.println("no image");
        }
        this.block.setBackgroud(new ImageBackground(img));
        this.moveRight = true;
        this.moveDown = false;
        this.leftRightMovement = DEF_SPEED;
        this.downwardMovement = 30;

    }

    /**
     *
     * @return the block alien hides.
     */
    public Block getBlock() {
        return block;
    }

    /**
     *
     * @return the lowest limit an alien can be positioned.
     */
    public double getLowestLimY() {
        return this.block.getBlock().getUpperLeft().getY() + this.block.getBlock().getHeight();
    }

    /**
     *
     * @param movement the speed of the alien.
     *                 sets the alien speed.
     */
    public void setSpeedBlock(double movement) {
        this.leftRightMovement = movement;
    }

    /**
     * Increase alien movement.
     */
    public void setLeftRightMovement() {
        this.leftRightMovement = this.leftRightMovement * 1.1;
    }

    /**
     *
     * @return the speed val.
     */
    public double getLeftRightMovmenet() {
        return this.leftRightMovement;
    }

    /**
     *
     * @param movesRight speed of alien.
     *                   set the speed of the alien by value.
     */
    public void setMoveRight(boolean movesRight) {
        this.moveRight = movesRight;
    }

    /**
     *
     * @param movesDown bool val.
     *                  changing the bool value.
     */
    public void setMoveDown(boolean movesDown) {
        this.moveDown = movesDown;
    }


    /**
     *
     * @return the right limit of the alien.
     */
    public double getRightLimit() {
        return this.block.getBlock().getUpperLeft().getX() + this.block.getWidth();
    }

    /**
     *
     * @return the left limit of the alien.
     */
    public double getLeftLimit() {
        return this.block.getBlock().getUpperLeft().getX();
    }

    /**
     *
     * @param d the surface.
     *          draws the alien on the surface.
     */

    @Override
    public void drawOn(DrawSurface d) {
        this.block.drawOn(d);
    }

    /**
     *
     * @param dt frame speed.
     *           sets the alien position.
     *           if he went to the laft/right limit he will be positioned back to his boundaries.
     */
    @Override
    public void timePassed(double dt) {
        if (this.moveRight) {
            this.block.setBlock(new Point(this.block.getBlock().getUpperLeft().getX() + leftRightMovement * dt,
                    this.block.getBlock().getUpperLeft().getY()));
            if (moveDown) {
                this.block.setBlock(new Point(this.block.getBlock().getUpperLeft().getX(),
                        this.block.getBlock().getUpperLeft().getY() + downwardMovement));
                this.moveDown = false;
            }
        }
        if (!this.moveRight) {
            this.block.setBlock(new Point(this.block.getBlock().getUpperLeft().getX() - leftRightMovement * dt,
                    this.block.getBlock().getUpperLeft().getY()));
            if (moveDown) {
                this.block.setBlock(new Point(this.block.getBlock().getUpperLeft().getX(),
                        this.block.getBlock().getUpperLeft().getY() + downwardMovement));
                this.moveDown = false;
            }
        }
    }

    /**
     *
     * @param game the class where the game start's
     *             adding alien as sprite and his block as collidables.
     */
    @Override
    public void addToGame(GameLevel game) {
        game.addSprite(this);
        game.addCollidable(this.block);
    }

    /**
     *
     * @param game the game.
     *             remove the alien from the sprites, and his block from the collidables.
     */
    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this.block);
        game.removeSprite(this);
    }
}
