package model.terrain;

import java.util.ArrayList;
import java.util.List;

import model.entites.Entite;
import model.entites.Intrus;
import model.entites.Robot;
import model.entites.SacArgent;
import observer.ObservateurGrille;
import observer.Sujet;
import utils.Position;

/**
 * Classe centrale représentant la grille de jeu
 * Implémente le pattern Observer pour notifier la vue
 */
public class Grille implements Sujet {

    // --------------------
    // Attributs principaux
    // --------------------
    private final int nbLignes;
    private final int nbColonnes;
    private final Case[][] cases;

    private final List<Robot> robots;
    private final List<Intrus> intrus;
    private final List<SacArgent> sacs;
    private final List<Position> sorties;

    // Observateurs pour le pattern Observer
    private final List<ObservateurGrille> observateurs;

    // --------------------
    // Constructeur
    // --------------------
    public Grille(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.cases = new Case[nbLignes][nbColonnes];

        this.robots = new ArrayList<>();
        this.intrus = new ArrayList<>();
        this.sacs = new ArrayList<>();
        this.sorties = new ArrayList<>();
        this.observateurs = new ArrayList<>();

        // Initialisation des cases vides
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                cases[i][j] = new Case(new Position(i, j), TypeCase.VIDE);
            }
        }
    }

    // --------------------
    // Méthodes d'ajout
    // --------------------
    public void ajouterObstacle(Position pos) {
        if (positionValide(pos)) {
            cases[pos.getX()][pos.getY()] = new Case(pos, TypeCase.OBSTACLE);
            notifierObservateurs();
        }
    }

    public void ajouterSortie(Position pos) {
        if (positionValide(pos)) {
            cases[pos.getX()][pos.getY()] = new Case(pos, TypeCase.SORTIE);
            sorties.add(pos);
            notifierObservateurs();
        }
    }

    public void ajouterRobot(Robot robot) {
        Position pos = robot.getPosition();
        if (positionValide(pos) && caseEstLibre(pos)) {
            cases[pos.getX()][pos.getY()].setEntite(robot);
            robots.add(robot);
            notifierObservateurs();
        }
    }

    public void ajouterIntrus(Intrus intrusObj) {
        Position pos = intrusObj.getPosition();
        if (positionValide(pos) && caseEstLibre(pos)) {
            cases[pos.getX()][pos.getY()].setEntite(intrusObj);
            intrus.add(intrusObj);
            notifierObservateurs();
        }
    }

    public void ajouterSac(SacArgent sac) {
        Position pos = sac.getPosition();
        if (positionValide(pos) && caseEstLibre(pos)) {
            sacs.add(sac);
            notifierObservateurs();
        }
    }

    // --------------------
    // Méthodes de validation
    // --------------------
    public boolean positionValide(Position pos) {
        return pos.getX() >= 0 && pos.getX() < nbLignes
                && pos.getY() >= 0 && pos.getY() < nbColonnes;
    }

    public boolean caseEstLibre(Position pos) {
        return positionValide(pos) && cases[pos.getX()][pos.getY()].estLibre();
    }

    public Case getCase(Position pos) {
        if (!positionValide(pos)) return null;
        return cases[pos.getX()][pos.getY()];
    }

    // --------------------
    // Méthodes de recherche
    // --------------------
    public List<Intrus> getIntrusAdjacents(Position pos) {
        List<Intrus> adj = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Position p = new Position(pos.getX() + dx, pos.getY() + dy);
                if (positionValide(p)) {
                    Case c = getCase(p);
                    Entite e = c.getEntite();
                    if (e instanceof Intrus) adj.add((Intrus) e);
                }
            }
        }
        return adj;
    }

    public List<SacArgent> getSacsAdjacents(Position pos) {
        List<SacArgent> adj = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Position p = new Position(pos.getX() + dx, pos.getY() + dy);
                if (positionValide(p)) {
                    for (SacArgent sac : sacs) {
                        if (!sac.estRamasse() && sac.getPosition().equals(p)) {
                            adj.add(sac);
                        }
                    }
                }
            }
        }
        return adj;
    }

    public Robot getRobotA(Position pos) {
        Case c = getCase(pos);
        if (c != null && c.getEntite() instanceof Robot) {
            return (Robot) c.getEntite();
        }
        return null;
    }

    // --------------------
    // Déplacement d'entité
    // --------------------
    public boolean deplacerEntite(Entite entite, Position nouvellePos) {
        if (!positionValide(nouvellePos) || !caseEstLibre(nouvellePos)) return false;

        // Retirer de l'ancienne case
        Position anciennePos = entite.getPosition();
        Case ancienneCase = getCase(anciennePos);
        if (ancienneCase != null) ancienneCase.setEntite(null);

        // Placer dans la nouvelle case
        entite.setPosition(nouvellePos);
        Case nouvelleCase = getCase(nouvellePos);
        if (nouvelleCase != null) nouvelleCase.setEntite(entite);

        // Notifier tous les observateurs après un déplacement
        notifierObservateurs();
        return true;
    }

    // --------------------
    // Observer
    // --------------------
    @Override
    public void ajouterObservateur(ObservateurGrille o) {
        if (!observateurs.contains(o)) observateurs.add(o);
    }

    @Override
    public void retirerObservateur(ObservateurGrille o) {
        observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (ObservateurGrille o : observateurs) {
            o.onGrilleModifiee();
        }
    }

    // --------------------
    // Fin de partie
    // --------------------
    public boolean partieTerminee() {
        for (Intrus i : intrus) {
            if (i.estVivant()) return false;
        }
        return true;
    }

    public String getResultat() {
        int sacsVolés = 0;
        for (Intrus i : intrus) {
            sacsVolés += i.getSacsPortes().size();
        }
        if (partieTerminee()) {
            if (sacsVolés == 0) return "Robots gagnent";
            else return "Intrus gagnent";
        }
        return "Partie en cours";
    }

    // --------------------
    // Méthodes utilitaires
    // --------------------
    public void retirerIntrus(Intrus i) {
        Case c = getCase(i.getPosition());
        if (c != null) c.setEntite(null);
        intrus.remove(i);
        i.relacherSacs();
        notifierObservateurs();
    }

    public void afficherGrille() {
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                System.out.print(cases[i][j].getType().name().charAt(0) + " ");
            }
            System.out.println();
        }
    }

    // --------------------
    // Getters
    // --------------------
    public int getNbLignes() { return nbLignes; }
    public int getNbColonnes() { return nbColonnes; }
    public List<Robot> getRobots() { return robots; }
    public List<Intrus> getIntrus() { return intrus; }
    public List<SacArgent> getSacs() { return sacs; }
    public List<Position> getSorties() { return sorties; }
}
