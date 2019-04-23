package running;

import animation.HighScoresAnimation;
import animation.MenuAnimation;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import inerfaces.Animation;
import inerfaces.LevelInformation;
import inerfaces.Menu;
import inerfaces.Task;
import levels.AnimationRunner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the game.
 */
public class Ass7Game {

    /**
     * @param args parameters to the main.
     *             The main func of the game who runs the game.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Game", 800, 600);
        while (true) {
            List<LevelInformation> levelList = new ArrayList<LevelInformation>();
            AnimationRunner r = new AnimationRunner(gui);
            KeyboardSensor k = gui.getKeyboardSensor();
            HighScoresTable table = new HighScoresTable(5);
            File fileName = new File("highscores");
            GameFlow gameFlow = new GameFlow(r, k, gui, table, fileName);
            List<LevelInformation> levelInformationList = new ArrayList<LevelInformation>();
            table = HighScoresTable.loadFromFile(fileName);
            Menu<Task<Void>> menu = new MenuAnimation<Task<Void>>("Space Invaders", k);
            Animation highScoresAnimation = new HighScoresAnimation(table, "space", k, false);
            menu.addSelection("s", "(s) Start Game", new RunGameTask(gameFlow, levelInformationList));
            menu.addSelection("h", "(h) High Scores", new ShowHiScoresTask(r, highScoresAnimation));
            menu.addSelection("e", "(e) Exit Scores", new ExitTask());

            r.run(menu);
            Task<Void> task = menu.getStatus();
            task.run();
        }
    }

}
