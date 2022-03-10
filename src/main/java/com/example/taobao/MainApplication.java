package com.example.taobao;

import com.example.taobao.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 600);
        scene.getStylesheets().addAll(
                BootstrapFX.bootstrapFXStylesheet(),
                "org/kordamp/bootstrapfx/sampler/sampler.css",
                "org/kordamp/bootstrapfx/sampler/xml-highlighting.css");
        stage.getIcons().add(new Image("/image/favicon.ico"));
        stage.setResizable(false);
        stage.setTitle("自动下载");
        stage.setScene(scene);
        stage.show();
        System.out.println("open");
    }

    public static void main(String[] args) {
        launch();
        MainController.executor.shutdownNow();
        System.out.println("close");
    }


}
