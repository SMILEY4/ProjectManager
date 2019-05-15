package com.ruegnerlukas.taskmanager_old.utils.uielements.menu;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public abstract class MenuFunction {

	protected String path[];
	protected String text;
	
	protected MenuItem item;
	
	private Map<String,Object> map = new HashMap<>();
	
	
	
	
	public MenuFunction(String... path) {
		if(path.length == 0) {
			throw new IllegalArgumentException("Path must contain at least one element (the name)");
		} 
		if(path.length == 1) {
			this.path = null;
			this.text = path[0];
		}
		if(path.length > 1) {
			this.text = path[path.length-1];
			this.path = new String[path.length-1];
			for(int i=0; i<this.path.length; i++) {
				this.path[i] = path[i];
			}
		}
	}
	
	
	
	
	
	
	public MenuFunction setValue(String key, Object value) {
		map.put(key, value);
		return this;
	}
	
	
	
	
	public Object getValue(String key) {
		return map.get(key);
	}

	
	
	
	public MenuFunction setDisable(boolean value) {
		if(this.item != null) {
			this.item.setDisable(value);
		}
		return this;
	}
	
	
	
	
	public MenuFunction setStyle(String style) {
		if(this.item != null) {
			this.item.setStyle(style);
		}
		return this;
	}
	
	
	
	
	protected MenuItem createItem() {
		return new MenuItem(text);
	}
	
	
	
	
	public MenuFunction addToMenuBar(MenuBar menuBar) {
		
		if(path == null || path.length == 0) {
			Logger.get().warn("Path of MenuFunction must be not null and must contain at least one element: " + text);
			return this;

		// is submenu level =1
		} else if(path.length == 1) {
			Menu parent = null;
			for(Menu m : menuBar.getMenus()) {
				if(path[0].equals(m.getText())) {
					parent = m;
					break;
				}
			}
			if(parent == null) {
				parent = new Menu(path[0]);
				menuBar.getMenus().add(parent);
			}
			this.item = createItem();
			this.item.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					onAction();
				}
			});
			parent.getItems().add(this.item);
			
			
		// is submenu level >1
		} else {

			Menu parent = null;
			for(Menu m : menuBar.getMenus()) {
				if(path[0].equals(m.getText())) {
					parent = m;
					break;
				}
			}
			if(parent == null) {
				parent = new Menu(path[0]);
				menuBar.getMenus().add(parent);
			}
			
			for(int i=1; i<path.length; i++) {
				boolean foundMenu = false;
				for(MenuItem item : parent.getItems()) {
					if(path[i].equals(item.getText()) && item instanceof Menu) {
						foundMenu = true;
						parent = (Menu)item;
					}
				}
				if(!foundMenu) {
					Menu m = new Menu(path[i]);
					parent.getItems().add(m);
					parent = m;
				}
			}
			
			if(parent != null) {
				this.item = createItem();
				this.item.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						onAction();
					}
				});
				parent.getItems().add(this.item);
			}
			
		}
		
		return this;
	}
	
	
	
	
	public MenuFunction addToMenuButton(MenuButton menuButton) {
		
		if(path == null || path.length == 0) {
			this.item = createItem();
			this.item.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					onAction();
				}
			});
			menuButton.getItems().add(this.item);
			return this;

		// is submenu level =1
		} else if(path.length == 1) {
			
			Menu parent = null;
			for(MenuItem m : menuButton.getItems()) {
				if(m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu)m;
					break;
				}
			}
			if(parent == null) {
				parent = new Menu(path[0]);
				menuButton.getItems().add(parent);
			}
			this.item = createItem();
			this.item.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					onAction();
				}
			});
			parent.getItems().add(this.item);
			
			
		// is submenu level >1
		} else {

			Menu parent = null;
			for(MenuItem m : menuButton.getItems()) {
				if(m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu)m;
					break;
				}
			}
			if(parent == null) {
				parent = new Menu(path[0]);
				menuButton.getItems().add(parent);
			}
			
			for(int i=1; i<path.length; i++) {
				boolean foundMenu = false;
				for(MenuItem item : parent.getItems()) {
					if(path[i].equals(item.getText()) && item instanceof Menu) {
						foundMenu = true;
						parent = (Menu)item;
					}
				}
				if(!foundMenu) {
					Menu m = new Menu(path[i]);
					parent.getItems().add(m);
					parent = m;
				}
			}
			
			if(parent != null) {
				this.item = createItem();
				this.item.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						onAction();
					}
				});
				parent.getItems().add(this.item);
			}
			
		}
		
		return this;
	}
	
	
	
	public MenuFunction addToContextMenu(ContextMenu contextMenu) {
		
		if(path == null || path.length == 0) {
			this.item = createItem();
			this.item.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					onAction();
				}
			});
			contextMenu.getItems().add(this.item);
			return this;

		// is submenu level =1
		} else if(path.length == 1) {
			
			Menu parent = null;
			for(MenuItem m : contextMenu.getItems()) {
				if(m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu)m;
					break;
				}
			}
			if(parent == null) {
				parent = new Menu(path[0]);
				contextMenu.getItems().add(parent);
			}
			this.item = createItem();
			this.item.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					onAction();
				}
			});
			parent.getItems().add(this.item);
			
			
		// is submenu level >1
		} else {

			Menu parent = null;
			for(MenuItem m : contextMenu.getItems()) {
				if(m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu)m;
					break;
				}
			}
			if(parent == null) {
				parent = new Menu(path[0]);
				contextMenu.getItems().add(parent);
			}
			
			for(int i=1; i<path.length; i++) {
				boolean foundMenu = false;
				for(MenuItem item : parent.getItems()) {
					if(path[i].equals(item.getText()) && item instanceof Menu) {
						foundMenu = true;
						parent = (Menu)item;
					}
				}
				if(!foundMenu) {
					Menu m = new Menu(path[i]);
					parent.getItems().add(m);
					parent = m;
				}
			}
			
			if(parent != null) {
				this.item = createItem();
				this.item.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						onAction();
					}
				});
				parent.getItems().add(this.item);
			}
			
		}
		
		return this;
	}
	
	
	
	public abstract void onAction();
	
	
}
