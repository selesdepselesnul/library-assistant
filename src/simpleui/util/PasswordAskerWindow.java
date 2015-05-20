package simpleui.util;

import java.io.IOException;

import library.main.controller.PasswordWindowController;
import library.main.util.dao.filesystem.AdminDaoFS;

public class PasswordAskerWindow {

	private WindowLoader windowLoader;

	public PasswordAskerWindow(boolean isCloseSystem, Runnable runnable,
			AdminDaoFS adminDaoMYSQL) throws IOException {
		windowLoader = new WindowLoader(
				"library/main/view/PasswordWindow.fxml",
				"Masukan Password",
				(fxmlLoader, stage) -> {
					PasswordWindowController passwordWindowController = (PasswordWindowController) fxmlLoader
							.getController();
					passwordWindowController.setRunnable(runnable);
					passwordWindowController.setCloseSystem(isCloseSystem);
					passwordWindowController.setAdminDaoMYSQL(adminDaoMYSQL);
					passwordWindowController.setStage(stage);
				});
	}

	public void showAndWait() {
		this.windowLoader.show(WindowLoader.SHOW_AND_WAITING);
	}

}
