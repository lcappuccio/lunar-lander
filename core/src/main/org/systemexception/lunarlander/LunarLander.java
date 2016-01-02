package org.systemexception.lunarlander;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.constants.Dimensions;
import org.systemexception.lunarlander.decorators.StringDecorator;
import org.systemexception.lunarlander.physics.GameEngine;
import org.systemexception.lunarlander.physics.MathUtils;

import java.util.HashMap;

public class LunarLander extends ApplicationAdapter {

	private OrthographicCamera camera;
	private GameEngine gameEngine;

	private PolygonSpriteBatch batch;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;
	private BitmapFont font;

	@Override
	public void create() {
		batch = new PolygonSpriteBatch();
		Music soundThruster = Gdx.audio.newMusic(Gdx.files.internal("thruster.ogg"));
		Music soundRcs = Gdx.audio.newMusic(Gdx.files.internal("rcs.ogg"));
		gameEngine = new GameEngine(soundThruster, soundRcs);
		gameEngine.setUpObjects();

		camera = new OrthographicCamera(Gdx.graphics.getWidth() * 4, Gdx.graphics.getHeight() * 4);
		camera.setToOrtho(false);

		debugMatrix = new Matrix4(camera.combined);
		debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, false);

		font = new BitmapFont();
		font.setColor(Color.WHITE);

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		gameEngine.logic();
		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(Dimensions.METERS_TO_PIXELS,
				Dimensions.METERS_TO_PIXELS, 0);
		batch.begin();
		processBody();
		batch.end();
		debugRenderer.render(gameEngine.getWorld(), debugMatrix);
	}

	private void processBody() {
		Body body = gameEngine.getBodies().get(BodiesNames.BOX_BODY);
		float v = (float) MathUtils.normalRelativeAngle(body.getAngle());
		font.draw(batch, "Angle: " + StringDecorator.floatToString2Decimal(v) + " deg", 20, 580);
		font.draw(batch, "Altitude: " + StringDecorator.floatToString1Decimal(body.getPosition().y) + " m", 20, 560);
		font.draw(batch, "H_Speed: " + StringDecorator.floatToString1Decimal(Math.abs(body.getLinearVelocity().x)) +
				"m/s", 20, 540);
		font.draw(batch, "V_Speed: " + StringDecorator.floatToString1Decimal(body.getLinearVelocity().y) +
				" m/s", 20, 520);
		font.draw(batch, "Mass: " + StringDecorator.floatToString0Decimal(body.getMass()) + " kg", 20, 500);
		HashMap<String, Object> userData = (HashMap<String, Object>) body.getUserData();
		font.draw(batch, "G: " + StringDecorator.floatToString1Decimal(MathUtils.calculateGForce(body)), 20, 480);
		font.draw(batch, "Thrust: " + userData.get(BodiesNames.THRUST) + "%", 20, 460);
		font.draw(batch, "Fuel: " +
				StringDecorator.floatToString0Decimal((float) userData.get(BodiesNames.FUEL_AMOUNT)) + " kg", 20, 440);
	}

	@Override
	public void dispose() {
		gameEngine.getWorld().dispose();
	}

}
