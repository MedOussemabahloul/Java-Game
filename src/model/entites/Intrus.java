package model.entites;

import java.util.ArrayList;
import java.util.List;

import model.terrain.Case;
import model.terrain.Grille;
import utils.Position;

/**
 * Classe représentant un intrus voleur
 */
public class Intrus extends Entite {

    private final int id;
    private final int CAPACITE_MAX = 2;

    private final List<SacArgent> sacsPortes;
    public  boolean aFui;

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
    // 1. Vérifier si sur une sortie (priorité : fuir)
    if (grille.getCase(position).estSortie()) {
        System.out.println("🚪 Intrus #" + id + " s'échappe par la sortie !");
        aFui = true;
        setVivant(false);
        grille.retirerIntrus(this);
        return;
    }
    
    // 2. Si peut ramasser un sac, le faire
    if (peutRamasserSac()) {
        List<SacArgent> sacsAdjacents = grille.getSacsAdjacents(position);
        
        if (!sacsAdjacents.isEmpty()) {
            SacArgent sac = sacsAdjacents.get(0);
            
            System.out.println("💰 Intrus #" + id + " ramasse un sac !");
            
            // Ramasser le sac
            ramasserSac(sac);
            
            // IMPORTANT : Retirer le sac de sa case
            Case caseSac = grille.getCase(sac.getPosition());
            if (caseSac != null && caseSac.getEntite() == sac) {
                caseSac.setEntite(null);
            }
        }
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
