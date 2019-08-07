package com.ruegnerlukas.taskmanager.ui.viewmain.notifications;


import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class Notification extends AnchorPane {


	public enum Type {
		MESSAGE, WARN, ERROR
	}






	public final Type type;
	public final String summary;
	public final String text;

	private TextArea area;
	private Label label;




	public Notification(Type type, String text) {
		this(type, text, (text.contains(System.lineSeparator()) ? text.split(System.lineSeparator())[0] : text));
	}




	public Notification(Type type, String text, String summary) {

		this.setMinSize(0, 0);
		this.setPrefSize(10000, -1);
		this.setMaxSize(10000, 10000);

		this.type = type;
		this.text = text;
		this.summary = summary;
		this.setStyle("-fx-border-color: #888888");

		this.label = new Label();
		this.label.setMinSize(0, 0);
		this.label.setPrefSize(10000, -1);
		this.label.setMaxSize(10000, 10000);
		this.label.setVisible(false);
		this.label.setDisable(true);
		this.label.setMouseTransparent(true);
		this.label.setPadding(new Insets(6, 10, 6, 10));
		AnchorUtils.fitToParent(this.label);
		this.getChildren().add(this.label);

		this.area = new TextArea();
		this.area.setMaxSize(10000, 10000);
		this.area.setWrapText(true);
		this.area.setEditable(false);
		AnchorUtils.setAnchors(area, 0, 0, 0, 0);
		this.getChildren().add(this.area);

		this.area.minHeightProperty().bind(this.heightProperty().subtract(2));
		this.area.prefHeightProperty().bind(this.heightProperty().subtract(2));
		this.area.maxHeightProperty().bind(this.heightProperty().subtract(2));

		this.area.focusedProperty().addListener(((observable, oldValue, newValue) -> {
			if (!newValue) {
				area.deselect();
			}
		}));

		this.label.setText(this.text);
		this.area.setText(this.text);


	}


}
