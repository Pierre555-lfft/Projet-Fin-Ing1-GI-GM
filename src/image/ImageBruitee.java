package image2;

import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageBruitee {
    private BufferedImage imageBruitee;

    public ImageBruitee(BufferedImage image, double ecartType) {
        this.imageBruitee = ajouterBruit(image, ecartType);
    }

    private BufferedImage ajouterBruit(BufferedImage original, double ecartType) {
        BufferedImage resultat = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        Random rand = new Random();

        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                int rgb = original.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r = clamp(r + (int) (rand.nextGaussian() * ecartType));
                g = clamp(g + (int) (rand.nextGaussian() * ecartType));
                b = clamp(b + (int) (rand.nextGaussian() * ecartType));
                
                int newRGB = (r << 16) | (g << 8) | b;
                resultat.setRGB(x, y, newRGB);
            }
        }

        return resultat;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public BufferedImage getImage() {
        return imageBruitee;
    }
}
