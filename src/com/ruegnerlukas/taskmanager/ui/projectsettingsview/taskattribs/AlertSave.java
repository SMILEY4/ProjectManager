package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertSave extends AnchorPane {


	private Stage stage;

	@FXML private Label text;
	@FXML private ChoiceBox<String> choice;
	@FXML private CheckBox cbOnlyInvalid;
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;


	public String selected = TaskLogic.CORR_BEH_DELETE;
	public boolean onlyInvalid = true;




	public AlertSave(Stage stage, int affectedTasks, boolean usesDefault) {
		this.stage = stage;

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_savewarning.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading AlertSave-FXML: " + e);
		}
		create(affectedTasks, usesDefault);
	}




	private void create(int affectedTasks, boolean usesDefault) {

		text.setText("Saving these changes may affect " + affectedTasks + " Tasks.");

		choice.getItems().add(TaskLogic.CORR_BEH_DELETE);
		if(usesDefault) {
			choice.getItems().add(TaskLogic.CORR_BEH_DEFAULT);
		}
		choice.getSelectionModel().select(TaskLogic.CORR_BEH_DELETE);
		choice.setOnAction(event -> {
			selected = choice.getSelectionModel().getSelectedItem();
		});

		cbOnlyInvalid.setSelected(onlyInvalid);
		cbOnlyInvalid.setOnAction(event -> {
			onlyInvalid = cbOnlyInvalid.isSelected();
		});

		btnCancel.setOnAction(event -> {
			selected = null;
			stage.close();
		});

		btnAccept.setOnAction(event -> {
			stage.close();
		});

	}

}
