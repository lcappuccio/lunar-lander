package org.systemexception.lunarlander.test;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.systemexception.lunarlander.LunarLander;

import static org.mockito.Mockito.mock;

/**
 * @author leo
 * @date 02/01/16 02:35
 */
@RunWith(GdxTestRunner.class)
public class LunarLanderTest {

	private LunarLander sut;

	@Before
	public void setUp() {
		Gdx.graphics = mock(Graphics.class);
		Gdx.audio = mock(Audio.class);
		Gdx.gl20 = new MockGL20();
		Gdx.input = mock(Input.class);
		Gdx.gl = new MockGL20();
	}

	@Test
	@Ignore
	public void init() {
		sut = new LunarLander();
		sut.create();
	}
}