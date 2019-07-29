package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ValueChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOSettings;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

import java.io.*;

public class WriteActionSettingsAttributeIDCounter extends WriteFileAction {






	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// check change type
		if (change.getType() != DataChange.ChangeType.VALUE) {
			return;
		}

		// check value type
		ValueChange valueChange = (ValueChange) change;
		if (!(valueChange.getNewValue() instanceof Integer)) {
			return;
		}

		// modify json
		try {

			File file = fileHandler.getSettingsFile(true);

			Gson gson = JsonUtils.getGson(project);

			// parse
			JsonReader reader = new JsonReader(new FileReader(file));
			POJOSettings data = gson.fromJson(reader, POJOSettings.class);
			if (data == null) {
				data = new POJOSettings();
			}

			// modify
			data.attributeIDCounter = (Integer) valueChange.getNewValue();

			// write back
			Writer writer = new FileWriter(file);
			gson.toJson(data, writer);
			writer.flush();
			writer.close();

		} catch (IOException e) {
			Logger.get().error(e);
		}

	}

}
