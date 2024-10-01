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
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * JavaFX chat application for User Two.
 */
public class Usertwo extends Application {

    private VBox chatBox;
    private TextArea text;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ScrollPane chatScrollPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chatter");

        BorderPane root = new BorderPane();

        User currentUser = new User("Two");
        currentUser.setProfilePhotoPath(getClass().getResource("/icons/2.png").toString());

        // Top Panel for showcasing the user profile photo and status
        VBox topPanel = createTopPanel(currentUser);
        root.setTop(topPanel);

        // Chat Box
        chatBox = new VBox(15);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: Transparent;");
        chatScrollPane = new ScrollPane(chatBox);
        chatScrollPane.setFitToWidth(true);
        root.setCenter(chatScrollPane);

        HBox inputPanel = createInputPanel();
        root.setBottom(inputPanel);

        primaryStage.setScene(new Scene(root, 490, 700));
        primaryStage.show();

        // Start server
        try {
            ServerSocket serverSocket = new ServerSocket(6001);
            clientSocket = serverSocket.accept();
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());

            // Start a thread to receive messages from user One
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
        VBox topPanel = new VBox(5);
        topPanel.setStyle("-fx-background-color: #018749;");
        topPanel.setPadding(new Insets(10));
        topPanel.setMinHeight(70);

        HBox profileBox = new HBox(10);
        profileBox.setAlignment(Pos.CENTER_LEFT);

        // Load and display profile photo
        ImageView profilePhoto = new ImageView(new Image(currentUser.getProfilePhotoPath()));
        profilePhoto.setFitHeight(50);
        profilePhoto.setFitWidth(50);

        Label nameLabel = new Label(currentUser.getUsername());
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setFont(new Font("SAN_SERIF", 18));
        profileBox.getChildren().addAll(profilePhoto, nameLabel);

        Label statusLabel = new Label("Active Now");
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(new Font("SAN_SERIF", 12));
        profileBox.getChildren().add(statusLabel);

        topPanel.getChildren().add(profileBox);
        return topPanel;
    }

    /**
     * Creates the input panel for sending messages.
     * @return the input panel as an HBox
     */
    private HBox createInputPanel() {
        HBox inputPanel = new HBox(10);
        inputPanel.setStyle("-fx-background-color: White;");
        inputPanel.setPadding(new Insets(10, 20, 20, 20));

        text = new TextArea();
        text.setMinSize(310, Region.USE_PREF_SIZE);
        text.setPrefRowCount(1);
        text.setWrapText(true);
        text.setStyle("-fx-background-color: Transparent; -fx-background-radius: 20px; -fx-padding: 5px; -fx-border-radius: 20px; -fx-border-color: #018749; -fx-border-width: 2px;");
        text.setFont(new Font("SAN_SERIF", 16));

        // Set the listener to adjust the height
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            long numLines = newValue.lines().count();
            text.setPrefRowCount(Math.toIntExact(Math.min(numLines + 1, 2)));
        });

        //send Button
        Button sendButton = new Button("Send");
        sendButton.setMinSize(123, 40);
        sendButton.setStyle("-fx-background-color: #018749; -fx-text-fill: white; -fx-background-radius: 20px;");
        sendButton.setFont(new Font("SAN_SERIF", 16));
        // Set the action on pressing the send button
        sendButton.setOnAction(event -> sendMessage(text.getText()));

        // Add text area and send button to the input panel
        inputPanel.getChildren().addAll(text, sendButton);

        return inputPanel;
    }

    /**
     * Sends a message to the server and adds it to the chat box.
     * @param message the message to be sent
     */
    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            try {
                dos.writeUTF(message); // Send message to user One
                addMessageToChat("You", message); // Display message in the chat box
                text.clear(); // Clear the text area after sending the message
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions
            }
        }
    }

    /**
     * Receives messages from the server and updates the chat box.
     */
    private void receiveMessages() {
        try {
            while (true) {
                String receivedMessage = dis.readUTF(); // Read new message from user One
                Platform.runLater(() -> {
                    addMessageToChat("One", receivedMessage); // Add the received message to the chat box
                    scrollChatToBottom(); // Scroll to the bottom of the chat box
                });
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    /**
     * Scrolls the chat box to the bottom.
     */
    private void scrollChatToBottom() {
        chatBox.heightProperty().addListener(observable -> chatScrollPane.setVvalue(1.0)); // Listen for changes in the chat box height and adjust the scroll pane
    }

    /**
     * Adds a message to the chat box.
     * @param sender the sender of the message
     * @param message the content of the message
     */
    private void addMessageToChat(String sender, String message) {
        Label messageLabel = new Label(message); // Create a label for the message content
        messageLabel.setWrapText(true); // Enable text wrapping within the label

        Label senderLabel = new Label(sender); // Create a label for the sender's name

        // Create a label for the timestamp using the current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Label timeLabel = new Label(sdf.format(cal.getTime()));

        // Create a container for the message components
        VBox messageBox = new VBox(5, senderLabel, messageLabel, timeLabel);
        messageBox.setAlignment(sender.equals("You") ? Pos.TOP_RIGHT : Pos.TOP_LEFT); // Align the message box based on the sender
        // Style the message label based on the sender
        messageLabel.setStyle(sender.equals("You") ? "-fx-background-color: #018749; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 20px; -fx-background-radius: 20px;" : "-fx-background-color: #3944BC; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        // Add the message box to the chat box
        chatBox.getChildren().add(messageBox);
    }
}