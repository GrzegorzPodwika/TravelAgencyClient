package main.java.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import main.java.Main;
import main.java.backend.model.User;

import java.io.IOException;

public class SceneCreator {

    public static void launchScene (String sceneName, User activeUser) throws IOException {
        String properLocation = "../resources/fxml-files/" + sceneName;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(properLocation));
        Main.setRoot(loader.load());
        Scene scene = new Scene(Main.getRoot());
        Main.getStage().setScene(scene);
        Main.getStage().show();
        Main.setUser(activeUser);
    }

}
