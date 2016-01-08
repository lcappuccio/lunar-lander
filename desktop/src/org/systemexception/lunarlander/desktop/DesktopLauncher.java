package org.systemexception.lunarlander.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.systemexception.lunarlander.LunarLander;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Lunar Lander";
		config.width = 1280;
		config.height = 800;
		new LwjglApplication(new LunarLander(), config);
	}
}
