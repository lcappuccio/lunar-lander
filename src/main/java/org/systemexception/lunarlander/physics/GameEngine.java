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
import org.systemexception.lunarlander.constants.GamePhysics;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author leo
 * @date 30/12/15 13:48
 */
public class GameEngine {

	private final World world = new World(new Vec2(0, GamePhysics.GRAVITY));
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

	public void logic() {
		if (Keyboard.isCreated()) {
			input();
		}
		world.step(1 / 60f, 8, 3);
	}

	private void input() {
		Body box = bodies.get(BodiesNames.BOX_BODY);
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			box.applyAngularImpulse(-GamePhysics.RCS_THRUST);
			if (!soundRCS_LEFT.isPlaying()) {
				soundRCS_LEFT.playAsSoundEffect(1, 0.5f, false);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			box.applyAngularImpulse(GamePhysics.RCS_THRUST);
			if (!soundRCS_RIGHT.isPlaying()) {
				soundRCS_RIGHT.playAsSoundEffect(1, 0.5f, false);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			float verticalThrust = (float) (GamePhysics.THRUST * Math.sin(box.getAngle()));
			float horizontalThrust = (float) (-GamePhysics.THRUST * Math.cos(box.getAngle()));
			box.applyForce(new Vec2(verticalThrust, horizontalThrust), box.getPosition());
			if (!soundThruster.isPlaying()) {
				soundThruster.playAsSoundEffect(1.0f, 1.0f, false);
			}
		} else {
			soundThruster.stop();
		}
		SoundStore.get().poll(0);
	}

	public void setUpObjects() {

		// Character Body
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(320 / 30 / 2, 240 / 30 / 2);
		boxDef.type = BodyType.DYNAMIC;
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.75f, 0.75f);
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
		boxHeadShape.setAsBox(0.02f, 0.02f);
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
		putWall(26.5f ,0,0,30,0,0,null);
	}

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
