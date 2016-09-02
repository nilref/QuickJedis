package org.quickjedis.core;

public class Unity {
	public static void CreateException(String errMsg) {
		Unity.CreateException(errMsg, (Exception) null);
	}

	public static void CreateException(String errMsg, Exception innerEx) {
		try {

			throw new Exception(errMsg, innerEx.getCause());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}