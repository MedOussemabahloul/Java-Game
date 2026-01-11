package model.terrain;

/**
 * Enumération des types de cases dans la grille
 */
public enum TypeCase {
    VIDE,       // Case normale, libre
    OBSTACLE,   // Case bloquée, aucune entité ne peut y entrer
    SORTIE      // Case de sortie pour les intrus
}
