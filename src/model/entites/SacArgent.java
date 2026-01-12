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
    public boolean estRamasse;
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
    public void setPorteur(Intrus intrus) {
        this.porteur = intrus;
    }

    // --------------------
    // Méthodes principales
    // --------------------
    /**
     * Ramasser le sac par un intrus
     */


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
        this.position = positionInitiale;
        this.estRamasse = false;
        this.porteur = null;
        
        System.out.println("   💰 Sac retourne à sa position initiale : " + positionInitiale);
    }

/**
 * Le sac est ramassé par un intrus.
 */
    public void etreRamasse(Intrus intrus) {
        this.estRamasse = true;
        this.porteur = intrus;
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
