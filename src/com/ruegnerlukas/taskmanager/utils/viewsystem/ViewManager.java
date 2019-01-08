package com.ruegnerlukas.taskmanager.utils.viewsystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.TaskManager;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewManager {

	
	private static Stage primaryStage;
	private static Map<String,View> views = new HashMap<String,View>();

	
	
	
	public static void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}
	
	
	
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	
	
	
	public static void addViews(List<View> views) {
		for(View view : views) {
			addView(view);
		}
	}
	
	
	public static void addView(View view) {
		if(views.containsKey(view.id)) {
			Logger.get().warn("View \"" + view.id + "\" already exists.");
		} else {
			views.put(view.id, view);
		}
	}

	
	
	
	public static IViewLoader getLoader(String id) {
		View view = views.get(id);
		if(view != null) {
			return view.loader;
		} else {
			return null;
		}
	}
	
	
	
	
	public static IViewController getController(String id) {
		View view = views.get(id);
		if(view != null) {
			return view.controller;
		} else {
			return null;
		}
	}
	
	
	
	
	public static IViewService getService(String id, boolean initService) {
		View view = views.get(id);
		if(view != null && initService) {
			view.service.initService();
			return view.service;
		} else {
			return null;
		}
	}
	
	
	public static Scene getScene(String id) {
		View view = views.get(id);
		if(view != null && view instanceof WindowView) {
			return ((WindowView)view).scene;
		} else {
			return null;
		}
	}
	
	
	
	
	public static Stage getStage(String id) {
		View view = views.get(id);
		if(view != null && view instanceof WindowView) {
			return ((WindowView)view).stage;
		} else {
			return null;
		}
	}
	
	
	
	
	public static Parent getRoot(String id) {
		View view = views.get(id);
		if(view != null && view instanceof ModuleView) {
			return ((ModuleView)view).root;
		} else {
			return null;
		}
	}
	
	
	
	
	public static void setController(String id, IViewController controller) {
		View view = views.get(id);
		if(view != null) {
			view.controller = controller;
		}
	}
	
	
	
	
	public static void setScene(String id, Scene scene) {
		View view = views.get(id);
		if(view != null && view instanceof WindowView) {
			((WindowView)view).scene = scene;
		}
	}
	
	
	
	public static void setStage(String id, Stage stage) {
		View view = views.get(id);
		if(view != null && view instanceof WindowView) {
			((WindowView)view).stage = stage;
		}
	}
	
	

	public static void setRoot(String id, Parent root) {
		View view = views.get(id);
		if(view != null && view instanceof ModuleView) {
			((ModuleView)view).root = root;
		}
	}
	
	
	
	
	private static final Class<?> RESOURCE_PATH_ORIGIN = TaskManager.class;

	public static void openFXScene(String id, Stage parent, String pathFXML, double width, double height, String title) {
		openFXScene(id, parent, pathFXML, width, height, title, false);
	}

	
	public static void openFXScene(String id, Stage parent, String pathFXML, double width, double height, String title, boolean wait) {
		
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		
		if(parent == null) {
			stage.initOwner(getPrimaryStage());
		} else {
			stage.initOwner(parent);
		}

		FXMLLoader loader = new FXMLLoader(RESOURCE_PATH_ORIGIN.getResource(pathFXML));
		Parent root = null;
		try {
			root = (Parent) loader.load();
		} catch (IOException e) {
			Logger.get().error("Error loading fxmlScene: " + pathFXML, e);
			return;
		}

		Scene scene = new Scene(root, width, height, true, SceneAntialiasing.BALANCED);
		scene.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		scene.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
		stage.setTitle(title);
		stage.setScene(scene);
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.R) {
					scene.getStylesheets().clear();
					scene.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
					scene.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
				}
			}
		});
		
		ViewManager.setScene(id, stage.getScene());
		ViewManager.setStage(id, stage);
		
		if(loader.getController() != null) {
			ViewManager.setController(id, loader.getController());
			ViewManager.getController(id).create();
		}
		
		if(wait) {
			stage.showAndWait();
		} else {
			stage.show();
		}
		
	}
	
	
	
	public static void loadFXModule(String id, String pathFXML) {
	
		try {
			
			FXMLLoader loader = new FXMLLoader(RESOURCE_PATH_ORIGIN.getResource(pathFXML));
			final Parent root = (Parent) loader.load();
			
			root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
			root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());

			root.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override public void handle(KeyEvent event) {
					if(event.getCode() == KeyCode.R) {
						root.getStylesheets().clear();
						root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
						root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
					}
				}
			});
			
			ViewManager.setRoot(id, root);

			if(loader.getController() != null) {
				ViewManager.setController(id, loader.getController());
				ViewManager.getController(id).create();
			}
			
		} catch (IOException e) {
			Logger.get().error("Error loading fxmlScene: " + pathFXML, e);
			return;
		}
		
	}
	
	
	
	
	public static void closeFXScene(String id) {
		Stage stage = ViewManager.getStage(id);
		if(stage != null) {
			stage.close();
		}
	}
	
}
