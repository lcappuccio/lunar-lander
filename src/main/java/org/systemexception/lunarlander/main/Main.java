package org.systemexception.lunarlander.main;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.physics.GameEngine;

import java.awt.*;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author leo
 * @date 29/12/15 14:22
 */
public class Main {

	private static final String WINDOW_TITLE = "Lunar Lander";
	private static final int[] WINDOW_DIMENSIONS = {800, 600};

	private final static double TWO_PI = 2 * Math.PI;

	private TrueTypeFont font;
	private static GameEngine gameEngine;

	public static void main(String[] args) {

		Main main = new Main();
		gameEngine = new GameEngine();
		main.start();
	}

	private void start() {
		initGL();
		initFonts();
		gameEngine.setUpObjects();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			render();
			gameEngine.input();
			gameEngine.logic();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
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

		glShadeModel(GL_SMOOTH);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_MODELVIEW);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private void initFonts() {
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
		Body box = gameEngine.getBodies().get(BodiesNames.BOX_BODY);
		Color.red.bind();
		glPushMatrix();
		Vec2 bodyPosition = box.getPosition().mul(30);
		glTranslatef(bodyPosition.x, bodyPosition.y, 0);
		glRotated(Math.toDegrees(box.getAngle()), 0, 0, 1);
		glRectf(-0.75f * 30, -0.75f * 30, 0.75f * 30, 0.75f * 30);
		glPopMatrix();
		font.drawString(0, 0, "Position: " + box.getPosition(), Color.yellow);
		TextureImpl.bindNone();
		// Draw box head
		Body boxHead = gameEngine.getBodies().get(BodiesNames.BOX_HEAD);
		Color.yellow.brighter().bind();
		glPushMatrix();
		glPushMatrix();
		double v = normalRelativeAngle(box.getAngle());
		font.drawString(0, 40, "Angle: " + String.format("%.2f", v) + ", Radians: " + box.getAngle(), Color.yellow);
		TextureImpl.bindNone();
		glTranslatef((float) (bodyPosition.x + Math.sin(box.getAngle()) * 20f),
				(float) (bodyPosition.y - Math.cos(-box.getAngle()) * 20f), 0);
		glRotated(Math.toDegrees(box.getAngle()), 0, 0, 1);
		glRectf(-0.25f * 30, -0.25f * 30, 0.25f * 30, 0.25f * 30);
		glPopMatrix();
	}

	private double normalRelativeAngle(double angle) {
		double v = ((angle %= TWO_PI) >= 0 ? (angle < Math.PI) ? angle : angle - TWO_PI : (angle >= -Math.PI) ? angle :
				angle + TWO_PI) * (180 / Math.PI);
		if (v < 0) {
			return 360 + v;
		}
		return v;
	}

}
