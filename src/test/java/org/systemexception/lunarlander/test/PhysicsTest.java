package org.systemexception.lunarlander.test;

import org.junit.Test;
import org.systemexception.lunarlander.model.Physics;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author leo
 * @date 28/12/15 21:42
 */
public class PhysicsTest {

	private Physics sut;

	@Test
	public void run_simulation_physics() {
		sut = new Physics();
		sut.runSimulation();
		HashMap<Integer, List> data = sut.getData();
		assertTrue(data.get(1).get(0).equals(9.8));
		assertTrue(data.get(2).get(0).equals(19.6));
		assertTrue(data.get(5).get(1).equals(122.5));
	}

}