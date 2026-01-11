package model.entites;

import model.terrain.Grille;
import model.entites.SacArgent;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un intrus voleur
 */
public class Intrus extends Entite {

    private final int id;
    private final int CAPACITE_MAX = 2;

    private final List<SacArgent> sacsPortes;
    private boolean aFui;

    public Intrus(int id, Position positionInitiale) {
        super(positionInitiale);
        this.id = id;
        this.sacsPortes = new ArrayList<>();
        this.aFui = false;
    }

    // --------------------
    // Getters
    // --------------------
    public int getId() { return id; }
    public List<SacArgent> getSacsPortes() { return sacsPortes; }
    public boolean aFui() { return aFui; }
    public int getCapaciteMax() { return CAPACITE_MAX; }

    // --------------------
    // Méthode principale
    // --------------------
    /**
     * Action de l'intrus à chaque tour :
     * - ramasser un sac si possible
     * - sinon s'échapper si sur une sortie
     */
    @Override
    public void executerAction(Grille grille) {
        // 1. Ramasser un sac adjacent si possible
        if (peutRamasserSac()) {
            List<SacArgent> sacsAdj = grille.getSacsAdjacents(getPosition());
            if (!sacsAdj.isEmpty()) {
                ramasserSac(sacsAdj.get(0)); // prend le premier disponible
                return;
            }
        }

        // 2. Vérifier si sur une sortie
        if (estSurSortie(grille)) {
            sEchapper();
        }
    }

    // --------------------
    // Gestion des sacs
    // --------------------
    public boolean peutRamasserSac() {
        return sacsPortes.size() < CAPACITE_MAX;
    }

    public void ramasserSac(SacArgent sac) {
        if (sac != null && peutRamasserSac()) {
            sacsPortes.add(sac);
            sac.etreRamasse(this);
        }
    }

    public void relacherSacs() {
        for (SacArgent sac : sacsPortes) {
            sac.retournerPositionInitiale();
        }
        sacsPortes.clear();
    }

    // --------------------
    // Évasion
    // --------------------
    public boolean estSurSortie(Grille grille) {
        return grille.getSorties().contains(getPosition());
    }

    public void sEchapper() {
        aFui = true;
        setVivant(false);
    }

    // --------------------
    // Vérification adjacence (optionnel si besoin)
    // --------------------
    public boolean sacEstAdjacent(Position positionSac) {
        return getPosition().estAdjacente(positionSac);
    }
}
