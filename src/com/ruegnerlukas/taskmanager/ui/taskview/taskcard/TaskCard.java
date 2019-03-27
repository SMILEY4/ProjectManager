package com.ruegnerlukas.taskmanager.ui.taskview.taskcard;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.CardAttributeDisplayTypeChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.CardAttributesChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.TaskValueChangedEvent;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.cardattributes.CardAttribute;
import com.ruegnerlukas.taskmanager.ui.taskview.tasklist.TaskList;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class TaskCard extends AnchorPane {


	public Task task;
	public TaskList parent;

	@FXML private Pane paneFlag;
	@FXML private Pane paneBackground;
	@FXML private Label labelID;
	@FXML private HBox boxIcons;
	@FXML private Label labelDesc;
	@FXML private VBox boxAttribs;




	public TaskCard(Task task, TaskList parent) {
		this.task = task;
		this.parent = parent;

//		layout_taskcardBase layout = new layout_taskcardBase();
//		this.paneFlag = layout.paneFlag;
//		this.paneBackground = layout.paneBackground;
//		this.labelID = layout.labelID;
//		this.paneDescription = layout.paneDescription;
//
//		Parent root = layout.root;
//		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
//		this.getChildren().add(root);

		try {
			Parent root = UIDataHandler.loadFXML(UIModule.ELEMENT_TASKCARD, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskCard-FXML: " + e);
		}

		this.setPrefSize(10000, 200);

		create();
	}




	private void create() {

		// listen for select
		this.setOnMouseClicked(event -> {
			if (parent != null && parent.parent != null) {
				parent.parent.onTaskSelected(TaskCard.this.task, false, true);
			}
		});


		// id
		Response<TaskAttributeValue> responseID = Logic.tasks.getAttributeValue(task, IDAttributeData.NAME);
		NumberValue value = (NumberValue) responseID.getValue();
		labelID.setText("T-" + value.getInt());


		// flag
		updateFlagColor();

		EventManager.registerListener(this, e -> {
			AttributeUpdatedEvent event = (AttributeUpdatedEvent) e;
			TaskAttribute attribute = event.getAttribute();
			if (attribute.data.getType() == TaskAttributeType.FLAG) {
				if (event.getChangedVars().containsKey(TaskAttributeData.Var.DEFAULT_VALUE) || event.getChangedVars().containsKey(TaskAttributeData.Var.FLAG_ATT_FLAGS)) {
					updateFlagColor();
				}
			}
		}, AttributeUpdatedEvent.class);


		// description
		updateDescription();
		EventManager.registerListener(this, e -> {
			TaskValueChangedEvent event = (TaskValueChangedEvent) e;
			if (event.getTask() == task) {
				TaskAttribute attribute = event.getAttribute();
				if (attribute.data.getType() == TaskAttributeType.DESCRIPTION) {
					TextValue newDescription = (TextValue) event.getNewValue();
					labelDesc.setText(newDescription.getText());
				}

			}
		}, TaskValueChangedEvent.class);


		// card attributes
		updateCardAttributes();
		EventManager.registerListener(this, e -> {
			updateCardAttributes();
		}, CardAttributesChangedEvent.class, AttributeUpdatedEvent.class, TaskValueChangedEvent.class, CardAttributeDisplayTypeChangedEvent.class);

	}




	private void updateFlagColor() {
		FlagValue value = (FlagValue) Logic.tasks.getAttributeValue(task, FlagAttributeData.NAME).getValue();
		if (value != null) {
			Color flagColor = value.getFlag().color.color;
			paneFlag.setStyle("-fx-background-color: rgba(" + flagColor.getRed() * 255 + "," + flagColor.getGreen() * 255 + "," + flagColor.getBlue() * 255 + ",1.0);");
		}
	}




	private void updateDescription() {
		TextValue value = (TextValue) Logic.tasks.getAttributeValue(task, DescriptionAttributeData.NAME).getValue();
		if (value != null) {
			labelDesc.setText(value.getText());
		}
	}




	private void updateCardAttributes() {
		boxAttribs.getChildren().clear();
		boxIcons.getChildren().clear();
		List<TaskAttribute> attributes = Logic.attribute.getCardAttributes().getValue();
		for (TaskAttribute attribute : attributes) {
			CardAttribute cardAttrib = CardAttribute.create(this.task, attribute);
			boxAttribs.getChildren().add(cardAttrib);
			Region iconNode = cardAttrib.getIconNode();
			if(iconNode != null) {
				iconNode.setMinHeight(20);
				iconNode.setMaxHeight(20);
				boxIcons.getChildren().add(iconNode);
			}
		}
	}




	public void onSelect() {
		final double borderSize = 4;
		this.setBorder(new Border(new BorderStroke(
				Color.web("#50aaff"),
				BorderStrokeStyle.SOLID,
				new CornerRadii(borderSize),
				new BorderWidths(borderSize),
				new Insets(-(borderSize / 2), -(borderSize / 2), -(borderSize / 2), -(borderSize / 2))
		)));
	}




	public void onDeselect() {
		this.setBorder(Border.EMPTY);
	}




	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
