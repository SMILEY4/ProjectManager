package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;

import java.util.List;

public class ExternalFileHandler implements ExternalDataHandler {


	@Override
	public boolean checkChanges() {
		// TODO
		// get all files
		// get new files -> n > 0 ? -> has changed
		// files missing -> has changed
		// compare checksums of existing files -> difference ? -> has changed
		// if has changed
		//		update checksums
		//		generate+cache change-list
		//		return true
		// else
		// 		return false
		return false;
	}




	@Override
	public List<DataChange> getAllChanges() {
		// TODO
		return null;
	}




	@Override
	public void applyChange(DataChange change) {
		// TODO
		// write change to file
		// update checksums
	}


}
