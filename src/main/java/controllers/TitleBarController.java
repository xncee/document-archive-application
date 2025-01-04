package controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import application.ContentSwitcher;
import utils.LocalizationUtil;
import utils.UserPreffrences;

import java.io.IOException;
import java.util.Locale;

public class TitleBarController {
    @FXML
    private Button previousPageButton;

    @FXML
    private HBox titleBar;

    @FXML
    private Button closeButton;

    private static final int RESIZE_MARGIN = 5;
    // Resizing variables
    private boolean isResizing = false;
    private double initialWidth;
    private double initialHeight;
    private double initialX;
    private double initialY;

    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    public void initialize() {
        //
    }

    private Stage getStage(Event event) {
        return (Stage) (((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void handlePreviousPage(ActionEvent event) {
        ContentSwitcher.switchToPreviousPage();
    }

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = getStage(event);
        stage.setIconified(true);
    }

    @FXML
    private void handleMaximize(ActionEvent event) {
        Stage stage = getStage(event);
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = getStage(event);
        stage.close();
    }

    // Mouse events for dragging
    @FXML
    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        Stage stage = getStage(event);
        if (!stage.isMaximized() && !isResizing) {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        }
    }

    // Mouse events for resizing
    @FXML
    private void handleMouseMoved(MouseEvent event) {
        Stage stage = getStage(event);
        double x = event.getX();
        double y = event.getY();

        boolean right = x > stage.getWidth() - RESIZE_MARGIN;
        boolean bottom = y > stage.getHeight() - RESIZE_MARGIN;

        if (right && bottom) {
            titleBar.setCursor(Cursor.SE_RESIZE);
        } else if (right) {
            titleBar.setCursor(Cursor.E_RESIZE);
        } else if (bottom) {
            titleBar.setCursor(Cursor.S_RESIZE);
        } else {
            titleBar.setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    private void handleMousePressedForResize(MouseEvent event) {
        if (titleBar.getCursor() == Cursor.SE_RESIZE || titleBar.getCursor() == Cursor.E_RESIZE || titleBar.getCursor() == Cursor.S_RESIZE) {
            isResizing = true;
            Stage stage = getStage(event);
            initialWidth = stage.getWidth();
            initialHeight = stage.getHeight();
            initialX = event.getScreenX();
            initialY = event.getScreenY();
        }
    }

    @FXML
    private void handleMouseDraggedForResize(MouseEvent event) {
        Stage stage = getStage(event);
        if (isResizing) {
            if (titleBar.getCursor() == Cursor.SE_RESIZE || titleBar.getCursor() == Cursor.E_RESIZE) {
                stage.setWidth(Math.max(300, initialWidth + (event.getScreenX() - initialX))); // Minimum width = 300
            }
            if (titleBar.getCursor() == Cursor.SE_RESIZE || titleBar.getCursor() == Cursor.S_RESIZE) {
                stage.setHeight(Math.max(200, initialHeight + (event.getScreenY() - initialY))); // Minimum height = 200
            }
        }
    }

    @FXML
    private void handleMouseReleased(MouseEvent event) {
        isResizing = false;
    }
}
