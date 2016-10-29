package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Config {

	private static Config instance = null;
	private Properties staticConfigs;
	public static String ext = ".cfg";


	public static void loadConfigFile(String configLocation) throws Exception {
		if (instance == null){
			instance = new Config();
			String staticConfigFile = configLocation + "Prop" + ext;
			instance.staticConfigs = new Properties();
			try {
				instance.staticConfigs.load(new FileInputStream(staticConfigFile));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failure in loading config file");
			}

		}
	}

	public static Config getInstance() throws Exception {

		return getInstance(getConfigLocation());
	}

	public static Config getInstance(String configLocation) throws Exception {
		if (instance == null){
			loadConfigFile(configLocation);
		}
		return instance;
	}


	public String getConfig(String key){

		if (instance == null)
			throw new RuntimeException("Initialize AppConfig");
		Object value = null;

		value = staticConfigs.get(key);
		if (value != null)
			return value.toString();
		else
			return null;
	}

	public static String getConfigLocation() {
		String path = System.getProperty("user.dir")+"/src/main/java/config/";
		File f = new File(path);
		if (f.exists()) {
			return path;
		}
		else
			return null;
	}


}

