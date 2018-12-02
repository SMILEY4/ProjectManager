package com.ruegnerlukas.taskmanager.utils.viewsystem;

import javafx.scene.Parent;

public class ModuleView extends View {

	protected Parent root;
	
	public ModuleView(String id, IViewLoader loader, IViewService service) {
		super(id, loader, service);
	}

}
