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




	
/**
 * Permet de gérer le débruitage d'une image
 * @author Pierre Laforest
 */
public class ImageDebruitee {
	
	/**
	 * Permet de définir un type de seuillage 
	 */
	public enum TypeSeuillage {
		    DUR,
		    DOUX,
		    AUTO
	}
	
	/**
	 * Permet de définir le type de seuil
	 * @author Etienne Angé
	 */
	public enum TypeSeuil {
	    VISU,
	    BAYES
	    
	}
	
	/**
	 * Permet de définir le typoe d'analyse
	 * @author Etienne Angé
	 */
	public enum Analyse{
		GLOBALE,
		LOCALE
	}
		    
	private Image imageDebruitee;

	/**
	 * 
	 * @param imageBruitee
	 * @param taillePatch
	 * @param typeSeuillage
	 * @param typeSeuil
	 * @param analyse
	 */
	public ImageDebruitee(Image imageBruitee, double taillePatch, ImageDebruitee.TypeSeuillage typeSeuillage, ImageDebruitee.TypeSeuil typeSeuil,ImageDebruitee.Analyse analyse) {
		if (analyse == ImageDebruitee.Analyse.LOCALE) {
			imageDebruitee = imageDenLocale(imageBruitee, (int)taillePatch, typeSeuillage,typeSeuil);
		} else {
			imageDebruitee = imageDen(imageBruitee, (int)taillePatch, typeSeuillage,typeSeuil);
		}
		
	}
	
	public Image getImage() {
		return imageDebruitee;
	}
	
	/** Méthode pour obtenir la luminance d'un pixel à partir de sa valeur ARGB
	 * @author Adrien
	 * @param argb
	 * @return
	 */

	public static int obtenirValeurGris(int argb) {
	    int r = (argb >> 16) & 0xFF;
	    int g = (argb >> 8) & 0xFF;
	    int b = argb & 0xFF;
	    // Formule de luminance
	    return (int) (0.299 * r + 0.587 * g + 0.114 * b);
	}

	/** Extrait les patchs de l'image
	 * @author Adrien
	 * @param image
	 * @param taillePatch
	 * @return les patchs de l'image
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
	 * Effectue un débruitage par ACP de manière locale :
	 * l'image est divisée en 4 sous-images (quadrants),
	 * chacune est débruitée séparément via la méthode imageDen().
	 * Les 4 parties sont ensuite recomposées pour obtenir l'image finale.
	 *
	 * @author Adrien
	 * @param imageBruitee l'image à débruiter
	 * @param taillePatch la taille des patchs utilisés pour l'ACP
	 * @param typeSeuillage le type de seuillage à appliquer (DUR, DOUX, AUTO)
	 * @param typeSeuil la méthode de calcul du seuil (VISU, BAYES)
	 * @return une image complète débruitée reconstruite à partir des 4 zones locales
	 */

	public Image imageDenLocale(Image imageBruitee, int taillePatch, TypeSeuillage typeSeuillage, TypeSeuil typeSeuil) {
	    int largeur = (int) imageBruitee.getWidth();
	    int hauteur = (int) imageBruitee.getHeight();
	    PixelReader lecteur = imageBruitee.getPixelReader();

	    int demiLargeur = largeur / 2;
	    int demiHauteur = hauteur / 2;

	    // Définir les 4 blocs : {x, y, w, h}
	    int[][] zones = {
	        {0, 0, demiLargeur, demiHauteur},                                // Haut-gauche
	        {demiLargeur, 0, largeur - demiLargeur, demiHauteur},            // Haut-droit
	        {0, demiHauteur, demiLargeur, hauteur - demiHauteur},           // Bas-gauche
	        {demiLargeur, demiHauteur, largeur - demiLargeur, hauteur - demiHauteur} // Bas-droit
	    };

	    // Images débruitées des 4 blocs
	    Image[] imagesDebruitees = new Image[4];

	    for (int i = 0; i < 4; i++) {
	        int x = zones[i][0];
	        int y = zones[i][1];
	        int w = zones[i][2];
	        int h = zones[i][3];

	        WritableImage sousImage = new WritableImage(lecteur, x, y, w, h);
	        imagesDebruitees[i] = imageDen(sousImage, taillePatch, typeSeuillage, typeSeuil);
	    }

	    // Recomposer l'image finale
	    WritableImage imageFinale = new WritableImage(largeur, hauteur);
	    PixelWriter writer = imageFinale.getPixelWriter();

	    for (int i = 0; i < 4; i++) {
	        int xOffset = zones[i][0];
	        int yOffset = zones[i][1];
	        int w = (int) imagesDebruitees[i].getWidth();
	        int h = (int) imagesDebruitees[i].getHeight();

	        PixelReader pr = imagesDebruitees[i].getPixelReader();
	        for (int y = 0; y < h; y++) {
	            for (int x = 0; x < w; x++) {
	                writer.setArgb(xOffset + x, yOffset + y, pr.getArgb(x, y));
	            }
	        }
	    }

	    return imageFinale;
	}


	
	/** Transforme une liste de patchs en une liste de vecteurs
	 * @author Adrien
	 * @param patchs
	 * @return une liste de patchs
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
	
	/** Reconstruction de l'image à partir des patchs débruités
	 * @author Adrien
	 * @param patchsDebruitee
	 * @return
	 */
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




/**
 * Methode principale du débruitage d'imagette
 * @param imageBruitee Image débruitée
 * @param taillePatch La taille des patch (carré)
 * @param typeSeuillage Type de seuillage
 * @param typeSeuil Type de seuil
 * @return L'imagette débruitée
 */
	public Image imageDen(Image imageBruitee, Integer taillePatch, ImageDebruitee.TypeSeuillage typeSeuillage,ImageDebruitee.TypeSeuil typeSeuil) {

	    // Patch

		List<Patch> patchs;
		patchs = extractPatchs(imageBruitee, taillePatch);
		

	    ArrayList<Patch> arrayListPatches = new ArrayList<>(patchs);
	    List<Vector<Float>> vecteurs = vectorPatchs(arrayListPatches);


		// Projection
		List<Vector<Float>> vecteursCentres = vecteur_centre_methode(vecteurs);
		RealMatrix U = ACP(vecteurs);
		List<Vector<Float>> projections = proj(U, vecteursCentres);

		/// Estimation bruit et tailles
		float[][] image2D = listVectorToArray(vecteurs); 

		// estimation de sigma
		int nbre_patch = vecteurs.size();      
		int taille_patch = vecteurs.get(0).size();

		double sigma;
		if (typeSeuil == TypeSeuil.BAYES) {
		    sigma = estimerSigmaB(projections); 
		} else {
		    sigma = estimerSigma(image2D);      
		}

		// Application du seuillage
		List<Vector<Float>> projectionsSeuillage;

		if (typeSeuil == TypeSeuil.BAYES) {
		    // BayesShrink → seuil par composante, donc seuillage spécifique intégré
		    if (typeSeuillage == TypeSeuillage.AUTO || typeSeuillage == TypeSeuillage.DOUX) {
		        projectionsSeuillage = seuilBayesShrinkParColonne(projections, sigma);
		    } else if (typeSeuillage == TypeSeuillage.DUR) {
		        System.err.println("⚠️ BayesShrink ne s’utilise normalement qu’avec le seuillage doux. Utilisation par défaut du doux.");
		        projectionsSeuillage = seuilBayesShrinkParColonne(projections, sigma);
		    } else {
		        throw new IllegalArgumentException("Type de seuillage inconnu : " + typeSeuillage);
		    }
		} else {
		    // VISU → seuil global
		    double seuil = seuilVisuShrink(sigma, taille_patch);

		    if (typeSeuillage == TypeSeuillage.AUTO) {
		        double varSignal = estimerVarianceSignal(projections, sigma * sigma);
		        if (choisirType(varSignal, seuil) == TypeSeuillage.DUR) {
		            projectionsSeuillage = seuillageDur(projections, seuil);
		        } else {
		            projectionsSeuillage = seuillageDoux(projections, seuil);
		        }
		    } else if (typeSeuillage == TypeSeuillage.DUR) {
		        projectionsSeuillage = seuillageDur(projections, seuil);
		    } else if (typeSeuillage == TypeSeuillage.DOUX) {
		        projectionsSeuillage = seuillageDoux(projections, seuil);
		    } else {
		        throw new IllegalArgumentException("Type de seuillage inconnu : " + typeSeuillage);
		    }
		}

	   

	    // Reconstruction des vecteurs centrés débruités
	    List<Vector<Float>> vecteursCentresDebruites = proj(U.transpose(), projectionsSeuillage);

	    // Ajout du vecteur moyen
	    List<Vector<Float>> vecteursDebruites = new ArrayList<>();
	    Vector<Float> mv = mv_methode(vecteurs);
	    for (Vector<Float> v : vecteursCentresDebruites) {
	        Vector<Float> vDebruite = new Vector<>();
	        for (int i = 0; i < v.size(); i++) {
	            vDebruite.add(v.get(i) + mv.get(i));
	        }
	        vecteursDebruites.add(vDebruite);
	    }

	    // Reconstruction des patchs à partir des vecteurs débruités
	    for (int i = 0; i < arrayListPatches.size(); i++) {
	        Patch p = arrayListPatches.get(i);
	        p.fromVector(vecteursDebruites.get(i));
	    }

	    // Reconstruction de l'image à partir des patchs débruités
	    Image imageReconstruite = reconstructPatchs(arrayListPatches);

	    return imageReconstruite;
	}

	/**
	 * @autor Bohain Mathis
	 * @param listVecteurs
	 * @return tableau
	 */
	
	public static float[][] listVectorToArray(List<Vector<Float>> listVecteurs) {
	    int rows = listVecteurs.size();
	    int cols = listVecteurs.get(0).size();

	    float[][] tableau = new float[rows][cols];

	    for (int i = 0; i < rows; i++) {
	        Vector<Float> vecteur = listVecteurs.get(i);
	        for (int j = 0; j < cols; j++) {
	            tableau[i][j] = vecteur.get(j);
	        }
	    }

	    return tableau;
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
	
	/** Convertie Une liste de vecteur en une liste de patch
	 * @author Etienne Angé
	 * @param vecteurs
	 * @param taillePatch
	 * @param patchPosition
	 * @return
	 */
	public List<Patch> patchsVector(List<Vector<Float>> vecteurs, int taillePatch, List<int[]> patchPosition) {
	    List<Patch> patchs = new ArrayList<>();
	    for (int i = 0; i < vecteurs.size(); i++) {
	        Vector<Float> vecteur = vecteurs.get(i);
	        int[] pos = patchPosition.get(i);
	        patchs.add(new Patch(vecteur, taillePatch, taillePatch, pos[0], pos[1]));
	    }
	    return patchs;
	}


	/** Renvoie l'estiamtion de sigma dans le cas Bayes
	 * @author Mathis Bohain
	 * @param projection
	 * @return sigma
	 */
	
	public static double estimerSigmaB(List<Vector<Float>> projections) { // à partir de proj
	    // dernieère composante
	    int j = projections.get(0).size() - 1;
	    double[] valeurs = new double[projections.size()];
	    for (int i = 0; i < projections.size(); i++) {
	        valeurs[i] = projections.get(i).get(j);
	    }

	    Arrays.sort(valeurs);
	    double mediane = valeurs[valeurs.length / 2];
	    return mediane ;  
	}

	/** Renvoie un 
	 * @author Pierre
	 * @param float[][]
	 * @return double
	 */
	
	public static double estimerSigma(float[][] image) { // à partir des pixels
	    List<Float> valeurs = new ArrayList<>();
	    int height = image.length;
	    int width = image[0].length;

	    for (int y = 1; y < height - 1; y++) {
	        for (int x = 1; x < width - 1; x++) {
	            float val = image[y][x]
	                      - 0.25f * (image[y][x-1] + image[y][x+1]
	                               + image[y-1][x] + image[y+1][x]);
	            valeurs.add(Math.abs(val));
	        }
	    }

	    Collections.sort(valeurs);
	    float mediane = valeurs.get(valeurs.size() / 2);
	    return 1.4826 * mediane;
	}

 
	/** Retourne le seuil par la méthode de VisuShrink
	 * @author Pierre
	 * @param double sigma, double tailleVecteur
	 * @return double
	 */
	
    public static double seuilVisuShrink(double sigma,double tailleVecteur) {
   
        return sigma * Math.sqrt(2 * Math.log(tailleVecteur))*0.5; 
    }
    
    /** Renvoie un 
	 * @author Pierre
	 * @param 
	 * @return
	 */

    /** Retourne le seuil par la méthode de BayesShrink
	 * @author Pierre
	 * @param float[][]
	 * @return double
	 */
    
    public static List<Vector<Float>> seuilBayesShrinkParColonne(List<Vector<Float>> projections, double sigma) {
        double sigma2 = sigma * sigma;
        int nbPatches = projections.size();
        int nbComposantes = projections.get(0).size();

        List<Vector<Float>> projectionsSeuillees = new ArrayList<>(nbPatches);
        for (int i = 0; i < nbPatches; i++) {
            projectionsSeuillees.add(new Vector<>(nbComposantes));
        }

        double epsilon = 1e-8; 

        // Pour chaque composante principale
        for (int j = 0; j < nbComposantes; j++) {
            // Extraire la colonne j
            float[] colonne = new float[nbPatches];
            for (int i = 0; i < nbPatches; i++) {
                colonne[i] = projections.get(i).get(j);
            }

            // Moyenne de la colonne
            double somme = 0.0;
            for (float v : colonne) somme += v;
            double moyenne = somme / nbPatches;

            // Variance observée sigma_Y^2
            double sigmaY2 = 0.0;
            for (float v : colonne) {
                double diff = v - moyenne;
                sigmaY2 += diff * diff;
            }
            sigmaY2 /= nbPatches;

            // Estimation de la variance du signal : sigma_X^2
            double sigmaX2 = Math.max(sigmaY2 - sigma2, 0);


            // Calcul du seuil BayesShrink (stabilisé)
            double seuil = (sigmaX2 < epsilon) ? Double.POSITIVE_INFINITY : sigma2 / Math.sqrt(sigmaX2 + epsilon);

            // Appliquer le seuillage doux (soft thresholding)
            for (int i = 0; i < nbPatches; i++) {
                double coeff = colonne[i];
                double valeur = Math.abs(coeff) - seuil;
                double coeffSeuille = (valeur > 0) ? Math.signum(coeff) * valeur : 0.0;
                projectionsSeuillees.get(i).add((float) coeffSeuille);
            }
        }

        return projectionsSeuillees;
    }
    


   
    
    /**
     * 
     * @param projections
     * @param varianceBruit
     * @return
     */

    public static double estimerVarianceSignal(List<Vector<Float>> projections, double varianceBruit) {
        double sumSquares = 0;
        int count = 0;

        for (Vector<Float> vect : projections) {
            for (float val : vect) {
                sumSquares += val * val;
                count++;
            }
        }

        double varianceBrute = sumSquares / count;
        double varianceSignal = varianceBrute - varianceBruit;

        return varianceSignal > 0 ? varianceSignal : 0;
    }

    /**
     * 
     * @param variance
     * @param seuilVi
     * @return
     */
    public static TypeSeuillage choisirType(double variance, double seuilVi) {
        if (variance > seuilVi) {
            return TypeSeuillage.DUR;
        } else {
            return TypeSeuillage.DOUX;
        }
    }
    
    
    
    /**
     * 
     * @param proj
     * @param val
     * @return
     */
    public static List<Vector<Float>> seuillageDur(List<Vector<Float>> proj, double val) {
        List<Vector<Float>> sD = new ArrayList<>();

        for (Vector<Float> vecteur : proj) {
            Vector<Float> seuilVector = new Vector<>();
            for (float x : vecteur) {
                if (Math.abs(x) <= val) {
                    seuilVector.add(0.0f);
                } else {
                    seuilVector.add(x);
                }
            }
            sD.add(seuilVector);
        }
        
        return sD;
    }



    /**
     * 
     * @param proj
     * @param seuil
     * @return
     */
    public static List<Vector<Float>> seuillageDoux(List<Vector<Float>> proj, double seuil) {
        List<Vector<Float>> sD = new ArrayList<>();

        for (Vector<Float> vecteur : proj) {
            Vector<Float> seuilVector = new Vector<>();
            for (float x : vecteur) {
                if (Math.abs(x) <= seuil) {
                    seuilVector.add(0.0f);
                } else if (x > 0) {
                    seuilVector.add(x - (float) seuil);
                } else {
                    seuilVector.add(x + (float) seuil);
                }
            }
            sD.add(seuilVector);
        }

        return sD;
    }
    
    /**
     * @author Mathis Bohain
     * @param v
     * @return moyenne de v
     */
    public static double moyenne(double[] v) {
        double somme = 0.0;
        for (double val : v) somme += val;
        return somme / v.length;
    }


    

   

}
