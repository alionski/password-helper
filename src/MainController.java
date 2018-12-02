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
    private Label labelQuestion, labelEmptyField;
    @FXML
    private Button buttonGeneratePwd;
    private String master;
    private String url;
    private int seed;
    private String question;
    private String answer;

    void setApp(App app) {
        this.app = app;
    }

    @FXML
    public void init() {
        hideEmptyFieldMessage();
        fieldMasterPassword.setText("");
        master = "";
        fieldURL.setText("");
        url = "";
        labelQuestion.setText("...");
        question = "";
        fieldAnswer.setText("");
        answer = "";
        fieldAnswer.setDisable(true);
        buttonGeneratePwd.setDisable(true);
        labelEmptyField.setText("");
    }

    void restoreState(State state) {
        master = state.getMaster();
        url = state.getUrl();
        answer = state.getAnswer();
        seed = state.getSeed();
        question = Questions.questions[seed];

        fieldMasterPassword.setText(master);
        fieldURL.setText(url);
        fieldAnswer.setText(answer);
        labelQuestion.setText(question);

        fieldAnswer.setDisable(false);
        buttonGeneratePwd.setDisable(false);
    }

    @FXML
    private void focusURL() {
        fieldURL.requestFocus();
    }

    @FXML
    private void generateSeed() {
        master = fieldMasterPassword.getText();
        url = fieldURL.getText();

        if (master == null || master.equals("") ||
                url == null || url.equals("")) {
            showEmptyFieldMessage();
            return;
        }

        hideEmptyFieldMessage();

        seed = getMD5Seed(master + url);
        question = Questions.questions[seed];
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

        return Math.abs(sum) % 10;
    }

    /**
     * Called when the user clicks "generate password" button, see main.fxml
     */
    @FXML
    private void generatePassword() {
        master = fieldMasterPassword.getText();
        url = fieldURL.getText();
        answer = fieldAnswer.getText();

        if (master == null || master.equals("") ||
                url == null || url.equals("") ||
                answer == null || answer.equals("")) {
            showEmptyFieldMessage();
            return;
        }

        String pwd = hash512Strings(master + url + stripAnswer(answer));
        pwd = postprocessHash(pwd);
        app.saveState(new State(master, url, seed, answer));
        app.showResult(pwd);
    }

    private void showEmptyFieldMessage() {
        labelEmptyField.setText("One of the fields is empty");
    }

    private void hideEmptyFieldMessage() {
        labelEmptyField.setText("");
    }

    private String stripAnswer(String answer) {
        String stripped = answer.replaceAll("[^a-zA-Z0-9_-]", "");
        stripped = stripped.toLowerCase();
        return stripped;
    }

    /**
     * convert hex string into new string containing uppercase/lowercase letters, numbers, and special characters
     */
    private String postprocessHash(String pwd) {
        int[] tmp = stringToIntArray(pwd);
        return intArrayToPwdString(tmp);
    }

    private int[] stringToIntArray(String pwd){
        int[] tmp = new int[pwd.length()/2];
        for(int i=0; i<tmp.length; i++)
            tmp[i] = hexCharToInt(pwd.charAt(i*2))*16 + hexCharToInt(pwd.charAt(i*2+1));
        return tmp;
    }

    private int hexCharToInt(char hexChar){
        int decimalValue;
        if(hexChar>='A' && hexChar<='F')
            return hexChar-'A'+10;
        else if(hexChar>='a' && hexChar<='f')
            return hexChar-'a'+10;
        else if(hexChar>='0' && hexChar<='9')
            return hexChar-'0';
        else return 0;/**else error!!*/
    }

    private String intArrayToPwdString(int[] tmp){
        char[] newPwd = new char[tmp.length];
        for(int i=0; i<tmp.length; i++){
            switch(i%4){
                case 0: newPwd[i] = (char)(tmp[i]%26 + 65);
                    break;
                case 1: newPwd[i] = (char)(tmp[i]%26 + 97);
                    break;
                case 2: newPwd[i] = (char)(tmp[i]%10 + 48);
                    break;
                case 3: newPwd[i] = (char)(tmp[i]%15 + 33);
                    break;
            }
        }
        return new String(newPwd);
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
