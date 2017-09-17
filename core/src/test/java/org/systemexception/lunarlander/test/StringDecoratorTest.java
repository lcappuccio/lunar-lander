package org.systemexception.lunarlander.test;

import org.junit.Test;
import org.systemexception.lunarlander.decorators.StringDecorator;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author leo
 * @date 02/01/16 17:59
 */
public class StringDecoratorTest {

	private StringDecorator sut;
	private final static float TEST = 0.12345f;

	@Test
	public void no_decimal() {
		assertTrue("0".equals(StringDecorator.floatToString0Decimal(TEST)));
	}

	@Test
	public void one_decimal() {
		assertEquals("0.1", StringDecorator.floatToString1Decimal(TEST));
	}

	@Test
	public void two_decimals() {
		assertEquals("0.12", StringDecorator.floatToString2Decimal(TEST));
	}


}