import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/rumah_sakit";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private TextField usnField;

    @FXML
    private PasswordField pwField;

    @FXML
    private Button loginButton;

    private Connection databaseConnection;

    public void initialize() {
        try {
            this.databaseConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
        }
    }

    @FXML
    private void login() {
        String username = usnField.getText().trim();
        String password = pwField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username and Password cannot be empty.");
            return;
        }

        int userType = getUserType(username, password);
        if (userType == -1) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        } else {
            redirectToPage(userType);
        }
    }

    private int getUserType(String username, String password) {
        String query = "SELECT type FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pst = databaseConnection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("type");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while checking credentials.");
        }
        return -1;
    }

    private void redirectToPage(int userType) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader;
            if (userType == 1) {
                loader = new FXMLLoader(getClass().getResource("DokterPage.fxml"));
            } 
            else if (userType == 2) {
                loader = new FXMLLoader(getClass().getResource("PatientPage.fxml"));
            } else if (userType == 3) {
                loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
            } 
            else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Unknown user type.");
                return;
            }
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the page for user type: " + userType);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
