package org.systemexception.lunarlander.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.constants.Dimensions;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author leo
 * @date 30/12/15 13:48
 */
public class GameEngine {

	private final World world = new World(new Vec2(0, Dimensions.GRAVITY));
	private final HashMap<Object, Body> bodies = new HashMap<>();
	private final Audio soundThruster, soundRCS_LEFT, soundRCS_RIGHT;

	public GameEngine(Audio soundThruster, Audio soundRCS) {
		this.soundThruster = soundThruster;
		this.soundRCS_LEFT = soundRCS;
		this.soundRCS_RIGHT = soundRCS;
	}

	public HashMap<Object, Body> getBodies() {
		return bodies;
	}

	/**
	 * Checks if any input and performs a "step" in the world for the given time amount.
	 */
	public void logic() {
		if (Keyboard.isCreated()) {
			input();
		}
		world.step(1 / 60f, 8, 3);
	}

	/**
	 * Read the input from keyboard, mouse, whatever. If any. Plays sounds accordingly.
	 */
	private void input() {
		Body box = bodies.get(BodiesNames.BOX_BODY);
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			box.applyAngularImpulse(-Dimensions.RCS_THRUST);
			if (!soundRCS_LEFT.isPlaying()) {
				soundRCS_LEFT.playAsSoundEffect(1, 0.5f, false);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			box.applyAngularImpulse(Dimensions.RCS_THRUST);
			if (!soundRCS_RIGHT.isPlaying()) {
				soundRCS_RIGHT.playAsSoundEffect(1, 0.5f, false);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			float verticalThrust = (float) (Dimensions.THRUST * Math.sin(box.getAngle()));
			float horizontalThrust = (float) (-Dimensions.THRUST * Math.cos(box.getAngle()));
			box.applyForce(new Vec2(verticalThrust, horizontalThrust), box.getPosition());
			if (!soundThruster.isPlaying()) {
				soundThruster.playAsSoundEffect(1.0f, 1.0f, false);
			}
		} else {
			soundThruster.stop();
		}
		SoundStore.get().poll(0);
	}

	/**
	 * Create the objects in the world
	 */
	public void setUpObjects() {

		// Character Body
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(320 / 30 / 2, 240 / 30 / 2);
		boxDef.type = BodyType.DYNAMIC;
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(Dimensions.BOX_SIZE, Dimensions.BOX_SIZE);
		Body box = world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 10000f;
		boxFixture.shape = boxShape;
		boxFixture.restitution = 0.5f;
		box.createFixture(boxFixture);
		bodies.put(BodiesNames.BOX_BODY, box);

		// Character "head"
		BodyDef boxHeadDef = new BodyDef();
		boxHeadDef.position.set(boxDef.position.x, boxDef.position.y);
		boxHeadDef.type = BodyType.DYNAMIC;
		PolygonShape boxHeadShape = new PolygonShape();
		boxHeadShape.setAsBox(Dimensions.BOX_HEAD_SIZE, Dimensions.BOX_HEAD_SIZE);
		Body boxHead = world.createBody(boxHeadDef);
		FixtureDef boxHeadFixture = new FixtureDef();
		boxHeadFixture.density = 0.5f;
		boxHeadFixture.shape = boxHeadShape;
		boxHeadFixture.restitution = 0.05f;
		boxHead.createFixture(boxHeadFixture);
		bodies.put(BodiesNames.BOX_HEAD, boxHead);

		WeldJointDef jointDef = new WeldJointDef();
		jointDef.collideConnected = false;
		jointDef.bodyA = box;
		jointDef.bodyB = boxHead;
		Joint joint = world.createJoint(jointDef);

		// Ground
		putWall(0, 20, 30, 0, 0, 5, BodiesNames.GROUND);
		// Top Wall
		putWall(0, 0, 30, 0, 0, 0, null);
		// Left Wall
		putWall(0, 0, 0, 30, 0, 0, null);
		// Right Wall
		putWall(26.5f, 0, 0, 30, 0, 0, null);
	}

	/**
	 * Create a boundary for the world, all coordinates in meters
	 *
	 * @param posX the starting X coordinate
	 * @param posY the starting Y coordinate
	 * @param sizeX the size on the X axis
	 * @param sizeY the size on the Y axis
	 * @param restitution the amount of force returned (bounce)
	 * @param friction the friction of the object
	 * @param wallName an identifier for the object, most have random items, ground has a specific element,
	 *                    see: org.systemexception.lunarlander.constants.BodiesNames#GROUND
	 */
	public void putWall(final float posX, final float posY, final float sizeX, final float sizeY,
	                    final float restitution, final float friction, final Object wallName) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(posX, posY);
		bodyDef.type = BodyType.STATIC;
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
