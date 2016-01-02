package org.systemexception.lunarlander.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.systemexception.lunarlander.LunarLander;

/**
 * @author leo
 * @date 02/01/16 02:35
 */
@RunWith(GdxTestRunner.class)
public class LunarLanderTest extends LunarLander {

	private LunarLander sut;

	@Before
	public void setUp() {
	}

	@Test
	public void init() {
		sut = new LunarLander();
		sut.create();
	}
}