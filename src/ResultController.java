import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ResultController {
    @FXML
    public TextField fieldPassword;

    void setPassword(String pwd) {
        fieldPassword.setText(pwd);
    }
}
