
package image;

import org.apache.commons.math3.linear.RealMatrix;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class App {
	 public static void main(String[] args) {
	        // Création de 3 vecteurs de taille 3 (3 patchs de 3 pixels chacun)
	        Vector<Float> v1 = new Vector<>();
	        v1.add(1.0f);
	        v1.add(2.0f);
	        v1.add(3.0f);

	        Vector<Float> v2 = new Vector<>();
	        v2.add(4.0f);
	        v2.add(5.0f);
	        v2.add(6.0f);

	        Vector<Float> v3 = new Vector<>();
	        v3.add(7.0f);
	        v3.add(8.0f);
	        v3.add(9.0f);

	        List<Vector<Float>> V = new ArrayList<>();
	        V.add(v1);
	        V.add(v2);
	        V.add(v3);

	        // Appel de la méthode ACP
	        RealMatrix matACPr = ImageDebruitee.ACP(V);

	        // Affichage des vecteurs propres (colonnes de la matrice U)
	        System.out.println("\n--- Vecteurs propres (colonnes de U) ---");
	        for (int i = 0; i < matACPr.getColumnDimension(); i++) {
	            double[] col = matACPr.getColumn(i);
	            System.out.printf("u%d: ", i);
	            for (double val : col) {
	                System.out.printf("%.4f ", val);
	            }
	            System.out.println();
	        }
	    }
	

}