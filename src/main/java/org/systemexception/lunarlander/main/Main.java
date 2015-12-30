package org.systemexception.lunarlander.main;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.util.ResourceLoader;
import org.systemexception.lunarlander.constants.BodiesNames;
import org.systemexception.lunarlander.constants.Dimensions;
import org.systemexception.lunarlander.physics.GameEngine;

import java.awt.*;
import java.io.IOException;
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
	private final static float FONT_SIZE = 14, FONT_SPACER = FONT_SIZE + 2;

	private TrueTypeFont font;
	private static Audio soundThruster;
	private static Audio soundRCS;
	private static GameEngine gameEngine;

	public static void main(String[] args) {

		Main main = new Main();
		main.start();
	}

	private void start() {
		initGL();
		initResources();
		gameEngine = new GameEngine(soundThruster, soundRCS);
		gameEngine.setUpObjects();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			render();
			gameEngine.logic();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		AL.destroy();
	}

	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS[0], WINDOW_DIMENSIONS[1]));
			Display.setTitle(WINDOW_TITLE);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
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

	private void initResources() {
		try {
			// Font
			InputStream inputStream = ResourceLoader.getResourceAsStream("ubuntu.ttf");
			Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(FONT_SIZE); // set font size
			font = new TrueTypeFont(awtFont, true);

			// Audio
			soundThruster = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("thruster.ogg"));
			soundRCS = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("rcs.ogg"));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	private void render() {
		// Draw box
		Body box = gameEngine.getBodies().get(BodiesNames.BOX_BODY);
		Color.red.bind();
		glPushMatrix();
		Vec2 bodyPosition = box.getPosition().mul(Dimensions.METERS_TO_PIXELS);
		glTranslatef(bodyPosition.x, bodyPosition.y, 0);
		glRotated(Math.toDegrees(box.getAngle()), 0, 0, 1);
		float scalarBox = Dimensions.BOX_SIZE * Dimensions.METERS_TO_PIXELS;
		float scalarHead = Dimensions.BOX_HEAD_SIZE * Dimensions.METERS_TO_PIXELS;
		glRectf(-scalarBox, -scalarBox, scalarBox, scalarBox);
		glPopMatrix();
		// Draw box head
		Body boxHead = gameEngine.getBodies().get(BodiesNames.BOX_HEAD);
		Color.yellow.brighter().bind();
		Vec2 bodyHeadPosition = boxHead.getPosition().mul(Dimensions.METERS_TO_PIXELS);
		glPushMatrix();
		glTranslatef((float) (bodyHeadPosition.x + Math.sin(box.getAngle()) * Dimensions.METERS_TO_PIXELS),
				(float) (bodyHeadPosition.y - Math.cos(-box.getAngle()) * Dimensions.METERS_TO_PIXELS), 0);
		glRotated(Math.toDegrees(boxHead.getAngle()), 0, 0, 1);
		glRectf(-scalarHead, -scalarHead, scalarHead, scalarHead);
		glPopMatrix();

		// Dashboard
		int stringPosX = 0;
		double v = normalRelativeAngle(boxHead.getAngle());
		Color.white.bind();
		font.drawString(0, stringPosX, "Angle: " + String.format("%.2f", v) + " deg");
		font.drawString(0, stringPosX += FONT_SPACER, "Height: " +
				String.format("%.2f", gameEngine.getBodies().get(BodiesNames.GROUND).getPosition().y
						- boxHead.getPosition().y) + " m");
		font.drawString(0, stringPosX += FONT_SPACER, "H_Speed: " +
				String.format("%.2f", Math.abs(boxHead.getLinearVelocity().x)) + " m/s");
		font.drawString(0, stringPosX += FONT_SPACER, "V_Speed: " +
				String.format("%.2f", boxHead.getLinearVelocity().y) + " m/s");
		TextureImpl.bindNone();
	}

	private double normalRelativeAngle(final double angle) {
		double tempAngle = angle;
		double v = ((tempAngle %= TWO_PI) >= 0 ? (tempAngle < Math.PI) ? tempAngle : tempAngle - TWO_PI :
				(tempAngle >= -Math.PI) ? tempAngle : tempAngle + TWO_PI) * (180 / Math.PI);
		if (v < 0) {
			return 360 + v;
		}
		return v;
	}

}
