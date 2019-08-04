package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.popupconfirmchange;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.utils.SetAttributeValueEffect;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ConfirmChangeItem extends AnchorPane {


	public ConfirmChangeItem(SetAttributeValueEffect effect) {
		this.setMinSize(32, 32);
		this.setPrefSize(10000, 32);
		this.setMaxSize(10000, 32);

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
		box.setPadding(new Insets(0, 5, 0, 5));
		box.setSpacing(5);
		AnchorUtils.fitToParent(box);
		this.getChildren().add(box);

		Label labelTask = new Label();
		labelTask.setAlignment(Pos.CENTER_LEFT);
		labelTask.setMinSize(60, 21);
		labelTask.setPrefSize(10000, 21);
		labelTask.setMaxSize(10000, 21);
		box.getChildren().add(labelTask);

		Label labelValuePrev = new Label();
		labelValuePrev.setAlignment(Pos.CENTER_RIGHT);
		labelValuePrev.setMinSize(200, 21);
		labelValuePrev.setPrefSize(200, 21);
		labelValuePrev.setMaxSize(200, 21);
		box.getChildren().add(labelValuePrev);

		Label labelArrow = new Label("->");
		labelArrow.setAlignment(Pos.CENTER);
		labelArrow.setMinSize(35, 21);
		labelArrow.setPrefSize(35, 21);
		labelArrow.setMaxSize(35, 21);
		box.getChildren().add(labelArrow);

		Label labelValueNext = new Label();
		labelValueNext.setAlignment(Pos.CENTER_LEFT);
		labelValueNext.setMinSize(200, 21);
		labelValueNext.setPrefSize(200, 21);
		labelValueNext.setMaxSize(200, 21);
		box.getChildren().add(labelValueNext);

		String strID = "T-" + TaskLogic.getTaskID(effect.task);
		String strDescr = TaskLogic.getTaskDescription(effect.task);
		labelTask.setText(strID + (strDescr.isEmpty() ? "" : " - " + strDescr));

		labelValuePrev.setText(TaskValue.valueToString(effect.prevTaskValue) + (effect.isPrevDefault ? " (D)" : ""));
		labelValueNext.setText(TaskValue.valueToString(effect.nextTaskValue) + (effect.isNextDefault ? " (D)" : ""));

	}


}
