package org.systemexception.lunarlander.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.constants.Dimensions;

import java.util.HashMap;
import java.util.Random;
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
		setUpObjects();
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
		descentStageBodyDef.position.set(
				(Gdx.graphics.getWidth() / Dimensions.METERS_TO_PIXELS) / Dimensions.METERS_TO_PIXELS,
				(Gdx.graphics.getHeight() * 0.9f) / Dimensions.METERS_TO_PIXELS);
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
		
		// Left landing gear
		BodyDef leftGear = new BodyDef();
		leftGear.type = DynamicBody;
		Vector2[] vecLeftGear = new Vector2[3];
		vecLeftGear[0] = new Vector2(-1f, Dimensions.COMMAND_MODULE_RADIUS + Dimensions.DESCENT_STAGE_HEIGHT - 1.5f);
		vecLeftGear[1] = new Vector2(-1.8f, Dimensions.COMMAND_MODULE_RADIUS + Dimensions.DESCENT_STAGE_HEIGHT - 2.5f);
		vecLeftGear[2] = new Vector2(-0.8f, Dimensions.COMMAND_MODULE_RADIUS + Dimensions.DESCENT_STAGE_HEIGHT - 1.5f);
		PolygonShape shapeLeftGear = new PolygonShape();
		shapeLeftGear.set(vecLeftGear);
		world.createBody(leftGear);
		FixtureDef fixtureLeftGear = new FixtureDef();
		fixtureLeftGear.shape = shapeLeftGear;
		fixtureLeftGear.restitution = 0.5f;
		descentStageBody.createFixture(fixtureLeftGear);

		// Right landing gear
		BodyDef rightGear = new BodyDef();
		rightGear.type = DynamicBody;
		Vector2[] vecRightGear = new Vector2[3];
		vecRightGear[0] = new Vector2(1f, Dimensions.COMMAND_MODULE_RADIUS + Dimensions.DESCENT_STAGE_HEIGHT - 1.5f);
		vecRightGear[1] = new Vector2(1.8f, Dimensions.COMMAND_MODULE_RADIUS + Dimensions.DESCENT_STAGE_HEIGHT - 2.5f);
		vecRightGear[2] = new Vector2(0.8f, Dimensions.COMMAND_MODULE_RADIUS + Dimensions.DESCENT_STAGE_HEIGHT - 1.5f);
		PolygonShape shapeRightGear = new PolygonShape();
		shapeRightGear.set(vecRightGear);
		world.createBody(rightGear);
		FixtureDef fixtureRightGear = new FixtureDef();
		fixtureRightGear.shape = shapeRightGear;
		fixtureRightGear.restitution = 0.5f;
		descentStageBody.createFixture(fixtureRightGear);

		// Apply initial position and speed
		descentStageBody.setTransform((Gdx.graphics.getWidth() / 30f) / Dimensions.METERS_TO_PIXELS,
				(Gdx.graphics.getHeight() * 0.9f) / Dimensions.METERS_TO_PIXELS, 1.5f);
		descentStageBody.setLinearVelocity(8f, 0f);

		// Ground
		generateGround();
		// Top Wall
		putWall(0, Gdx.graphics.getHeight() / Dimensions.METERS_TO_PIXELS,
				Gdx.graphics.getWidth() / Dimensions.METERS_TO_PIXELS, 0, 0, 0, null);
		// Left Wall
		putWall(0, Gdx.graphics.getHeight() / Dimensions.METERS_TO_PIXELS, 0,
				Gdx.graphics.getWidth() / Dimensions.METERS_TO_PIXELS, 0, 0, null);
		// Right Wall
		putWall(Gdx.graphics.getWidth() / Dimensions.METERS_TO_PIXELS, 0, 0,
				Gdx.graphics.getHeight() / Dimensions.METERS_TO_PIXELS, 0, 0, null);
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

	public void generateGround() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0,0);
		bodyDef.type = StaticBody;
		Vector2[] vector2s = new Vector2[50];
		for (int i = 0; i < vector2s.length; i++) {
			Random rnd = new Random();
			float rndY = rnd.nextFloat() + rnd.nextInt(1);
			vector2s[i] = new Vector2(i, rndY);
		}
		ChainShape polygonShape = new ChainShape();
		polygonShape.createChain(vector2s);
		Body body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1f;
		fixtureDef.restitution = 0f;
		fixtureDef.friction = 5f;
		fixtureDef.shape = polygonShape;
		body.createFixture(fixtureDef);
		bodies.put(BodiesNames.GROUND, body);
	}

}