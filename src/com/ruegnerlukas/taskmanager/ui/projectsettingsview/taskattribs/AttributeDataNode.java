package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattribs;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedRejection;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public abstract class AttributeDataNode extends AnchorPane {


	private TaskAttribute attribute;
	private TaskAttributeNode parent;

	private Button buttonSave;
	private Button buttonDiscard;
	private boolean displaySaveButtons;
	private boolean hasChanges = false;




	public AttributeDataNode(TaskAttribute attribute, TaskAttributeNode parent, String fxmlPath, boolean displaySaveButtons) {
		try {
			this.attribute = attribute;
			this.parent = parent;
			this.displaySaveButtons = displaySaveButtons;
			create(fxmlPath);
		} catch (IOException e) {
			Logger.get().error(e);
		}
	}




	private void create(String fxmlPath) throws IOException {

		// load/create fxml
		final int addHeight = displaySaveButtons ? 42 : 0;
		AnchorPane root = (AnchorPane) FXMLUtils.loadFXML(getClass().getResource(fxmlPath), this);
		AnchorUtils.setAnchors(root, 0, 0, addHeight, 0);
		this.getChildren().add(root);
		this.setMinSize(root.getMinWidth(), root.getMinHeight() + addHeight);
		this.setPrefSize(root.getPrefWidth(), root.getPrefHeight() + addHeight);
		this.setMaxSize(root.getMaxWidth(), root.getMaxHeight() + addHeight);

		// listen for changes to attribute
		EventManager.registerListener(this, e -> {
			TaskAttribute eventAttribute = null;
			if (e instanceof AttributeUpdatedEvent) {
				eventAttribute = ((AttributeUpdatedEvent) e).getAttribute();
			} else {
				eventAttribute = ((AttributeUpdatedRejection) e).getAttribute();
			}
			if (eventAttribute == getAttribute()) {
				onChange();
			}
		}, AttributeUpdatedEvent.class, AttributeUpdatedRejection.class);


		// create buttons: save/discard changes
		if (displaySaveButtons) {
			buttonSave = new Button("Save");
			buttonSave.setPrefSize(100, 32);
			AnchorPane.setBottomAnchor(buttonSave, 5.0);
			AnchorPane.setRightAnchor(buttonSave, 5.0);
			buttonSave.setDisable(!hasChanges);
			this.getChildren().add(buttonSave);

			buttonSave.setOnAction(event -> {

				if(hasChanges) {

					SyncRequest<List<Task>> request = new SyncRequest<>();
					Logic.tasks.getTaskWithValue(getAttribute(), request);
					Response<List<Task>> response = request.getResponse();

					if(response.getValue().isEmpty()) {

						EventManager.muteListeners(this);
						onSave();
						EventManager.unmuteListeners(this);
						hasChanges = false;
						buttonSave.setDisable(true);
						buttonDiscard.setDisable(true);
						onChange();


					} else {

						Stage stage = new Stage();
						stage.initModality(Modality.WINDOW_MODAL);
						stage.initOwner(ViewManager.getPrimaryStage());
						AlertSave alert = new AlertSave(stage, response.getValue().size(), getUseDefault());
						Scene scene = new Scene(alert, 400, 190);
						stage.setScene(scene);
						stage.showAndWait();

						String result = alert.selected;

						if(result != null) {

							EventManager.muteListeners(this);
							onSave();
							EventManager.unmuteListeners(this);
							hasChanges = false;
							buttonSave.setDisable(true);
							buttonDiscard.setDisable(true);
							onChange();

							Logic.tasks.correctTaskValues(getAttribute(), result, alert.onlyInvalid);

						}

					}



				}

			});


			buttonDiscard = new Button("Discard");
			buttonDiscard.setPrefSize(100, 32);
			AnchorPane.setBottomAnchor(buttonDiscard, 5.0);
			AnchorPane.setRightAnchor(buttonDiscard, 110.0);
			buttonDiscard.setDisable(!hasChanges);
			this.getChildren().add(buttonDiscard);

			buttonDiscard.setOnAction(event -> {
				onDiscard();
				hasChanges = false;
				buttonSave.setDisable(true);
				buttonDiscard.setDisable(true);
			});
		}

		onCreate();
	}




	protected void setChanged() {
		hasChanges = true;
		if (displaySaveButtons) {
			buttonSave.setDisable(false);
			buttonDiscard.setDisable(false);
		}
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public TaskAttributeNode getParentAttributeNode() {
		return parent;
	}




	public void close() {
		EventManager.deregisterListeners(this);
	}




	protected abstract void onCreate();


	protected abstract void onChange();


	protected abstract void onSave();


	protected abstract void onDiscard();


	protected abstract void onClose();


	public abstract double getNodeHeight();


	public abstract boolean getUseDefault();

}
