package com.example.chatter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A JavaFX chat application "Chatter".
 */
public class Userone extends Application {

    // UI components
    private TextArea text;
    private VBox chatBox;
    private ScrollPane chatScrollPane;

    // Networking components
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    /**
     * The main entry point for all JavaFX applications.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for JavaFX applications.
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        // Set the title of the window
        primaryStage.setTitle("Chatter");

        // Root pane
        BorderPane root = new BorderPane();

        // Create and set the top panel with user profile
        User currentUser = new User("One");
        currentUser.setProfilePhotoPath(getClass().getResource("/icons/1.png").toString());
        VBox topPanel = createTopPanel(currentUser);
        root.setTop(topPanel);

        // Chat box for displaying messages
        chatBox = new VBox(15);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: Transparent;");
        chatScrollPane = new ScrollPane(chatBox);
        chatScrollPane.setFitToWidth(true);
        root.setCenter(chatScrollPane);

        // Input panel for sending messages
        HBox inputPanel = createInputPanel();
        root.setBottom(inputPanel);

        // Set the scene and show the stage
        primaryStage.setScene(new Scene(root, 490, 700));
        primaryStage.show();

        // Connect to the chat server
        try {
            socket = new Socket("localhost", 6001);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // Start a thread to receive messages from the user Two
            new Thread(this::receiveMessages).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the top panel with user information.
     * @param currentUser the current user of the chat
     * @return the top panel as a VBox
     */
    private VBox createTopPanel(User currentUser) {
        // Top panel container
        VBox topPanel = new VBox(5);
        topPanel.setStyle("-fx-background-color: #018749;");
        topPanel.setPadding(new Insets(10));
        topPanel.setMinHeight(70);

        // Profile box for user profile and status
        HBox profileBox = new HBox(10);
        profileBox.setAlignment(Pos.CENTER_LEFT);

        // Load and display profile photo
        ImageView profilePhoto = new ImageView(new Image(currentUser.getProfilePhotoPath()));
        profilePhoto.setFitHeight(50);
        profilePhoto.setFitWidth(50);

        // User name label
        Label nameLabel = new Label(currentUser.getUsername());
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setFont(new Font("SAN_SERIF", 18));

        // User status label
        Label statusLabel = new Label("Active Now");
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(new Font("SAN_SERIF", 12));

        // Add components to profile box
        profileBox.getChildren().addAll(profilePhoto, nameLabel, statusLabel);

        // Add profile box to top panel
        topPanel.getChildren().add(profileBox);

        return topPanel;
    }

    /**
     * Creates the input panel for sending messages.
     * @return the input panel as an HBox
     */
    private HBox createInputPanel() {
        // Input panel container
        HBox inputPanel = new HBox(10);
        inputPanel.setStyle("-fx-background-color: White;");
        inputPanel.setPadding(new Insets(10, 20, 20, 20));

        // Text area for message input
        text = new TextArea();
        text.setMinSize(310, Region.USE_PREF_SIZE);
        text.setPrefRowCount(1);
        text.setWrapText(true);
        text.setStyle("-fx-background-color: Transparent; -fx-background-radius: 20px; -fx-padding: 5px; -fx-border-radius: 20px; -fx-border-color: #018749; -fx-border-width: 2px;");
        text.setFont(new Font("SAN_SERIF", 16));

        // Listener for adjusting the height of the text area
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            long numLines = newValue.lines().count();
            text.setPrefRowCount(Math.toIntExact(Math.min(numLines + 1, 2)));
        });

        // Send button
        Button sendButton = new Button("Send");
        sendButton.setMinSize(123, 40);
        sendButton.setStyle("-fx-background-color: #018749; -fx-text-fill: white; -fx-background-radius: 20px;");
        sendButton.setFont(new Font("SAN_SERIF", 16));
        sendButton.setOnAction(event -> sendMessage(text.getText()));

        // Add components to input panel
        inputPanel.getChildren().addAll(text, sendButton);

        return inputPanel;
    }

    /**
     * Sends a message to the server and adds it to the chat.
     * @param message the message to send
     */
    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            try {
                dos.writeUTF(message);
                addMessageToChat("You", message);
                text.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Receives messages from the server and updates the chat.
     */
    private void receiveMessages() {
        try {
            while (true) {
                String receivedMessage = dis.readUTF();
                Platform.runLater(() -> {
                    addMessageToChat("Two", receivedMessage);
                    scrollChatToBottom();
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrolls the chat to the bottom.
     */
    private void scrollChatToBottom() {
        chatBox.heightProperty().addListener(observable -> chatScrollPane.setVvalue(1.0));
    }

    /**
     * Adds a message to the chat box.
     * @param sender the sender of the message
     * @param message the message content
     */
    private void addMessageToChat(String sender, String message) {
        // Message label
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        // Sender label
        Label senderLabel = new Label(sender);

        // Time label
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Label timeLabel = new Label(sdf.format(cal.getTime()));

        // Message box container
        VBox messageBox = new VBox(5, senderLabel, messageLabel, timeLabel);
        messageBox.setAlignment(sender.equals("You") ? Pos.TOP_LEFT : Pos.TOP_RIGHT);
        messageLabel.setStyle(sender.equals("You") ? "-fx-background-color: #3944BC; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 20px; -fx-background-radius: 20px;" : "-fx-background-color: #018749; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        // Add message box to chat box
        chatBox.getChildren().add(messageBox);
    }
}