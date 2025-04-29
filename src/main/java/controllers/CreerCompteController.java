package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreerCompteController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Étudiant", "Professeur");
    }

    /**
     * Gère l'action de soumission pour créer un compte.
     */
    @FXML
    private void handleSubmit() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleComboBox.getValue();

        if (email.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        String sql = "INSERT INTO utilisateur (email, mot_de_passe, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);

            stmt.executeUpdate();
            showAlert("Succès", "Le compte a été créé avec succès.");
            closeStage();

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Impossible de créer le compte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Ferme la fenêtre modale.
     */
    private void closeStage() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}