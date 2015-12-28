package org.systemexception.lunarlander.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author leo
 * @date 28/12/15 21:30
 */
public class Physics {

	private double v, s;
	private final double a = 9.8;
	private int t;
	private HashMap<Integer, List> data = new HashMap<>();

	public HashMap<Integer, List> getData() {
		return data;
	}

	public void runSimulation() {
		for (t = 0; t < 100; t++) {
			v = a * t;
			v = new BigDecimal(v).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			s = 0.5 * a * Math.pow(t, 2);
			s = new BigDecimal(s).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			List<Double> list = new ArrayList<>();
			list.add(v);
			list.add(s);
			data.put(t, list);
		}
	}

}
