package org.systemexception.lunarlander.controller;

import org.systemexception.lunarlander.model.Physics;

/**
 * @author leo
 * @date 29/12/15 12:18
 */
public class MainController {

	private Physics physics = new Physics(20000);

	public void quit() {
		System.exit(0);
	}

	public void start() {
		physics.run();
	}
}
