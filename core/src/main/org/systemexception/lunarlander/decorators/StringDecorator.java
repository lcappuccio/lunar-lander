package org.systemexception.lunarlander.decorators;

/**
 * @author leo
 * @date 02/01/16 14.42
 */
public class StringDecorator {

	public static String floatToString0Decimal(float t) {
		return java.lang.String.format("%.0f", t);
	}

	public static String floatToString1Decimal(float t) {
		return java.lang.String.format("%.1f", t);
	}

	public static String floatToString2Decimal(float t) {
		return java.lang.String.format("%.2f", t);
	}
}
