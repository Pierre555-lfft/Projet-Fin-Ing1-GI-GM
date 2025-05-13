package image;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import java.util.ArrayList;
import java.util.Vector;

public class ImageDebruitee {
	private Image imageDebruitee;

	// Adrien
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

	            // on parcours chaque Patchsrc/image/ImageDebruitee.java
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
	
	//Adrien
	public List<Vector<Float>> vectorPatchs (ArrayList<Patch> patchs){
		List<Vector<Float>> vecteurs= new ArrayList<>();
		
		for (Patch patch: patchs) {
			Vector<Float> vecteur = new Vector<>();
			vecteur = patch.toVector();
			vecteurs.add(vecteur);
		}
		
		return vecteurs;
	}

	public Image imageDen(Image imageBruitee) {
		Integer taillePatch = 10;
		Integer ligne = 100;
		Integer colonne = 100;
		Integer nbLignePatch = 10;
		Integer nbColonnePatch = 10;
		
		List<Patch> patchs = extractPacths(imageBruitee, taillePatch);
		
		List<int[]> patchPosition = getPositions(patchs);
		
		vecteurs = vectorPatchs(patchs);
				
		// transformation des vecteurs
		//...
		
		
		Vector<Float> vecteursDebruitee;
		
		patchsDebruitee = patchsVector(vecteursDebruitee, nbLignePatch, nbColonnePatch, patchPosition);
				
		imageDebruitee = reconstructPatchs(patchsDebruitee);
				
		return imageDebruitee;
		
	}
	
	// Etienne Angé
	public List<int[]> getPositions(List<Patch> patchs){
		List<int[]> patchPosition = new ArrayList<>();
		
		for(Patch patch : patchs) {
			int[] pos = new int[2];
			pos[0] = patch.getX();
			pos[1] = patch.getY();
			patchPosition.add(pos);
		}
		
		return patchPosition;
	}
	
	// Etienne Angé
	public List<Patch> patchsVector(List<Vector<Float>> vecteurs,Integer nbLigne, Integer nbColonne, List<int[]> patchPosition){
		
		
		List<Patch> patchs = new ArrayList<>();
		int i = 0;
		for(Vector<Float> vecteur : vecteurs) { // pour chaque vecteur
			
			patchs.add(new Patch(vecteur, nbColonne, nbLigne, patchPosition.get(i)[0], patchPosition.get(i)[1]) );
			
		}
		
		return patchs;
	}

}
