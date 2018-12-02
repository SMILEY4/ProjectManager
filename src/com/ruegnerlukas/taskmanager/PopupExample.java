package com.ruegnerlukas.taskmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopupExample extends Application {


	public static void main(String[] args) {
		launch(args);
	}




	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Popup Example");
		final Popup popup = new Popup();
		popup.setX(300);
		popup.setY(200);
//		popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));
		
		TextField field = new TextField();
		field.setMinSize(300, 32);
		field.setMaxSize(300, 32);
		field.setPromptText("Enter text");
		popup.getContent().add(field);
		
		
		Button show = new Button("Show");
		show.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent event) {
				popup.show(primaryStage);
			}
		});

		Button hide = new Button("Hide");
		hide.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent event) {
				popup.hide();
			}
		});

		HBox layout = new HBox(10);
		layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
		layout.getChildren().addAll(show, hide);
		primaryStage.setScene(new Scene(layout));
		primaryStage.show();
	}
}