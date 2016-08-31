package org.quickjedis.test;

import junit.framework.TestCase;

public class MyTest extends TestCase {
	public static final String key = "mahatma.test";
	public static UserInfo userInfo = new UserInfo("mahatma", 18, true);

	public MyTest(String name) {
		super(name);

		System.out.println("MyTest(name)");

	}

	public MyTest() {
	}

}
