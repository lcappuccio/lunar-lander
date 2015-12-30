package org.systemexception.lunarlander.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
	private final Audio soundThruster, soundRCS;


	public GameEngine(Audio soundThruster, Audio soundRCS) {
		this.soundThruster = soundThruster;
		this.soundRCS = soundRCS;
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
			if (!soundRCS.isPlaying()) {
				soundRCS.playAsSoundEffect(1, 0.5f, false);
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			box.applyAngularImpulse(GamePhysics.RCS_THRUST);
			if (!soundRCS.isPlaying()) {
				soundRCS.playAsSoundEffect(1, 0.5f, false);
			}		}
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
		if (Mouse.isButtonDown(0)) {
			Vec2 mousePosition = new Vec2(Mouse.getX(), Mouse.getY()).mul(1 / 60f);
			Vec2 bodyPosition = box.getPosition();
			Vec2 force = mousePosition.sub(bodyPosition);
			box.applyForce(force, box.getPosition());
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
		BodyDef groundDef = new BodyDef();
		groundDef.position.set(0, 20);
		groundDef.type = BodyType.STATIC;
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(30, 0);
		Body ground = world.createBody(groundDef);
		FixtureDef groundFixture = new FixtureDef();
		groundFixture.density = 1;
		groundFixture.restitution = 0;
		groundFixture.friction = 5f;
		groundFixture.shape = groundShape;
		ground.createFixture(groundFixture);
		bodies.put(UUID.randomUUID(), ground);

		// Top Wall
		BodyDef roofDef = new BodyDef();
		roofDef.position.set(0, 0);
		roofDef.type = BodyType.STATIC;
		PolygonShape roofShape = new PolygonShape();
		roofShape.setAsBox(30, 0);
		Body roof = world.createBody(roofDef);
		FixtureDef roofFixture = new FixtureDef();
		roofFixture.density = 1;
		roofFixture.restitution = 0;
		roofFixture.friction = 5f;
		roofFixture.shape = roofShape;
		roof.createFixture(roofFixture);
		bodies.put(UUID.randomUUID(), ground);

		// Left Wall
		BodyDef leftWallDef = new BodyDef();
		leftWallDef.position.set(0, 0);
		leftWallDef.type = BodyType.STATIC;
		PolygonShape leftWallShape = new PolygonShape();
		leftWallShape.setAsBox(0, 30);
		Body leftWall = world.createBody(leftWallDef);
		FixtureDef leftWallFixture = new FixtureDef();
		leftWallFixture.density = 1;
		roofFixture.restitution = 0;
		leftWallFixture.friction = 5f;
		leftWallFixture.shape = leftWallShape;
		leftWall.createFixture(leftWallFixture);
		bodies.put(UUID.randomUUID(), ground);

		// Right Wall
		BodyDef rightWallDef = new BodyDef();
		rightWallDef.position.set(26.5f, 0);
		rightWallDef.type = BodyType.STATIC;
		PolygonShape rightWallShape = new PolygonShape();
		rightWallShape.setAsBox(0, 30);
		Body rightWall = world.createBody(rightWallDef);
		FixtureDef rightWallFixture = new FixtureDef();
		rightWallFixture.density = 1;
		roofFixture.restitution = 0;
		rightWallFixture.friction = 5f;
		rightWallFixture.shape = rightWallShape;
		rightWall.createFixture(rightWallFixture);
		bodies.put(UUID.randomUUID(), ground);
	}

}
