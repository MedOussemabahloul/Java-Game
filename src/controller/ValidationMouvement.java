package controller;

import java.util.ArrayList;
import java.util.List;

import model.entites.Entite;
import model.entites.Intrus;
import model.entites.Robot;
import model.terrain.Grille;
import utils.Position;

/**
 * Classe utilitaire pour valider les mouvements des entités
 */
public class ValidationMouvement {

    /**
     * Vérifie si un mouvement est valide pour une entité
     * AJOUT : Bloque les intrus qui tentent d'aller près d'un robot
     */
    public static boolean mouvementValide(Entite entite, Position pos, Grille grille) {
        // Vérifications de base
        if (!positionDansGrille(pos, grille)) {
            return false;
        }

        if (grille.getCase(pos).getEntite() instanceof model.entites.SacArgent) {
            return true;
        }

        if (!caseAccessible(pos, grille, entite)) return false;

        if (!estAdjacent(entite.getPosition(), pos)) return false;

        
        // NOUVELLE RÈGLE : Si c'est un intrus, bloquer si un robot est adjacent à la destination
        if (entite instanceof Intrus) {
            if (robotAdjacentAPosition(pos, grille)) {
                System.out.println("❌ MOUVEMENT BLOQUÉ : Un robot est adjacent à cette case !");
                return false;
            }
        }

        
        return true;
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
    public static boolean caseAccessible(Position pos, Grille grille, Entite entite) {
        Entite e = grille.getCase(pos).getEntite();

        if (e instanceof model.entites.SacArgent) {
            return true;
        }
        if (e == null) {
            return true; // Case occupée
        }
        if (grille.getSorties().contains(pos)) return true;

        if (robotAdjacentAPosition(pos, grille)) {
                return false; // Bloquer la case si un robot est adjacent
            }

        return true;
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
     * NOUVELLE MÉTHODE : Vérifie si un robot est adjacent à une position donnée
     * 
     * @param pos La position à vérifier
     * @param grille La grille de jeu
     * @return true si au moins un robot vivant est adjacent
     */
    private static boolean robotAdjacentAPosition(Position pos, Grille grille) {
        List<Robot> robots = grille.getRobots();
        
        for (Robot robot : robots) {
            if (robot.estVivant() && estAdjacent(pos, robot.getPosition())) {
                return true;
            }
        }
        
        return false;
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