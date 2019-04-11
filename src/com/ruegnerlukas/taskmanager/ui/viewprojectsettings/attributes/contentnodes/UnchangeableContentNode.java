package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class UnchangeableContentNode extends AttributeContentNode {


	public UnchangeableContentNode(TaskAttribute attribute) {
		super(attribute);
		Label label = new Label("This Attribute can not be changed.");
		label.setDisable(true);
		label.setAlignment(Pos.CENTER);
		AnchorUtils.setAnchors(label, 0, 0, 0, 0);
		this.getChildren().add(label);
	}




	@Override
	public double getContentHeight() {
		return 40;
	}

}
