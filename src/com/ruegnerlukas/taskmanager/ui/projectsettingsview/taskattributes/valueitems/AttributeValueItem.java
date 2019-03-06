package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import javafx.scene.layout.AnchorPane;

public abstract class AttributeValueItem extends AnchorPane {


	private boolean changed = false;
	private boolean isMuted = false;



	public AttributeValueItem() {
		this.setMinSize(0, 0);
		this.setMaxSize(10000, 10000);
	}




	public void setChanged(boolean changed) {
		this.changed = changed;
		if(!isMuted) {
			onChanged();
		}
	}




	public void mute() {
		isMuted = true;
	}




	public void unmute() {
		isMuted = false;
	}




	public abstract void onChanged();




	public boolean hasChanges() {
		return changed;
	}




	public abstract double getItemHeight();

}
