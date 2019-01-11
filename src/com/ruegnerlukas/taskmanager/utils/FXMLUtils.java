package com.ruegnerlukas.taskmanager.utils;

import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;

public class FXMLUtils {


	public static Parent loadFXML(URL location, Object controller) throws IOException {

		FXMLLoader loader = new FXMLLoader(location);
		loader.setController(controller);
		Parent root = loader.load();

		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());

		root.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.R) {
				root.getStylesheets().clear();
				root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
				root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
			}
		});

		return root;
	}


}
