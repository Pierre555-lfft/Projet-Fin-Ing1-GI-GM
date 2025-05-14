package image2;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Charger la photo
            Photo photo = new Photo("resources/gibbon.jpg");

            // 2. Bruiter l'image
            BufferedImage bufferedBruitee = photo.bruiter(50.0);

            // 3. Convertir vers Image JavaFX
            Image imageOriginale = SwingFXUtils.toFXImage(photo.getImage(), null);
            Image imageBruitee = SwingFXUtils.toFXImage(bufferedBruitee, null);

            // 4. Affichage
            ImageView viewOriginale = new ImageView(imageOriginale);
            ImageView viewBruitee = new ImageView(imageBruitee);

            viewOriginale.setFitWidth(300);
            viewOriginale.setPreserveRatio(true);
            viewBruitee.setFitWidth(300);
            viewBruitee.setPreserveRatio(true);

            HBox root = new HBox(10, viewOriginale, viewBruitee);
            Scene scene = new Scene(root);

            primaryStage.setTitle("Image Bruitée vs Originale");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
