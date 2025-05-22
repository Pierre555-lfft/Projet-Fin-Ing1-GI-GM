package image;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class EvaluateurQualiteImage {
	private Image imageOriginale;
	private Image imageDebruitee;
	private DoubleProperty bruitageValue = new SimpleDoubleProperty();
	private DoubleProperty mse = new SimpleDoubleProperty();
	private DoubleProperty psnr = new SimpleDoubleProperty();
	private IntegerProperty taillePatch = new SimpleIntegerProperty();
	private StringProperty seuillage = new SimpleStringProperty();
	private StringProperty seuil = new SimpleStringProperty();
	private StringProperty analyse = new SimpleStringProperty();
	
	public double getBruitageValue() {
        return mse.get();
    }

    public void setBruitageValue(double bruitageValue) {
        this.bruitageValue.set(bruitageValue);
    }

    public DoubleProperty bruitageValueProperty() {
        return bruitageValue;
    }
    
	public double getMse() {
        return mse.get();
    }

    public void setMse(double mse) {
        this.mse.set(mse);
    }

    public DoubleProperty mseProperty() {
        return mse;
    }
	
    public double getPsnr() {
        return psnr.get();
    }

    public void setPsnr(double psnr) {
        this.psnr.set(psnr);
    }
    
	public DoubleProperty psnrProperty() {
		return psnr;
	}
	
	
	public int getTaillePatch() {
	    return taillePatch.get();
	}

	public void setTaillePatch(int value) {
	    taillePatch.set(value);
	}

	public IntegerProperty taillePatchProperty() {
	    return taillePatch;
	}

	
	public String getSeuillage() {
	    return seuillage.get();
	}

	public void setSeuillage(String value) {
	    seuillage.set(value);
	}

	public StringProperty seuillageProperty() {
	    return seuillage;
	}


	public String getSeuil() {
	    return seuil.get();
	}

	public void setSeuil(String value) {
	    seuil.set(value);
	}

	public StringProperty seuilProperty() {
	    return seuil;
	}

	
	public String getAnalyse() {
	    return analyse.get();
	}

	public void setAnalyse(String value) {
	    analyse.set(value);
	}

	public StringProperty analyseProperty() {
	    return analyse;
	}

	
	
	
	/** Constructeur de la classe
	 * @author Adrien
	 * @return
	 */
	public EvaluateurQualiteImage (Image imageOriginale, Image imageDebruitee,double bruitageValue, Integer taillePatch, String seuillaString, String seuil, String analyse) {
		this.imageOriginale = imageOriginale;
		this.imageDebruitee = imageDebruitee;
		this.bruitageValue.set(bruitageValue);
		this.taillePatch.set(taillePatch);
		this.seuillage.set(seuillaString);
		this.seuil.set(seuil);
		this.analyse.set(analyse);
		
	}
	
	/** Effectue le calcul de l'Erreur quadratique moyenne
	 * @author Adrien
	 * @return l'erreur quadratique moyenne
	 */
	public double calculerMSE() {
		double mse;
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
	    mse = sommeErreursCarrees / nombrePixels;
	    this.mse.set(mse);
	    // Calcul de la moyenne
	    return mse;
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
	    this.psnr.set(psnr);
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
