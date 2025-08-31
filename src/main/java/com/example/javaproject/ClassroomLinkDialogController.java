package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.util.Optional;

public class ClassroomLinkDialogController {

    @FXML private TextField urlField;
    @FXML private Label errorLabel;

    private String resultUrl = null;

    /** Optional prefill (when editing an existing link). */
    public void setInitialUrl(String url) {
        if (url != null) {
            urlField.setText(url.trim());
        }
    }

    /** Returns the saved URL if user pressed Save and validation passed, else empty. */
    public Optional<String> getResult() {
        return Optional.ofNullable(resultUrl);
    }

    @FXML
    private void onSave() {
        String raw = urlField.getText() == null ? "" : urlField.getText().trim();

        // Basic validation: must start with http(s), be a valid URI, and contain no spaces
        if (raw.isEmpty()) {
            showError("Link cannot be empty.");
            return;
        }
        if (!(raw.startsWith("http://") || raw.startsWith("https://"))) {
            showError("Link must start with http:// or https://");
            return;
        }
        if (raw.contains(" ")) {
            showError("Link cannot contain spaces.");
            return;
        }
        try {
            // URI syntax check
            new URI(raw);
        } catch (Exception e) {
            showError("Invalid URL format.");
            return;
        }

        // Passed validation
        resultUrl = raw;
        close();
    }

    @FXML
    private void onCancel() {
        resultUrl = null;
        close();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    private void close() {
        Stage stage = (Stage) urlField.getScene().getWindow();
        stage.close();
    }
}
