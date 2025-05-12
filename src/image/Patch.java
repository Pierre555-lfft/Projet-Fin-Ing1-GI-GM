package image;

import java.util.List;
import java.util.Vector;

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
	
	//Etienne Angé
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
	
	
	//Etienne Angé
	public Vector<Float> toVector() {
	    Vector<Float> vector = new Vector<>();
	    for (int i = 0; i < tab.length; i++) {
	        for (int j = 0; j < tab[i].length; j++) {
	            vector.add(tab[i][j]);
	        }
	    }
	    return vector;
	}
}
