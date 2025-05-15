# Projet-Fin-Ing1-GI-GM

## Configuration
Importer le projet avec un IDE java et ajouter la librairie ``javafx`` au modulepath.

##Telechargement pour faire fonctionner partie maths

1.Telechargement de Apache Commons Math

C’est la bibliothèque qui contient RealMatrix, EigenDecomposition, etc.

Télécharge ici :
https://repo1.maven.org/maven2/org/apache/commons/commons-math3/3.6.1/commons-math3-3.6.1.jar

Télécharger JavaFX

    Va sur : https://gluonhq.com/products/javafx/

    Télécharge JavaFX SDK correspondant à ton OS (Linux, Windows, Mac).

    Décompresse l’archive, tu obtiendras un dossier comme javafx-sdk-21/

Enregistre-le dans un dossier local, par exemple lib/.
Structure du dossier

mon-projet/
├── src/
│   └── image/
│       ├── App.java
│       ├── Photo.java
│       ├── ImageBruitee.java
│       └── ImageDebruitee.java
├── lib/
│   ├── commons-math3-3.6.1.jar
│   └── javafx-sdk-XX.X.X/ (le dossier SDK JavaFX)
└── bin/ (sera généré)

Compilation

Taper: (peut etre faut modifier la version de javafx dans la commande)


mkdir -p bin

javac \
  --module-path "$PWD/lib/javafx-sdk-24.0.1/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "lib/commons-math3-3.6.1.jar" \
  -d bin \
  src/image/*.java


 Exécuter ton programme

java \
  --module-path lib/javafx-sdk-21/lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "bin:lib/commons-math3-3.6.1.jar" \
  image.App 
  
Ou image.App est la callse principal dans App y'a lemain
Puis : 

jar --create --file app.jar --main-class image.App -C bin .

Et enfin,
java \
  --module-path lib/javafx-sdk-21/lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "app.jar:lib/commons-math3-3.6.1.jar" \
  image.App

