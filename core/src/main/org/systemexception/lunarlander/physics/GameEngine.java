package org.systemexception.lunarlander.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.constants.Dimensions;

import java.util.HashMap;
import java.util.UUID;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;

/**
 * @author leo
 * @date 30/12/15 13:48
 */
public class GameEngine {

	private final World world = new World(new Vector2(0, Dimensions.GRAVITY), false);
	private final HashMap<Object, Body> bodies = new HashMap<>();
	private final HashMap<Object, Object> userData = new HashMap<>();
	private final Music soundThruster, soundRCS_LEFT, soundRCS_RIGHT;


	public GameEngine(final Music soundThruster, final Music soundRCS) {
		this.soundThruster = soundThruster;
		this.soundThruster.setLooping(true);
		this.soundRCS_LEFT = soundRCS;
		this.soundRCS_RIGHT = soundRCS;
		userData.put(BodiesNames.THRUST, 0);
		userData.put(BodiesNames.FUEL_AMOUNT, Dimensions.FUEL_AMOUNT);
	}

	public World getWorld() {
		return world;
	}

	public HashMap<Object, Body> getBodies() {
		return bodies;
	}

	public void logic() {
		input();
		Body box = bodies.get(BodiesNames.BOX_BODY);
		userData.put(BodiesNames.V1, box.getLinearVelocity().y);
		int thrustPercent = (int) userData.get(BodiesNames.THRUST);
		if (thrustPercent > 0 && (float) userData.get(BodiesNames.FUEL_AMOUNT) > 0f) {
			float remainingFuel = (float) userData.get(BodiesNames.FUEL_AMOUNT) -
					(Dimensions.FUEL_BURN_RATE * thrustPercent / 100f);
			userData.put(BodiesNames.FUEL_AMOUNT, remainingFuel);
			float verticalThrust = (float) (Dimensions.THRUST * Math.sin(-box.getAngle())) * thrustPercent / 100f;
			float horizontalThrust = (float) (Dimensions.THRUST * Math.cos(-box.getAngle())) * thrustPercent / 100f;
			box.applyForce(new Vector2(verticalThrust, horizontalThrust), box.getPosition(), true);
			MassData massData = box.getMassData();
			massData.mass = Dimensions.COMMAND_MODULE_MASS + Dimensions.DESCENT_STAGE_MASS + (float) userData.get
					(BodiesNames.FUEL_AMOUNT);
			if (!soundThruster.isPlaying()) {
				soundThruster.play();
			}
			massData.center.set(box.getLocalCenter());
			box.setMassData(massData);
		} else {
			soundThruster.stop();
		}
		world.step(Dimensions.TIME_STEP, 8, 3);
		userData.put(BodiesNames.V2, box.getLinearVelocity().y);
	}

	public void input() {
		Body box = bodies.get(BodiesNames.BOX_BODY);
		int thrustPercent = (int) userData.get(BodiesNames.THRUST);
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			box.applyAngularImpulse(Dimensions.RCS_THRUST, true);
			if (!soundRCS_LEFT.isPlaying()) {
				soundRCS_LEFT.play();
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			box.applyAngularImpulse(-Dimensions.RCS_THRUST, true);
			if (!soundRCS_RIGHT.isPlaying()) {
				soundRCS_RIGHT.play();
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PERIOD) & thrustPercent < 100) {
			userData.put(BodiesNames.THRUST, ++thrustPercent);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.COMMA) & thrustPercent > 0) {
			userData.put(BodiesNames.THRUST, --thrustPercent);
		}
	}

	/**
	 * Create the objects in the world
	 */
	public void setUpObjects() {

		// Descent Stage
		BodyDef descentStageBodyDef = new BodyDef();
		descentStageBodyDef.position.set(320 / Dimensions.METERS_TO_PIXELS, 240 / Dimensions.METERS_TO_PIXELS);
		descentStageBodyDef.type = DynamicBody;
		PolygonShape descentStageShape = new PolygonShape();
		descentStageShape.setAsBox(Dimensions.DESCENT_STAGE_WIDTH, Dimensions.DESCENT_STAGE_HEIGHT);
		Body descentStageBody = world.createBody(descentStageBodyDef);
		FixtureDef descentStageFixture = new FixtureDef();
		descentStageFixture.shape = descentStageShape;
		descentStageBody.createFixture(descentStageFixture);
		bodies.put(BodiesNames.BOX_BODY, descentStageBody);
		descentStageBody.setUserData(userData);
		// Descent Stage Mass
		MassData massData = new MassData();
		massData.mass = Dimensions.COMMAND_MODULE_MASS + Dimensions.DESCENT_STAGE_MASS + (float) userData.get
				(BodiesNames.FUEL_AMOUNT);
		massData.center.set(descentStageBody.getLocalCenter());
		massData.I = 5000f;
		descentStageBody.setMassData(massData);

		// Command Module
		BodyDef commandModuleBodyDef = new BodyDef();
		commandModuleBodyDef.type = DynamicBody;
		CircleShape commandModuleShape = new CircleShape();
		commandModuleShape.setRadius(Dimensions.COMMAND_MODULE_RADIUS);
		commandModuleShape.setPosition(new Vector2(0, Dimensions.COMMAND_MODULE_RADIUS +
				Dimensions.DESCENT_STAGE_HEIGHT));
		Body commandModuleBody = world.createBody(commandModuleBodyDef);
		FixtureDef commandModuleFixture = new FixtureDef();
		commandModuleFixture.shape = commandModuleShape;
		descentStageBody.createFixture(commandModuleFixture);
		bodies.put(BodiesNames.BOX_HEAD, commandModuleBody);

		// Pointy polygon
//		BodyDef pointDef = new BodyDef();
//		pointDef.position.set(descentStageBodyDef.position.x, descentStageBodyDef.position.y + 10);
//		pointDef.type = DynamicBody;
//		Vector2[] vec2s = new Vector2[5];
//		vec2s[0] = new Vector2(-1, 2);
//		vec2s[1] = new Vector2(-1, 0);
//		vec2s[2] = new Vector2(0, -3);
//		vec2s[3] = new Vector2(1, 0);
//		vec2s[4] = new Vector2(1, 1);
//		PolygonShape shape = new PolygonShape();
//		shape.set(vec2s);
//		Body pointBody = world.createBody(pointDef);
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.density = 1;
//		fixtureDef.shape = shape;
//		fixtureDef.restitution = 0f;
//		pointBody.createFixture(fixtureDef);
//		bodies.put("BAU", pointBody);

		// Ground Wall
		putWall(0, 0, 30, 0, 0, 5, BodiesNames.GROUND);
		// Top Wall
		putWall(0, 20, 30, 0, 0, 0, null);
		// Left Wall
		putWall(0, 20, 0, 30, 0, 0, null);
		// Right Wall
		putWall(26.7f, 0, 0, 30, 0, 0, null);
	}

	/**
	 * Create a boundary for the world, all coordinates in meters
	 *
	 * @param posX        the starting X coordinate
	 * @param posY        the starting Y coordinate
	 * @param sizeX       the size on the X axis
	 * @param sizeY       the size on the Y axis
	 * @param restitution the amount of force returned (bounce)
	 * @param friction    the friction of the object
	 * @param wallName    an identifier for the object, most have random items, ground has a specific element,
	 *                    see: BodiesNames#GROUND
	 */
	public void putWall(final float posX, final float posY, final float sizeX, final float sizeY,
	                    final float restitution, final float friction, final Object wallName) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(posX, posY);
		bodyDef.type = StaticBody;
		PolygonShape edgeShape = new PolygonShape();
		edgeShape.setAsBox(sizeX, sizeY);
		Body body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1;
		fixtureDef.restitution = restitution;
		fixtureDef.friction = friction;
		fixtureDef.shape = edgeShape;
		body.createFixture(fixtureDef);
		if (null == wallName) {
			bodies.put(UUID.randomUUID(), body);
		} else {
			bodies.put(wallName, body);
		}
	}

}