package com.ruegnerlukas.taskmanager.ui.uidata;

import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UIDataHandler {


	private static Map<UIModule, String> stylesheetPaths = new HashMap<>();
	private static Map<UIModule, String> fxmlPaths = new HashMap<>();

	private static Map<Parent, UIModule> roots = new HashMap<>();




	static {

		// stylsheets
		stylesheetPaths.put(UIModule.STYLE_BASE, "style/bootstrap4_2.css");

		stylesheetPaths.put(UIModule.VIEW_MAIN, "style/style_view_main.css");
		stylesheetPaths.put(UIModule.VIEW_PROJECTSETTINGS, "style/style_view_projectsettings.css");
		stylesheetPaths.put(UIModule.VIEW_TASKS, "style/style_view_tasks.css");
		stylesheetPaths.put(UIModule.ELEMENT_SIDEBAR, "style/style_element_sidebar.css");
		stylesheetPaths.put(UIModule.ELEMENT_TASKCARD, "style/style_element_taskcard.css");
		stylesheetPaths.put(UIModule.ELEMENT_TASKLIST, "style/style_element_tasklist.css");
		stylesheetPaths.put(UIModule.POPUP_SORT, "style/style_popup_sort.css");
		stylesheetPaths.put(UIModule.POPUP_GROUPBY, "style/style_popup_groupby.css");
		stylesheetPaths.put(UIModule.POPUP_PRESETS, "style/style_popup_presets.css");
		stylesheetPaths.put(UIModule.POPUP_FILTER, "style/style_popup_filter.css");
		stylesheetPaths.put(UIModule.CONTROL_EDITABLE_AREA, "style/style_control_editablearea.css");
		stylesheetPaths.put(UIModule.CONTROL_EDITABLE_LABEL, "style/style_control_editablelabel.css");

		// fxml-files
		fxmlPaths.put(UIModule.VIEW_MAIN, "fxml/layout_view_main.fxml");
		fxmlPaths.put(UIModule.VIEW_PROJECTSETTINGS, "fxml/layout_view_projectsettings.fxml");
		fxmlPaths.put(UIModule.VIEW_TASKS, "fxml/layout_view_tasks.fxml");
		fxmlPaths.put(UIModule.ELEMENT_SIDEBAR, "fxml/layout_element_sidebar.fxml");
		fxmlPaths.put(UIModule.ELEMENT_TASKCARD, "fxml/layout_element_taskcard.fxml");
		fxmlPaths.put(UIModule.ELEMENT_TASKLIST, "fxml/layout_element_tasklist.fxml");
		fxmlPaths.put(UIModule.POPUP_SORT, "fxml/layout_popup_sort.fxml");
		fxmlPaths.put(UIModule.POPUP_GROUPBY, "fxml/layout_popup_groupby.fxml");
		fxmlPaths.put(UIModule.POPUP_PRESETS, "fxml/layout_popup_presets.fxml");
		fxmlPaths.put(UIModule.POPUP_FILTER, "fxml/layout_popup_filter.fxml");
	}




	public static Parent loadFXML(UIModule module, Object controller) throws IOException {
		System.out.println("load module fxml: " + module + "   " + controller);
		javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getFXMLAsURL(module));
		loader.setController(controller);
		Parent root = loader.load();
		setStyle(root, module);
		addRoot(root, module);
		return root;
	}




	public static void setStyle(Parent root, UIModule module) {
		System.out.println("set style: " + module);
		root.getStylesheets().clear();
//		root.getStylesheets().add(getStylesheetAsURL(UIModule.STYLE_BASE).toExternalForm());
//		root.getStylesheets().add(getStylesheetAsURL(module).toExternalForm());
		// use this for hot-reload
		root.getStylesheets().add("file:D:/LukasRuegner/Programmieren/Java/Workspace/SimpleTaskManager/src/com/ruegnerlukas/taskmanager/ui/uidata/" + getStylesheetAsPath(UIModule.STYLE_BASE));
		root.getStylesheets().add("file:D:/LukasRuegner/Programmieren/Java/Workspace/SimpleTaskManager/src/com/ruegnerlukas/taskmanager/ui/uidata/" + getStylesheetAsPath(module));

	}




	public static void addRoot(Parent root, UIModule module) {
		roots.put(root, module);
	}




	public static void removeRoot(Parent root) {
		roots.remove(root);
	}




	public static void reloadAll() {
		System.out.println("Reload Styles: All");
		for (Parent root : roots.keySet()) {
			setStyle(root, roots.get(root));
		}
	}




	public static String getStylesheetAsPath(UIModule module) {
		return stylesheetPaths.get(module);
	}




	public static URL getStylesheetAsURL(UIModule module) {
		return UIDataHandler.class.getResource(getStylesheetAsPath(module));
	}




	public static String getFXMLAsPath(UIModule module) {
		return fxmlPaths.get(module);
	}




	public static URL getFXMLAsURL(UIModule module) {
		return UIDataHandler.class.getResource(getFXMLAsPath(module));
	}


}
