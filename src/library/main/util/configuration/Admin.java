package library.main.util.configuration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Admin {
	private static String username;
	private static String password;
	private static Properties adminProperties;

	static {
		adminProperties = new Properties();
		try {
			adminProperties.load(Files.newInputStream(Paths
					.get("admin.properties")));
			username = adminProperties.getProperty("username");
			password = adminProperties.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isValid(String username, String password) {
		return Admin.username.equals(username)
				&& Admin.password.equals(password);
	}

	public static void setUsername(String username)
			throws FileNotFoundException, IOException {
		Admin.adminProperties.replace("username", username);
		Admin.adminProperties.store(new FileOutputStream("admin.properties"),
				null);
		Admin.username = username;
	}

	public static void setPassword(String password)
			throws FileNotFoundException, IOException {
		Admin.adminProperties.replace("password", password);
		Admin.adminProperties.store(new FileOutputStream("admin.properties"),
				null);
		Admin.password = password;
	}

	public static String getUsername() {
		return username;
	}

}
