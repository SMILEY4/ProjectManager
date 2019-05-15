package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class EditableLabel extends AnchorPane {


	private Label label;
	private TextField field;
	private boolean editMode = false;

	private boolean editable = true;
	private boolean selectOnEdit = true;

	private Button icon;
	private boolean showEditIcon;

	private List<ChangeListener<String>> listener = new ArrayList<ChangeListener<String>>();




	public EditableLabel() {
		this("", true);
	}




	public EditableLabel(String text) {
		this(text, true);
	}




	public EditableLabel(boolean showEditIcon) {
		this("", true);
	}




	public EditableLabel(String text, boolean showEditIcon) {
		super();

		label = new Label(text);
		label.setVisible(true);
		label.setMouseTransparent(false);
		label.setPadding(new Insets(0, 10, 0, 10));
		this.getChildren().add(label);


		field = new TextField(text);
		field.setMouseTransparent(true);
		field.setVisible(false);
		this.getChildren().add(field);


		if (showEditIcon) {

			icon = new Button();
			icon.setMouseTransparent(true);
			ButtonUtils.makeIconButton(icon, SVGIcons.EDIT, 0.5f, "black");
			icon.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
			this.getChildren().add(icon);

			double iconSize = this.getHeight();
			AnchorUtils.setAnchors(label, 0, 0, 0, iconSize - (iconSize * 0.28));
			AnchorUtils.setAnchors(field, 0, 0, 0, iconSize - (iconSize * 0.28));
			AnchorPane.setBottomAnchor(icon, 0.0);
			AnchorPane.setTopAnchor(icon, 0.0);
			AnchorPane.setLeftAnchor(icon, 0.0);
			icon.setMinSize(iconSize, 0);
			icon.setPrefSize(iconSize, iconSize);
			icon.setMaxSize(iconSize, 100000);

			this.heightProperty().addListener((observable, oldValue, newValue) -> {
				double iconSize1 = newValue.doubleValue();
				AnchorUtils.setAnchors(label, 0, 0, 0, iconSize1 - (iconSize1 * 0.28));
				AnchorUtils.setAnchors(field, 0, 0, 0, iconSize1 - (iconSize1 * 0.28));
				icon.setMinSize(iconSize1, 0);
				icon.setPrefSize(iconSize1, iconSize1);
				icon.setMaxSize(iconSize1, 100000);
			});

		} else {
			AnchorUtils.setAnchors(label, 0, 0, 0, 0);
			AnchorUtils.setAnchors(field, 0, 0, 0, 0);
		}


		label.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 2) {
					enterEditMode();
				}
			}
		});

		field.setOnAction(event -> exitEditMode(false));
		field.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				exitEditMode(false);
			}
		});

		field.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				exitEditMode(true);
			}
		});

		UIDataHandler.addRoot(this, UIModule.CONTROL_EDITABLE_LABEL);
		UIDataHandler.setStyle(this, UIModule.CONTROL_EDITABLE_LABEL);
	}




	public EditableLabel setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}




	public EditableLabel setSelectOnEdit(boolean selectOnEdit) {
		this.selectOnEdit = selectOnEdit;
		return this;
	}




	@SuppressWarnings ("Duplicates")
	private void enterEditMode() {
		if (!editable) {
			return;
		}
		if (editMode) {
			return;
		}
		label.setVisible(false);
		label.setMouseTransparent(true);
		field.setText(label.getText());
		field.setMouseTransparent(false);
		field.setVisible(true);
		field.requestFocus();
		field.setStyle("-fx-border-color: transparent;");
		if (selectOnEdit) {
			field.selectAll();
		}
		editMode = true;
	}




	@SuppressWarnings ("Duplicates")
	private void exitEditMode(boolean cancel) {
		if (!editMode) {
			return;
		}
		if (cancel) {
			label.setVisible(true);
			label.setMouseTransparent(false);
			field.setMouseTransparent(true);
			field.setVisible(false);
			editMode = false;

		} else {
			String before = label.getText();
			String after = field.getText();
			label.setText(after);
			label.setVisible(true);
			label.setMouseTransparent(false);
			field.setMouseTransparent(true);
			field.setVisible(false);
			editMode = false;
			for (ChangeListener<String> listener : this.listener) {
				listener.changed(null, before, after);
			}
		}
	}




	public Label getLabel() {
		return this.label;
	}




	public TextField getTextField() {
		return this.field;
	}




	public boolean inEditMode() {
		return this.editMode;
	}




	public void setText(String text) {
		if (inEditMode()) {
			field.setText(text);
		} else {
			label.setText(text);
		}
	}




	public String getText() {
		if (inEditMode()) {
			return field.getText();
		} else {
			return label.getText();
		}
	}




	public void addListener(ChangeListener<String> listener) {
		this.listener.add(listener);
	}




	public void removeListener(ChangeListener<String> listener) {
		this.listener.add(listener);
	}


}
