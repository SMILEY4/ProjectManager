package com.ruegnerlukas.taskmanager.ui.taskview.sidebar;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class Breadcrumb extends AnchorPane {


	private Button btnBack;
	private HBox content;

	private List<Task> queue = new ArrayList<>();




	public Breadcrumb() {

		// button
		btnBack = new Button();
		ButtonUtils.makeIconButton(btnBack, SVGIcons.ARROW_LEFT, 0.6, "black");
		btnBack.setMinSize(32, 32);
		btnBack.setMaxSize(32, 32);
		AnchorPane.setLeftAnchor(btnBack, 0.0);
		AnchorPane.setTopAnchor(btnBack, 0.0);
		AnchorPane.setBottomAnchor(btnBack, 0.0);
		this.getChildren().add(btnBack);

		btnBack.setOnAction(event -> {
			if(onStepBack(queue.get(queue.size() - 2))) {
				queue.remove(queue.size()-1);
				refresh();
			}
		});

		// content bar
		content = new HBox();
		content.setSpacing(4);
		content.setPadding(new Insets(0, 0, 0, 4));
		AnchorUtils.setAnchors(content, 0, 0, 0, 32);
		this.getChildren().add(content);

	}




	private void refresh() {

		content.getChildren().clear();

		for (int i = 0; i < queue.size(); i++) {
			Task task = queue.get(i);

			// label
			Label label = new Label();
			Logic.tasks.getAttributeValue(task, IDAttributeData.NAME, new Request<TaskAttributeValue>() {
				@Override
				public void onResponse(Response<TaskAttributeValue> response) {
					label.setText("T-" + ((NumberValue) response.getValue()).getInt());
				}
			});
			label.setMinHeight(32);
			label.setMaxHeight(32);
			if (i == queue.size() - 1) {
				label.setStyle("-fx-font-weight: bold;");
			} else {
				label.styleProperty().bind(Bindings.when(label.hoverProperty())
						.then("-fx-text-fill: #3366BB; -fx-underline: true;")
						.otherwise("-fx-text-fill: #3366BB;"));
				label.setOnMouseEntered(event -> {
					getScene().setCursor(Cursor.HAND);
				});
				label.setOnMouseExited(event -> {
					getScene().setCursor(Cursor.DEFAULT);
				});

				int index = i;
				label.setOnMouseClicked(event -> {
					if(onJumpBack(queue.get(index))) {
						queue = queue.subList(0, index+1);
						refresh();
					}
				});

			}
			content.getChildren().add(label);

			// arrow Label
			if (i != queue.size() - 1) {
				Label labelArrow = new Label(">");
				labelArrow.setMinSize(13, 32);
				labelArrow.setMaxSize(13, 32);
				content.getChildren().add(labelArrow);
			}
		}

		if (queue.isEmpty()) {
			btnBack.setVisible(false);
		} else {
			btnBack.setVisible(true);
		}

		if (queue.size() <= 1) {
			btnBack.setDisable(true);
		} else {
			btnBack.setDisable(false);
		}

	}




	public void pushTask(Task task) {
		if (task != null) {
			queue.add(task);
			refresh();
		}
	}




	public Task peekTask() {
		if (queue.isEmpty()) {
			return null;
		} else {
			return queue.get(queue.size() - 1);
		}
	}




	public Task popTask() {
		if (queue.isEmpty()) {
			return null;
		} else {
			Task task = queue.remove(queue.size() - 1);
			refresh();
			return task;
		}
	}




	public void clearTasks() {
		queue.clear();
		refresh();
	}




	public boolean onStepBack(Task task) {
		return false; // return true to allow step
	}




	public boolean onJumpBack(Task task) {
		return false; // return true to allow jump
	}

}
