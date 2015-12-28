package org.systemexception.lunarlander.main;

import org.systemexception.lunarlander.model.Physics;

import java.io.IOException;

/**
 * @author leo
 * @date 28/12/15 21:15
 */
public class Main {

	private static Physics physics = new Physics(1000);

	public static void main(String[] args) throws IOException {
		physics.run();
		if ( System.in.available() > 0 )
		{
			char keyChar = (char)System.in.read();
			System.out.println(keyChar);
		}
	}
}
