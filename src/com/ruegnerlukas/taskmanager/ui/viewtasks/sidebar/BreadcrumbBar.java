package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXListChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbBar extends AnchorPane {


	private Button btnBack;
	private HBox content;

	private List<Task> queue = new ArrayList<>();

	private FXListChangeListener<Task> listenerTasks;




	public BreadcrumbBar() {

		// button
		btnBack = new Button();
		ButtonUtils.makeIconButton(btnBack, SVGIcons.ARROW_LEFT, 0.6);
		btnBack.setMinSize(32, 32);
		btnBack.setMaxSize(32, 32);
		AnchorPane.setLeftAnchor(btnBack, 0.0);
		AnchorPane.setTopAnchor(btnBack, 0.0);
		AnchorPane.setBottomAnchor(btnBack, 0.0);
		this.getChildren().add(btnBack);

		btnBack.setOnAction(event -> {
			if (onStepBack(queue.get(queue.size() - 2))) {
				queue.remove(queue.size() - 1);
				refresh();
			}
		});

		// content bar
		content = new HBox();
		content.setSpacing(4);
		content.setPadding(new Insets(0, 0, 0, 4));
		AnchorUtils.setAnchors(content, 0, 0, 0, 32);
		this.getChildren().add(content);

		// on task deleted
		listenerTasks = new FXListChangeListener<Task>(Data.projectProperty.get().data.tasks) {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Task> c) {
				boolean needsRefresh = false;
				for (Task task : this.getAllRemoved(c)) {
					if (queue.contains(task)) {
						while (queue.contains(task)) {
							if (queue.remove(task)) {
								needsRefresh = true;
							}
						}
					}
				}
				if (needsRefresh) {
					refresh();
				}
			}
		};

	}




	/**
	 * Refreshes the displayed list of links. Call this after changing the list.
	 */
	private void refresh() {

		content.getChildren().clear();

		// refresh queue
		List<Task> tasksProject = Data.projectProperty.get().data.tasks;
		List<Task> invalidTasks = new ArrayList<>();
		for (Task task : queue) {
			if (!tasksProject.contains(task)) {
				invalidTasks.add(task);
			}
		}
		queue.removeAll(invalidTasks);

		// create labels
		for (int i = 0; i < queue.size(); i++) {
			Task task = queue.get(i);

			IDValue valueID = (IDValue) TaskLogic.getValueOrDefault(task, AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID));

			// label
			Label label = new Label();
			label.setText("T-" + valueID.getValue());
			label.setMinHeight(32);
			label.setMaxHeight(32);
			if (i == queue.size() - 1) {
				label.setId("breadcrumb_current");
			} else {
				label.setId("breadcrumb_task");
				int index = i;
				label.setOnMouseClicked(event -> {
					if (onJumpBack(queue.get(index))) {
						queue = queue.subList(0, index + 1);
						refresh();
					}
				});
			}
			content.getChildren().add(label);

			// arrow Label
			if (i != queue.size() - 1) {
				Label labelArrow = new Label(">");
				labelArrow.setId("breadcrumb_seperator");
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




	/**
	 * Adds the given {@link Task} to the end (right-side).
	 */
	public void pushTask(Task task) {
		if (task != null) {
			queue.add(task);
			refresh();
		}
	}




	/**
	 * @return the last/right {@link Task} in the list without removing it.
	 */
	public Task peekTask() {
		if (queue.isEmpty()) {
			return null;
		} else {
			return queue.get(queue.size() - 1);
		}
	}




	/**
	 * @return the last/right {@link Task} in the list and removes it.
	 */
	public Task popTask() {
		if (queue.isEmpty()) {
			return null;
		} else {
			Task task = queue.remove(queue.size() - 1);
			refresh();
			return task;
		}
	}




	/**
	 * Removes all tasks from the list.
	 */
	public void clearTasks() {
		queue.clear();
		refresh();
	}




	/**
	 * Called when the user wants to step back one task.
	 *
	 * @return false, to cancel the action; true to allow it.
	 */
	public boolean onStepBack(Task task) {
		return false;
	}




	/**
	 * Called when the user wants to step/jump back multiple tasks.
	 *
	 * @return false, to cancel the action; true to allow it.
	 */
	public boolean onJumpBack(Task task) {
		return false;
	}


}
