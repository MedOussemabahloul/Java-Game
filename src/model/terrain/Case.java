package model.terrain;

import model.entites.Entite;
import utils.Position;

/**
 * Représente une case de la grille
 */
public class Case {

    private final Position position;
    private final TypeCase type;
    private Entite entite; // null si pas d'entité

    /**
     * Constructeur

     */
    public Case(Position position, TypeCase type) {
        this.position = position;
        this.type = type;
        this.entite = null; // initialement vide
    }

    // --------------------
    // Getters
    // --------------------

    public Position getPosition() {
        return position;
    }

    public TypeCase getType() {
        return type;
    }

    public Entite getEntite() {
        return entite;
    }

    // --------------------
    // Gestion entité
    // --------------------

    public void setEntite(Entite entite) {
        this.entite = entite;
    }

    public boolean estLibre() {
        return entite == null && type != TypeCase.OBSTACLE;
    }

    public boolean estObstacle() {
        return type == TypeCase.OBSTACLE;
    }

    public boolean estSortie() {
        return type == TypeCase.SORTIE;
    }


}
