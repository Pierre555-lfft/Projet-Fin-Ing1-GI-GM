package image;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import java.util.ArrayList;

public class ImageDebruitee {
	private Image imageDebruitee;

	public List<Patch> extractPatchs(Image image, int patchSize) {
	    List<Patch> patches = new ArrayList<>();
	    PixelReader pixelReader = image.getPixelReader();
	    int largeur = (int) image.getWidth();
	    int hauteur = (int) image.getHeight();

	    // on parcours l'image
	    for (int y = 0; y <= hauteur - patchSize; y++) {
	        for (int x = 0; x <= largeur - patchSize; x++) {
	            // Crée un tableau Float[][] pour le patch
	            Float[][] patchArray = new Float[patchSize][patchSize];

	            // on parcours chaque Patch
	            for (int dy = 0; dy < patchSize; dy++) {
	                for (int dx = 0; dx < patchSize; dx++) {
	                    int argb = pixelReader.getArgb(x + dx, y + dy);
	                    int r = (argb >> 16) & 0xFF;
	                    int g = (argb >> 8) & 0xFF;
	                    int b = argb & 0xFF;
	                    float luminance = 0.299f * r + 0.587f * g + 0.114f * b;
	                    patchArray[dy][dx] = luminance; // Stocke en Float
	                }
	            }
	            patches.add(new Patch(patchArray, x, y)); // Utilise le constructeur Patch(Float[][], Integer, Integer)
	        }
	    }
	    return patches;
	}

	public Image imageDen(Image imageBruitee) {
		Integer taillePatch = 10;
		Integer ligne = 100;
		Integer colonne = 100;
		List<Patch> patchs = ExtractPacths(imageBruitee, taillePatch);
		
		
		
		imageDebruitee = ReconstructPatchs()
				
		return imageDebruitee
		
	}

}
