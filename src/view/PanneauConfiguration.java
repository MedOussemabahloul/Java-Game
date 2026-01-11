package view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.entites.Intrus;
import model.entites.Robot;
import model.entites.SacArgent;
import utils.Position;

/**
 * Panneau de configuration pour initialiser une partie.
 * Permet de définir la grille, placer les obstacles, sacs, sorties et entités.
 */
public class PanneauConfiguration extends VBox {

    // Configuration en cours
    private Configuration config;

    // Callback quand configuration terminée
    private Consumer<Configuration> onConfigurationTerminee;

    // Composants UI - Étape 1 : Dimensions
    private TextField txtNbLignes;
    private TextField txtNbColonnes;
    private CheckBox chkAleatoireObstacles;
    private CheckBox chkAleatoireSorties;
    private CheckBox chkAleatoireRobots;
    private CheckBox chkAleatoireIntrus;
    private CheckBox chkAleatoireSacs;
    private Button btnValiderDimensions;

    // Composants UI - Étape 2 : Placement éléments
    private GridPane grillePreview;
    private ComboBox<String> cmbModeePlacement;
    private Label lblCompteur;
    private Button btnTerminerPlacement;

    // Composants UI - Étape 3 : Robots
    private Spinner<Integer> spinNbRobots;
    private Label lblRobotsPlaces;
    private Button btnValiderRobots;

    // Composants UI - Étape 4 : Intrus
    private Spinner<Integer> spinNbIntrus;
    private Label lblIntrusPlaces;
    private Button btnDemarrer;

    // Étapes
    private VBox etape1, etape2, etape3, etape4;
    private int etapeActuelle = 1;

    // Mode de placement actuel
    private enum ModePlacement {
        OBSTACLE, SAC, SORTIE, ROBOT, INTRUS
    }
    private ModePlacement modePlacement = ModePlacement.OBSTACLE;

    // Taille des cellules dans la preview
    private static final int TAILLE_CELLULE_PREVIEW = 30;

    /**
     * Constructeur.
     */
    public PanneauConfiguration() {
        config = new Configuration();

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f5f5f5;");

        construireInterface();
        afficherEtape1();
    }

    /**
     * Construit l'interface de configuration.
     */
    private void construireInterface() {
        // Titre principal
        Text titre = new Text("Configuration - Parametrage de la Partie");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        getChildren().add(titre);

        // Creer les 4 etapes
        etape1 = creerEtape1();
        etape2 = creerEtape2();
        etape3 = creerEtape3();
        etape4 = creerEtape4();
    }

    /**
     * Étape 1 : Saisir les dimensions.
     */
    private VBox creerEtape1() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-border-color: #2196f3; -fx-border-width: 2;");

        Text titre = new Text("Etape 1 - Dimensions et Generation Aleatoire");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox ligneN = new HBox(10);
        ligneN.setAlignment(Pos.CENTER);
        Label lblN = new Label("Nombre de lignes (N) :");
        txtNbLignes = new TextField("10");
        txtNbLignes.setPrefWidth(80);
        ligneN.getChildren().addAll(lblN, txtNbLignes);

        HBox ligneM = new HBox(10);
        ligneM.setAlignment(Pos.CENTER);
        Label lblM = new Label("Nombre de colonnes (M) :");
        txtNbColonnes = new TextField("10");
        txtNbColonnes.setPrefWidth(80);
        ligneM.getChildren().addAll(lblM, txtNbColonnes);

        // Checkboxes pour aléatoire par type
        Text titreAlea = new Text("Generation aleatoire pour :");
        titreAlea.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        chkAleatoireObstacles = new CheckBox("Obstacles");
        chkAleatoireObstacles.setSelected(true);

        chkAleatoireSorties = new CheckBox("Sorties");
        chkAleatoireSorties.setSelected(true);

        chkAleatoireRobots = new CheckBox("Robots");
        chkAleatoireRobots.setSelected(true);

        chkAleatoireIntrus = new CheckBox("Intrus");
        chkAleatoireIntrus.setSelected(true);

        chkAleatoireSacs = new CheckBox("Sacs d'Argent");
        chkAleatoireSacs.setSelected(true);

        VBox vboxCheckboxes = new VBox(8);
        vboxCheckboxes.setPadding(new Insets(10));
        vboxCheckboxes.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5;");
        vboxCheckboxes.getChildren().addAll(
            titreAlea,
            chkAleatoireObstacles,
            chkAleatoireSorties,
            chkAleatoireRobots,
            chkAleatoireIntrus,
            chkAleatoireSacs
        );

        btnValiderDimensions = new Button("✅ Valider les Dimensions");
        btnValiderDimensions.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        btnValiderDimensions.setOnAction(e -> validerDimensions());

        box.getChildren().addAll(titre, ligneN, ligneM, vboxCheckboxes, btnValiderDimensions);
        return box;
    }

    /**
     * Étape 2 : Placer obstacles, sacs, sorties.
     */
    private VBox creerEtape2() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-border-color: #2196f3; -fx-border-width: 2;");

        Text titre = new Text("Etape 2 - Placer les Elements");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Sélecteur de mode
        HBox ligneMode = new HBox(10);
        ligneMode.setAlignment(Pos.CENTER);
        Label lblMode = new Label("Mode de placement :");
        cmbModeePlacement = new ComboBox<>();
        cmbModeePlacement.getItems().addAll("🧱 Obstacles", "💰 Sacs d'Argent", "🚪 Sorties");
        cmbModeePlacement.setValue("🧱 Obstacles");
        cmbModeePlacement.setOnAction(e -> changerModePlacement());
        ligneMode.getChildren().addAll(lblMode, cmbModeePlacement);

        // Compteur
        lblCompteur = new Label("Cliquez sur la grille pour placer");
        lblCompteur.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        // Grille de preview (sera créée après validation dimensions)
        grillePreview = new GridPane();
        grillePreview.setHgap(2);
        grillePreview.setVgap(2);
        grillePreview.setAlignment(Pos.CENTER);
        grillePreview.setStyle("-fx-background-color: #ccc; -fx-padding: 10;");

        ScrollPane scroll = new ScrollPane(grillePreview);
        scroll.setMaxHeight(400);
        scroll.setFitToWidth(true);

        btnTerminerPlacement = new Button("✅ Terminer le Placement");
        btnTerminerPlacement.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        btnTerminerPlacement.setOnAction(e -> terminerPlacement());

        box.getChildren().addAll(titre, ligneMode, lblCompteur, scroll, btnTerminerPlacement);
        return box;
    }

    /**
     * Étape 3 : Placer les robots.
     */
    private VBox creerEtape3() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-border-color: #2196f3; -fx-border-width: 2;");

        Text titre = new Text("Etape 3 - Joueur 1 - Placement des Robots");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox ligneNb = new HBox(10);
        ligneNb.setAlignment(Pos.CENTER);
        Label lblNb = new Label("Nombre de robots (max 4) :");
        spinNbRobots = new Spinner<>(1, 4, 2);
        spinNbRobots.setPrefWidth(80);
        spinNbRobots.valueProperty().addListener((obs, old, newVal) -> resetRobots());
        ligneNb.getChildren().addAll(lblNb, spinNbRobots);

        lblRobotsPlaces = new Label("Robots placés : 0 / 2");
        lblRobotsPlaces.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label instruction = new Label("Cliquez sur la grille pour placer vos robots");
        instruction.setWrapText(true);
        instruction.setMaxWidth(400);
        instruction.setAlignment(Pos.CENTER);

        btnValiderRobots = new Button("✅ Valider les Robots");
        btnValiderRobots.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        btnValiderRobots.setDisable(true);
        btnValiderRobots.setOnAction(e -> validerRobots());

        box.getChildren().addAll(titre, ligneNb, lblRobotsPlaces, instruction, btnValiderRobots);
        return box;
    }

    /**
     * Étape 4 : Placer les intrus.
     */
    private VBox creerEtape4() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-border-color: #2196f3; -fx-border-width: 2;");

        Text titre = new Text("Etape 4 - Joueur 2 - Placement des Intrus");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox ligneNb = new HBox(10);
        ligneNb.setAlignment(Pos.CENTER);
        Label lblNb = new Label("Nombre d'intrus (max 4) :");
        spinNbIntrus = new Spinner<>(1, 4, 2);
        spinNbIntrus.setPrefWidth(80);
        spinNbIntrus.valueProperty().addListener((obs, old, newVal) -> resetIntrus());
        ligneNb.getChildren().addAll(lblNb, spinNbIntrus);

        lblIntrusPlaces = new Label("Intrus placés : 0 / 2");
        lblIntrusPlaces.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label instruction = new Label("Cliquez sur la grille pour placer vos intrus");
        instruction.setWrapText(true);
        instruction.setMaxWidth(400);
        instruction.setAlignment(Pos.CENTER);

        btnDemarrer = new Button("🎮 DÉMARRER LA PARTIE");
        btnDemarrer.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
        btnDemarrer.setDisable(true);
        btnDemarrer.setOnAction(e -> demarrerPartie());

        box.getChildren().addAll(titre, ligneNb, lblIntrusPlaces, instruction, btnDemarrer);
        return box;
    }

    /**
     * Affiche l'étape 1.
     */
    private void afficherEtape1() {
        getChildren().clear();
        Text titre = new Text("Configuration - Parametrage de la Partie");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        getChildren().addAll(titre, etape1);
        etapeActuelle = 1;
    }

    /**
     * Valide les dimensions et passe à l'étape 2.
     */
    private void validerDimensions() {
        try {
            int n = Integer.parseInt(txtNbLignes.getText());
            int m = Integer.parseInt(txtNbColonnes.getText());

            if (n < 5 || n > 20 || m < 5 || m > 20) {
                afficherErreur("Les dimensions doivent être entre 5 et 20 !");
                return;
            }

            config.setNbLignes(n);
            config.setNbColonnes(m);
            
            // Vérifier si au moins un élément est en mode aléatoire
            boolean alaeatoireActif = chkAleatoireObstacles.isSelected() ||
                                     chkAleatoireSorties.isSelected() ||
                                     chkAleatoireRobots.isSelected() ||
                                     chkAleatoireIntrus.isSelected() ||
                                     chkAleatoireSacs.isSelected();
            
            config.setModeAleatoireObstacles(chkAleatoireObstacles.isSelected());
            config.setModeAleatoireSorties(chkAleatoireSorties.isSelected());
            config.setModeAleatoireRobots(chkAleatoireRobots.isSelected());
            config.setModeAleatoireIntrus(chkAleatoireIntrus.isSelected());
            config.setModeAleatoireSacs(chkAleatoireSacs.isSelected());
            
            // Si tout est en mode aléatoire, aller directement au jeu
            if (alaeatoireActif && chkAleatoireObstacles.isSelected() && chkAleatoireSorties.isSelected() &&
                chkAleatoireRobots.isSelected() && chkAleatoireIntrus.isSelected() && chkAleatoireSacs.isSelected()) {
                // Tout aléatoire : pas besoin des étapes manuelles, aller au jeu
                if (onConfigurationTerminee != null) {
                    onConfigurationTerminee.accept(config);
                }
            } else {
                // Sinon : afficher les étapes manuelles pour ce qui n'est pas aléatoire
                creerGrillePreview();
                afficherEtape2();
            }

        } catch (NumberFormatException e) {
            afficherErreur("Veuillez entrer des nombres valides !");
        }
    }

    /**
     * Crée la grille de preview interactive.
     */
    private void creerGrillePreview() {
        grillePreview.getChildren().clear();

        for (int i = 0; i < config.getNbLignes(); i++) {
            for (int j = 0; j < config.getNbColonnes(); j++) {
                Position pos = new Position(i, j);
                StackPane cellule = creerCellulePreview(pos);

                int x = i, y = j;
                cellule.setOnMouseClicked(e -> gererClicCellule(x, y));

                grillePreview.add(cellule, j, i);
            }
        }
    }

    /**
     * Crée une cellule de preview.
     */
    private StackPane creerCellulePreview(Position pos) {
        StackPane cellule = new StackPane();
        cellule.setPrefSize(TAILLE_CELLULE_PREVIEW, TAILLE_CELLULE_PREVIEW);

        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(
                TAILLE_CELLULE_PREVIEW, TAILLE_CELLULE_PREVIEW
        );
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.GRAY);

        cellule.getChildren().add(rect);
        cellule.setUserData(pos);

        return cellule;
    }

    /**
     * Gère le clic sur une cellule.
     */
    private void gererClicCellule(int x, int y) {
        Position pos = new Position(x, y);
        StackPane cellule = getCellule(x, y);
        javafx.scene.shape.Rectangle rect = (javafx.scene.shape.Rectangle) cellule.getChildren().get(0);

        if (modePlacement == ModePlacement.OBSTACLE) {
            if (config.getObstacles().contains(pos)) {
                config.getObstacles().remove(pos);
                rect.setFill(Color.WHITE);
            } else {
                config.getObstacles().add(pos);
                rect.setFill(Color.web("#424242"));
            }
        } else if (modePlacement == ModePlacement.SAC) {
            boolean existe = config.getSacs().removeIf(s -> s.getPosition().equals(pos));
            if (!existe) {
                config.getSacs().add(new SacArgent(config.getSacs().size() + 1, pos));
                rect.setFill(Color.web("#ffc107"));
            } else {
                rect.setFill(Color.WHITE);
            }
        } else if (modePlacement == ModePlacement.SORTIE) {
            // Sortie uniquement sur les bords
            if (estSurBord(pos)) {
                if (config.getSorties().contains(pos)) {
                    config.getSorties().remove(pos);
                    rect.setFill(Color.WHITE);
                } else {
                    config.getSorties().add(pos);
                    rect.setFill(Color.web("#81c784"));
                }
            } else {
                afficherErreur("Les sorties doivent être sur les bords !");
            }
        } else if (modePlacement == ModePlacement.ROBOT) {
            if (!positionOccupee(pos) && config.getRobots().size() < spinNbRobots.getValue()) {
                Robot robot = new Robot(config.getRobots().size() + 1, pos);
                config.getRobots().add(robot);
                rect.setFill(Color.web("#2196f3"));

                Text texte = new Text("R" + robot.getId());
                texte.setFill(Color.WHITE);
                texte.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                if (cellule.getChildren().size() > 1) cellule.getChildren().remove(1);
                cellule.getChildren().add(texte);

                lblRobotsPlaces.setText("Robots placés : " + config.getRobots().size() + " / " + spinNbRobots.getValue());

                if (config.getRobots().size() == spinNbRobots.getValue()) {
                    btnValiderRobots.setDisable(false);
                }
            }
        } else if (modePlacement == ModePlacement.INTRUS) {
            if (!positionOccupee(pos) && config.getIntrus().size() < spinNbIntrus.getValue()) {
                Intrus intrus = new Intrus(config.getIntrus().size() + 1, pos);
                config.getIntrus().add(intrus);
                rect.setFill(Color.web("#f44336"));

                Text texte = new Text("I" + intrus.getId());
                texte.setFill(Color.WHITE);
                texte.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                if (cellule.getChildren().size() > 1) cellule.getChildren().remove(1);
                cellule.getChildren().add(texte);

                lblIntrusPlaces.setText("Intrus placés : " + config.getIntrus().size() + " / " + spinNbIntrus.getValue());

                if (config.getIntrus().size() == spinNbIntrus.getValue()) {
                    btnDemarrer.setDisable(false);
                }
            }
        }

        mettreAJourCompteur();
    }

    /**
     * Retourne la cellule à la position donnée.
     */
    private StackPane getCellule(int x, int y) {
        for (javafx.scene.Node node : grillePreview.getChildren()) {
            if (GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                return (StackPane) node;
            }
        }
        return null;
    }

    /**
     * Vérifie si position est sur le bord.
     */
    private boolean estSurBord(Position pos) {
        return pos.getX() == 0 || pos.getX() == config.getNbLignes() - 1 ||
                pos.getY() == 0 || pos.getY() == config.getNbColonnes() - 1;
    }

    /**
     * Vérifie si une position est déjà occupée.
     */
    private boolean positionOccupee(Position pos) {
        return config.getObstacles().contains(pos) ||
                config.getSacs().stream().anyMatch(s -> s.getPosition().equals(pos)) ||
                config.getRobots().stream().anyMatch(r -> r.getPosition().equals(pos)) ||
                config.getIntrus().stream().anyMatch(i -> i.getPosition().equals(pos));
    }

    /**
     * Change le mode de placement.
     */
    private void changerModePlacement() {
        String mode = cmbModeePlacement.getValue();
        if (mode.contains("Obstacles")) {
            modePlacement = ModePlacement.OBSTACLE;
        } else if (mode.contains("Sacs")) {
            modePlacement = ModePlacement.SAC;
        } else if (mode.contains("Sorties")) {
            modePlacement = ModePlacement.SORTIE;
        }
        mettreAJourCompteur();
    }

    /**
     * Met à jour le compteur.
     */
    private void mettreAJourCompteur() {
        if (null != modePlacement) switch (modePlacement) {
            case OBSTACLE:
                lblCompteur.setText("Obstacles placés : " + config.getObstacles().size());
                break;
            case SAC:
                lblCompteur.setText("Sacs placés : " + config.getSacs().size());
                break;
            case SORTIE:
                lblCompteur.setText("Sorties placées : " + config.getSorties().size());
                break;
            default:
                break;
        }
    }

    /**
     * Termine le placement des éléments.
     */
    private void terminerPlacement() {
        if (config.getSacs().isEmpty()) {
            afficherErreur("Vous devez placer au moins un sac d'argent !");
            return;
        }
        if (config.getSorties().isEmpty()) {
            afficherErreur("Vous devez placer au moins une sortie !");
            return;
        }
        afficherEtape3();
    }

    /**
     * Affiche l'étape 2.
     */
    private void afficherEtape2() {
        getChildren().clear();
        Text titre = new Text("Configuration - Parametrage de la Partie");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        ScrollPane scroll = new ScrollPane(grillePreview);
        scroll.setMaxHeight(400);

        VBox contenu = new VBox(15);
        contenu.setAlignment(Pos.CENTER);
        contenu.getChildren().addAll(etape2);

        getChildren().addAll(titre, contenu);
        etapeActuelle = 2;
    }

    /**
     * Affiche l'étape 3.
     */
    private void afficherEtape3() {
        modePlacement = ModePlacement.ROBOT;
        resetRobots();

        getChildren().clear();
        Text titre = new Text("Configuration - Parametrage de la Partie");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        ScrollPane scroll = new ScrollPane(grillePreview);
        scroll.setMaxHeight(300);

        getChildren().addAll(titre, etape3, scroll);
        etapeActuelle = 3;
    }

    /**
     * Valide les robots.
     */
    private void validerRobots() {
        afficherEtape4();
    }

    /**
     * Affiche l'étape 4.
     */
    private void afficherEtape4() {
        modePlacement = ModePlacement.INTRUS;
        resetIntrus();

        getChildren().clear();
        Text titre = new Text("Configuration - Parametrage de la Partie");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        ScrollPane scroll = new ScrollPane(grillePreview);
        scroll.setMaxHeight(300);

        getChildren().addAll(titre, etape4, scroll);
        etapeActuelle = 4;
    }

    /**
     * Reset robots.
     */
    private void resetRobots() {
        // Effacer visuellement les robots
        for (Robot r : config.getRobots()) {
            StackPane cellule = getCellule(r.getPosition().getX(), r.getPosition().getY());
            if (cellule != null) {
                javafx.scene.shape.Rectangle rect = (javafx.scene.shape.Rectangle) cellule.getChildren().get(0);
                rect.setFill(Color.WHITE);
                if (cellule.getChildren().size() > 1) {
                    cellule.getChildren().remove(1);
                }
            }
        }
        config.getRobots().clear();
        lblRobotsPlaces.setText("Robots placés : 0 / " + spinNbRobots.getValue());
        btnValiderRobots.setDisable(true);
    }

    /**
     * Reset intrus.
     */
    private void resetIntrus() {
        // Effacer visuellement les intrus
        for (Intrus i : config.getIntrus()) {
            StackPane cellule = getCellule(i.getPosition().getX(), i.getPosition().getY());
            if (cellule != null) {
                javafx.scene.shape.Rectangle rect = (javafx.scene.shape.Rectangle) cellule.getChildren().get(0);
                rect.setFill(Color.WHITE);
                if (cellule.getChildren().size() > 1) {
                    cellule.getChildren().remove(1);
                }
            }
        }
        config.getIntrus().clear();
        lblIntrusPlaces.setText("Intrus placés : 0 / " + spinNbIntrus.getValue());
        btnDemarrer.setDisable(true);
    }

    /**
     * Démarre la partie.
     */
    private void demarrerPartie() {
        if (onConfigurationTerminee != null) {
            onConfigurationTerminee.accept(config);
        }
    }

    /**
     * Affiche un message d'erreur.
     */
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Définit le callback de fin de configuration.
     */
    public void setOnConfigurationTerminee(Consumer<Configuration> callback) {
        this.onConfigurationTerminee = callback;
    }

    /**
     * Classe interne pour stocker la configuration.
     */
    public static class Configuration {
        private int nbLignes;
        private int nbColonnes;
        private boolean modeAleatoireObstacles = true;
        private boolean modeAleatoireSorties = true;
        private boolean modeAleatoireRobots = true;
        private boolean modeAleatoireIntrus = true;
        private boolean modeAleatoireSacs = true;
        private List<Position> obstacles = new ArrayList<>();
        private List<SacArgent> sacs = new ArrayList<>();
        private List<Position> sorties = new ArrayList<>();
        private List<Robot> robots = new ArrayList<>();
        private List<Intrus> intrus = new ArrayList<>();

        public int getNbLignes() { return nbLignes; }
        public void setNbLignes(int nbLignes) { this.nbLignes = nbLignes; }

        public int getNbColonnes() { return nbColonnes; }
        public void setNbColonnes(int nbColonnes) { this.nbColonnes = nbColonnes; }
        
        public boolean isModeAleatoireObstacles() { return modeAleatoireObstacles; }
        public void setModeAleatoireObstacles(boolean mode) { this.modeAleatoireObstacles = mode; }
        
        public boolean isModeAleatoireSorties() { return modeAleatoireSorties; }
        public void setModeAleatoireSorties(boolean mode) { this.modeAleatoireSorties = mode; }
        
        public boolean isModeAleatoireRobots() { return modeAleatoireRobots; }
        public void setModeAleatoireRobots(boolean mode) { this.modeAleatoireRobots = mode; }
        
        public boolean isModeAleatoireIntrus() { return modeAleatoireIntrus; }
        public void setModeAleatoireIntrus(boolean mode) { this.modeAleatoireIntrus = mode; }
        
        public boolean isModeAleatoireSacs() { return modeAleatoireSacs; }
        public void setModeAleatoireSacs(boolean mode) { this.modeAleatoireSacs = mode; }

        public List<Position> getObstacles() { return obstacles; }
        public List<SacArgent> getSacs() { return sacs; }
        public List<Position> getSorties() { return sorties; }
        public List<Robot> getRobots() { return robots; }
        public List<Intrus> getIntrus() { return intrus; }
        
        // Méthode pour vérifier si au moins un élément est aléatoire
        public boolean hasAnyRandomElement() {
            return modeAleatoireObstacles || modeAleatoireSorties || modeAleatoireRobots || 
                   modeAleatoireIntrus || modeAleatoireSacs;
        }
        
        // Méthode pour vérifier si tous les éléments sont aléatoires
        public boolean isFullyRandom() {
            return modeAleatoireObstacles && modeAleatoireSorties && modeAleatoireRobots && 
                   modeAleatoireIntrus && modeAleatoireSacs;
        }
    }
}