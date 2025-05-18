package image;


import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Photo photo = new Photo("images/gibbon.jpg");
	

        Image imageBruitee = photo.bruiter(50.0);
        Image imageOriginale = photo.getImage();
	Image imageNoiretBlanc = photo.nb();
		
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
