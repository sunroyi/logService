package com.inesa.common.utils;

import com.inesa.hadoop.controller.timer.ExtendTimerTask;

import java.io.*;
import java.util.Properties;

public class PropertyUtil {

	private static Properties props;
	static {
		loadProps();
	}

	synchronized static private void loadProps() {

		props = new Properties();
		InputStream in = null;
		try {
			in = PropertyUtil.class.getClassLoader().getResourceAsStream(
					"application.properties");
			props.load(in);
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {

			}
		}

	}

	public static String getProperty(String key) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}

	public static Properties getProperties(String fileName) {
		try {
			String outpath = System.getProperty("user.dir")+File.separator+"src/main/resources"+File.separator;//先读取config目录的，没有再加载classpath的
			System.out.println(outpath);
			Properties properties = new Properties();
			InputStream in = new FileInputStream(new File(outpath + fileName));
			properties.load(in);
			return properties;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			try {
				Properties properties = new Properties();
				InputStream in = ExtendTimerTask.class.getClassLoader().getResourceAsStream(fileName);//默认加载classpath的
				properties.load(in);
				return properties;
			} catch (IOException es) {
				System.out.println(es.getMessage());
				return null;
			}
		}
	}
}
