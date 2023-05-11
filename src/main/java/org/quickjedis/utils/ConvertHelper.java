package org.quickjedis.utils;

import static org.quickjedis.utils.ConvertHelper.GetDefaultVal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ConvertHelper {

	@SuppressWarnings("unchecked")
	public static <T> T GetDefaultVal(Class<T> className) {
		if (Integer.class.equals(className) || int.class.equals(className))
			return (T) GetIntegerDefaultVal(Integer.class);
		if (Long.class.equals(className) || long.class.equals(className))
			return (T) GetLongDefaultVal(Long.class);
		if (Byte.class.equals(className) || byte.class.equals(className))
			return (T) GetByteDefaultVal(Byte.class);
		if (Short.class.equals(className) || short.class.equals(className))
			return (T) GetShortDefaultVal(Short.class);
		if (Float.class.equals(className) || float.class.equals(className))
			return (T) GetFloatDefaultVal(Float.class);
		if (Double.class.equals(className) || double.class.equals(className))
			return (T) GetDoubleDefaultVal(Double.class);
		if (Character.class.equals(className) || char.class.equals(className))
			return (T) GetCharacterDefaultVal(Character.class);
		if (Boolean.class.equals(className) || boolean.class.equals(className))
			return (T) GetBooleanDefaultVal(Boolean.class);
		return (T) null;
	}

	public static Integer GetIntegerDefaultVal(Class<Integer> className) {
		return 0;
	}

	public static Long GetLongDefaultVal(Class<Long> className) {
		return (long) 0;
	}

	public static Byte GetByteDefaultVal(Class<Byte> className) {
		return 0;
	}

	public static Short GetShortDefaultVal(Class<Short> className) {
		return 0;
	}

	public static Float GetFloatDefaultVal(Class<Float> className) {
		return (float) 0;
	}

	public static Double GetDoubleDefaultVal(Class<Double> className) {
		return (double) 0;
	}

	public static Character GetCharacterDefaultVal(Class<Character> className) {
		return '\u0000';
	}

	public static Boolean GetBooleanDefaultVal(Class<Boolean> className) {
		return false;
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
