package org.systemexception.lunarlander.main;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author leo
 * @date 29/12/15 14:22
 */
public class TestJbox {

	private static final String WINDOW_TITLE = "Physics in 2D!";
	private static final int[] WINDOW_DIMENSIONS = {800, 600};

	private final String wall = "WALL", boxBody = "BOX", boxHeadBody = "BOXHEAD";
	private final static double TWO_PI = 2 * Math.PI;

	private final World world = new World(new Vec2(0, 9.8f));
	private final HashMap<String, Body> bodies = new HashMap<>();

	private TrueTypeFont font;

	public static void main(String[] args) {

		TestJbox testJbox = new TestJbox();
		testJbox.start();
	}

	public void start() {
		initGL();
		initFonts();
		setUpObjects();

		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			input();
			logic();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}

	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0], WINDOW_DIMENSIONS[1]));
			Display.setTitle(WINDOW_TITLE);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glMatrixMode(GL_PROJECTION);

		glShadeModel(GL11.GL_SMOOTH);
		glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL11.GL_DEPTH_TEST);
		glDisable(GL11.GL_LIGHTING);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1);

		glEnable(GL11.GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL11.GL_MODELVIEW);

		glMatrixMode(GL11.GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void initFonts() {
		try {
			InputStream inputStream = ResourceLoader.getResourceAsStream("ubuntu.ttf");
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(24f); // set font size
			font = new TrueTypeFont(awtFont, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void render() {
		// Draw box
		Body box = bodies.get(boxBody);
		Color.red.bind();
		glPushMatrix();
		Vec2 bodyPosition = box.getPosition().mul(30);
		glTranslatef(bodyPosition.x, bodyPosition.y, 0);
		glRotated(Math.toDegrees(box.getAngle()), 0, 0, 1);
		glRectf(-0.75f * 30, -0.75f * 30, 0.75f * 30, 0.75f * 30);
		glPopMatrix();
		font.drawString(0, 0, "Position: " + box.getPosition(), org.newdawn.slick.Color.yellow);
		TextureImpl.bindNone();
		// Draw box head
		Body boxHead = bodies.get(boxHeadBody);
		Color.yellow.brighter().bind();
		glPushMatrix();
		glPushMatrix();
		double v = normalRelativeAngle(box.getAngle());
		font.drawString(0, 40, "Angle: " + String.format("%.2f", v), org.newdawn.slick.Color.yellow);
		TextureImpl.bindNone();
		glTranslatef((float) (bodyPosition.x + Math.sin(box.getAngle()) * 20f),
				(float) (bodyPosition.y - Math.cos(-box.getAngle()) * 20f), 0);
		glRotated(Math.toDegrees(box.getAngle()), 0, 0, 1);
		glRectf(-0.25f * 30, -0.25f * 30, 0.25f * 30, 0.25f * 30);
		glPopMatrix();
	}

	private void logic() {
		world.step(1 / 60f, 8, 3);
	}

	public double normalRelativeAngle(double angle) {
		double v = ((angle %= TWO_PI) >= 0 ? (angle < Math.PI) ? angle : angle - TWO_PI : (angle >= -Math.PI) ? angle :
				angle + TWO_PI) * (180 / Math.PI);
		if (v < 0) {
			return 360 + v;
		}
		return v;
	}

	private void input() {
		Body box = bodies.get(boxBody);
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			box.applyAngularImpulse(-0.005f);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			box.applyAngularImpulse(+0.005f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			// TODO Verify bugs in thrust application
			Vec2 vec21 = box.getLinearVelocity();
			System.out.println(vec21);
			box.applyForce(new Vec2(box.getAngle(), -2f).sub(vec21), box.getPosition());
		}
		if (Mouse.isButtonDown(0)) {
			Vec2 mousePosition = new Vec2(Mouse.getX(), Mouse.getY()).mul(1 / 60f);
			Vec2 bodyPosition = box.getPosition();
			Vec2 force = mousePosition.sub(bodyPosition);
			box.applyForce(force, box.getPosition());
		}
	}

	private void setUpObjects() {

		// Character Body
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(320 / 30 / 2, 240 / 30 / 2);
		boxDef.type = BodyType.DYNAMIC;
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.75f, 0.75f);
		Body box = world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 0.1f;
		boxFixture.shape = boxShape;
		boxFixture.restitution = 0.5f;
		box.createFixture(boxFixture);
		bodies.put(boxBody, box);

		// Character "head"
		BodyDef boxHeadDef = new BodyDef();
		boxHeadDef.position.set(boxDef.position.x, boxDef.position.y);
		boxHeadDef.type = BodyType.DYNAMIC;
		PolygonShape boxHeadShape = new PolygonShape();
		boxHeadShape.setAsBox(0.02f, 0.02f);
		Body boxHead = world.createBody(boxHeadDef);
		FixtureDef boxHeadFixture = new FixtureDef();
		boxHeadFixture.density = 0.02f;
		boxHeadFixture.shape = boxHeadShape;
		boxHeadFixture.restitution = 0.05f;
		boxHead.createFixture(boxHeadFixture);
		bodies.put(boxHeadBody, boxHead);

		WeldJointDef jointDef = new WeldJointDef();
		jointDef.collideConnected = false;
		jointDef.bodyA = box;
		jointDef.bodyB = boxHead;
		Joint joint = world.createJoint(jointDef);

		// Bottom Wall
		BodyDef groundDef = new BodyDef();
		groundDef.position.set(0, 20);
		groundDef.type = BodyType.STATIC;
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(1000, 0);
		Body ground = world.createBody(groundDef);
		FixtureDef groundFixture = new FixtureDef();
		groundFixture.density = 1;
		groundFixture.restitution = 0.5f;
		groundFixture.friction = 5f;
		groundFixture.shape = groundShape;
		ground.createFixture(groundFixture);
		bodies.put(wall.concat("1"), ground);

		// Top Wall
		BodyDef roofDef = new BodyDef();
		roofDef.position.set(0, 0);
		roofDef.type = BodyType.STATIC;
		PolygonShape roofShape = new PolygonShape();
		roofShape.setAsBox(1000, 0);
		Body roof = world.createBody(roofDef);
		FixtureDef roofFixture = new FixtureDef();
		roofFixture.density = 1;
		roofFixture.restitution = 0.5f;
		roofFixture.friction = 5f;
		roofFixture.shape = roofShape;
		roof.createFixture(roofFixture);
		bodies.put(wall.concat("2"), roof);

		// Left Wall
		BodyDef leftWallDef = new BodyDef();
		leftWallDef.position.set(0, 0);
		leftWallDef.type = BodyType.STATIC;
		PolygonShape leftWallShape = new PolygonShape();
		leftWallShape.setAsBox(0, 1000);
		Body leftWall = world.createBody(leftWallDef);
		FixtureDef leftWallFixture = new FixtureDef();
		leftWallFixture.density = 1;
		leftWallFixture.restitution = 0.5f;
		leftWallFixture.friction = 5f;
		leftWallFixture.shape = leftWallShape;
		leftWall.createFixture(leftWallFixture);
		bodies.put(wall.concat("3"), leftWall);

		// Right Wall
		BodyDef rightWallDef = new BodyDef();
		rightWallDef.position.set(27, 0);
		rightWallDef.type = BodyType.STATIC;
		PolygonShape rightWallShape = new PolygonShape();
		rightWallShape.setAsBox(0, 1000);
		Body rightWall = world.createBody(rightWallDef);
		FixtureDef rightWallFixture = new FixtureDef();
		rightWallFixture.density = 1;
		rightWallFixture.restitution = 0.5f;
		rightWallFixture.friction = 5f;
		rightWallFixture.shape = rightWallShape;
		rightWall.createFixture(rightWallFixture);
		bodies.put(wall.concat("4"), rightWall);
	}

}
