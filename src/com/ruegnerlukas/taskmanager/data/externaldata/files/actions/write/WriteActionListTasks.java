package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.JsonUtils;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.data.raw.RawTask;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteActionListTasks extends WriteFileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// TASK HAS CHANGED
		if (change.getType() == DataChange.ChangeType.NESTED) {

			DataChange taskChange = ((NestedChange) change).getNext();
			if (taskChange.getType() == DataChange.ChangeType.NESTED) {
				String taskID = taskChange.getIdentifier().split("-")[1];

				final File file = fileHandler.getTaskFile(taskID, false);
				if (file == null) {
					return;
				}

				// ignore real change (value/map-change) here, since we will write the whole task to the file

				// get task
				Task task = TaskLogic.findTaskByID(project, Integer.parseInt(taskID));
				if (task == null) {
					return;
				}

				// write
				try {
					RawTask data = RawTask.toRaw(task);
					Gson gson = JsonUtils.getGson();
					Writer writer = new FileWriter(file);
					gson.toJson(data, writer);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}


		// ADD, REMOVE TASK
		if (change.getType() == DataChange.ChangeType.LIST) {
			ListChange listChange = (ListChange) change;

			// add
			if (listChange.wasAdded()) {
				if (!(listChange.getAdded() instanceof Task)) {
					return;
				}
				Task task = (Task) listChange.getAdded();

				File file = fileHandler.getTaskFile(TaskLogic.getTaskID(task) + "", true);

				// write
				try {
					RawTask data = RawTask.toRaw(task);
					Gson gson = JsonUtils.getGson();
					Writer writer = new FileWriter(file);
					gson.toJson(data, writer);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			// remove
			if (listChange.wasRemoved()) {
				if (!(listChange.getRemoved() instanceof Task)) {
					return;
				}
				Task task = (Task) listChange.getRemoved();
				TaskAttribute idAttrib = AttributeLogic.findAttributeByType(project, AttributeType.ID);
				IDValue idValue = (IDValue) TaskLogic.getTaskValue(task, idAttrib);

				File file = fileHandler.getTaskFile(idValue.getValue().toString(), false);
				if (file != null) {
					file.delete();
				}
			}

		}

	}

}
