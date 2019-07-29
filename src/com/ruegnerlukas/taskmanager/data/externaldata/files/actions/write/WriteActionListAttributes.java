package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOTaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteActionListAttributes extends WriteFileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// ATTRIBUTE HAS CHANGED
		if (change.getType() == DataChange.ChangeType.NESTED) {

			DataChange attribChange = ((NestedChange) change).getNext();
			if (attribChange.getType() == DataChange.ChangeType.NESTED) {
				String attribID = attribChange.getIdentifier().split("-")[1];


				final File file = fileHandler.getAttributeFile(attribID, false);
				if (file == null) {
					return;
				}

				// ignore real change (value/map-change) here, since we will write the whole attribute to the file

				// get attribute
				TaskAttribute attribute = AttributeLogic.findAttributeByID(project, Integer.parseInt(attribID));
				if (attribute == null) {
					return;
				}
				POJOTaskAttribute data = new POJOTaskAttribute(attribute);

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


		// ADD, REMOVE ATTRIBUTE
		if (change.getType() == DataChange.ChangeType.LIST) {
			ListChange listChange = (ListChange) change;


			// add
			if (listChange.wasAdded()) {
				if (!(listChange.getAdded() instanceof TaskAttribute)) {
					return;
				}
				TaskAttribute attribute = (TaskAttribute) listChange.getAdded();

				try {

					File file = fileHandler.getAttributeFile(Integer.toString(attribute.id), true);

					// write
					Gson gson = JsonUtils.getGson(project);
					POJOTaskAttribute data = new POJOTaskAttribute(attribute);
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
				if (!(listChange.getRemoved() instanceof TaskAttribute)) {
					return;
				}
				TaskAttribute attribute = (TaskAttribute) listChange.getRemoved();
				File file = fileHandler.getAttributeFile(Integer.toString(attribute.id), false);
				if (file != null) {
					file.delete();
				}
			}

		}

	}

}
