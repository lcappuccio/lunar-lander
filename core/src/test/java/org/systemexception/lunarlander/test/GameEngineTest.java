package org.systemexception.lunarlander.test;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Body;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.physics.GameEngine;
import org.systemexception.lunarlander.physics.MathUtils;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author leo
 * @date 30/12/15 14:52
 */
public class GameEngineTest {

	private static GameEngine sut;

	@Before
	public void setSut() {
		Gdx.graphics = mock(Graphics.class);
		Gdx.audio = mock(Audio.class);
		Gdx.gl20 = mock(GL20.class);
		Gdx.input = mock(Input.class);
		Music soundThruster = mock(Music.class);
		sut = new GameEngine(soundThruster, soundThruster);
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

	@Test
	public void thrust_applied_burns_fuel() {
		Body body = sut.getBodies().get(BodiesNames.BOX_BODY);
		HashMap<Object, Object> userData = (HashMap<Object, Object>) body.getUserData();
		float fuel1 = (float) userData.get(BodiesNames.FUEL_AMOUNT);
		when(Gdx.input.isKeyPressed(Input.Keys.PERIOD)).thenReturn(true);
		sut.logic();
		float fuel2 = (float) userData.get(BodiesNames.FUEL_AMOUNT);
		assertTrue(fuel2 < fuel1);
	}

	@Test
	public void no_thrust_applied_keeps_fuel() {
		Body body = sut.getBodies().get(BodiesNames.BOX_BODY);
		HashMap<Object, Object> userData = (HashMap<Object, Object>) body.getUserData();
		float fuel1 = (float) userData.get(BodiesNames.FUEL_AMOUNT);
		sut.logic();
		float fuel2 = (float) userData.get(BodiesNames.FUEL_AMOUNT);
		assertTrue(fuel2 == fuel1);
	}

	@Test
	public void rotate_left() {
		Body body = sut.getBodies().get(BodiesNames.BOX_BODY);
		body.setTransform(body.getPosition(), 0.5f);
		when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
		double angle1 = MathUtils.normalRelativeAngle(body.getAngle());
		sut.logic();
		double angle2 = MathUtils.normalRelativeAngle(body.getAngle());
		assertTrue(angle1 > angle2);
	}

	@Test
	public void rotate_right() {
		Body body = sut.getBodies().get(BodiesNames.BOX_BODY);
		body.setTransform(body.getPosition(), 0.5f);
		when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
		double angle1 = MathUtils.normalRelativeAngle(body.getAngle());
		sut.logic();
		double angle2 = MathUtils.normalRelativeAngle(body.getAngle());
		assertTrue(angle1 < angle2);
	}

	@Test
	public void acceleration_is_not_zero() {
		Body body = sut.getBodies().get(BodiesNames.BOX_BODY);
		HashMap<String, Object> userData = (HashMap<String, Object>) body.getUserData();
		sut.logic();
		float v1 = (float) userData.get(BodiesNames.V1);
		sut.logic();
		float v2 = (float) userData.get(BodiesNames.V2);
		assertTrue(v1 != v2);
	}

}