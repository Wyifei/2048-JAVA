package game;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class BoardTest {

	/** Builds a board and overwrites it with the given values (0 = empty); no random tile is added. */
	private static Board board(int[][] v) {
		Board b = new Board(v.length, new Random(1));
		for (int r = 0; r < v.length; r++) {
			for (int c = 0; c < v.length; c++) {
				b.setTileAt(r, c, new Tile(v[r][c]));
			}
		}
		b.syncEmptyTiles();
		return b;
	}

	private static int[] row(Board b, int r) {
		int[] out = new int[b.getSize()];
		for (int c = 0; c < out.length; c++) out[c] = b.getTileAt(r, c).getValue();
		return out;
	}

	private static int[] col(Board b, int c) {
		int[] out = new int[b.getSize()];
		for (int r = 0; r < out.length; r++) out[r] = b.getTileAt(r, c).getValue();
		return out;
	}

	private static int count(Board b) {
		int n = 0;
		for (int r = 0; r < b.getSize(); r++)
			for (int c = 0; c < b.getSize(); c++)
				if (b.getTileAt(r, c).getValue() != 0) n++;
		return n;
	}

	@Test
	public void newBoardStartsWithTwoTilesOfTwoOrFour() {
		Board b = new Board(4, new Random(42));
		assertEquals(2, count(b));
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < 4; c++) {
				int v = b.getTileAt(r, c).getValue();
				assertTrue(v == 0 || v == 2 || v == 4);
			}
	}

	@Test
	public void sameSeedGivesSameBoard() {
		Board a = new Board(4, new Random(7));
		Board b = new Board(4, new Random(7));
		for (int r = 0; r < 4; r++) assertArrayEquals(row(a, r), row(b, r));
	}

	@Test
	public void respectsCustomBoardSize() {
		Board b = new Board(5, new Random(3));
		assertEquals(5, b.getSize());
		assertEquals(5, b.getTiles().size());
		assertEquals(2, count(b));
	}

	@Test
	public void moveLeftSlidesAndMerges() {
		Board b = board(new int[][] {
			{2, 2, 0, 0},
			{0, 2, 0, 2},
			{2, 2, 2, 2},
			{4, 0, 0, 4}});
		b.moveLeft();
		assertArrayEquals(new int[] {4, 0, 0, 0}, row(b, 0));
		assertArrayEquals(new int[] {4, 0, 0, 0}, row(b, 1));
		assertArrayEquals(new int[] {4, 4, 0, 0}, row(b, 2));
		assertArrayEquals(new int[] {8, 0, 0, 0}, row(b, 3));
		assertEquals(4 + 4 + 8 + 8, b.getScore());
	}

	@Test
	public void moveRightSlidesAndMerges() {
		Board b = board(new int[][] {
			{2, 2, 0, 0},
			{4, 0, 0, 0},
			{2, 2, 4, 4},
			{2, 4, 4, 0}});
		b.moveRight();
		assertArrayEquals(new int[] {0, 0, 0, 4}, row(b, 0));
		assertArrayEquals(new int[] {0, 0, 0, 4}, row(b, 1));
		assertArrayEquals(new int[] {0, 0, 4, 8}, row(b, 2));
		assertArrayEquals(new int[] {0, 0, 2, 8}, row(b, 3));
	}

	@Test
	public void moveUpSlidesAndMerges() {
		Board b = board(new int[][] {
			{2, 0, 4, 0},
			{2, 0, 0, 2},
			{0, 4, 4, 0},
			{0, 4, 0, 2}});
		b.moveUp();
		assertArrayEquals(new int[] {4, 0, 0, 0}, col(b, 0));
		assertArrayEquals(new int[] {8, 0, 0, 0}, col(b, 1));
		assertArrayEquals(new int[] {8, 0, 0, 0}, col(b, 2));
		assertArrayEquals(new int[] {4, 0, 0, 0}, col(b, 3));
	}

	@Test
	public void moveDownSlidesAndMerges() {
		Board b = board(new int[][] {
			{2, 0, 4, 0},
			{2, 0, 0, 2},
			{0, 4, 4, 0},
			{0, 4, 0, 2}});
		b.moveDown();
		assertArrayEquals(new int[] {0, 0, 0, 4}, col(b, 0));
		assertArrayEquals(new int[] {0, 0, 0, 8}, col(b, 1));
		assertArrayEquals(new int[] {0, 0, 0, 8}, col(b, 2));
		assertArrayEquals(new int[] {0, 0, 0, 4}, col(b, 3));
	}

	@Test
	public void tileMergesOnlyOncePerMove() {
		Board b = board(new int[][] {
			{4, 2, 2, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}});
		b.moveLeft();
		assertArrayEquals(new int[] {4, 4, 0, 0}, row(b, 0));
	}

	@Test
	public void moveThatChangesBoardSpawnsOneTile() {
		Board b = board(new int[][] {
			{0, 0, 0, 2},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}});
		b.moveLeft();
		b.isGameOver();
		assertEquals(2, count(b));
		assertNull(b.getWonOrLost());
	}

	@Test
	public void moveThatChangesNothingSpawnsNoTile() {
		Board b = board(new int[][] {
			{2, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}});
		b.moveLeft();
		b.isGameOver();
		assertEquals(1, count(b));
	}

	@Test
	public void reaching2048Wins() {
		Board b = board(new int[][] {
			{1024, 1024, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}});
		b.moveLeft();
		b.isGameOver();
		assertEquals("WON", b.getWonOrLost());
		assertEquals(2048, b.getScore());
	}

	@Test
	public void fullBoardWithNoMergesLoses() {
		Board b = board(new int[][] {
			{2, 4, 2, 4},
			{4, 2, 4, 2},
			{2, 4, 2, 4},
			{4, 2, 4, 2}});
		b.moveLeft();
		b.isGameOver();
		assertEquals("LOST", b.getWonOrLost());
	}

	@Test
	public void fullBoardWithPossibleMergeKeepsPlaying() {
		Board b = board(new int[][] {
			{2, 2, 4, 8},
			{4, 8, 16, 32},
			{8, 16, 32, 64},
			{16, 32, 64, 128}});
		b.moveRight(); // merges 2+2 -> 4, frees one cell and spawns a tile
		b.isGameOver();
		assertNull(b.getWonOrLost());
		assertEquals(16, count(b));
	}
}
