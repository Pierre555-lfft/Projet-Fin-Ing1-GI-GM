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
		
		List<Patch> patchs = extractPatchs(imageBruitee, taillePatch);
		
		ArrayList<Patch> arrayListPatches = new ArrayList<>(patchs);
		
		List<int[]> patchPosition = getPositions(patchs);
		
		List<Vector<Float>> vecteurs = vectorPatchs(arrayListPatches);
				
		// transformation des vecteurs
		//...
		
		
		Vector<Float> vecteursDebruitee;
		
		List<Patch> patchsDebruitee = patchsVector(vecteursDebruitee, nbLignePatch, nbColonnePatch, patchPosition);
				
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
	
	//Mathis B
	public static Vector<Float> mv_methode (List<Vector<Float>> V){ //ou V est la collection de patch vectorisée
		
		// Initialisation longueur de lacollection de patch et des patchs
		
		int n = V.size(); // Nombre de vecteur (patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (patch)
		
		// Initialisation des variables
		
		Vector<Float> mv = new Vector<Float>();
		float res;
		
		// Exploitation des données
		
		for (int i = 0; i< p; i++) { // On parcour l'interieur du vecteur
			res = 0;
			for(int j =0; j<n; j ++) { // On parcour les vecteurs
				
				res += V.get(j).get(i); // on fait la somme de chaque composante de tout les vecteurs
				
			
			
			}
			mv.add(res / n); // Puis on fait la moyenne pour avoir
		}
		return mv;
	}
	
	// Mathis B
	
	public static List<Vector<Float>> Cov_methode (List<Vector<Float>> V{// V collection de patch vectorisé
		
		// Initialisation longueur de lacollection de patch et des patchs
		
		int n = V.size(); // Nombre de vecteur (nbr de patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (composant du patch)
		
		// Initialisation des variables
		
		Vector<Float> mv = mv_methode(collection_patch);
		ArrayList<Vector<Float>> Cov = new ArrayList<Vector<Float>>();
		
		// Exploitation des données
		
		for(int i = 0; i<p;i++) { // création d'une list de vecteur avec n patch
			Vector<Float> ligne = new Vector<>();
	        for (int j = 0; j < p; j++) {
	            ligne.add(0); // init à zéro
	        }
	        Cov.add(ligne);
		}
		
		for (int i = 0; i< p; i++) {
			for (int j = i; j<p; j++) {
				
				float somme = 0; // pour le calcul de (Vk -mv)*(Vk-mv)'
				
				for (Vector<Float> v : V) {
					
					somme += (v.get(i)-mv.get(i))*(v.get(j)-mv.get(j));
					
				}
				
				float cov_ij = somme/(n-1); // Coefficiant de la ligne i et colonne j
				
				// Matrice symetrique
				
				Cov.get(i).set(j,cov_ij);
				
				if (i != j) {
		            Cov.get(j).set(i, cov_ij); // par symetrie
				}
				
			}
		
		return Cov ;
		
		}
	}
	
	// Mathis B
	
	public static List<Vector<Float>> vecteur_centre_methode (List<Vector<Float>> V){// idem
		
		// Initialisation longueur de lacollection de patch et des patchs
		
		int n = V.size(); // Nombre de vecteur (patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (patch)
		
		// Initialisation des variables
		
		Vector<Float> mv = mv_methode(collection_patch);
		ArrayList<Vector<Float>> Vc = new ArrayList<Vector<Float>>();
		
		// Exploitation des données
		
		for (int i = 0; i< n; i++) {
			Vector<Float> v_centre = new Vector<>();
	        for (int j = 0; j < p; j++) {
	            float val = V.get(i).get(j) - mv.get(j); // soustraction composante par composante
	            v_centre.add(val);
	        }
	        Vc.add(v_centre);
		
		}
		
		return Vc;
	}
	
	//Mathis B
	
	public static RealMatrix ACP (List<Vector<Float>> V) { //idem
		
		// Initialisation longueur de lacollection de patch et des patchs
		
		int n = V.size(); // Nombre de vecteur (patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (patch)
		
		// Initialisation des variables
		
		Vector<Float> mv = mv_methode(V); // Vecteur Moyen
		List<Vector<Float>> Vc = vecteur_centre_methode(V); // List des vecteurs centré
		List<Vector<Float>> Cov = Cov_methode(V);// List de vecteur de covariance
		float [][] matrice_cov = new float[p][p]; // matrice de conversion de Cov
		
		// Convertion de Cov en matrice 2x2
		// Pour utiliser des lois utiles pour trouver les valeurs propres
		
		for (int i=0; i<p; i++) {
			for(int j=0; j<p; j++) {
				matrice_cov[i][j] = Cov.get(i).get(j);
			}
		}
		
		// Valeur propres :
		
		RealMatrix covMatrice = new Array2DRowRealMatrix(matrice_cov); 
	    EigenDecomposition eig = new EigenDecomposition(covMatrice); //Eigen normalise les ui

	    double[] valeurs_propres = eig.getRealEigenvalues();     
	    RealMatrix vecteurs_propres = eig.getV();
	    
	    System.out.println("Vecteur moyen : " + mv);
	    System.out.println("Valeurs propres : " + Arrays.toString(valeurs_propres));

	    System.out.println("Vecteurs propres (axes principaux) :");
	    for (int i = 0; i < p; i++) {
	        System.out.println(Arrays.toString(vecteurs_propres.getColumn(i)));
	    }
	   return vecteurs_propres;
	}
	
	// List<Vector<Float>> Vc = vecteur_centre_methode(collection_patch);//Vc c'est le terme Vk - mv
	
	//MathisB
	
	public static List<Vector<Float>> Proj (RealMatrix U, List<Vector<Float>> Vc ){
		
		// Definition Longueur
		
		int p = U.getRowDimension(); // nombnre de vecteur
	    int n = Vc.size(); // nombre vecteur centré
	    
	    // Definition des variables 
	    
	    
	    ArrayList<Vector<Float>> V_contrib = new ArrayList<Vector<Float>>();
	    
	    for(int i = 0; i<p;i++) { // création d'une list de vecteur avec n patch
			Vector<Float> ligne = new Vector<>();
	        for (int j = 0; j < p; j++) {
	            ligne.add(0); // rempli de 0
	        }
	        V_contrib.add(ligne);
		}
	    
	    //Exploitation
	    
	    for(int k = 0; k<n; k++) {
	    	Vector<Float> Vk = Vc.get(k);
	    	
	    	for (int i =0; i<p; i++) {// Pour la somme de i a s²
	    		
	    
	    		float[] ui = U.getColumn(i);
	    		float proj =0;
	    		
	    		for(int j =0; j<p; j++) {//parcourir chaque ligne et effcetuer l'operation de multiplication de colonne uitranspoe*Vc
	    			proj += ui[j]*Vk.get(j);
	    		}
	    		V_contrib.get(i).add(proj); //Contrsuction de V_contrib coeff par coeff a_ki
	    	}
	    }
	    	
	    	

	  return V_contrib;  
	}
	//Pierre Laforest 
	public enum TypeSeuillage {
	    DUR,
	    DOUX
	}
	
	//Pierre Laforest
		//fonction qui permet de déterminer le seuille 
		public void seuilV(Float sigma, int taille) {
			int L = width * height; // Nombre total de pixels
	        return sigma * Math.sqrt(2 * Math.log(L));
		}
		
		//Pierre Laforest 
		//fonction qui permet de déterminer la variance 
		public static float calculerVarianceFloat(List<Vector<Float>> matriceImage) {
		    int totalElements = 0;
		    float somme = 0f;
		    float sommeCarres = 0f;

		    for (Vector<Float> ligne : matriceImage) {
		        for (float coeff : ligne) {
		            somme += coeff;
		            sommeCarres += coeff * coeff;
		            totalElements++;
		        }
		    }

		    if (totalElements == 0) return 0f;

		    float moyenne = somme / totalElements;
		    float variance = (sommeCarres / totalElements) - (moyenne * moyenne);

		    return variance;
		}
		
		//Pierre Laforest
		//focntion qui permet de déterminer dépendant de la variance s'il faut utiliser un seuillage doux ou dur 
		public static TypeSeuillage choisirType(float variance) {
	        float seuilVariance = 50.0f;  // heuristique, à ajuster selon les cas

	        if (variance < seuilVariance) {
	            return TypeSeuillage.DUR;
	        } else {
	            return TypeSeuillage.DOUX;
	        }
	    }
		
		//Pierre Laforest
		//fonction qui permet de faire un seuillage dur
		public static Map<Integer, Vector<Float>> seuillageDur(Map<Integer, Vector<Float>> data, float seuil) {
		    Map<Integer, Vector<Float>> resultat = new HashMap<>();

		    for (Map.Entry<Integer, Vector<Float>> entry : data.entrySet()) {
		        int key = entry.getKey();
		        Vector<Float> ligne = entry.getValue();
		        Vector<Float> nouvelleLigne = new Vector<>();

		        for (float coeff : ligne) {
		            if (Math.abs(coeff) < seuil) {
		                nouvelleLigne.add(0f);
		            } else {
		                nouvelleLigne.add(coeff);
		            }
		        }

		        resultat.put(key, nouvelleLigne);
		    }

		    return resultat;
		}

		//Pierre Laforest
		//fonction qui permet de faire un seuillage doux
		public static Map<Integer, Vector<Float>> seuillageDoux(Map<Integer, Vector<Float>> data, float seuil) {
		    Map<Integer, Vector<Float>> resultat = new HashMap<>();

		    for (Map.Entry<Integer, Vector<Float>> entry : data.entrySet()) {
		        int key = entry.getKey();
		        Vector<Float> ligne = entry.getValue();
		        Vector<Float> nouvelleLigne = new Vector<>();

		        for (float coeff : ligne) {
		            if (Math.abs(coeff) <= seuil) {
		                nouvelleLigne.add(0f);
		            } else {
		                float signe = Math.signum(coeff);
		                float nouveauCoeff = signe * (Math.abs(coeff) - seuil);
		                nouvelleLigne.add(nouveauCoeff);
		            }
		        }

		        resultat.put(key, nouvelleLigne);
		    }

		    return resultat;
		}

		
		

}
