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
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import image.Album;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Visionneuse extends Application {

	private Album album;
	private ImageView imageView;
	private Slider slider;
	private ListView<String> creerListe;
	private List<RadioButton> listButton;
	private Stage primaryStage;
	private BorderPane window;
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Album Photo");
	

		album = new Album("/home/etienne/CyTech/Projet_java/Projet-Fin-Ing1-GI-GM/images");
		 
		VBox vbox = new VBox();
		
		window = new BorderPane();
		
		ScrollPane centre = creerCentre();
		window.setCenter(centre);
		slider = creerSlider();
		window.setRight(slider);
		creerListe = creerListe();
		window.setLeft(creerListe);
		
		HBox bandeauBas = creerBandeauBas();
		window.setBottom(bandeauBas);
		
		HBox bandeauHaut = creerBandeauHaut();
		window.setTop(bandeauHaut);
		
		
		
		MenuBar menuBar = creerMenu();
		
		vbox.getChildren().addAll(menuBar,window);
		
		
		Scene scene = new Scene(vbox);
		scene.getStylesheets().add("style.css");
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
			 listButton.get(listView.getSelectionModel().getSelectedIndex()).setSelected(true);
		 });
		 return listView;
	}
	
	public HBox creerBandeauBas() {
		ToggleGroup toggleGroup = new ToggleGroup();
		listButton = new ArrayList<RadioButton>();
		HBox p = new HBox();
		p.setAlignment(Pos.CENTER);
		RadioButton b;
		for (Integer i = 0; i < album.getSize(); i++) {
			b = new RadioButton();
			b.setToggleGroup(toggleGroup);
			b.setGraphic(new ImageView(album.getPhoto(i).getIcone()));;
			b.getStyleClass().add("custom-radio");
			listButton.add(b);
			b.setOnAction(arg0 -> {
				
				album.setIndexCourant(listButton.indexOf(arg0.getSource()));
				imageView.setImage(album.getPhotoCourante().getImage());
				slider.setValue(album.getPhotoCourante().getZoom());
				creerListe.getSelectionModel().selectIndices(listButton.indexOf(arg0.getSource()));
			});
			
			p.getChildren().add(b);
		}
		return p;
			
	}
		
	public HBox creerBandeauHaut() {
		HBox bandeauHaut = new HBox();
		
		Button btnBruiter = new Button("Bruiter");
		Button btnDebruiter = new Button("Débruiter");
		Button btnReset = new Button("Reset");
		btnReset.setOnAction(arg0 -> imageView.setImage(album.getPhotoCourante().reset()));
		HBox bruiter = new HBox();
		HBox debruiter = new HBox();
		bruiter.setAlignment(Pos.BASELINE_LEFT);
		bandeauHaut.setAlignment(Pos.CENTER);
		
		VBox bruitage = new VBox();
		bruitage.setAlignment(Pos.CENTER);
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
		
		btnDebruiter.setOnAction(arg0 -> {
			//imageView.setImage(album.getPhotoCourante().debruiter());
			
		});
		
		bruiter.getChildren().addAll(bruitage,btnBruiter);
		debruiter.getChildren().addAll(btnDebruiter);
		bandeauHaut.getChildren().addAll(bruiter,debruiter,btnReset);
		
		
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
				 window.setBottom(creerBandeauBas());
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
