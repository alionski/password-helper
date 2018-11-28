import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ResultController {
    private App app;
    private String password;
    @FXML
    public TextField fieldPassword, fieldLimit;
    @FXML
    public Button buttonCut;

    public void setApp(App app) {
        this.app = app;
    }

    void setPassword(String pwd) {
        password = pwd;
        fieldPassword.setText(password);
    }

    @FXML
    private void showMain() {
        app.showMainWindow();
    }

    @FXML
    private void cutPassword() {
        try {
            int limit = Integer.parseInt(fieldLimit.getText());
            if (limit > 0 && limit < password.length()) {
                String pwd = password.substring(0, limit);
                fieldPassword.setText(pwd);
            }
        } catch (NumberFormatException exc) {
            System.out.println("User inputted non-numbers");
            // TODO: show an error msg
        } finally {
            fieldLimit.setText("");
        }
    }
}
