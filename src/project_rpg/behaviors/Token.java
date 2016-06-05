package project_rpg.behaviors;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import project_rpg.BattleGrid;
import static project_rpg.BattleGrid.RIGHT_ANGLE;

/** Represents a token on the _grid.map.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Token implements Runnable {

    /** Creates a new token with IMAGE at (X, Y). */
    public Token(String image, int x, int y, BattleGrid grid) {
        _image = _grid.getImage(image);
        buffered = _grid.toBufferedImage(_image.getImage());
        _x = x;
        _y = y;
	_grid = grid;
        orientation = 0;
        _grid.map[x][y] = this;
        lastAction = System.currentTimeMillis();
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
        if (_grid.valid(x, y)) {
            _grid.map[_x][_y] = null;
            _x = x;
            _y = y;
            _grid.map[_x][_y] = this;
            _grid.repaint();
        }
    }

    /** Attacks. */
    public void attack() {
        // TODO
    }

    /** Switches to Skill 1. */
    public void switchAttack1() {
        // TODO
    }

    /** Switches to Skill 2. */
    public void switchAttack2() {
        // TODO
    }

    /** Switches to Skill 3. */
    public void switchAttack3() {
        // TODO
    }

    /** Switches to Skill 4. */
    public void switchAttack4() {
        // TODO
    }

    /** Moves towards the player. */
    void moveTowardsPlayer() {
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
        while (true) {
            if (System.currentTimeMillis() - lastAction > 250) {
                if (_grid.playerAdjacentTo(_x, _y)) {
                    _grid._player.reduceHealth(_grid.monsters.get(this).attack());
                    if (_grid._player.isDead()) {
                        _grid._gui.gameOver();
                        break;
                    }
                } else {
                    moveTowardsPlayer();
                }
                _grid._gui.refreshMenu();
                lastAction = System.currentTimeMillis();
            }
        }
    }

    /** My coordinates and orientation. */
    private int _x, _y, orientation;

    /** Contains the BattleGrid in which the Token is found. */
    private BattleGrid _grid;

    /** Contains my image. */
    private ImageIcon _image;

    /** Contains my buffered image. */
    private BufferedImage buffered;

    /** Contains the time of my last action. */
    private long lastAction;

}

