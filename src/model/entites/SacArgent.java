package model.entites;

import utils.Position;

/**
 * Classe représentant un sac d'argent dans le jeu
 */
public class SacArgent extends Entite {

    // --------------------
    // Attributs
    // --------------------
    private final int id;
    private boolean estRamasse;
    private Intrus porteur; // null si aucun intrus ne le porte
    private final Position positionInitiale;

    // --------------------
    // Constructeur
    // --------------------
    public SacArgent(int id, Position positionInitiale) {
        super(positionInitiale);
        this.estRamasse = false;
        this.porteur = null;
        this.id = id;
        this.positionInitiale = positionInitiale;
    }

    // --------------------
    // Getters et Setters
    // --------------------
    public int getId() {
        return id;
    }

    public boolean estRamasse() {
        return estRamasse;
    }

    public Intrus getPorteur() {
        return porteur;
    }

    // --------------------
    // Méthodes principales
    // --------------------
    /**
     * Ramasser le sac par un intrus
     */
    public void etreRamasse(Intrus intrus) {
        if (!estRamasse) {
            estRamasse = true;
            porteur = intrus;
            position = null; // plus sur la grille
        }
    }

    /**
     * Relâcher le sac (par exemple si intrus capturé)
     */
    public void etreRelache() {
        estRamasse = false;
        porteur = null;
        position = positionInitiale; // revient à sa position de départ
    }

    /**
     * Retourne le sac à sa position initiale
     */
    public void retournerPositionInitiale() {
        position = positionInitiale;
        estRamasse = false;
        porteur = null;
    }

    // --------------------
    // Méthode abstraite
    // --------------------
    @Override
    public void executerAction(model.terrain.Grille grille) {
        // Les sacs d'argent n'ont pas d'action particulière
        // Ils sont passifs et ne font rien à chaque tour
    }
}
