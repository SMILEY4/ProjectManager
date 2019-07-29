package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.MapChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOPresetSort;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteActionPresetsSort extends WriteFileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// ADD, REMOVE
		if (change.getType() == DataChange.ChangeType.MAP) {
			MapChange mapChange = (MapChange) change;

			// add
			if (mapChange.wasAdded()) {
				if (!(mapChange.getAddedValue() instanceof SortData)) {
					return;
				}
				String presetName = (String) mapChange.getAddedKey();
				SortData sortData = (SortData) mapChange.getAddedValue();

				try {

					File file = fileHandler.getPresetSortFile(presetName, true);

					// write
					Gson gson = JsonUtils.getGson(project);
					POJOPresetSort data = new POJOPresetSort(presetName, sortData);
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
				if (!(mapChange.getRemovedValue() instanceof SortData)) {
					return;
				}
				String presetName = (String) mapChange.getRemovedKey();

				File file = fileHandler.getPresetSortFile(presetName, false);
				if (file != null) {
					file.delete();
				}
			}
		}

	}

}
