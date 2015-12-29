package org.systemexception.lunarlander.model;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author leo
 * @date 28/12/15 21:30
 */
public class Physics implements Runnable {

	private double v, s;
	private double a = 9.8;
	private double height;
	private HashMap<Integer, List> data = new HashMap<>();

	public Physics(final long height) {
		if (height < 0) {
			throw new InvalidParameterException("Time cannot be negative");
		}
		this.height = height;
	}

	public HashMap<Integer, List> getData() {
		return data;
	}

	public void thrust() {
		this.a -= 1;
		System.out.println("G now: " + a);
	}

	@Override
	public void run() {
		double v0 = 0d;
		double s0 = 0d;
		while (s <= height) {
			int nano = LocalDateTime.now().getNano();
			v = v0 + (a * (nano / 100000000));
//			v = new BigDecimal(v).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			s = s0 + (0.5d * a * Math.pow((nano / 100000000), 2));
//			s = new BigDecimal(s).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
//			List<Double> list = new ArrayList<>();
//			list.add(v);
//			list.add(s);
//			data.put(0, list);
			System.out.println(v + ", " + s + ", " + a);
			v0 = v;
			s0 = s;
			try {
				Thread.sleep(50);
				a += 0.2;
				if (a > 9.8) {
					a = 9.8;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
