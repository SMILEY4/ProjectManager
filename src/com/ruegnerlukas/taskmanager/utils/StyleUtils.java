package com.ruegnerlukas.taskmanager.utils;

import javafx.scene.Parent;

import java.util.HashSet;
import java.util.Set;

public class StyleUtils {


	private static Set<Parent> roots = new HashSet<>();




	public static void addRoot(Parent root) {
		roots.add(root);
	}




	public static void removeRoot(Parent root) {
		roots.remove(root);
	}




	public static void reloadAll() {
		for (Parent root : roots) {
			setStyle(root);
		}
	}




	public static void setStyle(Parent root) {
		root.getStylesheets().clear();
//		DEV-MODE
		root.getStylesheets().add("file:D:/LukasRuegner/Programmieren/Java/Workspace/SimpleTaskManager/src/com/ruegnerlukas/taskmanager/utils/viewsystem/bootstrap4_2.css");
		root.getStylesheets().add("file:D:/LukasRuegner/Programmieren/Java/Workspace/SimpleTaskManager/src/com/ruegnerlukas/taskmanager/utils/viewsystem/style.css");
//		DEPLOYED
//		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
//		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
	}


}
