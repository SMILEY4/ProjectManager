package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ListChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.JsonUtils;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;
import com.ruegnerlukas.taskmanager.data.raw.RawDocumentationFile;
import com.ruegnerlukas.taskmanager.logic.MiscLogic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteActionListDocumentation extends WriteFileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// DOCS HAVE CHANGED
		if (change.getType() == DataChange.ChangeType.NESTED) {

			DataChange docChange = ((NestedChange) change).getNext();
			if (docChange.getType() == DataChange.ChangeType.NESTED) {
				String docID = docChange.getIdentifier().split("-")[1];

				final File file = fileHandler.getDocFile(docID, false);
				if (file == null) {
					return;
				}

				// ignore real change (value/map-change) here, since we will write the whole task to the file

				// get task
				DocumentationFile docFile = MiscLogic.findDocByID(project, Integer.parseInt(docID));
				if (docFile == null) {
					return;
				}

				// write
				try {
					RawDocumentationFile data = RawDocumentationFile.toRaw(docFile);
					Gson gson = JsonUtils.getGson();
					Writer writer = new FileWriter(file);
					gson.toJson(data, writer);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					Logger.get().error("Error while writing docfile to file.", e);
				}

			}

		}


		// ADD, REMOVE DOC
		if (change.getType() == DataChange.ChangeType.LIST) {
			ListChange listChange = (ListChange) change;

			// add
			if (listChange.wasAdded()) {
				if (!(listChange.getAdded() instanceof DocumentationFile)) {
					return;
				}
				DocumentationFile docFile = (DocumentationFile) listChange.getAdded();

				File file = fileHandler.getDocFile(docFile.id.get() + "", true);

				// write
				try {
					RawDocumentationFile data = RawDocumentationFile.toRaw(docFile);
					Gson gson = JsonUtils.getGson();
					Writer writer = new FileWriter(file);
					gson.toJson(data, writer);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					Logger.get().error("Error while writing docfile to file.", e);
				}

			}

			// remove
			if (listChange.wasRemoved()) {
				if (!(listChange.getRemoved() instanceof DocumentationFile)) {
					return;
				}
				DocumentationFile docFile = (DocumentationFile) listChange.getRemoved();
				File file = fileHandler.getDocFile(docFile.id.get().toString(), false);
				if (file != null) {
					file.delete();
				}
			}

		}

	}

}
