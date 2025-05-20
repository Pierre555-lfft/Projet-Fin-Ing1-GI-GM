package image;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class EvaluateurQualiteImage {
	private Image imageOriginale;
	private Image imageDebruitee;
	
	/** Constructeur de la classe
	 * @author Adrien
	 * @return
	 */
	public EvaluateurQualiteImage (Image imageOriginale, Image imageDebruitee) {
		this.imageOriginale = imageOriginale;
		this.imageDebruitee = imageDebruitee;
	}
	
	/** Effectue le calcul de l'Erreur quadratique moyenne
	 * @author Adrien
	 * @return l'erreur quadratique moyenne
	 */
	public double calculerMSE() {

	    PixelReader lecteurOriginal = imageOriginale.getPixelReader();
	    PixelReader lecteurDebruite = imageDebruitee.getPixelReader();
	    
	    int largeur = (int) imageOriginale.getWidth();
	    int hauteur = (int) imageOriginale.getHeight();
	    
	    double sommeErreursCarrees = 0.0;
	    int nombrePixels = largeur * hauteur;

	    // Parcours de tous les pixels
	    for (int y = 0; y < hauteur; y++) {
	        for (int x = 0; x < largeur; x++) {
	            // Extraction des niveaux de gris
	            int grisOriginal = ImageDebruitee.obtenirValeurGris(lecteurOriginal.getArgb(x, y));
	            int grisDebruite = ImageDebruitee.obtenirValeurGris(lecteurDebruite.getArgb(x, y));
	            
	            double difference = grisOriginal - grisDebruite;
	            sommeErreursCarrees += difference * difference;
	        }
	    }

	    // Calcul de la moyenne
	    return sommeErreursCarrees / nombrePixels;
	}
	
	/** Effectue le rapport du signal sur le bruit
	 * @author Adrien
	 * @return le rapport du signal sur le bruit
	 */
	public double calculerPSNR() {

	    double mse = calculerMSE();
	    
	    // 3. Valeur maximale possible d'un pixel (255 pour des images 8 bits)
	    final double valeurMaxPixel = 255.0;
	    
	    // 4. Calcul du PSNR selon la formule standard
	    double psnr = 10 * Math.log10((valeurMaxPixel * valeurMaxPixel) / mse);
	    
	    return psnr;
	}
	
	/** Affiche le résultat des méthodes calculerMSE et calculerPSNR
	 * @author Adrien
	 * @return
	 */
	public void resultatsQualite() {
	    double mse = calculerMSE();
	    double psnr = calculerPSNR();

	    System.out.println("===== Résultats de Qualité d'Image =====");
	    System.out.printf("MSE  (Erreur quadratique moyenne) : %.2f%n", mse);
	    System.out.printf("PSNR (Rapport signal sur bruit)   : %.2f dB%n", psnr);
	}
}
