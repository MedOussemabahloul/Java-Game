package model.entites;

import utils.Position;
import model.terrain.Grille;

/**
 * Classe abstraite représentant une entité mobile dans la grille
 * Superclasse de Robot et Intrus
 */
public abstract class Entite {

    // --------------------
    // Attributs communs
    // --------------------
    protected Position position;
    protected boolean estVivant;

    // --------------------
    // Constructeur
    // --------------------
    public Entite(Position position) {
        this.position = position;
        this.estVivant = true; // par défaut, toutes les entités sont vivantes
    }

    // --------------------
    // Getters et Setters
    // --------------------
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean estVivant() {
        return estVivant;
    }

    public void setVivant(boolean vivant) {
        this.estVivant = vivant;
    }

    // --------------------
    // Déplacement
    // --------------------
    /**
     * Déplace l'entité vers une nouvelle position.
     * La validation se fait dans la grille ou le contrôleur.
     *
     * @param nouvellePosition position cible
     */
    public void deplacer(Position nouvellePosition) {
        this.position = nouvellePosition;
    }

    // --------------------
    // Méthode abstraite
    // --------------------
    /**
     * Chaque entité doit définir sa propre action lors d'un tour de jeu
     *
     * @param grille la grille du jeu
     */
    public abstract void executerAction(Grille grille);
}
