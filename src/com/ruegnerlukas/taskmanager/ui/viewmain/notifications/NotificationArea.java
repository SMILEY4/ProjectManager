package com.ruegnerlukas.taskmanager.ui.viewmain.notifications;

import com.ruegnerlukas.simpleutils.logging.LogLevel;
import com.ruegnerlukas.simpleutils.logging.builder.DefaultMessageBuilder;
import com.ruegnerlukas.simpleutils.logging.filter.LogFilter;
import com.ruegnerlukas.simpleutils.logging.logger.LogModule;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.simpleutils.logging.target.LogTarget;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class NotificationArea {


	private Label labelInfobar;
	private Button btnExpandNotifications;

	private AnchorPane paneNotifications;
	private ScrollPane scrollNotifications;
	private VBox boxNotifications;

	private boolean isExpanded = false;




	public NotificationArea(Label labelInfobar, Button btnExpandNotifications, AnchorPane paneNotifications, ScrollPane scrollNotifications, VBox boxNotifications) {
		this.labelInfobar = labelInfobar;
		this.btnExpandNotifications = btnExpandNotifications;
		this.paneNotifications = paneNotifications;
		this.scrollNotifications = scrollNotifications;
		this.boxNotifications = boxNotifications;


		paneNotifications.setPrefHeight(100000);
		paneNotifications.setMaxHeight(((AnchorPane) paneNotifications.getParent()).heightProperty().doubleValue() - 50);
		((AnchorPane) paneNotifications.getParent()).heightProperty().addListener((observable, oldValue, newValue) -> {
			paneNotifications.setMaxHeight(newValue.doubleValue() - 170);
		});

		boxNotifications.heightProperty().addListener(observable -> scrollNotifications.setVvalue(1.0));


		onCollapse();
		btnExpandNotifications.setOnAction(e -> {
			if (isExpanded) {
				onCollapse();
			} else {
				onExpand();
			}
		});

		addLogModule();


	}




	private void addLogModule() {

		DefaultMessageBuilder builder = new DefaultMessageBuilder();
		builder.setDisplayLogLevel(false);
		builder.setDisplayTimestamp(false);
		builder.setDisplayThreadName(false);
		builder.setDisplayThreadID(false);
		builder.setDisplaySource(false);
		builder.useLevelSymbols(false);
		builder.setDisplayMethodName(false);
		builder.setUseSimpleClassName(true);

		final StringBuilder stringBuilder = new StringBuilder();

		LogTarget target = new LogTarget() {
			@Override
			public boolean write(LogLevel logLevel, String message) {
				String summary;
				if (message.contains(System.lineSeparator())) {
					String[] lines = message.split(System.lineSeparator());
					summary = lines[0];
				} else {
					summary = message;
				}
				switch (logLevel) {
					case INFO: {
						NotificationArea.this.addNotification(Notification.Type.MESSAGE, summary, message);
						break;
					}
					case WARN: {
						NotificationArea.this.addNotification(Notification.Type.WARN, summary, message);
						break;
					}
					case ERROR: {
						NotificationArea.this.addNotification(Notification.Type.ERROR, summary, message);
						break;
					}
					case FATAL: {
						NotificationArea.this.addNotification(Notification.Type.ERROR, summary, message);
						break;
					}
				}
				return true;
			}




			@Override
			public boolean close() {
				return false;
			}
		};

		LogModule logModule = new LogModule("NOTIFICATIONS", builder, target);

		logModule.getFilter().addFilter(new LogFilter() {
			@Override
			public boolean acceptLogEntry(LogLevel level, long time, StackTraceElement srcTrace, long threadID, String threadName) {
				return (level != LogLevel.DEBUG && level != LogLevel.NONE);
			}
		});

		Logger.get().addModule(logModule);

	}




	public void addNotification(Notification.Type type, String summary, String text) {
		Notification notification = new Notification(type, text, summary);
		notification.getStyleClass().add("notification-"+type.toString().toLowerCase());
		boxNotifications.getChildren().add(notification);
		labelInfobar.setText(notification.summary);
	}




	private void onExpand() {
		ButtonUtils.makeIconButton(btnExpandNotifications, SVGIcons.ARROW_DOWN, 0.4);
		paneNotifications.setDisable(false);
		paneNotifications.setVisible(true);
		this.isExpanded = true;
	}




	private void onCollapse() {
		ButtonUtils.makeIconButton(btnExpandNotifications, SVGIcons.ARROW_UP, 0.4);
		paneNotifications.setDisable(true);
		paneNotifications.setVisible(false);
		this.isExpanded = false;
	}


}
