package org.systemexception.lunarlander;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.constants.Dimensions;
import org.systemexception.lunarlander.physics.GameEngine;

public class LunarLander extends ApplicationAdapter {

	private OrthographicCamera camera;
	private GameEngine gameEngine;

	SpriteBatch spritebatch;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;

	@Override
	public void create() {
		spritebatch = new SpriteBatch();
		Sound soundThruster = Gdx.audio.newSound(Gdx.files.internal("thruster.ogg"));
		Sound soundRcs = Gdx.audio.newSound(Gdx.files.internal("rcs.ogg"));
		gameEngine = new GameEngine(soundThruster, soundRcs);
		gameEngine.setUpObjects();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		//Create a copy of camera projection matrix
		debugMatrix = new Matrix4(camera.combined);
		debugMatrix.scale(Dimensions.METERS_TO_PIXELS, Dimensions.METERS_TO_PIXELS, 1f);
		debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);

		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		gameEngine.logic();
		spritebatch.setProjectionMatrix(camera.combined);
		debugMatrix = spritebatch.getProjectionMatrix().cpy().scale(Dimensions.METERS_TO_PIXELS,
				Dimensions.METERS_TO_PIXELS, 0);
		spritebatch.begin();
		spritebatch.end();
		debugRenderer.render(gameEngine.getWorld(), debugMatrix);
		System.out.println(gameEngine.getBodies().get(BodiesNames.BOX_BODY).getPosition().y);
	}

	@Override
	public void dispose() {
		gameEngine.getWorld().dispose();
	}
}
