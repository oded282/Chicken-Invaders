package objects;

import geometry.Point;
import running.ColorBackground;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible of the shield, who protects the player paddle.
 */
public class Shield {

    private int xVal;
    private int yVal;
    private List<Block> theShield = new ArrayList<>();

    /**
     * @param x position.
     * @param y position.
     *          The constructor of the shield.
     */
    public Shield(int x, int y) {
        this.xVal = x;
        this.yVal = y;
    }

    /**
     * @return list of block creating one shield.
     */
    public List<Block> getShieldList() {
        return this.theShield;
    }

    /**
     * @return the size of the list shield.
     */
    public int getShieldSize() {
        return this.theShield.size();
    }

    /**
     * this func creates all the shield by setting the blocks position.
     */
    public void setTheShield() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 35; j++) {
                theShield.add(new Block(new Point(this.xVal + j * 4, this.yVal + i * 4), 4,
                        4, new ColorBackground(Color.CYAN), 1));
                theShield.add(new Block(new Point(270 + this.xVal + j * 4, this.yVal + i * 4), 4,
                        4, new ColorBackground(Color.CYAN), 1));
                theShield.add(new Block(new Point(540 + this.xVal + j * 4, this.yVal + i * 4), 4,
                        4, new ColorBackground(Color.CYAN), 1));
            }
        }
    }
}
