package org.systemexception.lunarlander.test;

import org.junit.Test;
import org.systemexception.lunarlander.main.TestJbox;

import static org.junit.Assert.assertTrue;

/**
 * @author leo
 * @date 29/12/15 19:55
 */
public class TestJboxTest {

	private TestJbox sut = new TestJbox();

	@Test
	public void test() {
		assertTrue(sut != null);
	}

}