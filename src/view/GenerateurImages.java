package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

/**
 * Génère les images des entités de manière procédurale (sans fichiers PNG).
 */
public class GenerateurImages {

    private static final int TAILLE = 64;

    /**
     * Génère l'image d'un robot (carré bleu avec robot en ASCII).
     */
    public static Image genererImageRobot() {
        Canvas canvas = new Canvas(TAILLE, TAILLE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fond dégradé bleu
        Stop[] stops = new Stop[]{
            new Stop(0, Color.web("#1976d2")),
            new Stop(1, Color.web("#1565c0"))
        };
        RadialGradient gradient = new RadialGradient(0, 0, TAILLE / 2, TAILLE / 2, TAILLE / 2, false, null, stops);
        gc.setFill(gradient);
        gc.fillRect(0, 0, TAILLE, TAILLE);

        // Dessin robot stylisé (carré + cercle)
        gc.setFill(Color.web("#90caf9"));
        gc.fillRect(TAILLE * 0.2, TAILLE * 0.15, TAILLE * 0.6, TAILLE * 0.5);

        gc.fillOval(TAILLE * 0.25, TAILLE * 0.05, TAILLE * 0.5, TAILLE * 0.4);

        // Lettre R
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 24));
        gc.fillText("R", TAILLE * 0.35, TAILLE * 0.75);

        return canvas.snapshot(null, null);
    }

    /**
     * Génère l'image d'un intrus (carré rouge avec personnage).
     */
    public static Image genererImageIntrus() {
        Canvas canvas = new Canvas(TAILLE, TAILLE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fond dégradé rouge
        Stop[] stops = new Stop[]{
            new Stop(0, Color.web("#f44336")),
            new Stop(1, Color.web("#c62828"))
        };
        RadialGradient gradient = new RadialGradient(0, 0, TAILLE / 2, TAILLE / 2, TAILLE / 2, false, null, stops);
        gc.setFill(gradient);
        gc.fillRect(0, 0, TAILLE, TAILLE);

        // Dessin intrus : cercle tête + corps
        gc.setFill(Color.web("#ffab91"));
        gc.fillOval(TAILLE * 0.2, TAILLE * 0.05, TAILLE * 0.6, TAILLE * 0.5);

        gc.fillRect(TAILLE * 0.25, TAILLE * 0.4, TAILLE * 0.5, TAILLE * 0.35);

        // Lettre I
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText("I", TAILLE * 0.38, TAILLE * 0.75);

        return canvas.snapshot(null, null);
    }

    /**
     * Génère l'image d'un sac d'argent (carré jaune avec $).
     */
    public static Image genererImageSac() {
        Canvas canvas = new Canvas(TAILLE, TAILLE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fond dégradé jaune
        Stop[] stops = new Stop[]{
            new Stop(0, Color.web("#ffc107")),
            new Stop(1, Color.web("#ffa000"))
        };
        RadialGradient gradient = new RadialGradient(0, 0, TAILLE / 2, TAILLE / 2, TAILLE / 2, false, null, stops);
        gc.setFill(gradient);
        gc.fillRect(0, 0, TAILLE, TAILLE);

        // Dessin sac : rectangle avec contour
        gc.setStroke(Color.web("#795548"));
        gc.setLineWidth(3);
        gc.fillRect(TAILLE * 0.15, TAILLE * 0.2, TAILLE * 0.7, TAILLE * 0.6);
        gc.strokeRect(TAILLE * 0.15, TAILLE * 0.2, TAILLE * 0.7, TAILLE * 0.6);

        // Symbole $
        gc.setFill(Color.web("#795548"));
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
        gc.fillText("$", TAILLE * 0.3, TAILLE * 0.65);

        return canvas.snapshot(null, null);
    }

    /**
     * Génère l'image d'un obstacle (carré gris foncé).
     */
    public static Image genererImageObstacle() {
        Canvas canvas = new Canvas(TAILLE, TAILLE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fond dégradé gris
        Stop[] stops = new Stop[]{
            new Stop(0, Color.web("#424242")),
            new Stop(1, Color.web("#212121"))
        };
        RadialGradient gradient = new RadialGradient(0, 0, TAILLE / 2, TAILLE / 2, TAILLE / 2, false, null, stops);
        gc.setFill(gradient);
        gc.fillRect(0, 0, TAILLE, TAILLE);

        // Motif croix X
        gc.setStroke(Color.web("#757575"));
        gc.setLineWidth(2);
        gc.strokeLine(TAILLE * 0.1, TAILLE * 0.1, TAILLE * 0.9, TAILLE * 0.9);
        gc.strokeLine(TAILLE * 0.9, TAILLE * 0.1, TAILLE * 0.1, TAILLE * 0.9);

        return canvas.snapshot(null, null);
    }

    /**
     * Génère l'image d'une sortie (vert avec flèche).
     */
    public static Image genererImageSortie() {
        Canvas canvas = new Canvas(TAILLE, TAILLE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fond dégradé vert
        Stop[] stops = new Stop[]{
            new Stop(0, Color.web("#81c784")),
            new Stop(1, Color.web("#2e7d32"))
        };
        RadialGradient gradient = new RadialGradient(0, 0, TAILLE / 2, TAILLE / 2, TAILLE / 2, false, null, stops);
        gc.setFill(gradient);
        gc.fillRect(0, 0, TAILLE, TAILLE);

        // Flèche diagonale
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeLine(TAILLE * 0.2, TAILLE * 0.8, TAILLE * 0.8, TAILLE * 0.2);

        // Pointe flèche
        gc.fillPolygon(
            new double[]{TAILLE * 0.8, TAILLE * 0.75, TAILLE * 0.85},
            new double[]{TAILLE * 0.2, TAILLE * 0.15, TAILLE * 0.25},
            3
        );

        return canvas.snapshot(null, null);
    }
}
