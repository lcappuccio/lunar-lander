package org.systemexception.lunarlander.main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * @author leo
 * @date 29/12/15 15:32
 */
public class InFrame {

	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("A fresh display!");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		while(!Display.isCloseRequested()) {
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}
}
