package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.MapChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOPresetFilter;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteActionPresetsFilter extends WriteFileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// ADD, REMOVE
		if (change.getType() == DataChange.ChangeType.MAP) {
			MapChange mapChange = (MapChange) change;

			// add
			if (mapChange.wasAdded()) {
				if (!(mapChange.getAddedValue() instanceof FilterCriteria)) {
					return;
				}
				String presetName = (String) mapChange.getAddedKey();
				FilterCriteria filterData = (FilterCriteria) mapChange.getAddedValue();

				try {

					File file = fileHandler.getPresetFilterFile(presetName, true);

					// write
					Gson gson = JsonUtils.getGson(project);
					POJOPresetFilter data = new POJOPresetFilter(presetName, filterData);
					Writer writer = new FileWriter(file);
					gson.toJson(data, writer);
					writer.flush();
					writer.close();

				} catch (IOException e) {
					Logger.get().error(e);
				}


			}

			//remove
			if (mapChange.wasRemoved()) {
				if (!(mapChange.getRemovedValue() instanceof FilterCriteria)) {
					return;
				}
				String presetName = (String) mapChange.getRemovedKey();

				File file = fileHandler.getPresetFilterFile(presetName, false);
				if (file != null) {
					file.delete();
				}
			}
		}

	}

}
