package library.main.util;

import java.io.IOException;

import library.main.controller.PasswordWindowController;

public class PasswordAskerWindow {

	private WindowLoader windowLoader;

	public PasswordAskerWindow(boolean isCloseSystem, Runnable runnable,
			AdminDaoMYSQL adminDaoMYSQL) throws IOException {
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
