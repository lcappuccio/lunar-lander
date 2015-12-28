package org.systemexception.lunarlander.main;

import org.systemexception.lunarlander.model.Physics;

import java.util.HashMap;
import java.util.List;

/**
 * @author leo
 * @date 28/12/15 21:15
 */
public class Main {

	private static Physics physics = new Physics(10);

	public static void main(String[] args) {
		physics.runSimulation();
		HashMap<Integer, List> data = physics.getData();
		for(Integer time: data.keySet()) {
			List list = data.get(time);
			System.out.println(list.get(0) + ", " + list.get(1));
		}
	}
}
