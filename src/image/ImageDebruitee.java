package image;
import org.apache.commons.math3.linear.RealMatrix;


import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.math3.linear.*;
import java.util.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import java.util.ArrayList;
import java.util.Vector;



//Pierre Laforest : permet de définir un type de seuillage 
	enum TypeSeuillage {
	    DUR,
	    DOUX
	}
	
/**
 * Permet de gérer le débruitage d'une image
 */
public class ImageDebruitee {
	private Image imageDebruitee;

	public ImageDebruitee(Image imageBruitee, double taillePatch) {
		imageDebruitee = imageDen(imageBruitee, (int)taillePatch);
	}
	
	public Image getImage() {
		return imageDebruitee;
	}
	
	/** Renvoie un 
	 * @author Adrien
	 * @param 
	 * @return
	 */
	// Méthode pour obtenir la luminance d'un pixel à partir de sa valeur ARGB
	public static int obtenirValeurGris(int argb) {
	    int r = (argb >> 16) & 0xFF;
	    int g = (argb >> 8) & 0xFF;
	    int b = argb & 0xFF;
	    // Formule de luminance
	    return (int) (0.299 * r + 0.587 * g + 0.114 * b);
	}

	/** Renvoie un 
	 * @author Adrien
	 * @param 
	 * @return
	 */
	
	public List<Patch> extractPatchs(Image image, int taillePatch) {
	    List<Patch> listePatchs = new ArrayList<>();
	    PixelReader lecteurPixel = image.getPixelReader();
	    int largeur = (int) image.getWidth();
	    int hauteur = (int) image.getHeight();

	    // Calculer le nombre de patchs qui peuvent être extraits sans chevauchement
	    int nbPatchsX = largeur / taillePatch;
	    int nbPatchsY = hauteur / taillePatch;

	    // Extraire les patchs non superposés
	    for (int y = 0; y < nbPatchsY; y++) {
	        for (int x = 0; x < nbPatchsX; x++) {
	            int debutX = x * taillePatch;
	            int debutY = y * taillePatch;
	            Float[][] matricePatch = new Float[taillePatch][taillePatch];

	            for (int dy = 0; dy < taillePatch; dy++) {
	                for (int dx = 0; dx < taillePatch; dx++) {
	                    int argb = lecteurPixel.getArgb(debutX + dx, debutY + dy);
	                    // Utilisation de la méthode obtenirValeurGris pour la luminance
	                    float luminance = obtenirValeurGris(argb);
	                    matricePatch[dy][dx] = luminance;
	                }
	            }
	            listePatchs.add(new Patch(matricePatch, debutX, debutY));
	        }
	    }

	    // Gérer les bords droit et inférieur si nécessaire
	    boolean resteX = (largeur % taillePatch) != 0;
	    boolean resteY = (hauteur % taillePatch) != 0;

	    // Bord droit
	    if (resteX) {
	        for (int y = 0; y < nbPatchsY; y++) {
	            int debutX = largeur - taillePatch;
	            int debutY = y * taillePatch;
	            Float[][] matricePatch = new Float[taillePatch][taillePatch];

	            for (int dy = 0; dy < taillePatch; dy++) {
	                for (int dx = 0; dx < taillePatch; dx++) {
	                    int argb = lecteurPixel.getArgb(debutX + dx, debutY + dy);
	                    // Utilisation de la méthode obtenirValeurGris pour la luminance
	                    float luminance = obtenirValeurGris(argb);
	                    matricePatch[dy][dx] = luminance;
	                }
	            }
	            listePatchs.add(new Patch(matricePatch, debutX, debutY));
	        }
	    }

	    // Bord inférieur
	    if (resteY) {
	        for (int x = 0; x < nbPatchsX; x++) {
	            int debutX = x * taillePatch;
	            int debutY = hauteur - taillePatch;
	            Float[][] matricePatch = new Float[taillePatch][taillePatch];

	            for (int dy = 0; dy < taillePatch; dy++) {
	                for (int dx = 0; dx < taillePatch; dx++) {
	                    int argb = lecteurPixel.getArgb(debutX + dx, debutY + dy);
	                    // Utilisation de la méthode obtenirValeurGris pour la luminance
	                    float luminance = obtenirValeurGris(argb);
	                    matricePatch[dy][dx] = luminance;
	                }
	            }
	            listePatchs.add(new Patch(matricePatch, debutX, debutY));
	        }
	    }

	    // Coin inférieur droit (si nécessaire)
	    if (resteX && resteY) {
	        int debutX = largeur - taillePatch;
	        int debutY = hauteur - taillePatch;
	        Float[][] matricePatch = new Float[taillePatch][taillePatch];

	        for (int dy = 0; dy < taillePatch; dy++) {
	            for (int dx = 0; dx < taillePatch; dx++) {
	                int argb = lecteurPixel.getArgb(debutX + dx, debutY + dy);
	                // Utilisation de la méthode obtenirValeurGris pour la luminance
	                float luminance = obtenirValeurGris(argb);
	                matricePatch[dy][dx] = luminance;
	            }
	        }
	        listePatchs.add(new Patch(matricePatch, debutX, debutY));
	    }

	    return listePatchs;
	}
	
	/**
	 * 
	 * @param patchs
	 * @return
	 */
	public List<Vector<Float>> vectorPatchs (ArrayList<Patch> patchs){
		List<Vector<Float>> vecteurs= new ArrayList<>();
		
		for (Patch patch: patchs) {
			Vector<Float> vecteur = new Vector<>();
			vecteur = patch.toVector();
			vecteurs.add(vecteur);
		}
		
		return vecteurs;
	}
	
	/** Renvoie le vecteur moyen mv
	 * @author Mathis Bohain
	 * @param V
	 * @return
	 */
	
	public static Vector<Float> mv_methode (List<Vector<Float>> V){ //ou V est la collection de patch vectorisée
				
	   // Initialisation longueur de la collection de patch et des patchs
					
		int n = V.size(); // Nombre de vecteur (patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (patch)
					
		// Initialisation des variables
					
		Vector<Float> mv = new Vector<Float>();
		double res;
					
		// Exploitation des données
					
		for (int i = 0; i< p; i++) { // On parcour l'interieur du vecteur
			res = 0;
			for(int j =0; j<n; j ++) { // On parcour les vecteurs
							
				res += V.get(j).get(i); // on fait la somme de chaque composante de tout les vecteurs
							
						
						
			}
			mv.add((float)(res / n)); // Puis on fait la moyenne pour avoir
		}
		return mv;
	}
				
	/** Renvoie la matrice de covariance de V en format List de Vecteurs
	 * @author Mathis Bohain
	 * @param V
	 * @return
	 */
				
	public static List<Vector<Float>> cov_methode (List<Vector<Float>> V ){// V collection de patch vectorisé
					
		// Initialisation longueur de lacollection de patch et des patchs
					
		int n = V.size(); // Nombre de vecteur (nbr de patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (composant du patch)
					
		// Initialisation des variables
					
		Vector<Float> mv = mv_methode(V);
		ArrayList<Vector<Float>> Cov = new ArrayList<Vector<Float>>();
					
		// Exploitation des données
		for(int i = 0; i<p;i++) { // création d'une list de vecteur avec n patch
			Vector<Float> ligne = new Vector<>();
			for (int j = 0; j < p; j++) {
				ligne.add(0f); // init à zéro
			}
			Cov.add(ligne);
		}
					
		for (int i = 0; i< p; i++) {
			for (int j = i; j<p; j++) {
							
				double somme = 0; // pour le calcul de (Vk -mv)*(Vk-mv)'
							
				for (Vector<Float> v : V) {
								

					somme += (v.get(i)-mv.get(i))*(v.get(j)-mv.get(j));

		
								
				}
							
				double cov_ij = somme/n; // Coefficiant de la ligne i et colonne j
							
				// Matrice symetrique
							
				Cov.get(i).set(j,(float) cov_ij);
							
				if (i != j) {
					Cov.get(j).set(i, (float)cov_ij); // par symetrie
				}
							
			}
		}
		return Cov ;
	}
				
	/** Renvoie la liste des vecteurs centrées de V
	 * @author Mathis Bohain
	 * @param V
	 * @return
	 */
				
	public static List<Vector<Float>> vecteur_centre_methode (List<Vector<Float>> V){
					
		// Initialisation longueur de lacollection de patch et des patchs
					
		int n = V.size(); // Nombre de vecteur (patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (patch)
					
		// Initialisation des variables
					
		Vector<Float> mv = mv_methode(V);
		ArrayList<Vector<Float>> Vc = new ArrayList<Vector<Float>>();
					
		// Exploitation des données
					
		for (int i = 0; i< n; i++) {
			Vector<Float> v_centre = new Vector<>();
			for (int j = 0; j < p; j++) {
				double val = V.get(i).get(j) - mv.get(j); // soustraction composante par composante
				v_centre.add((float)val);
			}
			Vc.add(v_centre);
					
		}
					
		return Vc;
	}
				
	
	/** Renvoie la base orthogonal U (la liste des vecteurs propres de V)
	 * @author Mathis Bohain
	 * @param V
	 * @return
	 */
	public static RealMatrix ACP (List<Vector<Float>> V) { 
					
		// Initialisation longueur de lacollection de patch et des patchs
					
		int n = V.size(); // Nombre de vecteur (patch)
		int p = V.get(0).size(); // Nombre de composante du vecteur (patch)
					
		// Initialisation des variables
					
		Vector<Float> mv = mv_methode(V); // Vecteur Moyen
		List<Vector<Float>> Vc = vecteur_centre_methode(V); // List des vecteurs centré
		List<Vector<Float>> Cov = cov_methode(V);// List de vecteur de covariance
		double [][] matrice_cov = new double[p][p]; // matrice de conversion de Cov
					
		// Convertion de Cov en matrice 2x2
					
		for (int i=0; i<p; i++) {
			for(int j=0; j<p; j++) {
				matrice_cov[i][j] = Cov.get(i).get(j);
			}
		}
			
		// Obtention des valeurs propres 
		
		RealMatrix covMatrice = new Array2DRowRealMatrix(matrice_cov);
	    EigenDecomposition eig = new EigenDecomposition(covMatrice);
	    
	    double[][] U = new double[p][p];

	    
	    for (int i = 0; i < p; i++) {
	    
	        RealVector vp = eig.getEigenvector(i);
	        for (int j = 0; j < p; j++) {
	            U[j][i] = vp.getEntry(j);
	        }
	    }
	    return new Array2DRowRealMatrix(U);
	    
	}
				
	/** Renvoi la projection des vecteur centrés Vc sur dans la base U
	 * @author Mathis Bohain
	 * @param U
	 * @param Vc
	 * @return
	 */
				
	public static List<Vector<Float>> proj (RealMatrix U, List<Vector<Float>> Vc ){
					
        // dimension 
		
		int s2 = U.getColumnDimension();   // nombre de composantes principales
		int M = Vc.size();                 // nombre de patchs 

		// Initialiser s² vecteurs (un pour chaque composante principale)
		ArrayList<Vector<Float>> V_contrib = new ArrayList<>();
		for (int i = 0; i < M; i++) {
			V_contrib.add(new Vector<Float>());
		}

		// Pour chaque vecteur centré Vk
	    for (int k = 0; k < M; k++) {
	        Vector<Float> Vk = Vc.get(k);

	        // Projeter Vk sur chaque composante principale i (ui)
	        for (int i = 0; i < s2; i++) {
	            double projection = 0.0;

	            // Produit scalaire entre Vk et la i-ème composante principale ui
	            for (int j = 0; j < s2; j++) {
	                projection += Vk.get(j) * U.getEntry(j, i);  // pas de transpose !
	            }

	            V_contrib.get(k).add((float) projection);  // α_i^(k)
	        }
	    }

	    return V_contrib;
	}
	
	// Adrien
	// Reconstruction de l'image à partir des patchs débruités
	public Image reconstructPatchs(List<Patch> patchsDebruitee) {
	    if (patchsDebruitee == null || patchsDebruitee.isEmpty()) {
	        return null;
	    }

	    // 1. Déterminer la taille de l'image à reconstruire
	    int largeurMax = 0;
	    int hauteurMax = 0;
	    int taillePatch = patchsDebruitee.get(0).getTab().length; // On suppose que les patchs sont carrés

	    for (Patch patch : patchsDebruitee) {
	        int finX = patch.getX() + taillePatch;
	        int finY = patch.getY() + taillePatch;
	        if (finX > largeurMax) largeurMax = finX;
	        if (finY > hauteurMax) hauteurMax = finY;
	    }

	    // 2. Créer les tableaux pour stocker la somme des luminances et le nombre de contributions par pixel
	    float[][] sommeLuminance = new float[hauteurMax][largeurMax];
	    int[][] compteur = new int[hauteurMax][largeurMax];

	    // 3. Ajouter les valeurs des patchs
	    for (Patch patch : patchsDebruitee) {
	        Float[][] tab = patch.getTab();
	        int debutX = patch.getX();
	        int debutY = patch.getY();

	        for (int dy = 0; dy < tab.length; dy++) {
	            for (int dx = 0; dx < tab[0].length; dx++) {
	                int x = debutX + dx;
	                int y = debutY + dy;
	                sommeLuminance[y][x] += tab[dy][dx];
	                compteur[y][x]++;
	            }
	        }
	    }

	    // 4. Créer l’image avec lissage (moyenne des patchs)
	    WritableImage imageReconstruite = new WritableImage(largeurMax, hauteurMax);
	    PixelWriter ecrivain = imageReconstruite.getPixelWriter();

	    for (int y = 0; y < hauteurMax; y++) {
	        for (int x = 0; x < largeurMax; x++) {
	            if (compteur[y][x] > 0) {
	                float moyenne = sommeLuminance[y][x] / compteur[y][x];
	                int niveauGris = Math.min(255, Math.max(0, Math.round(moyenne)));
	                int argb = (0xFF << 24) | (niveauGris << 16) | (niveauGris << 8) | niveauGris;
	                ecrivain.setArgb(x, y, argb);
	            } else {
	                // Remplir les pixels non couverts avec noir (optionnel)
	                ecrivain.setArgb(x, y, 0xFF000000);
	            }
	        }
	    }

	    return imageReconstruite;
	}


	public Image imageDen(Image imageBruitee, Integer taillePatch) {
		
	    // 1. Extraction des patchs non superposés
	    List<Patch> patchs = extractPatchs(imageBruitee, taillePatch);
	    ArrayList<Patch> arrayListPatches = new ArrayList<>(patchs);

	    // 2. Passage des patchs en vecteurs Float
	    List<Vector<Float>> vecteurs = vectorPatchs(arrayListPatches);

	    // 3. Centrer les vecteurs (Vk - mv)
	    List<Vector<Float>> vecteursCentres = vecteur_centre_methode(vecteurs);

	    // 4. Calcul de la matrice U (vecteurs propres) par ACP sur vecteurs centrés
	    RealMatrix U = ACP(vecteursCentres);

	    // 5. Projection des vecteurs centrés sur les composantes principales
	    List<Vector<Float>> projections = proj(U, vecteursCentres);

	    // 6. Seuillage sur les projections (par exemple seuillage dur)
	                                                   // à modifier selon le type
	    double sigma = 20; // exemple, à calculer idéalement
	    double seuil = VisuShrink(sigma);   
	    
	    // Definir le seuil Bayes ou Visu???
	    
	    // Choix du type de seuillage
	    
	   // variance = ???
	    List<Vector<Float>> projectionsSeuillage;
	    		
	    if (choisirType(variance) == TypeSeuillage.DUR) {
	    	
	    	projectionsSeuillage =  seuillageDur(projections, seuil);
	    	
	    } else {
	    	projectionsSeuillage =  seuillageDoux(projections, seuil);
	    }
	   // List<Vector<Float>> projectionsSeuillage =  DOux ou dur??(projections, seuil, type);

	    // 7. Reconstruction des vecteurs centrés débruités
	    List<Vector<Float>> vecteursCentresDebruites = proj(U.transpose(), projectionsSeuillage);

	    // 8. Ajout du vecteur moyen
	    List<Vector<Float>> vecteursDebruites = new ArrayList<>();
	    Vector<Float> mv = mv_methode(vecteurs);
	    for (Vector<Float> v : vecteursCentresDebruites) {
	        Vector<Float> vDebruite = new Vector<>();
	        for (int i = 0; i < v.size(); i++) {
	            vDebruite.add(v.get(i) + mv.get(i));
	        }
	        vecteursDebruites.add(vDebruite);
	    }

	    // 9. Reconstruction des patchs à partir des vecteurs débruités
	    for (int i = 0; i < arrayListPatches.size(); i++) {
	        Patch p = arrayListPatches.get(i);
	        p.fromVector(vecteursDebruites.get(i));
	    }

	    // 10. Reconstruction de l'image à partir des patchs débruités
	    Image imageReconstruite = reconstructPatchs(arrayListPatches);

	    return imageReconstruite;
	}


	
	
	/** Renvoie un tableau de position (x,y) permetant de garder la postion des patch vectoriser
	 * @author Etienne Angé
	 * @param patchs
	 * @return
	 */
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
	
	public List<Patch> patchsVector(List<Vector<Float>> vecteurs, int taillePatch, List<int[]> patchPosition) {
	    List<Patch> patchs = new ArrayList<>();
	    for (int i = 0; i < vecteurs.size(); i++) {
	        Vector<Float> vecteur = vecteurs.get(i);
	        int[] pos = patchPosition.get(i);
	        patchs.add(new Patch(vecteur, taillePatch, taillePatch, pos[0], pos[1]));
	    }
	    return patchs;
	}

	/** Renvoie un 
	 * @author Pierre
	 * @param 
	 * @return
	 */
	
	public static double VisuShrink(double sigma) {
	    // VisuShrink threshold: sigma * sqrt(2*log(N))
	    // N = taille patch squared
	    return sigma * Math.sqrt(2 * Math.log(64)); // 64 = 8x8 patch (adapter si taille différente)
	}
	
	/** Renvoie un 
	 * @author Pierre
	 * @param 
	 * @return
	 */


	public static double BayesShrink(List<Vector<Float>> projections, double sigma) {
	    // Calcul de la variance du signal
	    int n = projections.size();
	    int p = projections.get(0).size();

	    double varianceSignal = 0.0;
	    for (Vector<Float> v : projections) {
	        for (int i = 0; i < p; i++) {
	            varianceSignal += v.get(i) * v.get(i);
	        }
	    }
	    varianceSignal /= (n * p);

	    double seuil = sigma * sigma / Math.sqrt(varianceSignal);
	    return seuil;
	}
	
	/** Renvoie un 
	 * @author Pierre
	 * @param 
	 * @return
	 */
	
	public static List<Vector<Float>> seuillageDur(List<Vector<Float>> projections, double seuil) {
	    List<Vector<Float>> result = new ArrayList<>();
	    for (Vector<Float> v : projections) {
	        Vector<Float> vSeuillage = new Vector<>();
	        for (Float val : v) {
	            vSeuillage.add(Math.abs(val) < seuil ? 0f : val);
	        }
	        result.add(vSeuillage);
	    }
	    return result;
	}

	public static List<Vector<Float>> seuillageDoux(List<Vector<Float>> projections, double seuil) {
	    List<Vector<Float>> result = new ArrayList<>();
	    for (Vector<Float> v : projections) {
	        Vector<Float> vSeuillage = new Vector<>();
	        for (Float val : v) {
	            float signe = val >= 0 ? 1f : -1f;
	            float valSeuillage = (Math.abs(val) - (float)seuil) > 0 ? signe * (Math.abs(val) - (float)seuil) : 0f;
	            vSeuillage.add(valSeuillage);
	        }
	        result.add(vSeuillage);
	    }
	    return result;
	}


   

}
