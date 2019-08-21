package com.ruegnerlukas.taskmanager.data.localdata.projectdata;

import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedNode;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import com.ruegnerlukas.taskmanager.utils.listeners.CustomListener;

public class DocumentationFile implements SyncedElement {


	private final SyncedNode node;

	public final SyncedProperty<Integer> id;
	public final SyncedProperty<String> name;
	public final SyncedProperty<String> text;




	public DocumentationFile(int id, String name, Project project) {

		this.node = new SyncedNode("DocFile-" + id, project.data.docFiles.getNode(), project.dataHandler);
		this.node.setManagedElement(this);

		this.id = new SyncedProperty<>(Identifiers.DOC_ID, node, project.dataHandler, id);
		this.name = new SyncedProperty<>(Identifiers.DOC_NAME, node, project.dataHandler, name);
		this.text = new SyncedProperty<>(Identifiers.DOC_TEXT, node, project.dataHandler);


	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof NestedChange) {
			NestedChange nestedChange = (NestedChange) change;
			DataChange nextChange = nestedChange.getNext();
			if (nextChange.getIdentifier().equalsIgnoreCase(id.getNode().identifier)) {
				id.applyChange(nextChange);
			}
			if (nextChange.getIdentifier().equalsIgnoreCase(name.getNode().identifier)) {
				name.applyChange(nextChange);
			}
			if (nextChange.getIdentifier().equalsIgnoreCase(text.getNode().identifier)) {
				text.applyChange(nextChange);
			}
		}
	}




	@Override
	public SyncedNode getNode() {
		return this.node;
	}




	@Override
	public CustomListener<?> getListener() {
		return null;
	}




	@Override
	public void dispose() {
		id.dispose();
		name.dispose();
		text.dispose();
	}


}
