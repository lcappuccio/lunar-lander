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
		// TODO Test if this is really needed
		for (Body body : bodies.values()) {
			body.setActive(true);
		}
		input();
		Body box = bodies.get(BodiesNames.BOX_BODY);
		userData.put("V1", box.getLinearVelocity().y);
		int thrustPercent = (int) userData.get(BodiesNames.THRUST);
		float verticalThrust = (float) (Dimensions.THRUST * Math.sin(-box.getAngle())) * thrustPercent / 100f;
		float horizontalThrust = (float) (Dimensions.THRUST * Math.cos(-box.getAngle())) * thrustPercent / 100f;
		box.applyForce(new Vector2(verticalThrust, horizontalThrust), box.getPosition(), true);
		if (thrustPercent > 0) {
			userData.put(BodiesNames.FUEL_AMOUNT,
					(int) userData.get(BodiesNames.FUEL_AMOUNT) - (int) (Dimensions.FUEL_BURN_RATE * thrustPercent /
							100f));
			soundThruster.play();
		} else {
			soundThruster.stop();
		}
		world.step(1 / 60f, 8, 3);
		userData.put("V2", box.getLinearVelocity().y);
	}

	public void input() {
		Body box = bodies.get(BodiesNames.BOX_BODY);
		int thrustPercent = (int) userData.get(BodiesNames.THRUST);
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			box.applyAngularImpulse(Dimensions.RCS_THRUST, true);
			if (!soundRCS_LEFT.isPlaying()) {
				soundRCS_LEFT.play();
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			box.applyAngularImpulse(-Dimensions.RCS_THRUST, true);
			if (!soundRCS_RIGHT.isPlaying()) {
				soundRCS_RIGHT.play();
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
			if (thrustPercent < 100) {
				userData.put(BodiesNames.THRUST, ++thrustPercent);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
			if (thrustPercent > 0) {
				userData.put(BodiesNames.THRUST, --thrustPercent);
			}
		}
	}

	/**
	 * Create the objects in the world
	 */
	public void setUpObjects() {

		// Character Body
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(320 / Dimensions.METERS_TO_PIXELS, 240 / Dimensions.METERS_TO_PIXELS);
		boxDef.type = DynamicBody;
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(Dimensions.BOX_SIZE, Dimensions.BOX_SIZE);
		Body box = world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 3800f;
		boxFixture.shape = boxShape;
		boxFixture.restitution = 0.5f;
		box.createFixture(boxFixture);
		bodies.put(BodiesNames.BOX_BODY, box);
		box.setUserData(userData);

		// Character "head"
		BodyDef boxHeadDef = new BodyDef();
		boxHeadDef.position.set(boxDef.position.x, boxDef.position.y);
		boxHeadDef.type = DynamicBody;
		PolygonShape boxHeadShape = new PolygonShape();
		boxHeadShape.setAsBox(Dimensions.BOX_HEAD_SIZE, Dimensions.BOX_HEAD_SIZE,
				new Vector2(0, Dimensions.BOX_HEAD_SIZE + Dimensions.BOX_SIZE), 0);
		Body boxHead = world.createBody(boxHeadDef);
		FixtureDef boxHeadFixture = new FixtureDef();
		boxHeadFixture.shape = boxHeadShape;
		box.createFixture(boxHeadFixture);
		bodies.put(BodiesNames.BOX_HEAD, boxHead);

		// Pointy polygon
		BodyDef pointDef = new BodyDef();
		pointDef.position.set(boxDef.position.x, boxDef.position.y + 10);
		pointDef.type = DynamicBody;
		Vector2[] vec2s = new Vector2[5];
		vec2s[0] = new Vector2(-1, 2);
		vec2s[1] = new Vector2(-1, 0);
		vec2s[2] = new Vector2(0, -3);
		vec2s[3] = new Vector2(1, 0);
		vec2s[4] = new Vector2(1, 1);
		PolygonShape shape = new PolygonShape();
		shape.set(vec2s);
		Body pointBody = world.createBody(pointDef);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1;
		fixtureDef.shape = shape;
		fixtureDef.restitution = 0f;
		pointBody.createFixture(fixtureDef);
		bodies.put("BAU", pointBody);

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