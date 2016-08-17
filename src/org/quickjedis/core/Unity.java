package org.quickjedis.core;

public class Unity {
	public static void CreateException(String errMsg) throws Exception {
		Unity.CreateException(errMsg, (Exception) null);
	}

	public static void CreateException(String errMsg, Exception innerEx) throws Exception {
		throw new Exception(errMsg, innerEx.getCause());
	}

}