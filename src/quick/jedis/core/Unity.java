package quick.jedis.core;

public class Unity {
	public static void CreateException(String errMsg) throws Exception {
		Unity.CreateException(errMsg, (Exception) null);
	}

	public static void CreateException(String errMsg, Exception innerEx) throws Exception {
		// throw new ApplicationException(errMsg, innerEx);
		throw new Exception(errMsg, innerEx);
	}

	public static String GetHostIP() {
		return "";// Dns.GetHostAddresses(Dns.GetHostName())[0].ToString();
	}
}