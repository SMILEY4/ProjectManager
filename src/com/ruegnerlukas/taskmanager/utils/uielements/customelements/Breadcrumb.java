//package com.ruegnerlukas.taskmanager.utils.uielements.customelements;
//
//import com.ruegnerlukas.taskmanager.utils.SVGIcons;
//import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Breadcrumb<T> extends AnchorPane {
//
//
//	private Button btnBack;
//	private HBox content;
//
//	private final ObservableList<T> queue = FXCollections.observableArrayList();
//
//
//
//
//	public Breadcrumb() {
//
//		// button
//		btnBack = new Button();
//		ButtonUtils.makeIconButton(btnBack, SVGIcons.ARROW_LEFT, 0.6, "black");
//		btnBack.setMinSize(32, 32);
//		btnBack.setMaxSize(32, 32);
//		AnchorPane.setLeftAnchor(btnBack, 0.0);
//		AnchorPane.setTopAnchor(btnBack, 0.0);
//		AnchorPane.setBottomAnchor(btnBack, 0.0);
//		this.getChildren().add(btnBack);
//
//		btnBack.setOnAction(event -> {
//			if (onStepBack(queue.get(queue.size() - 2))) {
//				queue.remove(queue.size() - 1);
//				refresh();
//			}
//		});
//
//		// content bar
//		content = new HBox();
//		content.setSpacing(4);
//		content.setPadding(new Insets(0, 0, 0, 4));
//		AnchorUtils.setAnchors(content, 0, 0, 0, 32);
//		this.getChildren().add(content);
//
//	}
//
//
//
//
//	public void setElements(List<T> elements) {
//
//		content.getChildren().clear();
//
//		// refresh queue
//		List<Task> tasksProject = Logic.tasks.getTasks().getValue();
//		List<Task> invalidTasks = new ArrayList<>();
//		for (Task task : queue) {
//			if (!tasksProject.contains(task)) {
//				invalidTasks.add(task);
//			}
//		}
//		queue.removeAll(invalidTasks);
//
//		// create labels
//		for (int i = 0; i < queue.size(); i++) {
//			Task task = queue.get(i);
//
//			// label
//			Label label = new Label();
//			label.setText("T-" + ((NumberValue) Logic.tasks.getAttributeValue(task, IDAttributeData.NAME).getValue()).getInt());
//			label.setMinHeight(32);
//			label.setMaxHeight(32);
//			if (i == queue.size() - 1) {
//				label.setId("breadcrumb_current");
//			} else {
//				label.setId("breadcrumb_task");
//				int index = i;
//				label.setOnMouseClicked(event -> {
//					if (onJumpBack(queue.get(index))) {
//						queue = queue.subList(0, index + 1);
//						refresh();
//					}
//				});
//			}
//			content.getChildren().add(label);
//
//			// arrow Label
//			if (i != queue.size() - 1) {
//				Label labelArrow = new Label(">");
//				labelArrow.setId("breadcrumb_seperator");
//				labelArrow.setMinSize(13, 32);
//				labelArrow.setMaxSize(13, 32);
//				content.getChildren().add(labelArrow);
//			}
//		}
//
//		if (queue.isEmpty()) {
//			btnBack.setVisible(false);
//		} else {
//			btnBack.setVisible(true);
//		}
//
//		if (queue.size() <= 1) {
//			btnBack.setDisable(true);
//		} else {
//			btnBack.setDisable(false);
//		}
//
//	}
//
//
//
//
//	public void pushTask(T task) {
//		if (task != null) {
//			queue.add(task);
//			refresh();
//		}
//	}
//
//
//
//
//	public T peekTask() {
//		if (queue.isEmpty()) {
//			return null;
//		} else {
//			return queue.get(queue.size() - 1);
//		}
//	}
//
//
//
//
//	public T popTask() {
//		if (queue.isEmpty()) {
//			return null;
//		} else {
//			Task task = queue.remove(queue.size() - 1);
//			refresh();
//			return task;
//		}
//	}
//
//
//
//
//	public void clearTasks() {
//		queue.clear();
//		refresh();
//	}
//
//
//
//
//	public boolean onStepBack(T task) {
//		return false; // return true to allow step
//	}
//
//
//
//
//	public boolean onJumpBack(T task) {
//		return false; // return true to allow jump
//	}
//
//
//}
