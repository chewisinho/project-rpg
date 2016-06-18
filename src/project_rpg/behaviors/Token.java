package project_rpg.behaviors;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import project_rpg.BattleGrid;
import project_rpg.Skill;
import project_rpg.behaviors.SkillToken;
import static project_rpg.BattleGrid.RIGHT_ANGLE;

/** Represents a token on the _grid.map.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Token implements Runnable {

    /** Creates a new token with IMAGE at (X, Y). */
    public Token(BattleGrid grid, int x, int y, String image) {
        _image = _grid.getImage(image);
        buffered = _grid.toBufferedImage(_image.getImage());
        _x = x;
        _y = y;
        _grid = grid;
        orientation = 0;
        _grid.map[x][y] = this;
        lastAction = System.currentTimeMillis();
        lastMovement = System.currentTimeMillis();
    }

    /** Returns false, since the token is not an exit. */
    public boolean isExit() {
        return false;
    }

    /** Returns my x-coordinate. */
    public int x() {
        return _x;
    }

    /** Returns my y-coordinate. */
    public int y() {
        return _y;
    }

    /** Returns my orientation. */
    public int orientation() {
        return orientation;
    }

    /** Move one space down. */
    public void down() {
        move(_x, _y + 1);
        orientation = RIGHT_ANGLE;
    }

    /** Move one space left. */
    public void left() {
        move(_x - 1, _y);
        orientation = RIGHT_ANGLE * 2;
    }

    /** Move one space right. */
    public void right() {
        move(_x + 1, _y);
        orientation = 0;
    }

    /** Move one space up. */
    public void up() {
        move(_x, _y - 1);
        orientation = RIGHT_ANGLE * 3;
    }

    /** Makes a movement to (X, Y). */
    public void move(int x, int y) {
        if (_grid.inBounds(x, y)) {
            _grid.exit(this, x, y);
        }
        if (_grid.valid(x, y) && System.currentTimeMillis() - lastMovement > 100) {
            _grid.map[_x][_y] = null;
            _x = x;
            _y = y;
            _grid.map[_x][_y] = this;
            lastMovement = System.currentTimeMillis();
            _grid.repaint();
        }
    }

    /** Removes the token from the map. */
    protected void disappear() {
        _grid.map[_x][_y] = null;
    }

    /** Attacks. */
    public void attack() {
        int[] dir = orientationToArray(orientation);
        int x = _x + dir[0], y = _y + dir[1];
        Skill currentSkill = _grid._player.getBattleSkill();
        if (currentSkill != null && _grid.valid(x, y) && 
        	    _grid._player.hasEnoughMP(currentSkill.getCost()) &&
        	    System.currentTimeMillis() - lastAction > currentSkill.getCooldown()) {
        	Token token = new SkillToken(
                currentSkill.getImage(),
                x,
                y,
                _grid,
                dir[0],
                dir[1],
                currentSkill
            );
            token.orientation = orientation;
            _grid.map[x][y] = token;
            lastAction = System.currentTimeMillis();
            new Thread(token).start();
        }
        _grid.repaint();
    }

    /** Moves towards the player. */
    public void moveTowardsPlayer() {
        if (_grid.playerToken.x() > _x) {
            right();
        } else if (_grid.playerToken.x() < _x) {
            left();
        } else {
            if (_grid.playerToken.y() > _y) {
                down();
            } else if (_grid.playerToken.y() < _y) {
                up();
            }
        }
    }

    /** Returns my buffered image. */
    public BufferedImage bufferedImage() {
        return buffered;
    }

    /** Returns my image. */
    public ImageIcon image() {
        return _image;
    }

    @Override
    public void run() {
        // Do nothing.
    }

    /** Takes an ORIENTATION and returns an integer array representing the
     *  direction.
     */
    public static int[] orientationToArray(int orientation) {
        int[] direction = { 0, 0 };
        switch (orientation) {
        case 0:
            direction[0] = 1;
            break;
        case RIGHT_ANGLE:
            direction[1] = 1;
            break;
        case RIGHT_ANGLE * 2:
            direction[0] = -1;
            break;
        case RIGHT_ANGLE * 3:
            direction[1] = -1;
            break;
        default:
            break;
        }
        return direction;
    }

    /** My coordinates and orientation. */
    protected int _x, _y, orientation;

    /** Contains the BattleGrid in which the Token is found. */
    protected BattleGrid _grid;

    /** Contains my image. */
    private ImageIcon _image;

    /** Contains my buffered image. */
    private BufferedImage buffered;

    /** Records the time of the last action and movement for cooldown tracking. */
    protected long lastAction, lastMovement;

}

