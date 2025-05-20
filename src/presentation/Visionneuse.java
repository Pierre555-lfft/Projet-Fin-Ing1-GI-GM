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
	public ImageDebruitee.TypeSeuillage typeSeuillage = ImageDebruitee.TypeSeuillage.DOUX;
	private Album album;
	private ImageView imageView;
	private Slider slider;
	private ListView<String> creerListe;
	private List<RadioButton> listButton;
	private Stage primaryStage;
	private BorderPane window;
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Album Photo");
	

		album = new Album("images");
		 
		VBox vbox = new VBox();
		
		window = new BorderPane();
		
		ScrollPane centre = creerCentre();
		window.setCenter(centre);
		slider = creerSlider();
		window.setRight(slider);
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

		
		
		HBox bandeauHaut = new HBox();
		
		Button btnReset = new Button("Reset");
		btnReset.setOnAction(arg0 -> imageView.setImage(album.getPhotoCourante().reset()));
		
		
		//bruiter
		HBox bruiter = new HBox();
		bruiter.setAlignment(Pos.CENTER);
	
		Button btnBruiter = new Button("Bruiter");
		
		
		VBox bruitage = new VBox();
		Button btnDebruiter = new Button("Débruiter");
		
		
		Label textBruitage = new Label("Bruitage");
		Slider sliderBruitage = new Slider(0,100,50);

		sliderBruitage.setOrientation(Orientation.HORIZONTAL);
		sliderBruitage.setShowTickMarks(true);
		sliderBruitage.setShowTickLabels(true);
		sliderBruitage.setMajorTickUnit(10);
		
		
		bruitage.getChildren().addAll(textBruitage, sliderBruitage);
		
		
		btnBruiter.setOnAction(arg0 -> {
			
			imageView.setImage(album.getPhotoCourante().bruiter(sliderBruitage.getValue()));
			
			
		});
		bruiter.getChildren().addAll(bruitage,btnBruiter);
		
		
		
		// débruiter
		HBox debruiter = new HBox();
		debruiter.setAlignment(Pos.CENTER);
		
		VBox choixTaillePatch = new VBox();
		
		Slider sliderTaillePatch = new Slider(0,25,5);

		sliderTaillePatch.setOrientation(Orientation.HORIZONTAL);
		sliderTaillePatch.setShowTickMarks(true);
		sliderTaillePatch.setShowTickLabels(true);
		sliderTaillePatch.setMajorTickUnit(10);
		btnDebruiter.setOnAction(arg0 -> {
			imageView.setImage(album.getPhotoCourante().debruiter(sliderTaillePatch.getValue(), typeSeuillage));
			
		});
		choixTaillePatch.getChildren().addAll(new Label("Taille patch"),sliderTaillePatch);
		
		ToggleButton btnSeuillageDur = new ToggleButton("Dur");
		ToggleButton btnSeuillageDoux = new ToggleButton("Doux");
		ToggleButton btnSeuillageAuto = new ToggleButton("Auto");
		
		ToggleGroup btnSeuillage = new ToggleGroup();
		btnSeuillageDur.setToggleGroup(btnSeuillage);
		btnSeuillageDoux.setToggleGroup(btnSeuillage);
		btnSeuillageAuto.setToggleGroup(btnSeuillage);
		btnSeuillageDur.setOnAction(arg0 ->  typeSeuillage = ImageDebruitee.TypeSeuillage.DUR);
		btnSeuillageDoux.setOnAction(arg0 ->  typeSeuillage = ImageDebruitee.TypeSeuillage.DOUX);
		btnSeuillageAuto.setOnAction(arg0 ->  typeSeuillage = ImageDebruitee.TypeSeuillage.AUTO);
		VBox hBoxSeuillage = new VBox();
		hBoxSeuillage.getChildren().addAll(new Label("Seuillage"),btnSeuillageDur, btnSeuillageDoux, btnSeuillageAuto);
		debruiter.getChildren().addAll(choixTaillePatch, hBoxSeuillage, btnDebruiter);
		
		// Bouton évaluation qualité
	    Button btnEvaluerQualite = new Button("Évaluer qualité");
	    btnEvaluerQualite.setOnAction(arg0 -> {
	        Image originale = album.getPhotoCourante().getImageOriginelleGrisee();
	        Image actuelle = album.getPhotoCourante().getImage();
	        
	        EvaluateurQualiteImage evaluateur = new EvaluateurQualiteImage(originale, actuelle);
	        evaluateur.resultatsQualite();
	    });
	    
		Separator sep1 = new Separator();
		sep1.setOrientation(Orientation.VERTICAL);
		Separator sep2 = new Separator();
		sep2.setOrientation(Orientation.VERTICAL);
		bandeauHaut.getChildren().addAll(bruiter,sep1,debruiter,sep2,btnReset,btnEvaluerQualite);
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
