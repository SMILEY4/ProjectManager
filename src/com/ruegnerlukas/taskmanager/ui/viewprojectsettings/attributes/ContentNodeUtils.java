package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContentNodeUtils {


	public static HBox buildEntry(String name) {

		HBox boxEntry = new HBox();
		boxEntry.setSpacing(20);
		boxEntry.setMinSize(0, 32);
		boxEntry.setPrefSize(100000, 32);
		boxEntry.setMaxSize(100000, 32);

		Label label = new Label(name);
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setMinSize(0, 32);
		label.setPrefSize(100000, 32);
		label.setMaxSize(100000, 32);
		boxEntry.getChildren().add(label);

		return boxEntry;
	}




	public static HBox buildEntryWithAlignment(VBox root, String name) {

		HBox boxEntry = new HBox();
		boxEntry.setSpacing(20);
		boxEntry.setMinSize(0, 32);
		boxEntry.setPrefSize(100000, 32);
		boxEntry.setMaxSize(100000, 32);
		root.getChildren().add(boxEntry);

		Label label = new Label(name);
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setMinSize(0, 32);
		label.setPrefSize(100000, 32);
		label.setMaxSize(100000, 32);
		boxEntry.getChildren().add(label);

		HBox boxAlign = new HBox();
		boxAlign.setMinSize(0, 32);
		boxAlign.setPrefSize(100000, 32);
		boxAlign.setMaxSize(100000, 32);
		boxEntry.getChildren().add(boxAlign);

		return boxAlign;
	}




	public static CheckBox buildEntryUseDefault(VBox root, boolean selected) {
		HBox box = ContentNodeUtils.buildEntry("Use Default:");
		root.getChildren().add(box);

		CheckBox checkBox = new CheckBox("");
		checkBox.setMinSize(0, 32);
		checkBox.setPrefSize(100000, 32);
		checkBox.setMaxSize(100000, 32);
		checkBox.setSelected(selected);
		box.getChildren().add(checkBox);

		return checkBox;
	}




	public static Button[] buildButtons(VBox root) {

		HBox boxButtons = ContentNodeUtils.buildButtonBox();
		root.getChildren().add(boxButtons);

		Button btnDiscard = ContentNodeUtils.buildButton("Discard");
		boxButtons.getChildren().add(btnDiscard);

		Button btnSave = ContentNodeUtils.buildButton("Save");
		boxButtons.getChildren().add(btnSave);

		return new Button[]{btnDiscard, btnSave};
	}




	public static HBox buildButtonBox() {
		HBox box = new HBox();
		box.setPadding(new Insets(10, 0, 0, 0));
		box.setSpacing(5);
		box.setAlignment(Pos.TOP_RIGHT);
		box.setMinSize(0, 32);
		box.setPrefSize(100000, 32);
		box.setMaxSize(100000, 32);
		return box;
	}




	public static Button buildButton(String text) {
		Button button = new Button(text);
		button.setMinSize(80, 32);
		button.setPrefSize(80, 32);
		button.setMaxSize(80, 32);
		return button;
	}


}
