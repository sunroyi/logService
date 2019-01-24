package com.inesa.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

public class CommUtils {

	/**
	 * FastJSON的序列化设置
	 */
	private static SerializerFeature[] features = new SerializerFeature[]{
			//输出Map中为Null的值
			SerializerFeature.WriteMapNullValue,

			//如果Boolean对象为Null，则输出为false
			SerializerFeature.WriteNullBooleanAsFalse,

			//如果List为Null，则输出为[]
			SerializerFeature.WriteNullListAsEmpty,

			//如果Number为Null，则输出为0
			SerializerFeature.WriteNullNumberAsZero,

			//输出Null字符串
			SerializerFeature.WriteNullStringAsEmpty,

			//格式化输出日期
			SerializerFeature.WriteDateUseDateFormat
	};

	// JSON格式化
	public static String printDataJason(HttpServletResponse response,
			Object item) {
		try {
			response.setContentType("text/plain;charset=" + "UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = null;
			try{
				out = response.getWriter();
				out.write(JSON.toJSONString(item, features));
				out.flush();
			}catch(IOException e){
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 随机生成6位随机验证码
	 * 
	 */
	public static String createRandomVcode(int len) {
		// 验证码
		String vcode = "";
		for (int i = 0; i < len; i++) {
			vcode = vcode + (int) (Math.random() * 9);
		}
		return vcode;
	}

	// 数字判断
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {			
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isDouble(String str) {
		if (null == str || "".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isDecimal(String str) {
		try {

			BigDecimal a = new BigDecimal(str);

		} catch (Exception e) {
			return false;
		}

		return true;
	}

}
