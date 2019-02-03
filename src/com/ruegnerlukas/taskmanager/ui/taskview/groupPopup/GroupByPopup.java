package com.ruegnerlukas.taskmanager.ui.taskview.groupPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
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
		Logic.group.getTaskGroupOrder(new Request<List<TaskAttribute>>(true) {
			@Override
			public void onResponse(Response<List<TaskAttribute>> response) {
				List<TaskAttribute> order = response.getValue();
				for (TaskAttribute attribute : order) {
					boxAttributes.getChildren().add(new GroupByAttributeNode(attribute));
				}
			}
		});


		// add attribute
		btnAdd.setOnAction(event -> {
			Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
				@Override
				public void onResponse(Response<List<TaskAttribute>> response) {
					List<TaskAttribute> attributes = response.getValue();
					boxAttributes.getChildren().add(new GroupByAttributeNode(attributes.get(0)));
				}
			});
		});


		// custom header string
		Logic.group.getCustomHeaderString(new Request<String>() {
			@Override
			public void onResponse(Response<String> response) {
				if (response.getState() == Response.State.SUCCESS) {
					cbUseHeaderString.setSelected(true);
					fieldHeaderText.setText(response.getValue());
				} else {
					cbUseHeaderString.setSelected(false);
					fieldHeaderText.setText("");
				}
			}
		});
		fieldHeaderText.setDisable(!cbUseHeaderString.isSelected());
		cbUseHeaderString.setOnAction(event -> {
			fieldHeaderText.setDisable(!cbUseHeaderString.isSelected());
		});


		// accept
		btnAccept.setOnAction(event -> {
			List<TaskAttribute> attributes = new ArrayList<>();
			for (Node node : boxAttributes.getChildren()) {
				GroupByAttributeNode groupByNode = (GroupByAttributeNode) node;
				attributes.add(groupByNode.attribute);
			}
			Logic.group.setGroupOrder(attributes);
			Logic.group.setUseCustomHeaderString(cbUseHeaderString.isSelected());
			Logic.group.setGroupHeaderString(fieldHeaderText.getText());
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}


}
