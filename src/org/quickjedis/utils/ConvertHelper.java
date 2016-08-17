package org.quickjedis.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ConvertHelper {

	public static <T> T JsonBytesToObject(byte[] bytes, Class<T> className, String encoding) throws Exception {
		String objJson = ConvertHelper.BytesToString(bytes, encoding);
		return (T) JsonHelper.toObject(objJson, className);
	}

	public static <T> byte[] ObjectToJsonBytes(T obj, String encoding) throws Exception {
		String objJson = JsonHelper.toJson(obj);
		return ConvertHelper.StringToBytes(objJson, encoding);
	}

	public static byte[] StringToBytes(String obj, String encoding) {
		if (obj == null)
			return null;
		try {
			return obj.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String BytesToString(byte[] bytes, String encoding) {
		if (bytes == null)
			return "";
		try {
			return new String(bytes, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static byte[] StringToBytes(String obj) {
		if (obj == null)
			return null;
		try {
			return obj.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String BytesToString(byte[] bytes) {
		if (bytes == null)
			return "";
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static byte[] Base64StringToBytes(String obj) {
		if (obj == null)
			return null;
		try {
			return obj.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String BytesToBase64String(byte[] bytes) {
		if (bytes == null)
			return "";
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static List<String> BytesToList(byte[][] bytes) {
		List<String> list = new ArrayList<String>();
		if (bytes == null)
			return (List<String>) null;
		for (int index = 0; index < bytes.length; ++index) {
			String str = ConvertHelper.BytesToString(bytes[index]);
			if (!StringHelper.IsNullOrEmpty(str))
				list.add(str);
		}
		return list;
	}

	public static byte[][] ListToBytes(List<String> list) {
		byte[][] numArray1 = new byte[list.size()][];
		for (int index = 0; index < list.size(); ++index) {
			byte[] numArray2 = ConvertHelper.StringToBytes(list.get(index));
			numArray1[index] = numArray2;
		}
		return numArray1;
	}

	public static TryParseResult<Integer> TryParseInt(String str) {
		try {
			Integer intt = Integer.parseInt(str);
			return new TryParseResult<Integer>(true, intt);
		} catch (Exception ex) {
			return new TryParseResult<Integer>(false, 0);
		}
	}

	public static class TryParseResult<T> {
		private Boolean success;
		private T Object;

		public Boolean getSuccess() {
			return success;
		}

		public void setSuccess(Boolean success) {
			this.success = success;
		}

		public T getObject() {
			return Object;
		}

		public void setObject(T obj) {
			this.Object = obj;
		}

		public TryParseResult(Boolean success, T obj) {
			this.success = success;
			this.Object = obj;
		}
	}
}
