package com.ruegnerlukas.taskmanager.utils.uielements.editablelabel;

import java.util.ArrayList;
import java.util.List;

import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EditableLabel extends AnchorPane {

	
	private Label label;
	private TextField field;
	private boolean editMode = false;
	
	private boolean editable = true;
	private boolean selectOnEdit = true;
	
	private List<ChangeListener<String>> listener = new ArrayList<ChangeListener<String>>();
	
	
	
	
	public EditableLabel() {
		this("");
	}
	
	
	public EditableLabel(String text) {
		super();
		
		label = new Label(text);
		AnchorUtils.setAnchors(label, 0, 0, 0, 0);
		label.setVisible(true);
		label.setMouseTransparent(false);
		label.setPadding(new Insets(0, 10, 0, 10));
		this.getChildren().add(label);
		
		field = new TextField(text);
		AnchorUtils.setAnchors(field, 0, 0, 0, 0);
		field.setMouseTransparent(true);
		field.setVisible(false);
		this.getChildren().add(field);

		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (event.getClickCount() == 2) {
						enterEditMode();
					}
				}
			}
		});
		
		field.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				exitEditMode(false);
			}
		});
		field.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue == false) {
					exitEditMode(false);
				}
			}
		});
		
		field.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				exitEditMode(true);
			}
		});
		
		getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
		
		setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.R) {
					getStylesheets().clear();
					getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
					getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
				}
			}
		});
	}
	
	
	
	
	public EditableLabel setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}
	
	
	
	
	public EditableLabel setSelectOnEdit(boolean selectOnEdit) {
		this.selectOnEdit = selectOnEdit;
		return this;
	}
	
	
	
	
	private void enterEditMode() {
		if(!editable) {
			return;
		}
		if(editMode) {
			return;
		}
		label.setVisible(false);
		label.setMouseTransparent(true);
		field.setText(label.getText());
		field.setMouseTransparent(false);
		field.setVisible(true);
		field.requestFocus();
		if(selectOnEdit) {
			field.selectAll();
		}
		editMode = true;
	}
	
	
	
	
	private void exitEditMode(boolean cancel) {
		if(!editMode) {
			return;
		}
		if(cancel) {
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
			for(ChangeListener<String> listener : this.listener) {
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
		if(inEditMode()) {
			field.setText(text);
		} else {
			label.setText(text);
		}
	}
	
	
	
	
	public String getText() {
		if(inEditMode()) {
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
