package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import javafx.scene.control.*;


/**
 * A simple function for JavaFX menues. Contains only text.
 * */
@SuppressWarnings ("Duplicates")
public abstract class MenuFunction {


	protected String[] path;
	protected String text;

	protected MenuItem item;




	/**
	 * @param path the path to this function. The last entry is the name of this function
	 */
	public MenuFunction(String... path) {
		if (path.length == 0) {
			throw new IllegalArgumentException("Path must contain at least one element (the name)");
		}
		if (path.length == 1) {
			this.path = null;
			this.text = path[0];
		}
		if (path.length > 1) {
			this.text = path[path.length - 1];
			this.path = new String[path.length - 1];
			for (int i = 0; i < this.path.length; i++) {
				this.path[i] = path[i];
			}
		}
	}




	/**
	 * enable/disable this function
	 */
	public MenuFunction setDisable(boolean value) {
		if (this.item != null) {
			this.item.setDisable(value);
		}
		return this;
	}




	/**
	 * Set the css-style of this function
	 */
	public MenuFunction setStyle(String style) {
		if (this.item != null) {
			this.item.setStyle(style);
		}
		return this;
	}




	protected MenuItem createItem() {
		return new MenuItem(text);
	}




	/**
	 * Add this function to the given {@link MenuBar},
	 *
	 * @return this function for chaining
	 */
	public MenuFunction addToMenuBar(MenuBar menuBar) {

		if (path == null || path.length == 0) {
			Logger.get().warn("Path of MenuFunction must be not null and must contain at least one element: " + text);
			return this;

			// is submenu level =1
		} else if (path.length == 1) {
			Menu parent = null;
			for (Menu m : menuBar.getMenus()) {
				if (path[0].equals(m.getText())) {
					parent = m;
					break;
				}
			}
			if (parent == null) {
				parent = new Menu(path[0]);
				menuBar.getMenus().add(parent);
			}
			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			parent.getItems().add(this.item);


			// is submenu level >1
		} else {

			Menu parent = null;
			for (Menu m : menuBar.getMenus()) {
				if (path[0].equals(m.getText())) {
					parent = m;
					break;
				}
			}
			if (parent == null) {
				parent = new Menu(path[0]);
				menuBar.getMenus().add(parent);
			}

			for (int i = 1; i < path.length; i++) {
				boolean foundMenu = false;
				for (MenuItem item : parent.getItems()) {
					if (path[i].equals(item.getText()) && item instanceof Menu) {
						foundMenu = true;
						parent = (Menu) item;
					}
				}
				if (!foundMenu) {
					Menu m = new Menu(path[i]);
					parent.getItems().add(m);
					parent = m;
				}
			}

			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			parent.getItems().add(this.item);

		}

		return this;
	}




	/**
	 * Add this function to the given {@link MenuButton},
	 *
	 * @return this function for chaining
	 */
	public MenuFunction addToMenuButton(MenuButton menuButton) {

		if (path == null || path.length == 0) {
			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			menuButton.getItems().add(this.item);
			return this;

			// is submenu level =1
		} else if (path.length == 1) {

			Menu parent = null;
			for (MenuItem m : menuButton.getItems()) {
				if (m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu) m;
					break;
				}
			}
			if (parent == null) {
				parent = new Menu(path[0]);
				menuButton.getItems().add(parent);
			}
			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			parent.getItems().add(this.item);


			// is submenu level >1
		} else {

			Menu parent = null;
			for (MenuItem m : menuButton.getItems()) {
				if (m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu) m;
					break;
				}
			}
			if (parent == null) {
				parent = new Menu(path[0]);
				menuButton.getItems().add(parent);
			}

			for (int i = 1; i < path.length; i++) {
				boolean foundMenu = false;
				for (MenuItem item : parent.getItems()) {
					if (path[i].equals(item.getText()) && item instanceof Menu) {
						foundMenu = true;
						parent = (Menu) item;
					}
				}
				if (!foundMenu) {
					Menu m = new Menu(path[i]);
					parent.getItems().add(m);
					parent = m;
				}
			}

			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			parent.getItems().add(this.item);

		}

		return this;
	}




	/**
	 * Add this function to the given {@link ContextMenu},
	 *
	 * @return this function for chaining
	 */
	public MenuFunction addToContextMenu(ContextMenu contextMenu) {

		if (path == null || path.length == 0) {
			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			contextMenu.getItems().add(this.item);
			return this;

			// is submenu level =1
		} else if (path.length == 1) {

			Menu parent = null;
			for (MenuItem m : contextMenu.getItems()) {
				if (m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu) m;
					break;
				}
			}
			if (parent == null) {
				parent = new Menu(path[0]);
				contextMenu.getItems().add(parent);
			}
			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			parent.getItems().add(this.item);


			// is submenu level >1
		} else {

			Menu parent = null;
			for (MenuItem m : contextMenu.getItems()) {
				if (m instanceof Menu && path[0].equals(m.getText())) {
					parent = (Menu) m;
					break;
				}
			}
			if (parent == null) {
				parent = new Menu(path[0]);
				contextMenu.getItems().add(parent);
			}

			for (int i = 1; i < path.length; i++) {
				boolean foundMenu = false;
				for (MenuItem item : parent.getItems()) {
					if (path[i].equals(item.getText()) && item instanceof Menu) {
						foundMenu = true;
						parent = (Menu) item;
					}
				}
				if (!foundMenu) {
					Menu m = new Menu(path[i]);
					parent.getItems().add(m);
					parent = m;
				}
			}

			this.item = createItem();
			this.item.setOnAction(event -> onAction());
			parent.getItems().add(this.item);

		}

		return this;
	}




	/**
	 * This method is called when the user selects this function.
	 */
	public abstract void onAction();


}
