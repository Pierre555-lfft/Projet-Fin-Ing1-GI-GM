package image;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Photo permet de gérer une Image, donc de la bruiter/débruiter. Photo contient également les métadonnées de l'image
 */
public class Photo {

	private final float ZOOM_MIN = 0.1f;
	private final float ZOOM_MAX = 5.0f;
	private final float ICON_SIZE = 80.0f;

    private String filename;
    private Image image;
    private Image imageOriginelle;
    private Image imageOriginelleGrisee;
    private ImageDebruitee imageDebruitee;
    private ImageBruitee imageBruitee;

	
	
	private int largeurInit;
	private int hauteurInit;

	private String nom;
	
		
	

	/**
	 * Constructeur initialisant la photo � partir du fichier image filename
	 * 
	 * @param filename
	 *            chemin complet du fichier image
	 */
	public Photo(String filename) {
		this.filename = filename;
		this.image = new Image(filename);
		this.imageOriginelle = this.image;
		this.imageOriginelleGrisee = nb();
		this.largeurInit = this.getLargeur();
		this.hauteurInit = this.getHauteur();

		this.nom = (new File(filename)).getName();
		this.nom = this.nom.substring(0, this.nom.length() - 4);
	}
	
	
	/** Permet de bruitée l'image originelle
	 * @author Etienne Angé
	 * @param ecartType
	 * @return
	 */
	public Image bruiter(double ecartType) {
        	imageBruitee = new ImageBruitee(imageOriginelle, ecartType);
        	image = imageBruitee.getImage();
        	image = nb();
        	return image;
   	}
	
	public Image reset() {
		image = imageOriginelle;
		return image;
	}
	

/** Permet de débruitée l'image bruitée
 * @author Etienne Angé
 * @param taillePatch
 * @param typeSeuillage
 * @param typeSeuil
 * @param analyse
 * @return
 */
	public Image debruiter(double taillePatch, ImageDebruitee.TypeSeuillage typeSeuillage, ImageDebruitee.TypeSeuil typeSeuil, ImageDebruitee.Analyse analyse) {
		imageDebruitee = new ImageDebruitee(imageBruitee.getImage(), taillePatch, typeSeuillage,typeSeuil, analyse);


		image = imageDebruitee.getImage();
		return imageDebruitee.getImage();
	}
	
	public Image getImageOriginelleGrisee() {
	    return this.imageOriginelleGrisee;
	}	

	/**
	 * Retourne l'image de la photo
	 * 
	 * @return l'image de la photo.
	 */
	public Image getImage() {
		return this.image;
	}

	/**
	 * Retourne la largeur de la photo
	 * 
	 * @return la largeur de la photo.
	 */
	public int getLargeur() {
		return (int) this.image.getWidth();
	}

	/**
	 * Retourne la hauteur de la photo
	 * 
	 * @return la hauteur de la photo.
	 */
	public int getHauteur() {
		return (int) this.image.getHeight();
	}


	
	
 

	/**
	 * Retourne le nom de la photo
	 * 
	 * @return le nom de la photo
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Retourne une icone de la photo de dimension maximale ICON_SIZE
	 * 
	 * @return une icone de la photo de dimension maximale ICON_SIZE
	 */
	public Image getIcone() {
		float ratio = Math.max(this.largeurInit, this.hauteurInit);
		int largeur = (int) (ICON_SIZE * (float) this.largeurInit / ratio);
		int hauteur = (int) (ICON_SIZE * (float) this.hauteurInit / ratio);
		return new Image(this.filename, largeur, hauteur, false, false);
	}


	public Image nb() {
       		int width = (int) image.getWidth();
        	int height = (int) image.getHeight();
	
        	WritableImage result = new WritableImage(width, height);
        	PixelReader reader = image.getPixelReader();
        	PixelWriter writer = result.getPixelWriter();

        	for (int y = 0; y < height; y++) {
            	for (int x = 0; x < width; x++) {
                	Color color = reader.getColor(x, y);
                	double gris = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
                	Color grisColor = new Color(gris, gris, gris, color.getOpacity());
                	writer.setColor(x, y, grisColor);
            	}
        }

        this.imageOriginelleGrisee = result;
        	
        return result;
    }
}
