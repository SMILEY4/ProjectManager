package com.ruegnerlukas.taskmanager_old.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.AttributeValueItem;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class TextValueItem extends AttributeValueItem {


	private String value;
	private TextField textField;



	public TextValueItem(String name, String value, String prompt) {
		super();
		this.value = value;
		this.setId("item_text");

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

		textField = new TextField();
		textField.setText(value);
		textField.setPromptText(prompt);
		textField.setMinSize(50, 32);
		textField.setPrefSize(200, 32);
		textField.setMaxSize(10000, 32);
		boxValue.getChildren().add(textField);

//		textField.setOnAction(event -> {
//			setValue(textField.getText());
//		});
//		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
//			setValue(textField.getText());
//		});

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			setValue(newValue);
		});


		this.setPrefSize(10000, 32);
	}




	public void setValue(String value) {
		if (!this.value.equals(value)) {
			this.value = value;
			textField.setText(value);
			setChanged(true);
		}
	}




	public String getValue() {
		return value;
	}




	@Override
	public double getItemHeight() {
		return 32;
	}

}
