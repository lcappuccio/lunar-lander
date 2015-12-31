package org.systemexception.lunarlander.test;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.physics.GameEngine;

import static org.junit.Assert.assertTrue;

/**
 * @author leo
 * @date 30/12/15 14:52
 */
public class GameEngineTest {

	private GameEngine sut;

	@Before
	public void setSut() {
		sut = new GameEngine(null,null);
		sut.setUpObjects();
	}

	@Test
	public void game_has_all_objects() {
		assertTrue(7 == sut.getBodies().size());
	}

	@Test
	public void object_free_fall() {
		for (int i = 0; i < 100; i++) {
			float y1 = sut.getBodies().get(BodiesNames.BOX_HEAD).getPosition().y;
			sut.logic();
			float y2 = sut.getBodies().get(BodiesNames.BOX_HEAD).getPosition().y;
			assertTrue(y1 > y2);
		}
	}

	@Test
	public void fail() {
		assertTrue(false);
	}

}