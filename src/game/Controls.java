package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();

		boolean move = !Game.BOARD.isOver() && (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN
				|| keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT);
		if (move) {
			Game.BOARD.saveState();
		}

		switch (keyCode) {
			case KeyEvent.VK_UP:
				Game.BOARD.moveUp();
				break;
			case KeyEvent.VK_DOWN:
				Game.BOARD.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				Game.BOARD.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				Game.BOARD.moveRight();
				break;
			case KeyEvent.VK_U:
				Game.BOARD.undo();
				break;
			case KeyEvent.VK_R:
				Game.BOARD.restart();
				break;
			case KeyEvent.VK_ESCAPE:
				Game.WINDOW.dispose();
				break;
			default:
				break;
		}
		
		if (move) {
			Game.BOARD.discardStateIfUnchanged();
			Game.BOARD.isGameOver();
		}
		//Game.BOARD.show();
		Game.WINDOW.repaint();
		
	}

	public void bind() {
		Game.WINDOW.addKeyListener(this);
	}

	public void unbind() {
		Game.WINDOW.removeKeyListener(this);
	}

}
