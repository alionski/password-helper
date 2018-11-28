import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private PasswordField fieldMasterPassword, fieldAnswer;
    @FXML
    private TextField fieldURL;
    @FXML
    private Label labelQuestion;
    @FXML
    private Button buttonGeneratePwd;

    void setApp(App app) {
        this.app = app;
    }

    @FXML
    public void init() {
        fieldMasterPassword.setText("");
        fieldURL.setText("");
        labelQuestion.setText("...");
        fieldAnswer.setText("");
        fieldAnswer.setDisable(true);
        buttonGeneratePwd.setDisable(true);
    }

    @FXML
    private void focusURL() {
        fieldURL.requestFocus();
    }

    @FXML
    private void generateSeed() {
        String master = fieldMasterPassword.getText();
        String url = fieldURL.getText();
        int seed = getMD5Seed(master + url);
        String question = Questions.questions[seed];
        labelQuestion.setText(question);
        buttonGeneratePwd.setDisable(false);
        fieldAnswer.setDisable(false);
        fieldAnswer.setText("");
        fieldAnswer.requestFocus();
    }

    private int getMD5Seed(String toHash) {
        int sum = 0;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashed = new byte[0];
        if (digest != null) {
            hashed = digest.digest(
                    toHash.getBytes(StandardCharsets.UTF_8));
        }

        for (byte b : hashed) {
            sum += (int) b;
        }

        System.out.println("Sum: " + sum);
        System.out.println("Seed: " + Math.abs(sum) % 10);
        return Math.abs(sum) % 10;
    }

    /**
     * Called when the user clicks "generate passworrd" button, see main.fxml
     */
    @FXML
    private void generatePassword() {
        String master = fieldMasterPassword.getText();
        String url = fieldURL.getText();
        String answer = fieldAnswer.getText();
        String pwd = hash512Strings(master + url + answer);
        pwd = postprocessHash(pwd);
        app.showResult(pwd);
    }

    private String postprocessHash(String pwd) {
        // TODO postprocess the hash to show all types of chars
        return pwd;
    }

    private String hash512Strings(String toHash) {
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
