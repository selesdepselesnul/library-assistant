package library.main.util;

import java.io.IOException;
import java.util.Properties;

import library.main.model.Admin;

public class LibraryUtil {

	private Properties properties;

	public LibraryUtil(Properties prop) {
		this.properties = prop;
	}

	public boolean isValid(Admin admin) throws IOException,
			ClassNotFoundException {
		Admin validAdmin = new Admin(this.properties.getProperty("username"),
				this.properties.getProperty("password"));

		return validAdmin.equals(admin);
	}

}
