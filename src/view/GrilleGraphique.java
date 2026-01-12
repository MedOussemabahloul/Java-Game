package view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.entites.Entite;
import model.entites.Intrus;
import model.entites.Robot;
import model.entites.SacArgent;
import model.terrain.Case;
import model.terrain.Grille;
import model.terrain.TypeCase;
import utils.Position;

/**
 * Composant JavaFX qui affiche la grille de jeu.
 * Gère l'affichage des cases, entités et la sélection.
 */
public class GrilleGraphique extends GridPane {

    private Grille grille;
    private int tailleCellule;
    private double largeurScene;
    private double hauteurScene;

    // Map pour retrouver rapidement les StackPane par position
    private Map<Position, StackPane> cellules;

    // Entité actuellement sélectionnée
    private Entite entiteSelectionnee;
    private StackPane celluleSelectionnee;

    // Callback pour les clics
    private BiConsumer<Integer, Integer> onCaseCliquee;

    // Constantes de couleurs
    private static final Color COULEUR_VIDE = Color.web("#e0e0e0");
    private static final Color COULEUR_OBSTACLE = Color.web("#424242");
    private static final Color COULEUR_SORTIE = Color.web("#81c784");
    private static final Color COULEUR_ROBOT = Color.web("#2196f3");
    private static final Color COULEUR_INTRUS = Color.web("#f44336");
    private static final Color COULEUR_SAC = Color.web("#ffc107");
    private static final Color COULEUR_SELECTION = Color.web("#ffeb3b");
    private static final Color COULEUR_ACCESSIBLE = Color.web("#c8e6c9");

    private static final int TAILLE_CELLULE_MIN = 40;
    private static final int TAILLE_CELLULE_MAX = 80;
    private static final int MAX_GRILLE = 20;  // Limite max de cases par côté

    /**
     * Constructeur.
     */
    public GrilleGraphique(Grille grille) {
        this.grille = grille;
        this.cellules = new HashMap<>();
        this.entiteSelectionnee = null;
        
        // Vérifier limite grille
        if (grille.getNbLignes() > MAX_GRILLE || grille.getNbColonnes() > MAX_GRILLE) {
            throw new IllegalArgumentException("Grille trop grande : max " + MAX_GRILLE + "x" + MAX_GRILLE);
        }

        // Calculer la taille optimale des cellules
        calculerTailleCellule();

        // Construire la grille
        construireGrille();

        // Style du GridPane
        setHgap(2);
        setVgap(2);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #bdbdbd; -fx-padding: 10;");
        
        // Responsive : recalculer quand la taille change
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    }
    
    /**
     * Configure la grille pour écouter les changements de taille de scène.
     */
    public void configurerResponsive(Scene scene) {
        scene.widthProperty().addListener((obs, old, newVal) -> {
            // Recalculer si besoin
        });
        scene.heightProperty().addListener((obs, old, newVal) -> {
            // Recalculer si besoin
        });
    }

    // --------------------
    // Getters (API Publique)
    // --------------------
    /**
     * Retourne la cellule actuellement sélectionnée.
     */
    public StackPane getCelluleSelectionnee() {
        return celluleSelectionnee;
    }

    /**
     * Calcule la taille optimale des cellules selon la taille de la grille.
     */
    private void calculerTailleCellule() {
        int nbLignes = grille.getNbLignes();
        int nbColonnes = grille.getNbColonnes();

        // Adapter selon la taille
        if (nbLignes <= 10 && nbColonnes <= 10) {
            tailleCellule = 60;
        } else if (nbLignes <= 15 && nbColonnes <= 15) {
            tailleCellule = 50;
        } else {
            tailleCellule = 40;
        }
    }

    /**
     * Construit visuellement la grille.
     */
    private void construireGrille() {
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                Position pos = new Position(i, j);
                Case caseGrille = grille.getCase(pos);

                StackPane cellule = creerCellule(caseGrille);
                cellules.put(pos, cellule);

                // Gérer le clic
                int x = i, y = j;
                cellule.setOnMouseClicked(event -> {
                    if (onCaseCliquee != null) {
                        onCaseCliquee.accept(x, y);
                    }
                });

                add(cellule, j, i);
            }
        }
    }

    /**
     * Crée une cellule visuelle pour une case.
     */
    private StackPane creerCellule(Case caseGrille) {
        StackPane cellule = new StackPane();
        cellule.setPrefSize(tailleCellule, tailleCellule);

        // Rectangle de fond
        Rectangle fond = new Rectangle(tailleCellule, tailleCellule);
        fond.setStroke(Color.GRAY);
        fond.setStrokeWidth(1);

        // Couleur selon le type
        Color couleur = getCouleurCase(caseGrille.getType());
        fond.setFill(couleur);

        cellule.getChildren().add(fond);

        // Ajouter l'entité si présente
        if (caseGrille.getEntite() != null) {
            Node nodeEntite = creerNodeEntite(caseGrille.getEntite());
            cellule.getChildren().add(nodeEntite);
        }

        // Ajouter indication de sortie
        if (caseGrille.estSortie()) {
            Text texte = new Text("↗");
            texte.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            texte.setFill(Color.web("#2e7d32"));
            cellule.getChildren().add(texte);
        }

        return cellule;
    }

    /**
     * Retourne la couleur selon le type de case.
     */
    private Color getCouleurCase(TypeCase type) {
        switch (type) {
            case OBSTACLE:
                return COULEUR_OBSTACLE;
            case SORTIE:
                return COULEUR_SORTIE;
            case VIDE:
            default:
                return COULEUR_VIDE;
        }
    }

    /**
     * Crée un node visuel pour une entité.
     */
    private Node creerNodeEntite(Entite entite) {
        if (entite instanceof Robot) {
            return creerNodeRobot((Robot) entite);
        } else if (entite instanceof Intrus) {
            return creerNodeIntrus((Intrus) entite);
        } else if (entite instanceof SacArgent) {
            return creerNodeSac((SacArgent) entite);
        }
        return new Circle(10, Color.BLACK);
    }

    /**
     * Crée le node pour un robot.
     */
    private Node creerNodeRobot(Robot robot) {
        StackPane stack = new StackPane();
        
        // Image du robot
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
            GenerateurImages.genererImageRobot()
        );
        imageView.setFitWidth(tailleCellule * 0.8);
        imageView.setFitHeight(tailleCellule * 0.8);
        imageView.setPreserveRatio(true);
        
        stack.getChildren().add(imageView);
        
        // Ajouter le numéro du robot en overlay
        Text numero = new Text("R" + robot.getId());
        numero.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        numero.setFill(Color.WHITE);
        numero.setStroke(Color.BLACK);
        numero.setStrokeWidth(1.5);
        
        // Positionner en bas à droite
        StackPane.setAlignment(numero, javafx.geometry.Pos.BOTTOM_RIGHT);
        StackPane.setMargin(numero, new javafx.geometry.Insets(0, 3, 3, 0));
        stack.getChildren().add(numero);
        
        return stack;
    }

        /**
         * Crée le node pour un intrus.
         */
    private Node creerNodeIntrus(Intrus intrus) {
        StackPane stack = new StackPane();
        
        // Image de l'intrus
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
            GenerateurImages.genererImageIntrus()
        );
        imageView.setFitWidth(tailleCellule * 0.8);
        imageView.setFitHeight(tailleCellule * 0.8);
        imageView.setPreserveRatio(true);
        
        stack.getChildren().add(imageView);
        
        // Texte avec numéro + indicateur de sacs
        String texte = "I" + intrus.getId();
        
        // Si l'intrus porte des sacs, l'indiquer
        if (!intrus.getSacsPortes().isEmpty()) {
            texte += "\n💰×" + intrus.getSacsPortes().size();
        }
        
        Text info = new Text(texte);
        info.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        info.setFill(Color.WHITE);
        info.setStroke(Color.BLACK);
        info.setStrokeWidth(1.5);
        info.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        // Positionner en bas à droite
        StackPane.setAlignment(info, javafx.geometry.Pos.BOTTOM_RIGHT);
        StackPane.setMargin(info, new javafx.geometry.Insets(0, 3, 3, 0));
        stack.getChildren().add(info);
        
        return stack;
    }

    /**
     * Crée le node pour un sac.
     */
    private Node creerNodeSac(SacArgent sac) {
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
            GenerateurImages.genererImageSac()
        );
        imageView.setFitWidth(tailleCellule * 0.75);
        imageView.setFitHeight(tailleCellule * 0.75);
        imageView.setPreserveRatio(true);
        return imageView;
}

    /**
     * Rafraîchit l'affichage complet de la grille.
     */
    public void rafraichir() {
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                Position pos = new Position(i, j);
                Case caseGrille = grille.getCase(pos);
                StackPane cellule = cellules.get(pos);

                // Recréer la cellule
                cellule.getChildren().clear();

                // Rectangle de fond
                Rectangle fond = new Rectangle(tailleCellule, tailleCellule);
                fond.setStroke(Color.GRAY);
                fond.setStrokeWidth(1);
                fond.setFill(getCouleurCase(caseGrille.getType()));
                cellule.getChildren().add(fond);

                // Ajouter l'entité si présente
                if (caseGrille.getEntite() != null) {
                    Node nodeEntite = creerNodeEntite(caseGrille.getEntite());
                    cellule.getChildren().add(nodeEntite);
                }

                // Indication sortie
                if (caseGrille.estSortie()) {
                    Text texte = new Text("↗");
                    texte.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    texte.setFill(Color.web("#2e7d32"));
                    cellule.getChildren().add(texte);
                }
            }
        }

        // Réafficher la sélection si nécessaire
        if (entiteSelectionnee != null) {
            highlightSelection(entiteSelectionnee.getPosition());
        }
    }

    /**
     * Met en surbrillance la case sélectionnée.
     */
    public void highlightSelection(Position pos) {
        // Retirer l'ancienne surbrillance
        if (celluleSelectionnee != null) {
            Rectangle fond = (Rectangle) celluleSelectionnee.getChildren().get(0);
            fond.setStroke(Color.GRAY);
            fond.setStrokeWidth(1);
        }

        // Ajouter la nouvelle surbrillance
        StackPane cellule = cellules.get(pos);
        if (cellule != null) {
            Rectangle fond = (Rectangle) cellule.getChildren().get(0);
            fond.setStroke(COULEUR_SELECTION);
            fond.setStrokeWidth(4);
            celluleSelectionnee = cellule;
        }
    }

    /**
     * Retire la surbrillance.
     */
    public void retirerSurbrillance() {
        if (celluleSelectionnee != null) {
            Rectangle fond = (Rectangle) celluleSelectionnee.getChildren().get(0);
            fond.setStroke(Color.GRAY);
            fond.setStrokeWidth(1);
            celluleSelectionnee = null;
        }
        entiteSelectionnee = null;
    }

    /**
     * Affiche les cases accessibles en vert clair.
     */
    public void afficherCasesAccessibles(java.util.List<Position> positions) {
        for (Position pos : positions) {
            StackPane cellule = cellules.get(pos);
            if (cellule != null) {
                Rectangle fond = (Rectangle) cellule.getChildren().get(0);
                fond.setFill(COULEUR_ACCESSIBLE);
            }
        }
    }

    /**
     * Définit l'entité sélectionnée.
     */
    public void setEntiteSelectionnee(Entite entite) {
        this.entiteSelectionnee = entite;
    }

    /**
     * Retourne l'entité sélectionnée.
     */
    public Node getEntiteSelectionnee() {
        if (entiteSelectionnee != null && celluleSelectionnee != null) {
            // Retourner le node de l'entité (2ème enfant de la cellule)
            if (celluleSelectionnee.getChildren().size() > 1) {
                return celluleSelectionnee.getChildren().get(1);
            }
        }
        return null;
    }

    /**
     * Définit le callback pour les clics sur les cases.
     */
    public void setOnCaseCliquee(BiConsumer<Integer, Integer> callback) {
        this.onCaseCliquee = callback;
    }
}