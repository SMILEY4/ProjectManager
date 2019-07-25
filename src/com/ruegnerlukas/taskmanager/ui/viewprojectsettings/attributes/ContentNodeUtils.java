package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContentNodeUtils {


	/**
	 * Builds the bottom "Save" and "Discard" {@link Button}s and adds them to the given {@link VBox}.
	 *
	 * @return the created {@link Button}s
	 */
	public static Button[] buildButtons(VBox vbox) {

		HBox boxButtons = ContentNodeUtils.buildButtonBox();
		vbox.getChildren().add(boxButtons);

		Button btnDiscard = ContentNodeUtils.buildButton("Discard");
		boxButtons.getChildren().add(btnDiscard);

		Button btnSave = ContentNodeUtils.buildButton("Save");
		boxButtons.getChildren().add(btnSave);

		return new Button[]{btnDiscard, btnSave};
	}




	/**
	 * @return the {@link HBox} for the buttons
	 */
	private static HBox buildButtonBox() {
		HBox box = new HBox();
		box.setPadding(new Insets(10, 0, 0, 0));
		box.setSpacing(5);
		box.setAlignment(Pos.TOP_RIGHT);
		box.setMinSize(0, 32);
		box.setPrefSize(100000, 32);
		box.setMaxSize(100000, 32);
		return box;
	}




	/**
	 * @return a single {@link Button}
	 */
	private static Button buildButton(String text) {
		Button button = new Button(text);
		button.setMinSize(80, 32);
		button.setPrefSize(80, 32);
		button.setMaxSize(80, 32);
		return button;
	}


}
