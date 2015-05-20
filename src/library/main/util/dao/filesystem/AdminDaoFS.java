package library.main.util.dao.filesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import library.main.model.Admin;

public class AdminDaoFS {

	private String adminPath;

	public AdminDaoFS(String adminPath) {
		this.adminPath = adminPath;

	}

	public void updateIfExist(Admin admin) throws IOException {
		Files.deleteIfExists(Paths.get(this.adminPath));
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				Files.newOutputStream(Paths.get(this.adminPath)));
		objectOutputStream.writeObject(admin);
	}

	public Admin read() throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(
				Files.newInputStream(Paths.get(this.adminPath)));
		Admin admin = (Admin) objectInputStream.readObject();
		return admin;
	}

	public String getAdminPath() {
		return this.adminPath;
	}
}
