package com.ruegnerlukas.taskmanager.ui.taskview.sortPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
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

public class SortPopup extends AnchorPane {


	private Stage stage;
	@FXML private VBox boxAttributes;
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;
	@FXML private Button btnAdd;




	public SortPopup(Stage stage) {
		this.stage = stage;

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_sort.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading SortPopup-FXML: " + e);
		}

		create();

	}




	private void create() {

		// elements
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		Logic.sort.getSortElements(new Request() {
			@Override
			public void onResponse(Response response) {
				if (response.state == Response.State.SUCCESS) {
					List<SortElement> elements = (List<SortElement>) response.getValue();
					for (SortElement element : elements) {
						boxAttributes.getChildren().add(new SortElementNode(element.attribute, element.sortDir));
					}
				}
			}
		});


		// add element
		btnAdd.setOnAction(event -> {
			Logic.attribute.getAttributes(new Request() {
				@Override
				public void onResponse(Response response) {
					if (response.state == Response.State.SUCCESS) {
						List<TaskAttribute> attributes = (List<TaskAttribute>) response.getValue();
						boxAttributes.getChildren().add(new SortElementNode(attributes.get(0)));
					}
				}
			});
		});


		// accept
		btnAccept.setOnAction(event -> {
			List<SortElement> elements = new ArrayList<>();
			for (Node node : boxAttributes.getChildren()) {
				SortElementNode sortNode = (SortElementNode) node;
				elements.add(new SortElement(sortNode.sortDir, sortNode.attribute));
			}
			Logic.sort.setSort(elements);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}

}
