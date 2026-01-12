package model.entites;

import model.terrain.Grille;
import utils.Position;

/**
 * Classe représentant un robot gardien
 */
public class Robot extends Entite {

    private final int id;
    private int nombreCaptures;

    public Robot(int id, Position positionInitiale) {
        super(positionInitiale);
        this.id = id;
        this.nombreCaptures = 0;
    }

    // --------------------
    // Getters
    // --------------------
    public int getId() {
        return id;
    }

    public int getNombreCaptures() {
        return nombreCaptures;
    }

    // --------------------
    // Méthode principale
    // --------------------
    /**
     * Action du robot à chaque tour : tenter d'attraper un intrus adjacent
     */
    @Override
    public void executerAction(Grille grille) {
        for (Intrus intrus : grille.getIntrusAdjacents(getPosition())) {
            attraperIntrus(intrus, grille);
            break; // attrape un seul intrus par action
        }
    }

    // --------------------
    // Logique capture
    // --------------------
    public void attraperIntrus(Intrus intrus, Grille grille) {
        if (intrus != null && intrus.estVivant()) {
            intrus.setVivant(false);          // l'intrus est capturé
            intrus.relacherSacs();            // ses sacs retournent sur la grille
            grille.retirerIntrus(intrus);     // le retirer de la grille
            nombreCaptures++;
        }
    }

    // --------------------
    // Vérification adjacence
    // --------------------
    /**
     * Vérifie si un intrus est adjacent à la position du robot
     *
     * @param positionIntrus position de l'intrus
     * @return true si intrus est dans les 8 cases autour
     */
    public boolean intrusEstAdjacent(Position positionIntrus) {
        return getPosition().estAdjacente(positionIntrus);
    }
}
