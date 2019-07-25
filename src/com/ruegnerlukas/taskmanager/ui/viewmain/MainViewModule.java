package com.ruegnerlukas.taskmanager.ui.viewmain;

import javafx.scene.control.Tab;

public interface MainViewModule {


	/**
	 * Called when the {@link Tab} containing this module is created.
	 */
	void onModuleOpen();

	/**
	 * Called when the {@link Tab} containing this module is selected.
	 */
	void onModuleSelected();

	/**
	 * Called when the {@link Tab} containing this module is deselected.
	 */
	void onModuleDeselected();

	/**
	 * Called when the {@link Tab} containing this module is closed.
	 */
	void onModuleClose();

}
