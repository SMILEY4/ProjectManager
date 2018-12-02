package com.ruegnerlukas.taskmanager.logic.data;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class TaskList {
	
	public final int id;
	public String title;
	public Color color = Color.web("29323c");

	public List<TaskCard> cards = new ArrayList<TaskCard>();
	public boolean hidden = false;
	
	
	public TaskList(int id) {
		this.id = id;
	}
	
}
