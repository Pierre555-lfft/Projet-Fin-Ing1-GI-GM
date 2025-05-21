package presentation;


import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import image.Album;
import image.ImageDebruitee;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import image.EvaluateurQualiteImage;
import javafx.scene.image.Image;

/**
 * Interface utilisateur permettant de tester les differente fonction de bruitage et débruitage
 *@author Etienne Angé
 */
public class Visionneuse extends Application {
	public ImageDebruitee.TypeSeuillage typeSeuillage = ImageDebruitee.TypeSeuillage.AUTO;
	private Album album;
	private ImageView imageView;
	private Slider slider;
	private ListView<String> creerListe;
	private List<RadioButton> listButton;
	private Stage primaryStage;
	private BorderPane window;
	private Label labelEvaluateur;
	private ImageDebruitee.TypeSeuil typeSeuil = ImageDebruitee.TypeSeuil.BAYES;
	
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Album Photo");
	

		album = new Album("images");
		 
		VBox vbox = new VBox();
		
		window = new BorderPane();
		
		ScrollPane centre = creerCentre();
		window.setCenter(centre);
		//slider = creerSlider();
		//window.setRight(slider);
		creerListe = creerListe();
		window.setLeft(creerListe);
		
		
		HBox bandeauHaut = creerBandeauHaut();
		window.setTop(bandeauHaut);
		
		
		
		MenuBar menuBar = creerMenu();
		
		vbox.getChildren().addAll(menuBar,window);
		
		
		Scene scene = new Scene(vbox);
		
		//scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	
	public ScrollPane creerCentre() {
		ScrollPane p = new ScrollPane();
		p.setPrefSize(600,450);
		imageView = new ImageView(album.getPhotoCourante().getImage());
	
		p.setContent(imageView);
		return p;
	}
	
	public Slider creerSlider() {
		slider = new Slider(0,300,100);
		slider.setOrientation(Orientation.VERTICAL);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(100);
		slider.setMinorTickCount(9);
		
		slider.valueProperty().addListener (arg0 -> {
			album.redimensionnerPhotoCourante((float)slider.getValue());
			imageView.setImage(album.getPhotoCourante().getImage());
			
		});
		return slider;
		
	}
	
	public ListView<String> creerListe(){
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < album.getSize(); i++) {
			l.add(album.getPhoto(i).getNom());
		}
		 ObservableList<String> names = FXCollections.observableArrayList(l);
		 ListView<String> listView = new ListView<String>(names);
		 listView.setOnMouseClicked(arg0 -> {
			 album.setIndexCourant(listView.getSelectionModel().getSelectedIndex());
			 imageView.setImage(album.getPhotoCourante().getImage());
			 slider.setValue(album.getPhotoCourante().getZoom());
			 });
		 return listView;
	}
		
	public HBox creerBandeauHaut() {
	    labelEvaluateur = new Label("Pas d'image débruitée");

	    HBox bandeauHaut = new HBox();

	    Button btnReset = new Button("Reset");
	    btnReset.setOnAction(arg0 -> {
	        imageView.setImage(album.getPhotoCourante().reset());
	        labelEvaluateur.setText("Pas d'image débruitée");
	    });

<<<<<<< Updated upstream
		sliderTaillePatch.setOrientation(Orientation.HORIZONTAL);
		sliderTaillePatch.setShowTickMarks(true);
		sliderTaillePatch.setShowTickLabels(true);
		sliderTaillePatch.setMajorTickUnit(10);
		btnDebruiter.setOnAction(arg0 -> {
			imageView.setImage(album.getPhotoCourante().debruiter(sliderTaillePatch.getValue(), typeSeuillage, typeSeuil));
			Image originale = album.getPhotoCourante().getImageOriginelleGrisee();
=======
	    // Bruitage
	    HBox bruiter = new HBox();
	    bruiter.setAlignment(Pos.CENTER);
	    Button btnBruiter = new Button("Bruiter");

	    VBox bruitage = new VBox();
	    Button btnDebruiter = new Button("Débruiter");
	    Label textBruitage = new Label("Bruitage");
	    Slider sliderBruitage = new Slider(0, 100, 50);
	    sliderBruitage.setOrientation(Orientation.HORIZONTAL);
	    sliderBruitage.setShowTickMarks(true);
	    sliderBruitage.setShowTickLabels(true);
	    sliderBruitage.setMajorTickUnit(10);
	    bruitage.getChildren().addAll(textBruitage, sliderBruitage);
	    btnBruiter.setOnAction(arg0 -> {
	        imageView.setImage(album.getPhotoCourante().bruiter(sliderBruitage.getValue()));
	        labelEvaluateur.setText("Pas d'image débruitée");
	    });
	    bruiter.getChildren().addAll(bruitage, btnBruiter);

	    // Débruitage
	    HBox debruiter = new HBox();
	    debruiter.setAlignment(Pos.CENTER);

	    VBox choixTaillePatch = new VBox();
	    Slider sliderTaillePatch = new Slider(2, 25, 5);
	    sliderTaillePatch.setOrientation(Orientation.HORIZONTAL);
	    sliderTaillePatch.setShowTickMarks(true);
	    sliderTaillePatch.setShowTickLabels(true);
	    sliderTaillePatch.setMajorTickUnit(5);
	    sliderTaillePatch.setSnapToTicks(true);
	    choixTaillePatch.getChildren().addAll(new Label("Taille patch"), sliderTaillePatch);

	    // Type de seuillage
	    ToggleButton btnSeuillageDur = new ToggleButton("Dur");
	    ToggleButton btnSeuillageDoux = new ToggleButton("Doux");
	    ToggleButton btnSeuillageAuto = new ToggleButton("Auto");
	    ToggleGroup btnSeuillage = new ToggleGroup();
	    btnSeuillageDur.setToggleGroup(btnSeuillage);
	    btnSeuillageDoux.setToggleGroup(btnSeuillage);
	    btnSeuillageAuto.setToggleGroup(btnSeuillage);
	    btnSeuillageDur.setOnAction(arg0 -> typeSeuillage = ImageDebruitee.TypeSeuillage.DUR);
	    btnSeuillageDoux.setOnAction(arg0 -> typeSeuillage = ImageDebruitee.TypeSeuillage.DOUX);
	    btnSeuillageAuto.setOnAction(arg0 -> typeSeuillage = ImageDebruitee.TypeSeuillage.AUTO);
	    VBox hBoxSeuillage = new VBox();
	    hBoxSeuillage.getChildren().addAll(new Label("Seuillage"), btnSeuillageDur, btnSeuillageDoux, btnSeuillageAuto);

	    // Choix extraction globale / locale
	    VBox modeExtractionBox = new VBox();
	    ToggleGroup modeExtractionGroup = new ToggleGroup();
	    RadioButton rbGlobal = new RadioButton("Globale");
	    RadioButton rbLocale = new RadioButton("Locale");
	    rbGlobal.setToggleGroup(modeExtractionGroup);
	    rbLocale.setToggleGroup(modeExtractionGroup);
	    rbGlobal.setSelected(true);
	    modeExtractionBox.getChildren().addAll(new Label("Extraction"), rbGlobal, rbLocale);

	    // Taille imagette (slider entier)
	    VBox boxTailleImagette = new VBox();
	    Label labelTailleImagette = new Label("Taille imagette");
	    Slider sliderTailleImagette = new Slider(10, 100, 32);
	    sliderTailleImagette.setOrientation(Orientation.HORIZONTAL);
	    sliderTailleImagette.setShowTickMarks(true);
	    sliderTailleImagette.setShowTickLabels(true);
	    sliderTailleImagette.setMajorTickUnit(10);
	    sliderTailleImagette.setMinorTickCount(0);
	    sliderTailleImagette.setSnapToTicks(true);
	    boxTailleImagette.getChildren().addAll(labelTailleImagette, sliderTailleImagette);
	    boxTailleImagette.setVisible(false); // caché si extraction globale

	    // 🔁 Synchronisation taillePatch ≤ tailleImagette
	    sliderTailleImagette.valueProperty().addListener((obs, oldVal, newVal) -> {
	        if (rbLocale.isSelected()) {
	            int nouvelleTailleImagette = newVal.intValue();
	            sliderTaillePatch.setMax(nouvelleTailleImagette);
	            if (sliderTaillePatch.getValue() > nouvelleTailleImagette) {
	                sliderTaillePatch.setValue(nouvelleTailleImagette);
	            }
	        }
	    });

	    // ➕ Réactivation de la contrainte quand on passe en "locale"
	    rbLocale.setOnAction(e -> {
	        boxTailleImagette.setVisible(true);
	        int tailleImagette = (int) sliderTailleImagette.getValue();
	        sliderTaillePatch.setMax(tailleImagette);
	        if (sliderTaillePatch.getValue() > tailleImagette) {
	            sliderTaillePatch.setValue(tailleImagette);
	        }
	    });

	    rbGlobal.setOnAction(e -> {
	        boxTailleImagette.setVisible(false);
	        sliderTaillePatch.setMax(25); // valeur maximale par défaut
	    });

	    // ➤ Action bouton débruiter
	    btnDebruiter.setOnAction(arg0 -> {
	        boolean locale = rbLocale.isSelected();
	        int tailleImagette = (int) sliderTailleImagette.getValue();
	        int taillePatch = (int) sliderTaillePatch.getValue();

	        if (locale) {
	            int largeur = (int) album.getPhotoCourante().getImage().getWidth();
	            int hauteur = (int) album.getPhotoCourante().getImage().getHeight();
	            if (tailleImagette > largeur || tailleImagette > hauteur) {
	                labelEvaluateur.setText("Erreur : taille imagette > taille image");
	                return;
	            }
	        }

	        imageView.setImage(album.getPhotoCourante().debruiter(taillePatch, typeSeuillage, locale, tailleImagette));

	        Image originale = album.getPhotoCourante().getImageOriginelleGrisee();
>>>>>>> Stashed changes
	        Image actuelle = album.getPhotoCourante().getImage();
	        EvaluateurQualiteImage evaluateur = new EvaluateurQualiteImage(originale, actuelle);
<<<<<<< Updated upstream
	        //evaluateur.resultatsQualite();
	        Double mseEval = (Double)evaluateur.calculerMSE();
	        Double psnrEval = (Double)evaluateur.calculerPSNR();
	        labelEvaluateur.setText("MSE : " + mseEval.toString() + "\nPSNR : " + psnrEval.toString() + " dB");
	        
			
		});
		choixTaillePatch.getChildren().addAll(new Label("Taille patch"),sliderTaillePatch);
		
		ToggleButton btnSeuillageDur = new ToggleButton("Dur");
		ToggleButton btnSeuillageDoux = new ToggleButton("Doux");
		ToggleButton btnSeuillageAuto = new ToggleButton("Auto");
		btnSeuillageAuto.setSelected(true);
		ToggleGroup btnSeuillage = new ToggleGroup();
		btnSeuillageDur.setToggleGroup(btnSeuillage);
		btnSeuillageDoux.setToggleGroup(btnSeuillage);
		btnSeuillageAuto.setToggleGroup(btnSeuillage);
		btnSeuillageDur.setOnAction(arg0 ->  typeSeuillage = ImageDebruitee.TypeSeuillage.DUR);
		btnSeuillageDoux.setOnAction(arg0 ->  typeSeuillage = ImageDebruitee.TypeSeuillage.DOUX);
		btnSeuillageAuto.setOnAction(arg0 ->  typeSeuillage = ImageDebruitee.TypeSeuillage.AUTO);
		VBox hBoxSeuillage = new VBox();
		hBoxSeuillage.getChildren().addAll(new Label("Seuillage"),btnSeuillageDur, btnSeuillageDoux, btnSeuillageAuto);
		
		ToggleButton btnSeuilBayes = new ToggleButton("Bayes");
		btnSeuilBayes.setSelected(true);
		ToggleButton btnSeuilVisu = new ToggleButton("Visu");
		ToggleGroup btnSeuil = new ToggleGroup();
		btnSeuilBayes.setToggleGroup(btnSeuil);
		btnSeuilVisu.setToggleGroup(btnSeuil);
		btnSeuilBayes.setOnAction(arg0 ->  typeSeuil = ImageDebruitee.TypeSeuil.BAYES);
		btnSeuilVisu.setOnAction(arg0 ->  typeSeuil = ImageDebruitee.TypeSeuil.VISU);
		VBox vboxTypeSeuil = new VBox();
		vboxTypeSeuil.getChildren().addAll(new Label("Seuil"),btnSeuilBayes,btnSeuilVisu);
		vboxTypeSeuil.setAlignment(Pos.CENTER);
		debruiter.getChildren().addAll(choixTaillePatch, hBoxSeuillage,vboxTypeSeuil, btnDebruiter);
		
		// Bouton évaluation qualité
=======
	        Double mseEval = evaluateur.calculerMSE();
	        Double psnrEval = evaluateur.calculerPSNR();
	        labelEvaluateur.setText("MSE : " + mseEval + "\nPSNR : " + psnrEval + " dB");
	    });
>>>>>>> Stashed changes

	    debruiter.getChildren().addAll(choixTaillePatch, hBoxSeuillage, modeExtractionBox, boxTailleImagette, btnDebruiter);

	    // Évaluation
	    VBox vboxEvaluerQualite = new VBox();
	    vboxEvaluerQualite.getChildren().addAll(new Label("Évaluateur"), labelEvaluateur);

	    // Séparateurs
	    Separator sep1 = new Separator(Orientation.VERTICAL);
	    Separator sep2 = new Separator(Orientation.VERTICAL);
	    Separator sep3 = new Separator(Orientation.VERTICAL);

	    bandeauHaut.getChildren().addAll(bruiter, sep1, debruiter, sep2, btnReset, sep3, vboxEvaluerQualite);
	    bandeauHaut.setAlignment(Pos.CENTER);

	    return bandeauHaut;
	}


	
	public MenuBar creerMenu() {
		MenuBar menuBar = new MenuBar();
		Menu fichier = new Menu("Fichier");
		MenuItem ajoutPhoto = new MenuItem("Ajouter une photo");
		MenuItem quitter = new MenuItem("Quitter");
		fichier.getItems().addAll(ajoutPhoto, new SeparatorMenuItem(),quitter);
		menuBar.getMenus().add(fichier);
		
		ajoutPhoto.setOnAction(arg0 -> {
			 FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Open Resource File");
			 fileChooser.getExtensionFilters().addAll(
					 new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			 File selectedFile = fileChooser.showOpenDialog(primaryStage);
			 if (selectedFile != null) {
				 album.addPhoto(selectedFile.toURI().toString());
				 
				 creerListe = creerListe();
				window.setLeft(creerListe);
			 }
		});
		
		quitter.setOnAction(arg0 -> {
			System.exit(0);
		});
		return menuBar;
	}
	

}
