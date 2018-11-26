import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class representing the main window.
 */
public class MainController {
    private App app;
    @FXML
    private PasswordField fieldMasterPassword, fieldExtraString;
    @FXML
    private TextField fieldURL;

    void setApp(App app) {
        this.app = app;
    }

    /**
     * Called when the user clicks "generate passworrd" button, see main.fxml
     */
    @FXML
    private void generatePassword() {
        String master = fieldMasterPassword.getText();
        String url = fieldURL.getText();
        String extra = fieldExtraString.getText();
        String pwd = hashStrings(master + url + extra);
        // System.out.println(pwd);
        app.showResult(pwd);
    }

    private String hashStrings(String toHash) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashed = new byte[0];
        if (digest != null) {
            hashed = digest.digest(
					toHash.getBytes(StandardCharsets.UTF_8));
        }
        StringBuilder hexString = new StringBuilder();
        for (byte hashedByte : hashed) {
            String hex = Integer.toHexString(0xff & hashedByte);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
