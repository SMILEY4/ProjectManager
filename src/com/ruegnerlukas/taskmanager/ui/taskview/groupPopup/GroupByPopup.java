package com.ruegnerlukas.taskmanager.ui.taskview.groupPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.eventsystem.Event;
import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.eventsystem.events.GroupByOrderChangedEvent;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupByPopup extends AnchorPane {

	private Stage stage;
	@FXML private VBox boxAttributes;
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;
	@FXML private Button btnAdd;




	public GroupByPopup(Stage stage) {
		this.stage = stage;
		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_groupBy.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading GroupByPopup-FXML: " + e);
		}

		setupListeners();
		create();
	}




	private void create() {

		// attributes
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		for(TaskAttribute attribute : Logic.project.getProject().groupByOrder) {
			boxAttributes.getChildren().add(new GroupByAttributeNode(attribute));
		}

		// add attribute
		btnAdd.setOnAction(event -> {
			boxAttributes.getChildren().add(new GroupByAttributeNode(Logic.project.getProject().attributes.get(0)));
		});

		// accept
		btnAccept.setOnAction(event -> {
			List<TaskAttribute> attributes = new ArrayList<>();
			for(Node node : boxAttributes.getChildren()) {
				GroupByAttributeNode groupByNode = (GroupByAttributeNode)node;
				attributes.add(groupByNode.attribute);
			}
			Logic.groupBy.setGroupByOrder(attributes);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}




	private void setupListeners() {

		// listen for changed groupBy-order
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				GroupByOrderChangedEvent event = (GroupByOrderChangedEvent)e;
			}
		}, GroupByOrderChangedEvent.class);

	}


}
