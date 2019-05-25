package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

public abstract class ResizableCanvas extends Canvas {


	private AnchorPane parent;



	public ResizableCanvas(AnchorPane parent, int borderRight, int borderBottom) {
		this.parent = parent;
		if (parent != null) {
			widthProperty().bind(parent.widthProperty().subtract(borderRight));
			heightProperty().bind(parent.heightProperty().subtract(borderBottom));
		}
		widthProperty().addListener(evt -> repaint());
		heightProperty().addListener(evt -> repaint());
		repaint();
	}



	
	public void repaint() {
		GraphicsContext g = this.getGraphicsContext2D();
//		g.clearRect(0, 0, getWidth(), getHeight());
		onRepaint(g);
	}
	


	
	public abstract void onRepaint(GraphicsContext g);
	
	


	@Override
	public boolean isResizable() {
		return true;
	}




	@Override
	public double prefWidth(double height) {
		return getWidth();
	}




	@Override
	public double prefHeight(double width) {
		return getHeight();
	}
}