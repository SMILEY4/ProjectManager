package com.ruegnerlukas.taskmanager.ui.taskview.taskcard;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class layout_taskcardBase {


	public final AnchorPane root;
	public final Pane paneFlag;
	public final Pane paneBackground;
	public final VBox vBox;
	public final Label labelID;
	public final AnchorPane paneDescription;




	public layout_taskcardBase() {

		root = new AnchorPane();

		paneFlag = new Pane();
		paneBackground = new Pane();
		vBox = new VBox();
		labelID = new Label();
		paneDescription = new AnchorPane();

		root.setPrefHeight(200.0);
		root.setPrefWidth(320.0);

		AnchorPane.setBottomAnchor(paneFlag, 0.0);
		AnchorPane.setLeftAnchor(paneFlag, 0.0);
		AnchorPane.setTopAnchor(paneFlag, 0.0);
		paneFlag.setLayoutX(139.0);
		paneFlag.setLayoutY(89.0);
		paneFlag.setMaxHeight(10000.0);
		paneFlag.setMaxWidth(5.0);
		paneFlag.setMinHeight(0.0);
		paneFlag.setMinWidth(8.0);
		paneFlag.setPrefHeight(10000.0);
		paneFlag.setPrefWidth(5.0);

		AnchorPane.setBottomAnchor(paneBackground, 0.0);
		AnchorPane.setLeftAnchor(paneBackground, 8.0);
		AnchorPane.setRightAnchor(paneBackground, 0.0);
		AnchorPane.setTopAnchor(paneBackground, 0.0);
		paneBackground.setLayoutX(147.0);
		paneBackground.setLayoutY(89.0);
		paneBackground.setPrefHeight(200.0);
		paneBackground.setPrefWidth(200.0);

		AnchorPane.setBottomAnchor(vBox, 0.0);
		AnchorPane.setLeftAnchor(vBox, 8.0);
		AnchorPane.setRightAnchor(vBox, 0.0);
		AnchorPane.setTopAnchor(vBox, 0.0);
		vBox.setLayoutX(147.0);
		vBox.setLayoutY(89.0);
		vBox.setPrefHeight(200.0);
		vBox.setPrefWidth(100.0);
		vBox.setSpacing(4.0);

		labelID.setMinHeight(20.0);
		labelID.setMinWidth(0.0);
		labelID.setPrefWidth(10000.0);
		labelID.setText("T-42");

		paneDescription.setMaxHeight(100000.0);
		paneDescription.setMinHeight(32.0);
		paneDescription.setMinWidth(0.0);
		paneDescription.setPrefHeight(100000.0);
		paneDescription.setPrefWidth(100000.0);
		vBox.setPadding(new Insets(5.0));

		root.getChildren().add(paneFlag);
		root.getChildren().add(paneBackground);
		vBox.getChildren().add(labelID);
		vBox.getChildren().add(paneDescription);
		root.getChildren().add(vBox);

	}

}
