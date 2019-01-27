package com.ruegnerlukas.taskmanager.ui.taskview.groupPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupByPopup extends AnchorPane {


	private Stage stage;
	@FXML private VBox boxAttributes;
	@FXML private Button btnAdd;
	@FXML private CheckBox cbUseHeaderString;
	@FXML private TextField fieldHeaderText;
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public GroupByPopup(Stage stage) {
		this.stage = stage;
		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_groupBy.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading GroupByPopup-FXML: " + e);
		}

		create();
	}




	private void create() {

		// values
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		for (TaskAttribute attribute : Logic.project.getProject().groupByOrder) {
			boxAttributes.getChildren().add(new GroupByAttributeNode(attribute));
		}


		// add attribute
		btnAdd.setOnAction(event -> {
			boxAttributes.getChildren().add(new GroupByAttributeNode(Logic.project.getProject().attributes.get(0)));
		});


		// use custom header string
		cbUseHeaderString.setSelected(Logic.project.getProject().useCustomHeaderString);
		fieldHeaderText.setDisable(!cbUseHeaderString.isSelected());
		cbUseHeaderString.setOnAction(event -> {
			fieldHeaderText.setDisable(!cbUseHeaderString.isSelected());
		});


		// list header string
		fieldHeaderText.setText(Logic.project.getProject().groupByHeaderString);


		// accept
		btnAccept.setOnAction(event -> {
			List<TaskAttribute> attributes = new ArrayList<>();
			for (Node node : boxAttributes.getChildren()) {
				GroupByAttributeNode groupByNode = (GroupByAttributeNode) node;
				attributes.add(groupByNode.attribute);
			}
			Logic.groupBy.setGroupByOrder(attributes);
			Logic.groupBy.setUseCustomHeaderString(cbUseHeaderString.isSelected());
			Logic.groupBy.setGroupHeaderString(fieldHeaderText.getText());
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}


}
