package com.binaryname.view;

import java.util.Iterator;

import com.binaryname.controller.ConversionController;
import com.sun.javafx.tk.Toolkit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class Main extends Application {

	private ConversionController conversionController = new ConversionController();

	private PopupWindow keyboard;

	private final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	private final Rectangle2D bounds = Screen.getPrimary().getBounds();
	private final double taskbarHeight = bounds.getHeight() - visualBounds.getHeight();

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Binary Name");

		Label helloLbl = new Label("Hello");
		helloLbl.setAlignment(Pos.CENTER);
		helloLbl.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 68));
		helloLbl.setStyle("-fx-background-color: red;padding: 20px;");
		helloLbl.setTextFill(Color.web("#ffffff"));

		Label myNameLbl = new Label("my name is");
		myNameLbl.setAlignment(Pos.CENTER);
		myNameLbl.setFont(Font.font("Comic Sans MS", 48));
		myNameLbl.setStyle("-fx-background-color: red;padding: 20px;");
		myNameLbl.setTextFill(Color.web("#ffffff"));

		TextArea nameTxtArea = new TextArea();
		nameTxtArea.setWrapText(Boolean.TRUE);
		nameTxtArea.getStyleClass().add("center-text-area");
		nameTxtArea.setFont(Font.font("Comic Sans MS", 28));
		nameTxtArea.setStyle("padding: 20px;");

		Button printBtn = new Button("PRINT");
		printBtn.setFont(Font.font("Arial Rounded MT Bold", 30));
		printBtn.setStyle("-fx-background-color: #ffcccc; padding: 20px;margin: 10px;-fx-text-fill: #006666;");
		printBtn.setDisable(Boolean.TRUE);

		Button convertBtn = new Button("Convert Now!");
		convertBtn.setFont(Font.font("Arial Rounded MT Bold", 30));
		convertBtn.setStyle("-fx-background-color: #ffcccc; padding: 20px;margin: 10px;-fx-text-fill: #006666;");
		convertBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				nameTxtArea.setText(conversionController.getBinaryName(nameTxtArea.getText()));
				convertBtn.setDisable(Boolean.TRUE);
				printBtn.setDisable(Boolean.FALSE);
			}
		});

		HBox hBox = new HBox(100);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(convertBtn, printBtn);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(helloLbl, myNameLbl, nameTxtArea, hBox);
		vBox.setStyle("-fx-background-color: red;margin: 20px;");

		printBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Start printing
				print(vBox);
				convertBtn.setDisable(Boolean.FALSE);
				printBtn.setDisable(Boolean.TRUE);
				nameTxtArea.setText("");
			}
		});

		Scene scene = new Scene(vBox);
		scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());

		Toolkit tk = Toolkit.getToolkit();

		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setX(visualBounds.getMinX());
		primaryStage.setY(visualBounds.getMinY());
		primaryStage.setWidth(visualBounds.getWidth());
		primaryStage.setHeight(visualBounds.getHeight());

		primaryStage.show();

		nameTxtArea.focusedProperty().addListener((ob, b, b1) -> {
			if (keyboard == null) {
				keyboard = getPopupWindow();

				keyboard.yProperty().addListener(obs -> {
					System.out.println("wi " + keyboard.getY());
					System.out.println("wi " + nameTxtArea.getHeight());
					
					Platform.runLater(() -> {
						Double y = bounds.getHeight() - taskbarHeight - keyboard.getY();
//						primaryStage.setY(y > 0 ? -y : 0);
						nameTxtArea.setPrefRowCount((int)nameTxtArea.getHeight() - (int)keyboard.getY());
					});
				});
			}
		});

		nameTxtArea.onMouseClickedProperty().addListener((ob, b, b1) -> {
			if (keyboard == null) {
				keyboard = getPopupWindow();

				keyboard.yProperty().addListener(obs -> {
					System.out.println("wi " + keyboard.getY());
					Platform.runLater(() -> {
						double y = bounds.getHeight() - taskbarHeight - keyboard.getY();
						primaryStage.setY(y > 0 ? -y : 0);
					});
				});
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void print(Node node) {
		// Create a printer job for the default printer
		PrinterJob job = PrinterJob.createPrinterJob();

		if (job != null) {
			// Print the node
			boolean printed = job.printPage(node);

			if (printed) {
				// End the printer job
				job.endJob();
			} else {
				// Write Error Message
				System.out.println("Printing failed.");
			}
		} else {
			// Write Error Message
			System.out.println("Could not create a printer job.");
		}
	}

	private PopupWindow getPopupWindow() {

		@SuppressWarnings("deprecation")
		final Iterator<Window> windows = Window.impl_getWindows();

		while (windows.hasNext()) {
			final Window window = windows.next();
			if (window instanceof PopupWindow) {
				if (window.getScene() != null && window.getScene().getRoot() != null) {
					Parent root = window.getScene().getRoot();
					if (root.getChildrenUnmodifiable().size() > 0) {
						Node popup = root.getChildrenUnmodifiable().get(0);
						if (popup.lookup(".fxvk") != null) {
							return (PopupWindow) window;
						}
					}
				}
				return null;
			}
		}
		return null;
	}

}
