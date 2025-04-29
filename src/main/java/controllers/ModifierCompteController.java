package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModifierCompteController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private int createurId; // ID de l'utilisateur connecté

    /**
     * Définit l'ID de l'utilisateur connecté.
     */
    public void setCreateurId(int id) {
        this.createurId = id;
    }

    /**
     * Gère l'action de soumission pour enregistrer les modifications.
     */
    @FXML
    private void handleSubmit() {
        String nouvelEmail = emailField.getText().trim();
        String nouveauMotDePasse = passwordField.getText().trim();

        if (nouvelEmail.isEmpty() || nouveauMotDePasse.isEmpty()) {
            showAlert("Erreur", "L'email et le mot de passe ne peuvent pas être vides.");
            return;
        }

        String sql = "UPDATE utilisateur SET email = ?, mot_de_passe = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouvelEmail);
            stmt.setString(2, nouveauMotDePasse); // Pas besoin de hachage ici
            stmt.setInt(3, createurId);

            stmt.executeUpdate();
            closeStage(); // Fermer la fenêtre après modification

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Impossible de mettre à jour les informations du compte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère l'action de retour à la page précédente.
     */
    @FXML
    private void handleBack() {
        closeStage();
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