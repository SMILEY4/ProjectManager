package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOTask;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
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
				POJOTask data = new POJOTask(task);

				try {

					// write attribute
					Gson gson = JsonUtils.getGson(project);

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

				try {

					TaskAttribute idAttrib = AttributeLogic.findAttributeByType(project, AttributeType.ID);
					IDValue idValue = (IDValue) TaskLogic.getTaskValue(task, idAttrib);
					File file = fileHandler.getTaskFile(idValue.getValue().toString(), true);

					// write
					Gson gson = JsonUtils.getGson(project);
					POJOTask data = new POJOTask(task);
					Writer writer = new FileWriter(file);
					gson.toJson(data, writer);
					writer.flush();
					writer.close();

				} catch (IOException e) {
					Logger.get().error(e);
				}


			}

			// remove
			if (listChange.wasRemoved()) {
				if (!(listChange.getRemoved() instanceof Task)) {
					return;
				}
				Task task = (Task) listChange.getAdded();
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
