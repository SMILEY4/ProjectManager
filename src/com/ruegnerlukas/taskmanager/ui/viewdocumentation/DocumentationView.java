package com.ruegnerlukas.taskmanager.ui.viewdocumentation;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.DocumentationFile;
import com.ruegnerlukas.taskmanager.logic.MiscLogic;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewmain.MainViewModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DocumentationView extends AnchorPane implements MainViewModule {


	public static final String TITLE = "Documentation";

	@FXML private AnchorPane rootDoc;
	@FXML private VBox boxFiles;
	@FXML private Button btnCreateFile;
	@FXML private TextArea file_area;
	@FXML private AnchorPane paneTitle;
	private EditableLabel labelTitle;

	private FileEntry selectedEntry = null;

	private Timer timer;




	public DocumentationView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_DOCUMENTATION, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading DocumentationView-FXML.", e);
		}
		create();
	}




	private void create() {

		rootDoc.getStyleClass().add("root-doc-view");
		boxFiles.getStyleClass().add("box-files");

		// btn create file
		btnCreateFile.setOnAction(e -> {
			DocumentationFile docFile = MiscLogic.createDocFile(Data.projectProperty.get(),
					"File " + Integer.toHexString(("file" + new Random().nextInt()).hashCode()));
			ProjectLogic.addDocumentationToProject(Data.projectProperty.get(), docFile);
			addDocFile(docFile, true);
		});
		btnCreateFile.getStyleClass().add("button-create");


		// title
		labelTitle = new EditableLabel();
		AnchorUtils.fitToParent(labelTitle);
		paneTitle.getChildren().add(labelTitle);
		labelTitle.addListener(((observable, oldValue, newValue) -> {
			onTitleChanged(newValue.trim());
		}));

		paneTitle.getStyleClass().add("pane-title");
		labelTitle.getStyleClass().add("label-title");


		// text area
		timer = new Timer();
		file_area.textProperty().addListener(((observable, oldValue, newValue) -> {
			restartTimer();
		}));
		file_area.focusedProperty().addListener(((observable, oldValue, newValue) -> {
			if (!newValue) {
				restartTimer();
				onTextChanged(file_area.getText());
			}
		}));
		file_area.getStyleClass().add("area-doc");


		// add all existing
		for (DocumentationFile docFile : Data.projectProperty.get().data.docFiles) {
			addDocFile(docFile, false);
		}

		onSelectFile(null);
	}




	private void restartTimer() {
		timer.cancel();
		if (file_area.focusedProperty().get()) {
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					onTextChanged(file_area.getText());
				}
			};
			timer = new Timer();
			timer.schedule(timerTask, 1000);
		}
	}




	private void onTextChanged(String newText) {
		if (selectedEntry != null) {
			selectedEntry.docFile.text.set(newText);
		}
	}




	private void onTitleChanged(String newTitle) {
		if (selectedEntry != null) {
			selectedEntry.docFile.name.set(newTitle);
		}
	}




	private void addDocFile(DocumentationFile docFile, boolean select) {
		FileEntry docEntry = new FileEntry(docFile) {
			@Override
			public void onSelect() {
				onSelectFile(this);
			}




			@Override
			public void onRemove() {
				onRemoveFile(this);
			}
		};
		boxFiles.getChildren().add(boxFiles.getChildren().size() - 1, docEntry);
		if (select) {
			onSelectFile(docEntry);
		}
	}




	private void onSelectFile(FileEntry file) {

		if(selectedEntry != null) {
			selectedEntry.getStyleClass().remove("selected-file-entry");
		}
		if(file != null) {
			file.getStyleClass().add("selected-file-entry");
		}

		selectedEntry = file;
		if (selectedEntry != null) {
			labelTitle.setEditable(true);
			labelTitle.setText(selectedEntry.getName());
			file_area.setText(selectedEntry.getText());
			file_area.setDisable(false);
		} else {
			labelTitle.setEditable(false);
			labelTitle.setText("");
			file_area.setText("");
			file_area.setDisable(true);
		}


	}




	private void onRemoveFile(FileEntry file) {
		if (file != null) {
			file.dispose();
			boxFiles.getChildren().remove(file);
			ProjectLogic.removeDocumentationFromProject(Data.projectProperty.get(), file.docFile);
		}
	}




	@Override
	public void onModuleClose() {
		for (Node node : boxFiles.getChildren()) {
			if (node instanceof FileEntry) {
				((FileEntry) node).dispose();
			}
		}
	}




	@Override
	public void onModuleOpen() {
	}




	@Override
	public void onModuleSelected() {
	}




	@Override
	public void onModuleDeselected() {
	}


}
