package gui;

import encryption.CryptoUtils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Account;
import services.VaultService;

import javax.swing.text.LabelView;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public class PasswordManagerApp extends Application {
    private VaultService vault = new VaultService();
    private ListView<Account> accountList = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    private void showLoginScreen(Stage primaryStage) {  // this is the start of the login window
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");

        Label label = new Label("Enter master password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label errorLabel = new Label();

        loginButton.setOnAction(e -> {
            String enteredPassword = passwordField.getText();
            if ("admin".equals(enteredPassword)) {
                loginStage.close();
                showMainApp(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect password. Try again!");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(10, label, passwordField, loginButton, errorLabel);
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 300, 170);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        loginStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("icon.png")));
        loginStage.setScene(scene);
        loginStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {// this is the start of the application
        showLoginScreen(primaryStage);
    }

    private void showMainApp(Stage primaryStage) {
        accountList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TextField siteField = new TextField();
        siteField.setText("Site name");

        TextField userField = new TextField();
        userField.setText("User name");

        PasswordField passwordField = new PasswordField();
        passwordField.setText("Password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        ToggleButton showPassword = new ToggleButton("👁️");
        showPassword.setPrefWidth(50);
        showPassword.setPrefHeight(30);
        showPassword.setStyle("-fx-font-family: 'Apple Color Emoji'; -fx-font-size: 16;");

        showPassword.setOnAction(e -> {   // the logic for the showing/hide button for the password
            if (showPassword.isSelected()) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
            }
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (!showPassword.isSelected()) {
                visiblePasswordField.setText(newText);
            }
        });

        visiblePasswordField.textProperty().addListener((obs, oldText, newText) -> {
            if (showPassword.isSelected()) {
                passwordField.setText(newText);
            }
        });

        TextField encryptedPasswordField = new TextField();
        encryptedPasswordField.setEditable(false);
        encryptedPasswordField.setPromptText("Encrypted password");
        encryptedPasswordField.setPrefWidth(300);

        Button showEncryptedButton = new Button("Show encrypted password"); // the button for showing encrypted password
        showEncryptedButton.setOnAction(e -> {
            Account selected = accountList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String plainPassword = selected.getPassword();
                String encrypted = CryptoUtils.encrypt(plainPassword);
                encryptedPasswordField.setText(encrypted);
            } else {
                encryptedPasswordField.setText("");
            }
        });

        Button addButton = new Button("Add account");       // the button for adding an account
        addButton.setOnAction(actionEvent -> {
            String site = siteField.getText();
            String user = userField.getText();
            String password = passwordField.getText();

            Account account = new Account(site, user, password);
            vault.addAccount(account);
            accountList.getItems().add(account);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("Account added successfully!");
            alert.showAndWait();
        });

        Button deleteButton = new Button("Delete selected accounts");  // the button for deleting selected account
        deleteButton.setOnAction(e -> {
            ObservableList<Account> selectedItems = accountList.getSelectionModel().getSelectedItems();
            List<Account> toRemove = new ArrayList<>(selectedItems);
            for (Account selected : toRemove) {
                vault.deleteAccount(selected.getSiteName(), selected.getUsername(), selected.getPassword());
                accountList.getItems().remove(selected);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deleted");
            alert.setHeaderText(null);
            alert.setContentText("Account(s) deleted successfully!");
            alert.showAndWait();
        });

        GridPane grid = new GridPane();
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(70);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(70);

        grid.getColumnConstraints().addAll(col0, col1);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Site: "), 0, 0);
        grid.add(siteField, 1, 0);

        grid.add(new Label("User:"), 0, 1);
        grid.add(userField, 1, 1);

        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(visiblePasswordField, 1, 2);
        grid.add(showPassword, 2, 2);

        VBox encryptionBox = new VBox(15);
        encryptionBox.getChildren().addAll(encryptedPasswordField, showEncryptedButton);
        grid.add(encryptionBox, 1, 5);
        GridPane.setMargin(showEncryptedButton, new Insets(0, 0, 0, 0));

        grid.add(addButton, 1, 3);
        grid.add(deleteButton, 1, 4);

        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().addAll(addButton, deleteButton);
        grid.add(buttonBox, 1, 3);

        grid.add(accountList, 6, 0, 1, GridPane.REMAINING);
        accountList.setPrefWidth(400);
        accountList.setPrefHeight(400);

        Scene scene = new Scene(grid, 950, 500);   // the starting resolution of the app
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Password Manager");
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.show();

        accountList.getItems().addAll(vault.getAccounts());
    }
}
