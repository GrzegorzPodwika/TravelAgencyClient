package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import controllers.Main;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SceneCreator {

    public static void launchScene (String sceneName) {
        String properLocation = "fxml-files/" + sceneName;
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource(properLocation));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (pane != null) {
            Main.setRoot(pane);
            Scene scene = new Scene(pane);
            Main.getStage().setScene(scene);
            Main.getStage().show();
        }
    }

}
