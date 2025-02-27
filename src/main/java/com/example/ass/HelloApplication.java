package com.example.ass;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.Cursor;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private List<String> imagePaths = new ArrayList<>();
    private int currentIndex = 0;
    private Stage stage;
    private Scene galleryScene;

    private void loadImages() {
        File folder = new File("src/images"); // Change this to your image directory
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                    imagePaths.add(file.toURI().toString());
                }
            }
        }

        if (imagePaths.isEmpty()) {
            System.out.println("No images found in the directory!");
            return;
        }
    }


    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Load images from a directory (Modify path as needed)


        loadImages();

        // Create the gallery scene once and store it
        galleryScene = createGalleryScene();

        stage.setTitle("Image Gallery");
        stage.setScene(galleryScene);
        stage.show();
    }

    // Create the gallery scene with a grid of thumbnails
    private Scene createGalleryScene() {
        // Create Label for the heading
        Label titleLabel = new Label("Welcome to Sigma Gallery");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Custom font size
        titleLabel.setStyle("-fx-text-fill: #000000;");
        titleLabel.getStyleClass().add("title-label");

        // Create the grid pane for thumbnails
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("gallery-grid");
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int columns = 5;
        int row = 0, col = 0;

        for (int i = 0; i < imagePaths.size(); i++) {
            StackPane thumbnail = createThumbnail(imagePaths.get(i), i);
            gridPane.add(thumbnail, col, row);

            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }

        // Wrap gridPane inside a ScrollPane for scrolling
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: transparent;"); // Transparent background

        // VBox layout with titleLabel at the top
        VBox root = new VBox(20, titleLabel, scrollPane); // Title at the top
        root.setAlignment(Pos.TOP_CENTER); // Ensure alignment at the top
        root.setPadding(new Insets(20, 10, 10, 10)); // Add padding for spacing

        return new Scene(root, 600, 500);
    }
    // Create a thumbnail ImageView
    private StackPane createThumbnail(String imagePath, int index) {
        Image image = new Image(imagePath, 100, 100, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        StackPane thumbnailPane = new StackPane(imageView); // Wrap ImageView in StackPane
        thumbnailPane.getStyleClass().add("thumbnail-pane");
        thumbnailPane.setCursor(Cursor.HAND);

        // Add hover effect
        thumbnailPane.setOnMouseEntered(e -> {
            thumbnailPane.setScaleX(1.1);
            thumbnailPane.setScaleY(1.1);
        });

        thumbnailPane.setOnMouseExited(e -> {
            thumbnailPane.setScaleX(1.0);
            thumbnailPane.setScaleY(1.0);
        });

        // Click event to open full image
        thumbnailPane.setOnMouseClicked(e -> openFullSizeImage(index));

        return thumbnailPane; // Return the StackPane instead of ImageView
    }

    // Open full-size image view
    private void openFullSizeImage(int index) {
        currentIndex = index;

        try {
            ImageView imageView = new ImageView(new Image(imagePaths.get(currentIndex)));
            imageView.setFitWidth(500);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("full-image");

            Button backButton = new Button("Back");
            backButton.setOnAction(e -> stage.setScene(galleryScene)); // Uses the cached scene

            Button prevButton = new Button("Previous");
            prevButton.setOnAction(e -> showPreviousImage(imageView));

            Button nextButton = new Button("Next");
            nextButton.setOnAction(e -> showNextImage(imageView));

            HBox controls = new HBox(10, prevButton, nextButton, backButton);
            controls.setAlignment(Pos.CENTER);
            controls.getStyleClass().add("nav-buttons");

            VBox fullImageView = new VBox(20, imageView, controls);
            fullImageView.setAlignment(Pos.CENTER);

            Scene fullImageScene = new Scene(fullImageView, 600, 500);
            fullImageScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());


            stage.setScene(fullImageScene);
        } catch (Exception e) {
            System.out.println("Error loading full-size image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showPreviousImage(ImageView imageView) {
        if (currentIndex > 0) {
            currentIndex--;
            imageView.setImage(new Image(imagePaths.get(currentIndex)));
        }
    }

    private void showNextImage(ImageView imageView) {
        if (currentIndex < imagePaths.size() - 1) {
            currentIndex++;
            imageView.setImage(new Image(imagePaths.get(currentIndex)));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

