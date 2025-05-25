package image;

import java.util.List;
import java.util.Vector;

/**
 * Objet contenant une portion des pixels d'une image, permetant de débruiter un image
 */
public class Patch {
	private Float[][] tab;

	//Position
	private Integer x;
	private Integer y;
	
	
	public Patch(Float[][] tab, Integer x, Integer y){
		this.tab = tab;
		this.x = x;
		this.y = y;
	}
	
	/** Creer un Patch à partir d'un vecteur
	 * @author Etienne Angé
	 * @param vector
	 * @param largeur
	 * @param hauteur
	 * @param x
	 * @param y
	 */
	public Patch(Vector<Float> vector, int largeur, int hauteur, int x, int y) {
	    if (vector.size() != largeur * hauteur) {
	        throw new IllegalArgumentException("La taille du vecteur ne correspond pas aux dimensions spécifiées.");
	    }

	    this.tab = new Float[hauteur][largeur];
	    for (int i = 0; i < hauteur; i++) {
	        for (int j = 0; j < largeur; j++) {
	            this.tab[i][j] = vector.get(i * largeur + j);
	        }
	    }

	    this.x = x;
	    this.y = y;
	}

	
	public Float[][] getTab() {
		return tab;
	}
	
	public Integer getX() {
		return x;
	}
	
	
	public Integer getY() {
		return y;
	}
	
	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}
	
	//Etienne
	@Override
	public String toString() {
		String string = "";
		
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[i].length; j++) {
				string = string + "-" + tab[i][j].toString(); 
			}
			string = string + "\n";
		}		
		return string;
				
	}
	
	
	/**
	 * @author Etienne Angé
	 * @return
	 */
	public Vector<Float> toVector() {
	    Vector<Float> vector = new Vector<>();
	    for (int i = 0; i < tab.length; i++) {
	        for (int j = 0; j < tab[i].length; j++) {
	            vector.add(tab[i][j]);
	        }
	    }
	    return vector;
	}
	
	public void fromVector(Vector<Float> vecteur) {
	    int taille = (int) Math.sqrt(vecteur.size());
	    Float[][] matrice = new Float[taille][taille];
	    for (int i = 0; i < taille; i++) {
	        for (int j = 0; j < taille; j++) {
	            matrice[i][j] = vecteur.get(i * taille + j);
	        }
	    }
	    this.setTab(matrice); // ou this.tab = matrice;
	}
	
	public void setTab(Float[][] nouveauTab) {
	    this.tab = nouveauTab;
	}


}