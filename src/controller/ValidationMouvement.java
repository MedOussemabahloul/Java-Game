package controller;

import model.entites.Entite;
import model.terrain.Grille;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour valider les mouvements des entités
 */
public class ValidationMouvement {

    /**
     * Vérifie si un mouvement est valide pour une entité
     */
    public static boolean mouvementValide(Entite entite, Position pos, Grille grille) {
        return positionDansGrille(pos, grille) &&
                caseAccessible(pos, grille) &&
                estAdjacent(entite.getPosition(), pos);
    }

    /**
     * Vérifie que la position est dans les limites de la grille
     */
    public static boolean positionDansGrille(Position pos, Grille grille) {
        return pos.getX() >= 0 && pos.getX() < grille.getNbLignes()
                && pos.getY() >= 0 && pos.getY() < grille.getNbColonnes();
    }

    /**
     * Vérifie que la case n'est pas un obstacle et est libre
     */
    public static boolean caseAccessible(Position pos, Grille grille) {
        if (!positionDansGrille(pos, grille)) return false;
        return grille.getCase(pos).estLibre() && grille.getCase(pos).getType() != null;
    }

    /**
     * Vérifie que la position cible est adjacente (deltaX <=1 et deltaY <=1)
     */
    public static boolean estAdjacent(Position pos1, Position pos2) {
        int dx = Math.abs(pos1.getX() - pos2.getX());
        int dy = Math.abs(pos1.getY() - pos2.getY());
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
    }

    /**
     * Retourne toutes les positions accessibles pour une entité
     */
    public static List<Position> getPositionsAccessibles(Entite entite, Grille grille) {
        List<Position> positions = new ArrayList<>();
        Position current = entite.getPosition();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Position p = new Position(current.getX() + dx, current.getY() + dy);
                if (mouvementValide(entite, p, grille)) {
                    positions.add(p);
                }
            }
        }
        return positions;
    }
}
