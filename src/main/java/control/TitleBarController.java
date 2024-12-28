package control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class TitleBarController extends HBox {

    @FXML
    private HBox titleBar;

    @FXML
    private Region spacer;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button closeButton;

    private double xOffset = 0;
    private double yOffset = 0;

    private Stage stage;

    // Resizing variables
    private boolean isResizing = false;
    private double initialWidth;
    private double initialHeight;
    private double initialX;
    private double initialY;

    private static final int RESIZE_MARGIN = 5;

    public TitleBarController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/path/to/titlebar.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        // Add dragging functionality
        spacer.setOnMousePressed(this::handleMousePressed);
        spacer.setOnMouseDragged(this::handleMouseDragged);

        // Minimize button action
        if (minimizeButton != null) {
            minimizeButton.setOnAction(event -> stage.setIconified(true));
        }

        // Maximize/Restore button action
        if (maximizeButton != null) {
            maximizeButton.setOnAction(event -> {
                if (stage.isMaximized()) {
                    stage.setMaximized(false);
                } else {
                    stage.setMaximized(true);
                }
            });
        }

        // Close button action
        if (closeButton != null) {
            closeButton.setOnAction(event -> stage.close());
        }

        // Add window resize functionality
        addResizeFunctionality();
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        if (!stage.isMaximized() && !isResizing) {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        }
    }

    private void addResizeFunctionality() {
        this.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();

            boolean right = x > stage.getWidth() - RESIZE_MARGIN;
            boolean bottom = y > stage.getHeight() - RESIZE_MARGIN;

            if (right && bottom) {
                this.setCursor(Cursor.SE_RESIZE);
            } else if (right) {
                this.setCursor(Cursor.E_RESIZE);
            } else if (bottom) {
                this.setCursor(Cursor.S_RESIZE);
            } else {
                this.setCursor(Cursor.DEFAULT);
            }
        });

        this.setOnMousePressed(event -> {
            if (this.getCursor() == Cursor.SE_RESIZE || this.getCursor() == Cursor.E_RESIZE || this.getCursor() == Cursor.S_RESIZE) {
                isResizing = true;
                initialWidth = stage.getWidth();
                initialHeight = stage.getHeight();
                initialX = event.getScreenX();
                initialY = event.getScreenY();
            }
        });

        this.setOnMouseDragged(event -> {
            if (isResizing) {
                if (this.getCursor() == Cursor.SE_RESIZE || this.getCursor() == Cursor.E_RESIZE) {
                    stage.setWidth(Math.max(300, initialWidth + (event.getScreenX() - initialX))); // Minimum width = 300
                }
                if (this.getCursor() == Cursor.SE_RESIZE || this.getCursor() == Cursor.S_RESIZE) {
                    stage.setHeight(Math.max(200, initialHeight + (event.getScreenY() - initialY))); // Minimum height = 200
                }
            }
        });

        this.setOnMouseReleased(event -> isResizing = false);
    }
}