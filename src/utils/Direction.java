package utils;
import javafx.scene.input.KeyCode;


/**
 * Représente les 8 directions possibles de déplacement sur la grille.
 * Chaque direction possède un déplacement (dx, dy).
 */
public enum Direction {
    HAUT(-1, 0), BAS(1, 0), GAUCHE(0, -1), DROITE(0, 1),
    HAUT_GAUCHE(-1, -1), HAUT_DROITE(-1, 1), BAS_GAUCHE(1, -1), BAS_DROITE(1, 1),
    STAY(0, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() { return dx; }
    public int getDy() { return dy; }

    // Méthode utilitaire pour convertir dx/dy en Direction
    public static Direction fromDelta(int dx, int dy) {
        for (Direction d : values()) {
            if (d.dx == dx && d.dy == dy) return d;
        }
        return null; // si aucun match
    }
    public static Direction fromKeyCode(KeyCode code) {
        switch (code) {
            // WASD classique
            case W: return HAUT;
            case S: return BAS;
            case A: return GAUCHE;
            case D: return DROITE;

            // Diagonales WASD avec Q et E / ZC pour QWERTY ou AZERTY
            case Q: return HAUT_GAUCHE;
            case E: return HAUT_DROITE;
            case Z: return BAS_GAUCHE;
            case C: return BAS_DROITE;

            // Pavé numérique (1-9)
            case NUMPAD1: return BAS_GAUCHE;
            case NUMPAD2: return BAS;
            case NUMPAD3: return BAS_DROITE;
            case NUMPAD4: return GAUCHE;
            case NUMPAD6: return DROITE;
            case NUMPAD7: return HAUT_GAUCHE;
            case NUMPAD8: return HAUT;
            case NUMPAD9: return HAUT_DROITE;

            default: return null;
        }
    }
}


