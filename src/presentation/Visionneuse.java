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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
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
 * @author Etienne Angé
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
	private ImageDebruitee.TypeSeuil typeSeuil = ImageDebruitee.TypeSeuil.VISU;
	private ImageDebruitee.Analyse analyse = ImageDebruitee.Analyse.GLOBALE;
	private ObservableList<EvaluateurQualiteImage> historique =  FXCollections.observableArrayList();;
	private double bruitageValue;
	
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Débruiteur d'image");
	
		
		album = new Album("images");
		 
		VBox vbox = new VBox();
		
		window = new BorderPane();
		
		ScrollPane centre = creerCentre();
		window.setCenter(centre);
		//slider = creerSlider();
		//window.setRight(slider);
		creerListe = creerListe();
		window.setLeft(creerListe);
		
		BorderPane creerHistorique = creerHistorique();
		window.setBottom(creerHistorique);
		
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
			 //slider.setValue(album.getPhotoCourante().getZoom());
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
		
		
		
		//bruiter
		HBox bruiter = new HBox(20);
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
		
		Label valeurBruitage = new Label(String.format("%.0f", sliderBruitage.getValue())); // afficher valeur

		// Lier dynamiquement la valeur du slider au label
		sliderBruitage.valueProperty().addListener((obs, oldVal, newVal) -> {
			int intValue = (int)newVal.doubleValue();
			valeurBruitage.setText(String.format("%.0f", (double)intValue));
		});
		
		bruitage.getChildren().addAll(textBruitage, new HBox(sliderBruitage, valeurBruitage));
		
		
		btnBruiter.setOnAction(arg0 -> {
			bruitageValue = sliderBruitage.getValue();
			imageView.setImage(album.getPhotoCourante().bruiter(bruitageValue));
			labelEvaluateur.setText("Pas d'image débruitée");
			
		});
		bruiter.getChildren().addAll(bruitage,btnBruiter);
		
		
		
		// débruiter
		
		ToggleButton btnGlobale = new ToggleButton("Globale");
		ToggleButton btnLocale = new ToggleButton("Locale");
		btnGlobale.setOnAction(arg0 -> analyse = ImageDebruitee.Analyse.GLOBALE);
		btnLocale.setOnAction(arg0 -> analyse = ImageDebruitee.Analyse.LOCALE);
		btnGlobale.setSelected(true);
		ToggleGroup groupeLocale = new ToggleGroup();
		btnGlobale.setToggleGroup(groupeLocale);
		btnLocale.setToggleGroup(groupeLocale);
		VBox vboxLocale= new VBox(new Label("Analyse"),btnGlobale,btnLocale);
		vboxLocale.setAlignment(Pos.CENTER);
		HBox debruiter = new HBox(20);
		debruiter.setAlignment(Pos.CENTER);
		
		VBox choixTaillePatch = new VBox();
		
		Slider sliderTaillePatch = new Slider(0,25,5);
		
		
		
		sliderTaillePatch.setOrientation(Orientation.HORIZONTAL);
		sliderTaillePatch.setShowTickMarks(true);
		sliderTaillePatch.setShowTickLabels(true);
		sliderTaillePatch.setMajorTickUnit(10);
		
		Label valeurTaillePatch = new Label(String.format("%.0f", sliderTaillePatch.getValue())); // afficher valeur

		// Lier dynamiquement la valeur du slider au label
		sliderTaillePatch.valueProperty().addListener((obs, oldVal, newVal) -> {
			int intValue = (int)newVal.doubleValue();
			valeurTaillePatch.setText(String.format("%.0f", (double)intValue));
		});
		
		
		btnDebruiter.setOnAction(arg0 -> {
			imageView.setImage(album.getPhotoCourante().debruiter(sliderTaillePatch.getValue(), typeSeuillage, typeSeuil, analyse));
			Image originale = album.getPhotoCourante().getImageOriginelleGrisee();
	        Image actuelle = album.getPhotoCourante().getImage();
	        
	        EvaluateurQualiteImage evaluateur = new EvaluateurQualiteImage(originale, actuelle,(double)((int)bruitageValue), (int)sliderTaillePatch.getValue(),  typeSeuillage.toString(), typeSeuil.toString(), analyse.toString());
	        historique.add(evaluateur);
	        Double mseEval = (Double)evaluateur.calculerMSE();
	        Double psnrEval = (Double)evaluateur.calculerPSNR();
	        labelEvaluateur.setText("MSE : " + mseEval.toString() + "\nPSNR : " + psnrEval.toString() + " dB");
	        
			
		});
		choixTaillePatch.getChildren().addAll(new Label("Taille patch"),new HBox(sliderTaillePatch,valeurTaillePatch));
		
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
		
		ToggleButton btnSeuilVisu = new ToggleButton("Visu");
		btnSeuilVisu.setSelected(true);
		ToggleGroup btnSeuil = new ToggleGroup();
		btnSeuilBayes.setToggleGroup(btnSeuil);
		btnSeuilVisu.setToggleGroup(btnSeuil);
		btnSeuilBayes.setOnAction(arg0 ->  typeSeuil = ImageDebruitee.TypeSeuil.BAYES);
		btnSeuilVisu.setOnAction(arg0 ->  typeSeuil = ImageDebruitee.TypeSeuil.VISU);
		VBox vboxTypeSeuil = new VBox();
		vboxTypeSeuil.getChildren().addAll(new Label("Seuil"),btnSeuilBayes,btnSeuilVisu);
		vboxTypeSeuil.setAlignment(Pos.CENTER);
		debruiter.getChildren().addAll(choixTaillePatch, hBoxSeuillage,vboxTypeSeuil,vboxLocale, btnDebruiter);
		
		
	    
	    

	    
		Separator sep1 = new Separator();
		sep1.setOrientation(Orientation.VERTICAL);
		Separator sep2 = new Separator();
		sep2.setOrientation(Orientation.VERTICAL);
		Separator sep3 = new Separator();
		sep3.setOrientation(Orientation.VERTICAL);
		bandeauHaut.getChildren().addAll(bruiter,sep1,debruiter,sep2,btnReset);
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
	
	
	public BorderPane creerHistorique() {
		
		// Historique
		TableView<EvaluateurQualiteImage> table = new TableView();
		table.setItems(historique);
		
		
		
		TableColumn<EvaluateurQualiteImage,Double> bruitageValueCol = new TableColumn<EvaluateurQualiteImage,Double>("Bruitage");
		bruitageValueCol.setCellValueFactory(new PropertyValueFactory<>("bruitageValue"));
		
		TableColumn<EvaluateurQualiteImage,Double> mseCol = new TableColumn<EvaluateurQualiteImage,Double>("MSE");
		mseCol.setCellValueFactory(new PropertyValueFactory<>("mse"));
		
		TableColumn<EvaluateurQualiteImage,Double> psnrCol = new TableColumn<EvaluateurQualiteImage,Double>("PSNR");
		psnrCol.setCellValueFactory(new PropertyValueFactory<>("psnr"));
		
		TableColumn<EvaluateurQualiteImage, Integer> taillePatchCol = new TableColumn<>("Taille Patch");
		taillePatchCol.setCellValueFactory(new PropertyValueFactory<>("taillePatch"));

		TableColumn<EvaluateurQualiteImage, String> seuillageCol = new TableColumn<>("Seuillage");
		seuillageCol.setCellValueFactory(new PropertyValueFactory<>("seuillage"));

		TableColumn<EvaluateurQualiteImage, String> seuilCol = new TableColumn<>("Seuil");
		seuilCol.setCellValueFactory(new PropertyValueFactory<>("seuil"));

		TableColumn<EvaluateurQualiteImage, String> analyseCol = new TableColumn<>("Analyse");
		analyseCol.setCellValueFactory(new PropertyValueFactory<>("analyse"));

		
		table.getColumns().addAll(bruitageValueCol,taillePatchCol, seuillageCol, seuilCol, analyseCol,mseCol,psnrCol);
		
		
		
		table.setMaxHeight(200);
		table.setPlaceholder(new Label("Aucune donnée disponible"));
		BorderPane bpHistorique = new BorderPane(table);
		return bpHistorique;
	}

}


