package org.systemexception.lunarlander.test;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.physics.GameEngine;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author leo
 * @date 30/12/15 14:52
 */
public class GameEngineTest {

	private static GameEngine sut;

	@BeforeClass
	public static void setSut() {
		Gdx.graphics = mock(Graphics.class);
		Gdx.audio = mock(Audio.class);
		Gdx.gl20 = mock(GL20.class);
		Gdx.input = mock(Input.class);
		Music soundThruster = mock(Music.class);
		sut = new GameEngine(soundThruster, soundThruster);
		sut.setUpObjects();
	}

	@Test
	public void game_has_all_objects() {
		assertTrue(6 == sut.getBodies().size());
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

//	@Test
//	public void fail() {
//		assertTrue(false);
//	}

}