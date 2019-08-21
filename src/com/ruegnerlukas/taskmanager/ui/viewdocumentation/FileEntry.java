package com.ruegnerlukas.taskmanager.ui.viewdocumentation;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class FileEntry extends AnchorPane {


	public final DocumentationFile docFile;

	private final FXChangeListener<String> listener;




	public FileEntry(DocumentationFile docFile) {
		this.docFile = docFile;

		this.getStyleClass().add("file-entry");
		this.setMinSize(0, 32);
		this.setPrefSize(10000, 32);
		this.setMaxSize(10000, 32);

		// label name
		Label label = new Label(getName());
		AnchorUtils.setAnchors(label, 0, 32, 0, 0);
		this.getChildren().add(label);
		label.getStyleClass().add("entry-name");


		listener = new FXChangeListener<String>(docFile.name) {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				label.setText(newValue);
			}
		};

		// button remove
		Button btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7);
		AnchorUtils.setAnchors(btnRemove, 0, 0, 0, null);
		this.getChildren().add(btnRemove);
		btnRemove.setOnAction(e -> onRemove());
		btnRemove.getStyleClass().add("entry-remove");

		// select
		this.setOnMouseClicked(e -> onSelect());

	}




	public String getName() {
		return this.docFile.name.get();
	}




	public String getText() {
		return this.docFile.text.get();
	}




	public void onSelect() {
	}




	public void onRemove() {

	}




	public void dispose() {
		listener.removeFromAll();
	}

}
