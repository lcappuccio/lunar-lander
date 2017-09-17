package org.systemexception.lunarlander.decorators;

/**
 * @author leo
 * @date 02/01/16 14.42
 */
public class StringDecorator {

	private static final String DECIMAL_COMMA_SEPARATOR = ",", DECIMAL_POINT_SEPARATOR = ".";

	public static String floatToString0Decimal(float t) {
		return java.lang.String.format("%.0f", t).replace(DECIMAL_COMMA_SEPARATOR, DECIMAL_POINT_SEPARATOR);
	}

	public static String floatToString1Decimal(float t) {
		return java.lang.String.format("%.1f", t).replace(DECIMAL_COMMA_SEPARATOR, DECIMAL_POINT_SEPARATOR);
	}

	public static String floatToString2Decimal(float t) {
		return java.lang.String.format("%.2f", t).replace(DECIMAL_COMMA_SEPARATOR, DECIMAL_POINT_SEPARATOR);
	}
}
