package model.jeu;

import java.util.ArrayList;
import java.util.List;

import model.entites.Entite;
import model.entites.Intrus;
import model.entites.Robot;
import model.terrain.Grille;
import utils.Direction;
import utils.Position;

/**
 * Chef d'orchestre de la partie
 * Gère le tour, l'état du jeu, les sélections et les statistiques
 */
public class GestionnaireJeu {

    // --------------------
    // Attributs principaux
    // --------------------
    private  Grille grille;
    private EtatJeu etatActuel;
    private int tourActuel;        // 1 ou 2
    private int nombreToursJoues;

    private int intrusCaptures;
    private int intrusEchappes;

    private Robot robotSelectionne;
    private Intrus intrusSelectionne;

    // --------------------
    // Constructeur & initialisation
    // --------------------
    public GestionnaireJeu(int nbLignes, int nbColonnes) {
        this.grille = new Grille(nbLignes, nbColonnes);
        this.etatActuel = EtatJeu.CONFIGURATION;
        this.tourActuel = 1;
        this.nombreToursJoues = 0;
        this.intrusCaptures = 0;
        this.intrusEchappes = 0;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }

    public void initialiserGrille() {
        // Ici tu peux placer des obstacles, sorties, entités initiales
        // Ex : grille.ajouterObstacle(...), grille.ajouterRobot(...), etc.
        this.etatActuel = EtatJeu.CONFIGURATION;
    }

    public void demarrerPartie() {
        this.etatActuel = EtatJeu.EN_COURS;
    }

    // --------------------
    // Gestion des tours
    // --------------------
    public int getTourActuel() {
        return tourActuel;
    }

    public void changerTour() {
        tourActuel = (tourActuel == 1) ? 2 : 1;
    }

    public void incrementerTour() {
        nombreToursJoues++;
    }

    // --------------------
    // Sélection des entités
    // --------------------
    public void selectionnerRobot(Robot robot) {
        this.robotSelectionne = robot;
        this.intrusSelectionne = null; // déselectionner intrus si sélection robot
    }

    public void selectionnerIntrus(Intrus intrus) {
        this.intrusSelectionne = intrus;
        this.robotSelectionne = null; // déselectionner robot si sélection intrus
    }

    public void deselectionner() {
        this.robotSelectionne = null;
        this.intrusSelectionne = null;
    }

    // --------------------
    // Exécution d'un coup
    // --------------------
    public boolean jouerCoup(Entite entite, Direction direction) {
        if (etatActuel != EtatJeu.EN_COURS) return false;

        // Vérification du joueur (simplifiée : tour 1 = robots, tour 2 = intrus)
        if ((tourActuel == 1 && entite instanceof Intrus) ||
                (tourActuel == 2 && entite instanceof Robot)) {
            return false; // Mauvais joueur
        }

        // Calcul de la nouvelle position
        boolean deplace = grille.deplacerEntite(entite, entite.getPosition().deplacer(direction));

        if (deplace) {
            // Vérifier les captures après déplacement
            verifierCaptures(entite);
            
            incrementerTour();
            if (grille.partieTerminee()) terminerPartie();
            else changerTour();
        }

        return deplace;
    }
    
    /**
     * Vérifie si l'entité qui vient de se déplacer capture un intrus voisin
     * (si c'est un robot) ou se fait capturer (si c'est un intrus).
     */
    private void verifierCaptures(Entite entiteDeplacee) {
        Position pos = entiteDeplacee.getPosition();
        List<Position> positionsVoisines = obtenirPositionsVoisines(pos);
        
        if (entiteDeplacee instanceof Robot) {
            // Le robot capture les intrus voisins
            Robot robot = (Robot) entiteDeplacee;
            for (Position posVoisine : positionsVoisines) {
                Entite voisin = grille.getCase(posVoisine).getEntite();
                if (voisin instanceof Intrus) {
                    capturerIntrus((Intrus) voisin, robot);
                }
            }
        } else if (entiteDeplacee instanceof Intrus) {
            // L'intrus se fait capturer s'il y a un robot voisin
            Intrus intrus = (Intrus) entiteDeplacee;
            for (Position posVoisine : positionsVoisines) {
                Entite voisin = grille.getCase(posVoisine).getEntite();
                if (voisin instanceof Robot) {
                    capturerIntrus(intrus, (Robot) voisin);
                    break; // Capturer une seule fois
                }
            }
        }
    }
    
    /**
     * Retourne les 4 positions voisines (haut, bas, gauche, droite)
     */
    private List<Position> obtenirPositionsVoisines(Position pos) {
        List<Position> voisins = new ArrayList<>();
        int x = pos.getX();
        int y = pos.getY();
        
        // Haut
        if (x > 0) voisins.add(new Position(x - 1, y));
        // Bas
        if (x < grille.getNbLignes() - 1) voisins.add(new Position(x + 1, y));
        // Gauche
        if (y > 0) voisins.add(new Position(x, y - 1));
        // Droite
        if (y < grille.getNbColonnes() - 1) voisins.add(new Position(x, y + 1));
        
        return voisins;
    }
    
    /**
     * Capture un intrus.
     */
    private void capturerIntrus(Intrus intrus, Robot robot) {
        grille.retirerIntrus(intrus);
        incrementerIntrusCaptures();
        // TODO: Animer la capture
    }

    // --------------------
    // Vérification de fin
    // --------------------
    public boolean partieTerminee() {
        return grille.partieTerminee();
    }

    public void terminerPartie() {
        etatActuel = EtatJeu.TERMINEE;
    }

    public String obtenirResultat() {
        return grille.getResultat();
    }

    // --------------------
    // Statistiques
    // --------------------
    public int getIntrusCaptures() {
        return intrusCaptures;
    }

    public void incrementerIntrusCaptures() {
        intrusCaptures++;
    }

    public int getIntrusEchappes() {
        return intrusEchappes;
    }

    public void incrementerIntrusEchappes() {
        intrusEchappes++;
    }

    // --------------------
    // Getters
    // --------------------
    public Grille getGrille() { return grille; }
    public EtatJeu getEtatActuel() { return etatActuel; }
    public Robot getRobotSelectionne() { return robotSelectionne; }
    public Intrus getIntrusSelectionne() { return intrusSelectionne; }
    public int getNombreToursJoues() { return nombreToursJoues; }
}
