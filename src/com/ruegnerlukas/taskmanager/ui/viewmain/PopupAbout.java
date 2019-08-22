package com.ruegnerlukas.taskmanager.ui.viewmain;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PopupAbout extends PopupBase {


	@FXML private Label labelVersion;
	@FXML private Hyperlink linkGithub;




	public PopupAbout() {
		super(450, 150);
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_ABOUT, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading PopupAbout-FXML.", e);
		}

		labelVersion.setText("v " + TaskManager.BUILD_VERSION + "  (" + TaskManager.BUILD_DATE + ")");

		linkGithub.setOnAction(e -> {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/SMILEY4/ProjectManager"));
				} catch (IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

}
