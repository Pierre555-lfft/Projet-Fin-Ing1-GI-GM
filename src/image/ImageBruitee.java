package image;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.Random;

public class ImageBruitee {
    private Image imageBruitee;

    public ImageBruitee(Image image, double ecartType) {
        this.imageBruitee = ajouterBruit(image, ecartType);
    }

    private Image ajouterBruit(Image original, double ecartType) {
        int width = (int) original.getWidth();
        int height = (int) original.getHeight();

        WritableImage result = new WritableImage(width, height);
        PixelReader reader = original.getPixelReader();
        PixelWriter writer = result.getPixelWriter();

        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);

                double r = clamp(color.getRed() + rand.nextGaussian() * ecartType / 255.0);
                double g = clamp(color.getGreen() + rand.nextGaussian() * ecartType / 255.0);
                double b = clamp(color.getBlue() + rand.nextGaussian() * ecartType / 255.0);

                writer.setColor(x, y, new Color(r, g, b, color.getOpacity()));
            }
        }

        return result;
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    public Image getImage() {
        return imageBruitee;
    }
}
