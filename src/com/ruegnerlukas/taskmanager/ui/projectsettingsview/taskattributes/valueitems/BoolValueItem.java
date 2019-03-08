package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class BoolValueItem extends AttributeValueItem {


	private boolean value;
	private CheckBox checkBox;



	public BoolValueItem(String name, boolean value) {
		super();
		this.value = value;

		HBox box = new HBox();
		box.setSpacing(20);
		box.setMinSize(0, 32);
		box.setPrefSize(10000, 32);
		box.setMaxSize(10000, 32);
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().add(box);

		VBox boxLabel = new VBox();
		boxLabel.setAlignment(Pos.CENTER_RIGHT);
		boxLabel.setMinSize(0, 32);
		boxLabel.setPrefSize(10000, 32);
		boxLabel.setMaxSize(10000, 32);
		box.getChildren().add(boxLabel);

		Label label = new Label(name);
		boxLabel.getChildren().add(label);

		VBox boxValue = new VBox();
		boxValue.setAlignment(Pos.CENTER_LEFT);
		boxValue.setMinSize(0, 32);
		boxValue.setPrefSize(10000, 32);
		boxValue.setMaxSize(10000, 32);
		box.getChildren().add(boxValue);

		checkBox = new CheckBox("");
		checkBox.setMinSize(50, 32);
		checkBox.setPrefSize(200, 32);
		checkBox.setMaxSize(10000, 32);
		boxValue.getChildren().add(checkBox);

		checkBox.setOnAction(event -> {
			setValue(checkBox.isSelected());
		});

		this.setPrefSize(10000, 32);
	}




	public void setValue(boolean value) {
		if (this.value != value) {
			this.value = value;
			checkBox.setSelected(value);
			setChanged(true);
		}
	}




	public boolean getValue() {
		return value;
	}




	@Override
	public double getItemHeight() {
		return 32;
	}

}
