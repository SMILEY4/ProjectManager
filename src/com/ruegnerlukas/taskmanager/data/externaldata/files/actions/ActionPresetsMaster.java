package com.ruegnerlukas.taskmanager.data.externaldata.files.actions;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.MapChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOPresetMaster;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ActionPresetsMaster extends FileAction {


	@Override
	public void onChange(DataChange change, Project project, FileHandler fileHandler) {

		// ADD, REMOVE
		if (change.getType() == DataChange.ChangeType.MAP) {
			MapChange mapChange = (MapChange) change;

			// add
			if (mapChange.wasAdded()) {
				if (!(mapChange.getAddedValue() instanceof MasterPreset)) {
					return;
				}
				String presetName = (String) mapChange.getAddedKey();
				MasterPreset masterData = (MasterPreset) mapChange.getAddedValue();

				try {

					File file = fileHandler.getPresetMasterFile(presetName, true);

					// write
					Gson gson = JsonUtils.buildGson();
					POJOPresetMaster data = new POJOPresetMaster(presetName, masterData);
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
				if (!(mapChange.getRemovedValue() instanceof MasterPreset)) {
					return;
				}
				String presetName = (String) mapChange.getRemovedKey();

				File file = fileHandler.getPresetMasterFile(presetName, false);
				if (file != null) {
					file.delete();
				}
			}
		}

	}

}
