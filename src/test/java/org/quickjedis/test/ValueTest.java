package org.quickjedis.test;

import org.quickjedis.utils.ConvertHelper;

import junit.framework.TestCase;

public class ValueTest extends TestCase {

	public ValueTest() {
		// TODO Auto-generated constructor stub
	}

	public ValueTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testDefaultVal() {
		System.out.println("testDefaultVal");
		try {
			System.out.println("int:" + ConvertHelper.GetDefaultVal(int.class));
			System.out.println("Integer:" + ConvertHelper.GetDefaultVal(Integer.class));
			//
			System.out.println("long:" + ConvertHelper.GetDefaultVal(long.class));
			System.out.println("Long:" + ConvertHelper.GetDefaultVal(Long.class));
			//
			System.out.println("byte:" + ConvertHelper.GetDefaultVal(byte.class));
			System.out.println("Byte:" + ConvertHelper.GetDefaultVal(Byte.class));
			//
			System.out.println("short:" + ConvertHelper.GetDefaultVal(short.class));
			System.out.println("Short:" + ConvertHelper.GetDefaultVal(Short.class));
			//
			System.out.println("float:" + ConvertHelper.GetDefaultVal(float.class));
			System.out.println("Float:" + ConvertHelper.GetDefaultVal(Float.class));
			//
			System.out.println("double:" + ConvertHelper.GetDefaultVal(double.class));
			System.out.println("Double:" + ConvertHelper.GetDefaultVal(Double.class));
			//
			System.out.println("char:" + ConvertHelper.GetDefaultVal(char.class));
			System.out.println("Character:" + ConvertHelper.GetDefaultVal(Character.class));
			//
			System.out.println("boolean:" + ConvertHelper.GetDefaultVal(boolean.class));
			System.out.println("Boolean:" + ConvertHelper.GetDefaultVal(Boolean.class));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
