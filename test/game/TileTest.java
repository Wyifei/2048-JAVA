package game;

import static org.junit.Assert.*;

import org.junit.Test;

public class TileTest {

	@Test
	public void defaultTileIsEmpty() {
		assertTrue(new Tile().isEmpty());
		assertEquals(0, new Tile().getValue());
	}

	@Test
	public void mergingDoublesValue() {
		Tile t = new Tile(4);
		assertEquals(8, t.merging());
		assertEquals(8, t.getValue());
	}

	@Test
	public void hasMovedDetectsPositionChange() {
		Tile t = new Tile(2, 1, 1);
		assertFalse(t.hasMoved(1, 1));
		assertTrue(t.hasMoved(1, 2));
		assertTrue(t.hasMoved(0, 1));
	}

	@Test
	public void emptyTileNeverCountsAsMoved() {
		Tile t = new Tile();
		t.setPosition(0, 0);
		assertFalse(t.hasMoved(3, 3));
	}
}
