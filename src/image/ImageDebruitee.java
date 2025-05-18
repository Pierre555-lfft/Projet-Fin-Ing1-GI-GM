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

public class ImageDebruitee {
	private Image imageDebruitee;

	public ImageDebruitee(Image imageBruitee) {
		imageDebruitee = imageDen(imageBruitee);
	}
	
	public Image getImage() {
		return imageDebruitee;
	}
	
	// Adrien
	// Méthode pour obtenir la luminance d'un pixel à partir de sa valeur ARGB
	public static int obtenirValeurGris(int argb) {
	    int r = (argb >> 16) & 0xFF;
	    int g = (argb >> 8) & 0xFF;
	    int b = argb & 0xFF;
	    // Formule de luminance
	    return (int) (0.299 * r + 0.587 * g + 0.114 * b);
	}

	// Adrien
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
	
	// MathisB
	
	public static Vector<Float> mv_methode (List<Vector<Float>> V){ //ou V est la collection de patch vectorisée
				
	// Initialisation longueur de lacollection de patch et des patchs
					
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
				
	// Mathis B
				
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
							
				double cov_ij = somme/(n-1); // Coefficiant de la ligne i et colonne j
							
				// Matrice symetrique
							
				Cov.get(i).set(j,(float) cov_ij);
							
				if (i != j) {
					Cov.get(j).set(i, (float)cov_ij); // par symetrie
				}
							
			}
		}
		return Cov ;
	}
				
	// Mathis B
				
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
				
	//Mathis B
				
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
				    
		// Tri décroissant des valeurs propres (et garder leur index)
	    Integer[] indices = new Integer[p];
	    for (int i = 0; i < p; i++) indices[i] = i;
	    Arrays.sort(indices, (i, j) -> Double.compare(valeurs_propres[j], valeurs_propres[i]));
	    
	    // Calcul du nombre de composantes à garder (ex: 90% de la variance)
	    double seuil = 0.90;
	    double sommeTotale = 0.0;
	    for (double val : valeurs_propres) sommeTotale += val;

	    double sommePartielle = 0.0;
	    int k = 0;
	    while (k < p && (sommePartielle / sommeTotale) < seuil) {
	        sommePartielle += valeurs_propres[indices[k]];
	        k++;
	    }
				    
		System.out.println("Vecteur moyen : " + mv);
		System.out.println("Valeurs propres : " + Arrays.toString(valeurs_propres));
		System.out.println("Nombre de composantes principales gardées (k) : " + k);

		// Construction de la matrice des k vecteurs propres associés aux plus grandes valeurs propres
	    double[][] vecteurs_principaux = new double[p][k];
	    for (int i = 0; i < k; i++) {
	        double[] col = vecteurs_propres.getColumn(indices[i]);
	        for (int j = 0; j < p; j++) {
	            vecteurs_principaux[j][i] = col[j];
	        }
	    }

	    return new Array2DRowRealMatrix(vecteurs_principaux);
	}
				
				// List<Vector<Float>> Vc = vecteur_centre_methode(collection_patch);//Vc c'est le terme Vk - mv
				
				//MathisB
				
				public static List<Vector<Float>> proj (RealMatrix U, List<Vector<Float>> Vc ){
					
					// Definition Longueur
					
					int p = U.getRowDimension(); // nombnre de vecteur
				    int n = Vc.size(); // nombre vecteur centré
				    
				    // Definition des variables 
				    
				    
				    ArrayList<Vector<Float>> V_contrib = new ArrayList<Vector<Float>>();
				    
				    for(int i = 0; i<n;i++) { // création d'une list de vecteur avec n patch
						V_contrib.add(new Vector<Float>());
				       
				        
					}
				    
				    //Exploitation
				    
				    for(int k = 0; k<n; k++) {
				    	Vector<Float> Vk = Vc.get(k);
				    	
				    	for (int i =0; i<p; i++) {// Pour la somme de i a s²
				    		
				    
				    		double[] ui = U.getColumn(i);
				    		double proj =0;
				    		
				    		for(int j =0; j<p; j++) {//parcourir chaque ligne et effcetuer l'operation de multiplication de colonne uitranspoe*Vc
				    			proj += ui[j]*Vk.get(j);
				    		}
				    		V_contrib.get(k).add((float)proj); //Contrsuction de V_contrib coeff par coeff a_ki
				    	}
				    }
				    	
				    	

				  return V_contrib;  
				}

	// Adrien
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
	                // on combine plusieurs entiers binaires pour avoir le argb
	                int argb = (0xFF << 24) | (niveauGris << 16) | (niveauGris << 8) | niveauGris;
	                ecrivain.setArgb(x, y, argb);
	            }
	        }
	    }

	    return imageReconstruite;
	}

	
	public Image imageDen(Image imageBruitee) {
		Integer taillePatch = 10;
		Integer ligneImage = 450;
		Integer colonneImage = 600;
		Integer nbLignePatch = 10;
		Integer nbColonnePatch = 10;
		
		List<Patch> patchs = extractPatchs(imageBruitee, taillePatch);
		
		
		
		
		
		ArrayList<Patch> arrayListPatches = new ArrayList<>(patchs);
		
		List<int[]> patchPosition = getPositions(patchs);
		
		List<Vector<Float>> vecteurs = vectorPatchs(arrayListPatches);
		
		// transformation des vecteurs
		
		
		List<Vector<Float>> alpha_i = proj(ACP(vecteurs), vecteur_centre_methode(vecteurs)); //Mathis
		
		
		
	    float variance = ImageDebruitee.calculerVarianceFloat(alpha_i);  // Pierre : fonction qui sert à calculer la variance 
	    float sigma = (float) Math.sqrt(variance); // Pierre : fonction qui sert à déterminer le paramètre sigma 
	    float seuil = ImageDebruitee.seuilV(sigma, alpha_i.size(), alpha_i.get(0).size()); // Pierre : fonction qui permettra de déterminer le seuil 

	 
	    TypeSeuillage type = ImageDebruitee.choisirType(variance); // Pierre : fonction qui permet de déterminer le type de seuillage à utiliser selon la variance 

	    List<Vector<Float>> alphaSeuil;  // Pierre : fonction qui permet d'utiliser le bon seuillage
	    if (type == TypeSeuillage.DUR) {
	        alphaSeuil = ImageDebruitee.seuillageDur(alpha_i, seuil);
	    } else {
	        alphaSeuil = ImageDebruitee.seuillageDoux(alpha_i, seuil);
	    }
	    

	    List<Vector<Float>> vecteursDebruitee = reconstruireDepuisACP(alphaSeuil, ACP(vecteurs), mv_methode(vecteurs)); // Pierre : fonction qui renvoie les patchs vectorisé débruiter 

		
		
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
			i=i+1;
		}
		
		return patchs;
	}
	
	
	
	//Pierre Laforest
	//fonction qui permet de déterminer le seuille 
	public static float seuilV(float sigma, int nbPatchs, int taillePatch) {
		int l; // cette variable permet de calculer la taille pour calculer le seuil avec la méthode de VisuSrhrink
		float visuSrhrink; // seuil de VisuSrhrink
		l = nbPatchs * taillePatch;
		visuSrhrink = sigma * (float)Math.sqrt(2 * Math.log(l)); // calcule du seuil avec la méthoe de VisuSrhrink
        return visuSrhrink; // retourner le seuilV
	}
	
	//Pierre Laforest 
	//fonction qui permet de déterminer la variance 
	public static float calculerVarianceFloat(List<Vector<Float>> matriceImage) {
	    int elementTotals = 0;
	    float somme = 0f;
	    float sommeCarres = 0f;

	    for (Vector<Float> ligne : matriceImage) {
	        for (float coeff : ligne) {
	            somme += coeff;
	            sommeCarres += coeff * coeff;
	            elementTotals++;
	        }
	    }

	    if (elementTotals == 0) return 0f;

	    float moyenne = somme / elementTotals;
	    float variance = (sommeCarres / elementTotals) - (moyenne * moyenne);

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
		public static List<Vector<Float>> seuillageDur(List<Vector<Float>> data, float seuil) {
		    List<Vector<Float>> resultat = new ArrayList<>();

		    for (Vector<Float> ligne : data) {
		
		    	

		        Vector<Float> nouvelleLigne = new Vector<>();
		        for (float coeff : ligne) {
		            if (Math.abs(coeff) < seuil) {
		                nouvelleLigne.add(0f);
		            } else {
		                nouvelleLigne.add(coeff);
		            }
		        }
		        resultat.add(nouvelleLigne);
		    }

		    return resultat;
		}

		//Pierre Laforest
		//fonction qui permet de faire un seuillage doux
		public static List<Vector<Float>> seuillageDoux(List<Vector<Float>> data, float seuil) {
		    List<Vector<Float>> resultat = new ArrayList<>();

		    for (Vector<Float> ligne : data) {
		    	
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
		        resultat.add(nouvelleLigne);
		    }

		    return resultat;
		}


		//Pierre Laforest 
		//fonction qui permet de faire reconstruire les patchs depuis l'ACP
		public static List<Vector<Float>> reconstruireDepuisACP(List<Vector<Float>> alphaSeuillé,RealMatrix baseU,Vector<Float> moyenne) {
			
		        List<Vector<Float>> resultat = new ArrayList<>();

		        for (Vector<Float> alpha : alphaSeuillé) {
		            // Convertir alpha (Vector<Float>) en RealMatrix colonne
		            double[] alphaArray = new double[alpha.size()];
		            for (int i = 0; i < alpha.size(); i++) {
		                alphaArray[i] = alpha.get(i);
		            }
		            RealMatrix alphaColonne = new Array2DRowRealMatrix(alphaArray);
		            
		            System.out.println("baseU: " + baseU.getRowDimension() + "x" + baseU.getColumnDimension());
		            System.out.println("alphaColonne: " + alphaColonne.getRowDimension() + "x" + alphaColonne.getColumnDimension());

		            // Reprojection : U * alpha
		            RealMatrix projection = baseU.multiply(alphaColonne);

		            // Ajout de la moyenne
		            Vector<Float> patchReconstruit = new Vector<>();
		            for (int i = 0; i < projection.getRowDimension(); i++) {
		                float valeur = (float) projection.getEntry(i, 0) + moyenne.get(i);
		                patchReconstruit.add(valeur);
		            }

		            resultat.add(patchReconstruit);
		        }

		        return resultat;
		    }

		
		

}
