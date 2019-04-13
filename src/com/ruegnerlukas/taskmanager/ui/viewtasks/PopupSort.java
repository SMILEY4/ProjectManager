package com.ruegnerlukas.taskmanager.ui.viewtasks;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PopupSort extends TasksPopup {


	@FXML private Button btnReset;

	@FXML private ComboBox<String> choiceSaved;
	@FXML private Button btnDeleteSaved;
	@FXML private TextField fieldSave;
	@FXML private Button btnSave;

	@FXML private VBox boxAttributes;
	@FXML private Button btnAdd;

	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public PopupSort() {
		super(1000, 400);
	}




	@Override
	public void create() {

		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_SORT, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading PopupSort-FXML: " + e);
		}

	}

}
