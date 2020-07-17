package Utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class readConfig {
	
		private static Properties configFile;

		static {

			try {
				String path = "Config.properties";
				FileInputStream input = new FileInputStream(path);

				configFile = new Properties();
				configFile.load(input);

				input.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		public static String getProp(String keyName) {
			return configFile.getProperty(keyName);
		}
}