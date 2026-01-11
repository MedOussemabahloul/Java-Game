package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.jeu.GestionnaireJeu;

/**
 * Panneau d'informations affiché à droite de la grille.
 * Affiche le tour actuel, les statistiques et les contrôles.
 */
public class PanneauInformation extends VBox {

    private GestionnaireJeu gestionnaire;

    // Labels dynamiques
    private Label labelTourActuel;
    private Label labelJoueurActif;
    private Label labelNombreTours;
    private Label labelIntrusCaptures;
    private Label labelIntrusEchappes;
    private Label labelSacsRestants;
    private Label labelInstructions;

    // Boutons
    private Button btnNouvellePartie;
    private Button btnQuitter;

    private static final int LARGEUR_PANNEAU = 300;

    /**
     * Constructeur.
     */
    public PanneauInformation(GestionnaireJeu gestionnaire) {
        this.gestionnaire = gestionnaire;

        construirePanneau();
        mettreAJour();
    }

    /**
     * Construit le panneau avec tous ses composants.
     */
    private void construirePanneau() {
        setPrefWidth(LARGEUR_PANNEAU);
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 2;");

        // Titre
        Text titre = new Text("📊 INFORMATIONS");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Section Tour
        VBox sectionTour = creerSectionTour();

        // Section Statistiques
        VBox sectionStats = creerSectionStatistiques();

        // Section Contrôles
        VBox sectionControles = creerSectionControles();

        // Section Instructions
        VBox sectionInstructions = creerSectionInstructions();

        // Ajouter tous les éléments
        getChildren().addAll(
                titre,
                new Separator(),
                sectionTour,
                new Separator(),
                sectionStats,
                new Separator(),
                sectionControles,
                new Separator(),
                sectionInstructions
        );
    }

    /**
     * Crée la section affichant le tour actuel.
     */
    private VBox creerSectionTour() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER_LEFT);

        Text sousTitre = new Text("🎮 TOUR ACTUEL");
        sousTitre.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        labelTourActuel = new Label();
        labelTourActuel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        labelJoueurActif = new Label();
        labelJoueurActif.setFont(Font.font("Arial", 14));

        labelNombreTours = new Label();
        labelNombreTours.setFont(Font.font("Arial", 12));

        section.getChildren().addAll(sousTitre, labelTourActuel, labelJoueurActif, labelNombreTours);
        return section;
    }

    /**
     * Crée la section des statistiques.
     */
    private VBox creerSectionStatistiques() {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER_LEFT);

        Text sousTitre = new Text("📈 STATISTIQUES");
        sousTitre.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        labelIntrusCaptures = new Label();
        labelIntrusCaptures.setFont(Font.font("Arial", 12));

        labelIntrusEchappes = new Label();
        labelIntrusEchappes.setFont(Font.font("Arial", 12));

        labelSacsRestants = new Label();
        labelSacsRestants.setFont(Font.font("Arial", 12));

        section.getChildren().addAll(
                sousTitre,
                labelIntrusCaptures,
                labelIntrusEchappes,
                labelSacsRestants
        );
        return section;
    }

    /**
     * Crée la section des contrôles.
     */
    private VBox creerSectionControles() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);

        Text sousTitre = new Text("⚙️ CONTRÔLES");
        sousTitre.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        btnNouvellePartie = new Button("🔄 Nouvelle Partie");
        btnNouvellePartie.setPrefWidth(LARGEUR_PANNEAU - 40);
        btnNouvellePartie.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");

        btnQuitter = new Button("❌ Quitter");
        btnQuitter.setPrefWidth(LARGEUR_PANNEAU - 40);
        btnQuitter.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        btnQuitter.setOnAction(e -> System.exit(0));

        section.getChildren().addAll(sousTitre, btnNouvellePartie, btnQuitter);
        return section;
    }

    /**
     * Crée la section des instructions.
     */
    private VBox creerSectionInstructions() {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER_LEFT);

        Text sousTitre = new Text("🎯 INSTRUCTIONS");
        sousTitre.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        labelInstructions = new Label();
        labelInstructions.setWrapText(true);
        labelInstructions.setFont(Font.font("Arial", 12));
        labelInstructions.setMaxWidth(LARGEUR_PANNEAU - 40);

        // Aide clavier
        Text aideClavier = new Text(
                "🎮 PAVÉ NUMÉRIQUE:\n" +
                        "  7  8  9    ↖ ↑ ↗\n" +
                        "  4  ·  6  =  ← · →\n" +
                        "  1  2  3    ↙ ↓ ↘\n\n" +
                        "OU WASD + QEZC"
        );
        aideClavier.setFont(Font.font("Courier New", 10));
        aideClavier.setStyle("-fx-fill: #666;");

        section.getChildren().addAll(sousTitre, labelInstructions, aideClavier);
        return section;
    }

    /**
     * Met à jour toutes les informations affichées.
     */
    public void mettreAJour() {
        // Tour actuel
        int tour = gestionnaire.getTourActuel();
        labelTourActuel.setText("Tour " + gestionnaire.getNombreToursJoues());
        labelTourActuel.setStyle(
                tour == 1 ? "-fx-text-fill: #2196f3;" : "-fx-text-fill: #f44336;"
        );

        // Joueur actif
        String joueur = tour == 1 ? "Joueur 1 (🤖 Robots)" : "Joueur 2 (👤 Intrus)";
        labelJoueurActif.setText(joueur);
        labelJoueurActif.setStyle(
                tour == 1 ? "-fx-text-fill: #2196f3;" : "-fx-text-fill: #f44336;"
        );

        labelNombreTours.setText("Tours joués: " + gestionnaire.getNombreToursJoues());

        // Statistiques
        labelIntrusCaptures.setText("🎯 Intrus capturés: " + gestionnaire.getIntrusCaptures());
        labelIntrusEchappes.setText("💨 Intrus échappés: " + gestionnaire.getIntrusEchappes());

        int sacsRestants = gestionnaire.getGrille().getSacs().size();
        labelSacsRestants.setText("💰 Sacs restants: " + sacsRestants);

        // Instructions contextuelles
        if (gestionnaire.getRobotSelectionne() != null) {
            labelInstructions.setText(
                    "✅ Robot sélectionné !\n" +
                            "Appuyez sur le pavé numérique pour déplacer."
            );
            labelInstructions.setStyle("-fx-text-fill: #2196f3; -fx-font-weight: bold;");
        } else if (gestionnaire.getIntrusSelectionne() != null) {
            labelInstructions.setText(
                    "✅ Intrus sélectionné !\n" +
                            "Appuyez sur le pavé numérique pour déplacer."
            );
            labelInstructions.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
        } else {
            if (tour == 1) {
                labelInstructions.setText(
                        "1️⃣ Cliquez sur un de vos robots\n" +
                                "2️⃣ Utilisez le pavé pour déplacer"
                );
            } else {
                labelInstructions.setText(
                        "1️⃣ Cliquez sur un de vos intrus\n" +
                                "2️⃣ Utilisez le pavé pour déplacer"
                );
            }
            labelInstructions.setStyle("-fx-text-fill: black;");
        }
    }

    /**
     * Définit l'action du bouton Nouvelle Partie.
     */
    public void setOnNouvellePartie(Runnable action) {
        btnNouvellePartie.setOnAction(e -> action.run());
    }
}