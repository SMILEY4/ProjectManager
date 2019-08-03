package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.popupconfirmchange;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ConfirmChangeHeader extends AnchorPane {


	public ConfirmChangeHeader(AttributeValueType type) {
		Label label = new Label(type.toString());
		label.setPadding(new Insets(0, 5, 0, 5));
		label.setAlignment(Pos.CENTER_LEFT);
		AnchorUtils.fitToParent(label);
		this.getChildren().add(label);
	}

}