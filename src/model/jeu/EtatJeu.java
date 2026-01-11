package model.jeu;

/**
 * Représente les différents états possibles d'une partie
 */
public enum EtatJeu {
    CONFIGURATION, // Avant de commencer la partie
    EN_COURS,      // Partie en cours
    TERMINEE,      // Partie terminée
    PAUSE          // Partie en pause (optionnel)
}
