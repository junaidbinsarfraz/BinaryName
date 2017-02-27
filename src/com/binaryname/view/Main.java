package com.binaryname.view;

import java.util.Iterator;

import com.binaryname.controller.ConversionController;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import com.sun.javafx.scene.control.skin.FXVK;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
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
		printBtn.setId("ipad-grey");
		printBtn.setDisable(Boolean.TRUE);

		Button convertBtn = new Button("Convert Now!");
		convertBtn.setId("ipad-grey");
		convertBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				nameTxtArea.requestFocus();
				nameTxtArea.setText(conversionController.getBinaryName(nameTxtArea.getText()));
				convertBtn.setDisable(Boolean.TRUE);
				printBtn.setDisable(Boolean.FALSE);
			}
		});

		HBox hBox = new HBox(100);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(convertBtn, printBtn);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.TOP_CENTER);
		vBox.getChildren().addAll(helloLbl, myNameLbl, nameTxtArea, hBox);
		vBox.setStyle("-fx-background-color: red;margin: 20px;");

		printBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				nameTxtArea.requestFocus();
				// Start printing
				print(vBox, nameTxtArea.getText());
				convertBtn.setDisable(Boolean.FALSE);
				printBtn.setDisable(Boolean.TRUE);
				nameTxtArea.setText("");
			}
		});

		Scene scene = new Scene(vBox);
		scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
		primaryStage.setX(visualBounds.getMinX());
		primaryStage.setY(visualBounds.getMinY());
		primaryStage.setWidth(visualBounds.getWidth());
		primaryStage.setHeight(visualBounds.getHeight());
		
		adjustTextAreaLayout(nameTxtArea);
		
		primaryStage.show();
		
		// attach keyboard to first node on scene:
		Node first = scene.getRoot().getChildrenUnmodifiable().get(0);
		if (first != null) {
			FXVK.init(first);
			FXVK.attach(first);
			keyboard = getPopupWindow();
		}
		
		nameTxtArea.focusedProperty().addListener((ob, b, b1) -> {
			if (keyboard == null) {
				keyboard = getPopupWindow();
			}
			
			keyboard.setHideOnEscape(Boolean.FALSE);
			keyboard.setAutoHide(Boolean.FALSE);
			keyboard.centerOnScreen();
			keyboard.requestFocus();
			
			keyboard.yProperty().addListener(obs -> {
				
				Platform.runLater(() -> {
					Double y = bounds.getHeight() - taskbarHeight - keyboard.getY();
					nameTxtArea.setMaxHeight((bounds.getHeight() - y) * 0.4);
					nameTxtArea.setMinHeight((bounds.getHeight() - y) * 0.4);
				});
			});
			
			/*keyboard.setOnAutoHide(new EventHandler<Event>() {
				
				@Override
				public void handle(Event event) {
					System.out.println("I am here");
				}
				
			});
			
			keyboard.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					System.out.println("I am here");
				}
				
			});*/
		});
		

//						primaryStage.setY(y > 0 ? -y : 0);
		/*nameTxtArea.onMouseClickedProperty().addListener((ob, b, b1) -> {
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
		});*/
		
		/*nameTxtArea.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
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
	        	
	        	keyboard.show(primaryStage);
	        }
	    });*/
		
//		keyboard = getPopupWindow();
		
		/*keyboard.onCloseRequestProperty().addListener((ob, b, b1) -> {
			System.out.println("I am here");
		});*/

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void print(Node node1, String text) {
		// Create a printer job for the default printer
		
		Printer printer = Printer.getDefaultPrinter();
		Paper label = PrintHelper.createPaper("2.5x3.5", 2.5, 3.5, Units.INCH);
		PageLayout pageLayout = printer.createPageLayout(label, PageOrientation.LANDSCAPE, Printer.MarginType.EQUAL);

		PrinterJob job = PrinterJob.createPrinterJob();
		
		Node node = createFullNode(text);
		
		double scaleX = pageLayout.getPrintableWidth() / node1.getBoundsInParent().getWidth();
		double scaleY = pageLayout.getPrintableHeight() / node1.getBoundsInParent().getHeight();
		node.getTransforms().add(new Scale(scaleX, scaleY));
		
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
		
		node.getTransforms().remove(node.getTransforms().size() - 1);
	}

	/*private PopupWindow getPopupWindow() {

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
							FXVK vk = (FXVK) popup.lookup(".fxvk");
                            // hide the key:
                            vk.lookup(".hide").setVisible(false);
							return (PopupWindow) window;
						}
					}
				}
				return null;
			}
		}
		return null;
	}*/
	
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
                            FXVK vk = (FXVK) popup.lookup(".fxvk");
                            // hide the key:
                            vk.lookup(".hide").setVisible(false);
                            return (PopupWindow) window;
                        }
                    }
                }
                return null;
            }
        }
        return null;
    }
	
	private Node createFullNode(String text) {
		
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
		nameTxtArea.setFont(Font.font("Comic Sans MS", 28));
		nameTxtArea.setStyle("padding: 20px;");
		nameTxtArea.setText(text);
		nameTxtArea.getStyleClass().add("center-text-area");

		HBox hBox = new HBox(1000);
		hBox.setAlignment(Pos.CENTER);

		VBox vBox = new VBox(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(helloLbl, myNameLbl, nameTxtArea, hBox);
		vBox.setStyle("-fx-background-color: red;margin: 20px;");
		
		vBox.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());
		
		return vBox;
	}
	
	private void adjustTextAreaLayout(TextArea textArea) {
        textArea.applyCss();
        textArea.layout();

        ScrollPane textAreaScroller = (ScrollPane) textArea.lookup(".scroll-pane");
        Text text = (Text) textArea.lookup(".text");


        ChangeListener<? super Bounds> listener = 
                (obs, oldBounds, newBounds) -> centerTextIfNecessary(textAreaScroller, text);
        textAreaScroller.viewportBoundsProperty().addListener(listener);
        text.boundsInLocalProperty().addListener(listener);

    }

    private void centerTextIfNecessary(ScrollPane textAreaScroller, Text text) {
        double textHeight = text.getBoundsInLocal().getHeight();
        double viewportHeight = textAreaScroller.getViewportBounds().getHeight();
        double offset = Math.max(0, (viewportHeight - textHeight) / 2 );
        text.setTranslateY(offset);
        Parent content = (Parent)textAreaScroller.getContent();
        for (Node n : content.getChildrenUnmodifiable()) {
            if (n instanceof Path) { // caret
                n.setTranslateY(offset);
            }
        }
    }
}
