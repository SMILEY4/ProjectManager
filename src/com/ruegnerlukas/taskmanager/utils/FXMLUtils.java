package com.ruegnerlukas.taskmanager.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class FXMLUtils {


	public static Parent loadFXML(URL location, Object controller) throws IOException {
		FXMLLoader loader = new FXMLLoader(location);
		loader.setController(controller);
		Parent root = loader.load();
		StyleUtils.setStyle(root);
		StyleUtils.addRoot(root);
		return root;
	}


}
