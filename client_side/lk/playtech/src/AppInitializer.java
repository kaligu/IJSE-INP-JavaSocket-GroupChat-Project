/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 5:07 PM
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setScene(new Scene(
                    FXMLLoader.load(
                            getClass().getResource("forms/ChatForm.fxml")
                    )
            ));
            primaryStage.initStyle(StageStyle.UNDECORATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.show();
    }
}
