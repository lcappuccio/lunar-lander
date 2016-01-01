package org.systemexception.lunarlander.physics;

import com.badlogic.gdx.physics.box2d.Body;
import org.systemexception.lunarlander.constants.Dimensions;

import java.util.HashMap;

/**
 * @author leo
 * @date 02/01/16 00:00
 */
public class MathUtils {

	private final static double TWO_PI = 2 * Math.PI;

	public static double normalRelativeAngle(final double angle) {
		double tempAngle = angle;
		double v = ((tempAngle %= TWO_PI) >= 0 ? (tempAngle < Math.PI) ? tempAngle : tempAngle - TWO_PI :
				(tempAngle >= -Math.PI) ? tempAngle : tempAngle + TWO_PI) * (-180 / Math.PI);
		if (v < 0) {
			return 360 + v;
		}
		return v;
	}

	public static float calculateAcceleration(Body body) {
		HashMap<String, Object> userData = (HashMap<String, Object>) body.getUserData();
		return ((float) userData.get("V2") - (float) userData.get("V1")) / Dimensions.TIME_STEP;
	}
}
