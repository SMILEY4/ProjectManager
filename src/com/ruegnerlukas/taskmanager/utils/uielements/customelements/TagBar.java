package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


/**
 * A text field that will display entered words/entries as Tags. Tags can be removed by clicking the remove-button on each tag.
 */
public class TagBar extends HBox {


	private HBox boxTags;
	private TextField input;
	private final ObservableList<Tag> tags = FXCollections.observableArrayList();

	public final SimpleIntegerProperty maxTags = new SimpleIntegerProperty(Integer.MAX_VALUE);
	public final SimpleIntegerProperty maxChars = new SimpleIntegerProperty(Integer.MAX_VALUE);
	public final SimpleBooleanProperty trimValue = new SimpleBooleanProperty(true);
	public final SimpleBooleanProperty allowDuplicates = new SimpleBooleanProperty(true);

	private ChangeListener<String> handlerAddTag;
	private ChangeListener<String> handlerRemoveTag;
	private ChangeListener<String> handlerChangeTag;
	private Predicate<String> predicate;




	public TagBar() {

		// style
		this.getStyleClass().add("tag-bar");
		UIDataHandler.setStyle(this, UIModule.STYLE_BASE);

		// tags
		boxTags = new HBox();
		boxTags.setAlignment(Pos.CENTER_LEFT);
		boxTags.prefHeightProperty().bind(this.prefHeightProperty());
		this.getChildren().add(boxTags);

		// text input
		input = new TextField();
		input.prefHeightProperty().bind(this.prefHeightProperty());
		input.setPrefWidth(-1);
		HBox.setHgrow(input, Priority.ALWAYS);
		this.getChildren().add(input);
		input.setOnAction(event -> {
			if (addTag(input.getText())) {
				input.setText("");
			}
		});

		// tag list
		tags.addListener((ListChangeListener<Tag>) c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					for (Tag tag : c.getAddedSubList()) {
						boxTags.getChildren().add(tag);
						if (handlerAddTag != null) {
							handlerAddTag.changed(null, null, tag.text.get());
						}
					}
				}
				if (c.wasRemoved()) {
					for (Tag tag : c.getRemoved()) {
						boxTags.getChildren().remove(tag);
						if (handlerRemoveTag != null) {
							handlerRemoveTag.changed(null, null, tag.text.get());
						}
					}
				}
			}
		});

		// max tags
		maxTags.addListener((observable, oldValue, newValue) -> {
			if (oldValue.intValue() > newValue.intValue() && tags.size() < newValue.intValue()) {
				tags.removeAll(tags.subList(newValue.intValue(), tags.size()));
			}
		});

		// max chars
		maxChars.addListener(((observable, oldValue, newValue) -> {
			if (oldValue.intValue() > newValue.intValue()) {
				for (Tag tag : tags) {
					if (tag.text.get().length() > newValue.intValue()) {
						tag.text.set(tag.text.get().substring(0, newValue.intValue()));
					}
				}
			}
		}));

		// trim value
		trimValue.addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				for (Tag tag : tags) {
					tag.text.set(tag.text.get().trim());
				}
			}
		}));

		// allow duplicates
		allowDuplicates.addListener(((observable, oldValue, newValue) -> {
			List<Tag> duplicates = new ArrayList<>();
			List<String> visited = new ArrayList<>();
			for (Tag tag : tags) {
				if (visited.contains(tag.text.get())) {
					duplicates.add(tag);
				} else {
					visited.add(tag.text.get());
				}
			}
			tags.removeAll(duplicates);
		}));

	}




	/**
	 * Adds a css-styleClass to the specified tag.
	 *
	 * @param tag the string-representation of the tag. Leave as null to add the style class to all current tags.
	 */
	public void addCssStyleClass(String tag, String styleClass) {
		for (Tag t : (tag == null ? tags : findTags(tag))) {
			t.getStyleClass().add(styleClass);
		}
	}




	/**
	 * Removes the css-styleClass from the specified tag.
	 *
	 * @param tag the string-representation of the tag. Leave as null to remove the style class from all current tags.
	 */
	public void removeCssStyleClass(String tag, String styleClass) {
		for (Tag t : (tag == null ? tags : findTags(tag))) {
			t.getStyleClass().remove(styleClass);
		}
	}




	/**
	 * Set a filter for valid tags.
	 */
	public void setTagFilter(Predicate<String> predicate) {
		this.predicate = predicate;
	}




	/**
	 * Listen to changes to the content of existing tags.
	 */
	public void setOnTagChanged(ChangeListener<String> listener) {
		handlerChangeTag = listener;
	}




	/**
	 * Listen to added tags.
	 */
	public void setOnTagAdded(ChangeListener<String> listener) {
		handlerAddTag = listener;
	}




	/**
	 * Listen to removed tags.
	 */
	public void setOnTagRemoved(ChangeListener<String> listener) {
		handlerRemoveTag = listener;
	}




	/**
	 * Add the given tags to this TagBar.
	 */
	public boolean addTags(String... tags) {
		boolean success = true;
		for (String tag : tags) {
			if (!addTag(tag)) {
				success = false;
			}
		}
		return success;
	}




	/**
	 * Add the given tag this this TagBar.
	 */
	public boolean addTag(String tag) {
		if ((tags.size() >= maxTags.get()) || (!allowDuplicates.get() && existsTag(tag))) {
			return false;
		}
		String strTag = trimValue.get() ? tag.trim() : tag;
		if (strTag.length() > maxChars.get()) {
			return false;
		}
		if (predicate != null && !predicate.test(strTag)) {
			return false;
		}

		Tag t = new Tag(strTag);
		t.text.addListener(((observable, oldValue, newValue) -> {
			if (handlerChangeTag != null) {
				handlerChangeTag.changed(null, oldValue, newValue);
			}
		}));
		t.removed.addListener(((observable, oldValue, newValue) -> {
			if (newValue) {
				tags.remove(t);
			}
		}));
		tags.add(t);

		return true;
	}




	/**
	 * Remove the given tag from this TagBar
	 *
	 * @return true, if a tag was removed
	 */
	public boolean removeTag(String tag) {
		Tag t = findTag(tag);
		if (t == null) {
			return false;
		}
		return tags.remove(t);
	}




	/**
	 * Remove all tags from this TagBar.
	 */
	public void removeAll() {
		tags.clear();
	}




	/**
	 * @return all current tags as an array
	 */
	public String[] getTagArray() {
		String[] array = new String[tags.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = tags.get(i).text.get();
		}
		return array;
	}




	/**
	 * @return all current tags as a list
	 */
	public List<String> getTags() {
		List<String> list = new ArrayList<>();
		for (Tag tag : tags) {
			list.add(tag.text.get());
		}
		return list;
	}




	/**
	 * @return true, if the given tag exists
	 */
	public boolean existsTag(String tag) {
		return findTag(tag) != null;
	}




	/**
	 * @return the tag with the given text or null
	 */
	private Tag findTag(String tag) {
		String strTag = trimValue.get() ? tag.trim() : tag;
		for (Tag t : tags) {
			if (t.text.get().equals(strTag)) {
				return t;
			}
		}
		return null;
	}




	/**
	 * @return a list of all tags with the given text or an empty list
	 */
	private List<Tag> findTags(String tag) {
		String strTag = trimValue.get() ? tag.trim() : tag;
		List<Tag> list = new ArrayList<>();
		for (Tag t : tags) {
			if (t.text.get().equals(strTag)) {
				list.add(t);
			}
		}
		return list;
	}


}






class Tag extends HBox {


	public final SimpleStringProperty text = new SimpleStringProperty("");
	public final SimpleBooleanProperty removed = new SimpleBooleanProperty(false);

	private Label label;
	private Button btnRemove;




	public Tag(String text) {

		this.text.set(text);
		this.text.addListener(((observable, oldValue, newValue) -> {
			label.setText(newValue);
		}));

		// this
		this.getStyleClass().add("tag");
		this.setSpacing(2);
		this.setAlignment(Pos.CENTER_LEFT);

		// label
		label = new Label(text);
		this.getChildren().add(label);

		label.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
			String originalString = label.getText();
			Text textNode = (Text) label.lookup(".text");
			String actualString = textNode.getText();
			boolean clipped = !actualString.isEmpty() && !originalString.equals(actualString);
			if (clipped) {
				label.setTooltip(new Tooltip(Tag.this.text.get()));
			} else {
				label.setTooltip(null);
			}
		});

		// button
		btnRemove = new Button();
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CANCEL, 1.2);
		this.getChildren().add(btnRemove);

		// on remove
		btnRemove.setOnAction(event -> {
			removed.set(true);
		});

	}

}