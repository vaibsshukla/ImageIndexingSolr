package com.b3ds.ifarm.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties {

	private static Properties properties = null;
	private static LoadProperties props;
	
	public static LoadProperties loadProperties()
	{
		properties = new Properties();
		try {
			properties.load(new FileInputStream(new File("../props.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		props = new LoadProperties();
		return props;
	}

	public static String getProperty(String key)
	{
		return properties.getProperty(key);
	}
}
