package com.ruegnerlukas.taskmanager.utils;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		System.out.println("Reload CSS-Styles: all");
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




	public static void showAllBorders(Parent parent, String colorParent, String colorNode) {
		List<Parent> open = new ArrayList<>();
		open.add(parent);

		while(!open.isEmpty()) {
			Parent p = open.remove(0);
			p.setStyle("-fx-border-color: " + colorParent + ";");

			for(Node node : p.getChildrenUnmodifiable()) {
				if(node instanceof Parent) {
					open.add((Parent)node);
				} else {
					node.setStyle("-fx-border-color: " + colorNode + ";");
				}
			}

		}

	}

}
