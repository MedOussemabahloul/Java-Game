package utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import model.entites.Intrus;
import model.entites.Robot;
import model.entites.SacArgent;
import model.terrain.Grille;
import model.terrain.TypeCase;

/**
 * Génère aléatoirement une grille de jeu avec obstacles, sorties, robots, intrus et sacs.
 */

public class GenerateurGrilleAleatoire {

    private Random random;
    private Grille grille;
    private Set<Position> positionsOccupees;

    // Pourcentages (0-100)
    private int pourcentageObstacles = 15;
    private int pourcentageSorties = 10;
    private int nbRobots = 2;
    private int nbIntrus = 3;
    private int nbSacs = 4;

    /**
     * Constructeur.
     */
    public GenerateurGrilleAleatoire(Grille grille) {
        this.grille = grille;
        this.random = new Random();
        this.positionsOccupees = new HashSet<>();
    }

    /**
     * Génère la grille aléatoirement.
     */
    public void generer() {
        positionsOccupees.clear();

        // 1. Placer les obstacles
        placerObstacles();

        // 2. Placer les sorties
        placerSorties();

        // 3. Placer les robots
        placerRobots();

        // 4. Placer les intrus
        placerIntrus();

        // 5. Placer les sacs d'argent
        placerSacs();
    }

    /**
     * Place les obstacles aléatoirement.
     */
    public void placerObstacles() {
        int totalCases = grille.getNbLignes() * grille.getNbColonnes();
        int nbObstacles = (totalCases * pourcentageObstacles) / 100;

        int placed = 0;
        int attempts = 0;
        int maxAttempts = totalCases * 2;

        while (placed < nbObstacles && attempts < maxAttempts) {
            Position pos = positionAleatoire();
            if (!positionsOccupees.contains(pos) && grille.getCase(pos).estLibre()) {
                grille.ajouterObstacle(pos);  // Utiliser la méthode publique
                positionsOccupees.add(pos);
                placed++;
            }
            attempts++;
        }
    }

    /**
     * Place les sorties aléatoirement.
     */
    public void placerSorties() {
        int totalCases = grille.getNbLignes() * grille.getNbColonnes();
        int nbSorties = (totalCases * pourcentageSorties) / 100;
        nbSorties = Math.max(1, nbSorties); // Au moins une sortie

        int placed = 0;
        int attempts = 0;
        int maxAttempts = totalCases * 2;

        while (placed < nbSorties && attempts < maxAttempts) {
            Position pos = positionAleatoire();
            if (!positionsOccupees.contains(pos) && grille.getCase(pos).estLibre()) {
                grille.ajouterSortie(pos);  // Utiliser la méthode publique
                positionsOccupees.add(pos);
                placed++;
            }
            attempts++;
        }
    }

    /**
     * Place les robots aléatoirement.
     */
    public void placerRobots() {
        int id = 0;
        for (int i = 0; i < nbRobots; i++) {
            Position pos = positionLibreAleatoire();
            if (pos != null) {
                Robot robot = new Robot(id++, pos);
                grille.ajouterRobot(robot);
                positionsOccupees.add(pos);
            }
        }
    }

    /**
     * Place les intrus aléatoirement.
     */
    public void placerIntrus() {
        int id = 0;
        for (int i = 0; i < nbIntrus; i++) {
            Position pos = positionLibreAleatoire();
            if (pos != null) {
                Intrus intrus = new Intrus(id++, pos);
                grille.ajouterIntrus(intrus);
                positionsOccupees.add(pos);
            }
        }
    }

    /**
     * Place les sacs d'argent aléatoirement.
     */
    public void placerSacs() {
        int id = 0;
        for (int i = 0; i < nbSacs; i++) {
            Position pos = positionLibreAleatoire();
            if (pos != null) {
                SacArgent sac = new SacArgent(id++, pos);  // 2 paramètres seulement
                grille.ajouterSac(sac);
                positionsOccupees.add(pos);
            }
        }
    }

    /**
     * Retourne une position aléatoire libre (sans obstacle, sans entité).
     */
    private Position positionLibreAleatoire() {
        int attempts = 0;
        int maxAttempts = grille.getNbLignes() * grille.getNbColonnes();

        while (attempts < maxAttempts) {
            Position pos = positionAleatoire();
            if (!positionsOccupees.contains(pos) && 
                grille.getCase(pos).getType() != TypeCase.OBSTACLE &&
                grille.getCase(pos).getEntite() == null) {
                return pos;
            }
            attempts++;
        }
        return null; // Pas de position libre trouvée
    }

    /**
     * Retourne une position aléatoire.
     */
    private Position positionAleatoire() {
        int x = random.nextInt(grille.getNbLignes());
        int y = random.nextInt(grille.getNbColonnes());
        return new Position(x, y);
    }

    // ---- Setters pour configuration ----
    public void setPourcentageObstacles(int pourcentage) {
        this.pourcentageObstacles = Math.max(0, Math.min(100, pourcentage));
    }

    public void setPourcentageSorties(int pourcentage) {
        this.pourcentageSorties = Math.max(0, Math.min(100, pourcentage));
    }

    public void setNbRobots(int nb) {
        this.nbRobots = Math.max(1, nb);
    }

    public void setNbIntrus(int nb) {
        this.nbIntrus = Math.max(1, nb);
    }

    public void setNbSacs(int nb) {
        this.nbSacs = Math.max(1, nb);
    }
}
