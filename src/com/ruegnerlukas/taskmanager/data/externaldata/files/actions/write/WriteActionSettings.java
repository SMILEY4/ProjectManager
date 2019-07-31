package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.JsonUtils;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.raw.RawSettings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteActionSettings extends WriteFileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		File file = fileHandler.getSettingsFile(true);

		// write
		try {
			RawSettings data = RawSettings.toRaw(project.settings);
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
