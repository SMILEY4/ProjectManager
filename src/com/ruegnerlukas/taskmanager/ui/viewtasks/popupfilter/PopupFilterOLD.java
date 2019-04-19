//package com.ruegnerlukas.taskmanager.ui.viewtasks.popupfilter;
//
//import com.ruegnerlukas.simpleutils.logging.logger.Logger;
//import com.ruegnerlukas.taskmanager.data.Data;
//import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
//import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
//import com.ruegnerlukas.taskmanager.data.projectdata.popupfilter.*;
//import com.ruegnerlukas.taskmanager.logic.PresetLogic;
//import com.ruegnerlukas.taskmanager.logic.TaskLogic;
//import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
//import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
//import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
//import com.ruegnerlukas.taskmanager.ui.viewtasks.TasksPopup;
//import com.ruegnerlukas.taskmanager.utils.CustomProperty;
//import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.scene.Parent;
//import javafx.scene.control.*;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.VBox;
//import javafx.util.Callback;
//
//import java.io.IOException;
//
//public class PopupFilterOLD extends TasksPopup {
//
//
//	// header
//	@FXML private Button btnReset;
//
//	// presets
//	@FXML private VBox boxPresets;
//	@FXML private ComboBox<String> choiceSaved;
//	@FXML private Button btnDeleteSaved;
//	@FXML private TextField fieldSave;
//	@FXML private Button btnSave;
//
//	// body
//	@FXML private AnchorPane paneCriteria;
//	@FXML private ComboBox<FilterCriteria.CriteriaType> boxAdd;
//
//	// button
//	@FXML private Button btnCancel;
//	@FXML private Button btnAccept;
//
//
//	private final CustomProperty<FilterCriteria> filterCriteria = new CustomProperty<>();
//	private final CustomProperty<CriteriaNode> criteriaNode = new CustomProperty<>();
//	private final SimpleStringProperty currentPreset = new SimpleStringProperty();
//
//
//
//	public PopupFilterOLD() {
//		super(1000, 400);
//	}
//
//
//
//
//	@Override
//	public void create() {
//		try {
//			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_FILTER, this);
//			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
//			this.getChildren().add(root);
//		} catch (IOException e) {
//			Logger.get().error("Error loading FilterPopup-FXML: " + e);
//		}
//
//
//		// load popupfilter criteria
//		filterCriteria.set(Data.projectProperty.get().data.filterCriteria.get());
//
//
//		// button reset
//		btnReset.setOnAction(event -> {
//			System.out.println();
////			createFromFilterCriteria(this.filterCriteria.get());
//		});
//
//
//		// load preset
//		choiceSaved.getItems().addAll(Data.projectProperty.get().data.filterPresets.keySet());
//		choiceSaved.getSelectionModel().clearSelection();
//		choiceSaved.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> Platform.runLater(() -> {
//			if (choiceSaved.getValue() != null && !newValue.equals(currentPreset.get())) {
//				this.filterCriteria.set(PresetLogic.getFilterPreset(Data.projectProperty.get(), choiceSaved.getValue()));
//				createFromFilterCriteria(this.filterCriteria.get());
//
//				System.out.println("reset modified loaded");
//				onModified(false);
//			}
//		})));
//
//
//		// delete preset
//		btnDeleteSaved.setOnAction(event -> {
//			boolean deleted = PresetLogic.deleteFilterPreset(Data.projectProperty.get(), choiceSaved.getValue());
//			if (deleted && choiceSaved.getValue() != null) {
//				choiceSaved.getItems().addAll(Data.projectProperty.get().data.filterPresets.keySet());
//				choiceSaved.getSelectionModel().clearSelection();
//			}
//		});
//
//
//		// save preset
//		btnSave.setOnAction(event -> {
//			String name = fieldSave.getText().trim();
//			boolean saved = PresetLogic.saveFilterPreset(Data.projectProperty.get(), name, criteriaNode.get().buildCriteriaTree());
//			if (saved) {
//				choiceSaved.getItems().setAll(Data.projectProperty.get().data.filterPresets.keySet());
//				choiceSaved.getSelectionModel().select(name);
//				System.out.println("reset modified saved");
//				onModified(false);
//			}
//		});
//
//
//		// subCriteria list
//		initCombox(boxAdd);
//		boxAdd.getItems().addAll(FilterCriteria.CriteriaType.values());
//		boxAdd.setOnAction(event -> Platform.runLater(() -> onCreateNew(boxAdd.getValue())));
//
//		criteriaNode.addListener(((observable, oldValue, newValue) -> {
//			if (newValue != null) {
//				AnchorUtils.setAnchors(criteriaNode.get(), 0, 0, 0, 0);
//				paneCriteria.getChildren().add(criteriaNode.get());
//				boxAdd.setDisable(true);
//				boxAdd.setVisible(false);
//			} else {
//				paneCriteria.getChildren().remove(oldValue);
//				boxAdd.setDisable(false);
//				boxAdd.setVisible(true);
//			}
//			boxAdd.getSelectionModel().clearSelection();
//			System.out.println("popup root");
//			onModified();
//		}));
//
//
//		// button cancel
//		btnCancel.setOnAction(event -> this.getStage().close());
//
//
//		// button accept
//		btnAccept.setOnAction(event -> {
//			TaskLogic.setFilter(Data.projectProperty.get(), criteriaNode.get() == null ? null : criteriaNode.get().buildCriteriaTree());
//			this.getStage().close();
//		});
//
//
//		// setup initial values
//		if (this.filterCriteria.get() != null) {
//			createFromFilterCriteria(this.filterCriteria.get());
//			boxAdd.setDisable(true);
//			boxAdd.setVisible(false);
//		} else {
//			boxAdd.setDisable(false);
//			boxAdd.setVisible(true);
//		}
//		boxAdd.getSelectionModel().clearSelection();
//	}
//
//
//
//
//	private void createFromFilterCriteria(FilterCriteria criteria) {
//		this.criteriaNode.set(createNewNode(criteria, event -> onModified()));
//		this.criteriaNode.get().setOnRemove(event -> criteriaNode.set(null));
//		this.criteriaNode.get().expandTree();
//	}
//
//
//
//
//	private void onCreateNew(FilterCriteria.CriteriaType type) {
//		if (type == null) {
//			return;
//		}
//		this.criteriaNode.set(createNewNode(createNewCriteria(type), event -> onModified()));
//		this.criteriaNode.get().setOnRemove(event -> criteriaNode.set(null));
//		this.criteriaNode.get().expandTree();
//	}
//
//
//
//
//	private void onModified() {
//		onModified(true);
//	}
//
//
//
//
//	private void onModified(boolean modified) {
//		System.out.println("MODIFIED " + modified);
//		if(modified) {
//			currentPreset.set(null);
//			choiceSaved.getSelectionModel().clearSelection();
//			btnDeleteSaved.setDisable(true);
//		} else {
//			currentPreset.set(choiceSaved.getValue());
//			btnDeleteSaved.setDisable(false);
//		}
//	}
//
//
//
//
//	public static FilterCriteria createNewCriteria(FilterCriteria.CriteriaType type) {
//		if (type == FilterCriteria.CriteriaType.AND) {
//			return new AndFilterCriteria();
//		}
//		if (type == FilterCriteria.CriteriaType.OR) {
//			return new OrFilterCriteria();
//		}
//		if (type == FilterCriteria.CriteriaType.TERMINAL) {
//			return createDefaultTerminalCriteria();
//		}
//		return null;
//	}
//
//
//
//
//	private static TerminalFilterCriteria createDefaultTerminalCriteria() {
//		final TaskAttribute attribute = AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID);
//		final FilterOperation operation = FilterOperation.EQUALS;
//		final Object[] values = new Object[]{0};
//		return new TerminalFilterCriteria(attribute, operation, values);
//	}
//
//
//
//
//	public static CriteriaNode createNewNode(FilterCriteria criteria, EventHandler<ActionEvent> handerModified) {
//		if (criteria.type == FilterCriteria.CriteriaType.AND) {
//			return new AndNode(criteria, handerModified);
//		}
//		if (criteria.type == FilterCriteria.CriteriaType.OR) {
//			return new OrNode(criteria, handerModified);
//		}
//		if (criteria.type == FilterCriteria.CriteriaType.TERMINAL) {
//			return new TerminalNode(criteria, handerModified);
//		}
//		return null;
//	}
//
//
//
//
//	public static void initCombox(ComboBox<FilterCriteria.CriteriaType> combobox) {
//		combobox.setPromptText("Create Criteria");
//		combobox.setCellFactory(new Callback<ListView<FilterCriteria.CriteriaType>, ListCell<FilterCriteria.CriteriaType>>() {
//			@Override
//			public ListCell<FilterCriteria.CriteriaType> call(ListView<FilterCriteria.CriteriaType> p) {
//				return new ListCell<FilterCriteria.CriteriaType>() {
//					@Override
//					protected void updateItem(FilterCriteria.CriteriaType item, boolean empty) {
//						super.updateItem(item, empty);
//						if (item == null || empty) {
//							setText("");
//							setGraphic(null);
//						} else {
//							setText(item.toString());
//							setGraphic(null);
//						}
//					}
//				};
//			}
//		});
//	}
//
//
//}
