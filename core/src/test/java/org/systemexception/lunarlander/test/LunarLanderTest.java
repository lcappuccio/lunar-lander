package org.systemexception.lunarlander.test;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import org.systemexception.lunarlander.LunarLander;
import org.systemexception.lunarlander.physics.GameEngine;

import static org.mockito.Mockito.mock;

/**
 * @author leo
 * @date 02/01/16 02:35
 */
//@RunWith(GdxTestRunner.class)
public class LunarLanderTest extends LunarLander {

	private LunarLander sut;
	private static GameEngine gameEngine;

	public LunarLanderTest() {
		super(gameEngine);
	}

	@BeforeClass
	public static void setUp() {
		Music soundThruster = mock(Music.class);
		gameEngine = new GameEngine(soundThruster, soundThruster);
		Gdx.graphics = mock(Graphics.class);
		Gdx.audio = mock(Audio.class);
		Gdx.gl20 = mock(GL20.class);
		Gdx.input = mock(Input.class);
		Gdx.gl = new MockGL20();
	}

	@Test
	@Ignore
	public void init() throws InitializationError {
		sut = new LunarLander(gameEngine);
		sut.create();
	}
}