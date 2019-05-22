package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DependencyValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.events.AttributeValueChangeEvent;
import com.ruegnerlukas.taskmanager.logic.events.TaskValueChangeEvent;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.dependency.ItemDep;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.dependency.ItemPrereq;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.dependency.PopupDependency;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;


public class ItemDependency extends SidebarItem {


	private VBox boxDep;
	private VBox boxPrereq;
	private Button btnManage;

	private EventHandler<TaskValueChangeEvent> listenerDependency;




	public ItemDependency(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
		setupControls();
		setupInitialValue();
		setupLogic();
	}




	@Override
	protected void onAttChangedEvent(AttributeValueChangeEvent e) {
		setupControls();
		setupInitialValue();
		setupLogic();
	}




	private void setupControls() {

		VBox box = new VBox();
		box.setMinWidth(0);
		box.setSpacing(10);
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().setAll(box);

		Label labelName = new Label(getAttribute().name.get() + ":");
		labelName.setMinWidth(0);
		box.getChildren().add(labelName);

		Label labelDep = new Label("Depends on:");
		labelDep.setMinWidth(0);
		labelDep.setPadding(new Insets(0, 0, 0, 20));
		box.getChildren().add(labelDep);

		boxDep = new VBox();
		boxDep.setMinWidth(0);
		boxDep.setSpacing(3);
		boxDep.setPadding(new Insets(0, 0, 0, 30));
		box.getChildren().add(boxDep);

		btnManage = new Button("Change");
		btnManage.setMinSize(10, 32);
		btnManage.setPrefSize(150, 32);
		btnManage.setMaxSize(150, 32);
		VBox.setMargin(btnManage, new Insets(0, 0, 0, 30));
		btnManage.setOnAction(e -> {
			PopupDependency popup = new PopupDependency(getTask(), getAttribute());
			Stage stage = new Stage();
			popup.setStage(stage);
			popup.create();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(TaskManager.getPrimaryStage());
			Scene scene = new Scene(popup, popup.getPopupWidth(), popup.getPopupHeight());
			scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
				if (ke.getCode() == KeyCode.R) {
					UIDataHandler.reloadAll();
					ke.consume();
				}
			});
			stage.setScene(scene);
			stage.show();
		});
		box.getChildren().add(btnManage);

		Label labelPreq = new Label("Prerequisite for:");
		labelPreq.setMinWidth(0);
		labelPreq.setPadding(new Insets(0, 0, 0, 20));
		box.getChildren().add(labelPreq);

		boxPrereq = new VBox();
		boxPrereq.setMinWidth(0);
		boxPrereq.setSpacing(3);
		boxPrereq.setPadding(new Insets(0, 0, 0, 30));
		box.getChildren().add(boxPrereq);


	}




	private void setupInitialValue() {
		TaskValue<?> taskValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		setDependencyValue(taskValue);
		updatePrerequisites();
	}




	private void setupLogic() {
		listenerDependency = e -> {
			if (e.getTask() == getTask() && e.getAttribute() == getAttribute()) {
				setDependencyValue(e.getNewValue());
			}
			if (e.getTask() != getTask() && e.getAttribute() == getAttribute()) {
				onOtherTaskChanged(e.getTask(), e.getNewValue());
			}
		};
		TaskLogic.addOnTaskValueChanged(listenerDependency);
	}




	private void setDependencyValue(TaskValue<?> taskValue) {
		boxDep.getChildren().clear();
		if (taskValue != null && taskValue.getAttType() == AttributeType.DEPENDENCY) {
			DependencyValue value = (DependencyValue) taskValue;
			for (Task task : value.getValue()) {
				addDependencyItem(task);
			}
		}
	}




	private void updatePrerequisites() {

		List<Task> prerequisites = new ArrayList<>();
		for (int i = 0, n = Data.projectProperty.get().data.tasks.size(); i < n; i++) {
			Task task = Data.projectProperty.get().data.tasks.get(i);
			if (task == getTask()) {
				continue;
			}
			TaskValue<?> taskValue = TaskLogic.getValueOrDefault(task, getAttribute());
			if (taskValue.getAttType() == AttributeType.DEPENDENCY) {
				DependencyValue value = (DependencyValue) taskValue;
				for (int j = 0; j < value.getValue().length; j++) {
					Task t = value.getValue()[j];
					if (t == this.getTask()) {
						prerequisites.add(task);
					}
				}
			}
		}

		for (Task task : prerequisites) {
			addPrerequisiteItem(task);
		}
	}




	private void onOtherTaskChanged(Task otherTask, TaskValue<?> newValue) {
		if(newValue == null) {
			return;
		}

		// if new value contains this.getTask() && otherTask not contained in prereqList -> add otherTask to prereq list
		if (newValue.getAttType() == AttributeType.DEPENDENCY) {
			Set<Task> tasks = new HashSet<>(Arrays.asList(((DependencyValue) newValue).getValue()));
			if (tasks.contains(this.getTask()) && findItemPrereq(otherTask) == null) {
				addPrerequisiteItem(otherTask);
			}
		}

		// if otherTask is contained in prereqList && newValue does not contain this.getTask() -> remove otherTask from prereqList
		if (findItemPrereq(otherTask) != null) {
			if (newValue.getAttType() == null) {
				removePrerequisiteItem(otherTask);
			} else if (newValue.getAttType() == AttributeType.DEPENDENCY) {
				Set<Task> tasks = new HashSet<>(Arrays.asList(((DependencyValue) newValue).getValue()));
				if (!tasks.contains(this.getTask())) {
					removePrerequisiteItem(otherTask);
				}
			}
		}

	}




	public void removeDependency(ItemDep item) {
		TaskValue<?> taskValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (taskValue.getAttType() != null) {
			DependencyValue value = (DependencyValue) taskValue;
			List<Task> list = new ArrayList<>(Arrays.asList(value.getValue()));
			list.remove(item.task);
			if (list.isEmpty()) {
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
			} else {
				Task[] array = new Task[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = list.get(i);
				}
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new DependencyValue(array));
			}
		}
	}




	private void addDependencyItem(Task task) {
		ItemDep item = new ItemDep(this, task);
		boxDep.getChildren().add(item);
	}




	private void removeDependencyItem(Task task) {
		ItemDep item = findItemDep(task);
		if (item != null) {
			boxDep.getChildren().remove(item);
		}
	}




	private void addPrerequisiteItem(Task task) {
		ItemPrereq item = new ItemPrereq(this, task);
		boxPrereq.getChildren().add(item);
	}




	private void removePrerequisiteItem(Task task) {
		ItemPrereq item = findItemPrereq(task);
		if (item != null) {
			boxPrereq.getChildren().remove(item);
		}
	}




	private ItemDep findItemDep(Task task) {
		for (int i = 0; i < boxDep.getChildren().size(); i++) {
			Node node = boxDep.getChildren().get(i);
			if (node instanceof ItemDep) {
				ItemDep item = (ItemDep) node;
				if (item.task == task) {
					return item;
				}
			}
		}
		return null;
	}




	private ItemPrereq findItemPrereq(Task task) {
		for (int i = 0; i < boxPrereq.getChildren().size(); i++) {
			Node node = boxPrereq.getChildren().get(i);
			if (node instanceof ItemPrereq) {
				ItemPrereq item = (ItemPrereq) node;
				if (item.task == task) {
					return item;
				}
			}
		}
		return null;
	}




	@Override
	public void dispose() {
		TaskLogic.removeOnTaskValueChanged(listenerDependency);
		super.dispose();
	}


}






