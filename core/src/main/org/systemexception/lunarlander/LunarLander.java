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

import javax.annotation.PreDestroy;
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
		Music soundThruster = Gdx.audio.newMusic(Gdx.files.internal("thruster_loop.ogg"));
		Music soundRcs = Gdx.audio.newMusic(Gdx.files.internal("rcs.ogg"));
		gameEngine = new GameEngine(soundThruster, soundRcs);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		int fontXCoord = 20;
		int fontYCoord = 780;
		int fontStep = 20;
		if (body.getPosition().y < 50f) {
			camera.zoom = 0.3f;
			camera.position.x = body.getPosition().x * Dimensions.METERS_TO_PIXELS;
			camera.position.y = body.getPosition().y * Dimensions.METERS_TO_PIXELS;
			camera.position.z = 0f;
			fontXCoord = (int) (body.getPosition().x * Dimensions.METERS_TO_PIXELS) - 180;
			fontYCoord = (int) (body.getPosition().y * Dimensions.METERS_TO_PIXELS) + 100;
			font.getData().setScale(0.3f, 0.3f);
			fontStep = 5;
		}
		float v = (float) MathUtils.normalRelativeAngle(body.getAngle());
		font.draw(batch, "Angle: " + StringDecorator.floatToString2Decimal(v) + " deg", fontXCoord, fontYCoord);
		font.draw(batch, "Altitude: " + StringDecorator.floatToString1Decimal(body.getPosition().y) + " m",
				fontXCoord, fontYCoord -= fontStep);
		font.draw(batch, "H_Speed: " + StringDecorator.floatToString1Decimal(Math.abs(body.getLinearVelocity().x)) +
				"m/s", fontXCoord, fontYCoord -= fontStep);
		font.draw(batch, "V_Speed: " + StringDecorator.floatToString1Decimal(body.getLinearVelocity().y) +
				" m/s", fontXCoord, fontYCoord -= fontStep);
		font.draw(batch, "Mass: " + StringDecorator.floatToString0Decimal(body.getMass()) + " kg", fontXCoord, fontYCoord -= fontStep);
		HashMap<String, Object> userData = (HashMap<String, Object>) body.getUserData();
		font.draw(batch, "G: " + StringDecorator.floatToString1Decimal(MathUtils.calculateGForce(body)), fontXCoord,
				fontYCoord -= fontStep);
		font.draw(batch, "Thrust: " + userData.get(BodiesNames.THRUST) + "%", fontXCoord, fontYCoord -= fontStep);
		font.draw(batch, "Fuel: " +
				StringDecorator.floatToString0Decimal((float) userData.get(BodiesNames.FUEL_AMOUNT)) + " kg",
				fontXCoord, fontYCoord - fontStep);
	}

	@Override
	@PreDestroy
	public void dispose() {
		gameEngine.getWorld().dispose();
	}

}
