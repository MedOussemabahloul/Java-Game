package view;

import controller.ControleurJeu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.jeu.GestionnaireJeu;
import observer.ObservateurGrille;
import utils.Direction;

/**
 * Classe principale de l'interface graphique JavaFX.
 * Fenêtre principale qui contient tous les composants visuels.
 * Implémente ObservateurGrille pour se mettre à jour automatiquement.
 */
public class VueJeu extends Application implements ObservateurGrille {

    // Composants graphiques
    private Stage stage;
    private Scene scene;
    private BorderPane root;

    private GrilleGraphique grilleGraphique;
    private PanneauInformation panneauInfo;
    private PanneauConfiguration panneauConfig;
    private AnimationHandler animationHandler;

    // Logique
    private ControleurJeu controleur;
    private GestionnaireJeu gestionnaire;

    // Constantes
    private static final int LARGEUR_FENETRE = 1200;
    private static final int HAUTEUR_FENETRE = 800;
    private static final String TITRE = "Surveillance de Zone Géographique";

    /**
     * Point d'entrée de l'application JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Afficher d'abord la configuration
        afficherConfiguration();
    }

    /**
     * Affiche le panneau de configuration pour initialiser la partie.
     */
    private void afficherConfiguration() {
        panneauConfig = new PanneauConfiguration();

        Scene configScene = new Scene(panneauConfig, 600, 500);
        stage.setScene(configScene);
        stage.setTitle(TITRE + " - Configuration");
        stage.show();

        // Quand la configuration est terminée
        panneauConfig.setOnConfigurationTerminee(config -> {
            initialiserJeu(config.getNbLignes(), config.getNbColonnes(), config);
        });
    }

    /**
     * Initialise le jeu avec la configuration donnée.
     */
    private void initialiserJeu(int nbLignes, int nbColonnes, PanneauConfiguration.Configuration config) {
        // Créer le gestionnaire de jeu
        gestionnaire = new GestionnaireJeu(nbLignes, nbColonnes);

        // Appliquer la configuration (obstacles, sacs, sorties, entités)
        appliquerConfiguration(config);

        // Créer le contrôleur
        controleur = new ControleurJeu(gestionnaire);

        // S'enregistrer comme observateur
        gestionnaire.getGrille().ajouterObservateur(this);

        // Construire l'interface de jeu
        construireInterface();

        // Démarrer la partie
        gestionnaire.demarrerPartie();

        // Afficher la fenêtre de jeu
        stage.setScene(scene);
        stage.setTitle(TITRE);
        stage.show();
    }

    /**
     * Applique la configuration à la grille.
     */
    private void appliquerConfiguration(PanneauConfiguration.Configuration config) {
        // Créer le générateur pour les éléments aléatoires
        utils.GenerateurGrilleAleatoire generateur = new utils.GenerateurGrilleAleatoire(gestionnaire.getGrille());
        generateur.setNbRobots(config.getRobots().size() > 0 ? config.getRobots().size() : 2);
        generateur.setNbIntrus(config.getIntrus().size() > 0 ? config.getIntrus().size() : 3);
        generateur.setNbSacs(config.getSacs().size() > 0 ? config.getSacs().size() : 4);
        generateur.setPourcentageObstacles(15);
        generateur.setPourcentageSorties(10);
        
        // Générer les éléments en mode aléatoire
        if (config.isModeAleatoireObstacles()) {
            generateur.placerObstacles();
        }
        if (config.isModeAleatoireSorties()) {
            generateur.placerSorties();
        }
        
        // Ajouter les éléments placés manuellement
        if (!config.isModeAleatoireObstacles()) {
            config.getObstacles().forEach(pos ->
                    gestionnaire.getGrille().ajouterObstacle(pos));
        }
        
        if (!config.isModeAleatoireSorties()) {
            config.getSorties().forEach(pos ->
                    gestionnaire.getGrille().ajouterSortie(pos));
        }
        
        if (!config.isModeAleatoireSacs()) {
            config.getSacs().forEach(sac ->
                    gestionnaire.getGrille().ajouterSac(sac));
        }
        
        if (!config.isModeAleatoireRobots()) {
            config.getRobots().forEach(robot ->
                    gestionnaire.getGrille().ajouterRobot(robot));
        }
        
        if (!config.isModeAleatoireIntrus()) {
            config.getIntrus().forEach(intrus ->
                    gestionnaire.getGrille().ajouterIntrus(intrus));
        }
        
        // Générer les entités en mode aléatoire si non encore générés
        if (config.isModeAleatoireRobots() || config.isModeAleatoireIntrus() || config.isModeAleatoireSacs()) {
            if (config.isModeAleatoireRobots()) {
                generateur.placerRobots();
            }
            if (config.isModeAleatoireIntrus()) {
                generateur.placerIntrus();
            }
            if (config.isModeAleatoireSacs()) {
                generateur.placerSacs();
            }
        }
    }

    /**
     * Construit l'interface de jeu principale.
     */
    private void construireInterface() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // Créer les composants
        grilleGraphique = new GrilleGraphique(gestionnaire.getGrille());
        panneauInfo = new PanneauInformation(gestionnaire);
        animationHandler = new AnimationHandler();

        // Connecter les handlers de clic
        grilleGraphique.setOnCaseCliquee((x, y) -> {
            controleur.gererClicCase(x, y);
        });

        // Placer les composants
        root.setCenter(grilleGraphique);
        root.setRight(panneauInfo);

        // Créer la scène
        scene = new Scene(root, LARGEUR_FENETRE, HAUTEUR_FENETRE);

        // Gérer les touches clavier
        scene.setOnKeyPressed(event -> gererToucheClavier(event.getCode()));

        // Styliser
        root.setStyle("-fx-background-color: #f5f5f5;");
    }

    /**
     * Gère les touches du clavier (pavé numérique et WASD).
     */
    private void gererToucheClavier(KeyCode code) {
        Direction direction = Direction.fromKeyCode(code);

        if (direction != null) {
            // Le joueur a appuyé sur une touche de direction valide
            boolean deplacementReussi = controleur.deplacerEntiteSelectionnee(direction);

            if (!deplacementReussi) {
                // Mouvement invalide - afficher feedback
                afficherMouvementInvalide();
            }
        }
    }

    /**
     * Callback du pattern Observer.
     * Appelée automatiquement quand la grille est modifiée.
     */
    @Override
    public void onGrilleModifiee() {
        Platform.runLater(() -> {
            rafraichirAffichage();
            verifierFinPartie();
        });
    }

    /**
     * Rafraîchit tout l'affichage.
     */
    private void rafraichirAffichage() {
        grilleGraphique.rafraichir();
        panneauInfo.mettreAJour();
    }

    /**
     * Vérifie si la partie est terminée.
     */
    private void verifierFinPartie() {
        if (gestionnaire.partieTerminee()) {
            String resultat = gestionnaire.obtenirResultat();
            afficherMessageFin(resultat);
        }
    }

    /**
     * Affiche un message de fin de partie.
     */
    private void afficherMessageFin(String resultat) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partie Terminée");
        alert.setHeaderText("La partie est terminée !");
        alert.setContentText(resultat);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Retour à la configuration
                afficherConfiguration();
            }
        });
    }

    /**
     * Affiche un feedback visuel pour mouvement invalide.
     */
    private void afficherMouvementInvalide() {
        // Animation shake ou son d'erreur
        animationHandler.animerErreur(grilleGraphique.getEntiteSelectionnee());
    }

    /**
     * Méthode main pour lancer l'application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}