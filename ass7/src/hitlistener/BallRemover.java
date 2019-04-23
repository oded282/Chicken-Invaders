package hitlistener;

import animation.GameLevel;
import inerfaces.HitListener;
import objects.Ball;
import objects.Block;

/**
 * This class implements HitListener So it can "hear" notification from hitting objects.
 * And remove balls that got out of the bounds.
 */
public class BallRemover implements HitListener {
    private GameLevel game;


    /**
     * @param game the level who sent to the constructor.
     */
    public BallRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * @param beingHit the block which being hit.
     * @param hitter   the object who hit the block.
     *                 This func will "hear" that the block was being hit,
     *                 and disappear this ball.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.voidremoveFromGame(this.game);
    }

}
