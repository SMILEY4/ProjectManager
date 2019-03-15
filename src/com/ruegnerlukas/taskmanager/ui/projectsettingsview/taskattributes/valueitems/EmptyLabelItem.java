package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public abstract class EmptyLabelItem extends AttributeValueItem {


	public EmptyLabelItem() {
		super();

		this.setId("item_empty");

		Label label = new Label("This Attribute can not be changed.");
		label.setAlignment(Pos.TOP_CENTER);
		label.setDisable(true);
		AnchorUtils.setAnchors(label, 0, 0, 0, 0);
		this.getChildren().add(label);

		this.setPrefSize(10000, 32);
	}




	@Override
	public double getItemHeight() {
		return 32;
	}

}
