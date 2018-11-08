import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Password Helper");
        this.primaryStage = primaryStage;

        showMainWindow();
    }

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("view/main.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();

        MainController controller = loader.getController();
        controller.setApp(this);
    }

    public void showResult(String password) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("view/result.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();

        ResultController controller = loader.getController();
        controller.setPassword(password);
    }

    /**
     * Is not needed for JavaFX but is kept to make it possible to rung it with CLI or if the app
     * is embedded/is used together with Swing modules.
     * @param args -- CLI args.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
