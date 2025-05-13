package image;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javafx.scene.image.Image;

public class ImageDebruitee {
	private Image imageDebruitee;















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
