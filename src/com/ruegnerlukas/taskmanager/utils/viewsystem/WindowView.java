package com.ruegnerlukas.taskmanager.utils.viewsystem;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowView extends View {

	protected Scene scene;
	protected Stage stage;
	
	public WindowView(String id, IViewLoader loader, IViewService service) {
		super(id, loader, service);
	}

}
