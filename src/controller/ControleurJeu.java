package controller;

import model.entites.Entite;
import model.jeu.GestionnaireJeu;
import model.terrain.Grille;
import utils.Direction;
import utils.Position;

/**
 * Controleur du jeu : fait le lien entre GestionnaireJeu et la View
 */
public class ControleurJeu {

    private final GestionnaireJeu gestionnaire;  // Référence au modèle
    private final ValidationMouvement validation;      // Utilitaire pour valider les mouvements
    private Entite entiteSelectionnee;           // Entité sélectionnée actuellement

    public ControleurJeu(GestionnaireJeu gestionnaire) {
        this.gestionnaire = gestionnaire;
        this.validation = new ValidationMouvement();
        this.entiteSelectionnee = null;
    }

    // --------------------
    // Gestion de la sélection
    // --------------------
    public void gererSelectionEntite(Entite entite) {
        this.entiteSelectionnee = entite;
    }

    /**
     * Déplace l'entité sélectionnée dans la direction donnée (utilisé par la vue clavier).
     * Retourne true si le déplacement a été effectué.
     */
    public boolean deplacerEntiteSelectionnee(Direction dir) {
        if (entiteSelectionnee == null || dir == null) return false;

        Position posActuelle = entiteSelectionnee.getPosition();
        Position cible = new Position(posActuelle.getX() + dir.getDx(), posActuelle.getY() + dir.getDy());

        Grille grille = gestionnaire.getGrille();
        if (!ValidationMouvement.mouvementValide(entiteSelectionnee, cible, grille)) return false;

        boolean deplace = gestionnaire.jouerCoup(entiteSelectionnee, dir);
        if (deplace) {
            deselectionnerEntite();
        }
        return deplace;
    }

    public void deselectionnerEntite() {
        this.entiteSelectionnee = null;
    }

    // --------------------
    // Gestion des clics sur la grille
    // --------------------
    public void gererClicCase(int x, int y) {
        Grille grille = gestionnaire.getGrille();
        Position pos = new Position(x, y);
        if (!grille.positionValide(pos)) return;

        if (entiteSelectionnee == null) {
            Entite e = grille.getCase(pos).getEntite();
            if (e != null) gererSelectionEntite(e);
        } else {
            gererDeplacementVers(pos);
        }
    }
    // --------------------
    // Déplacement d'une entité
    // --------------------
    public void gererDeplacementVers(utils.Position pos) {
        if (entiteSelectionnee == null) return;

        Grille grille = gestionnaire.getGrille();

        // Vérifier si le mouvement est valide
        if (!ValidationMouvement.mouvementValide(entiteSelectionnee, pos, grille)) return;

        // Calculer la direction
        int dx = pos.getX() - entiteSelectionnee.getPosition().getX();
        int dy = pos.getY() - entiteSelectionnee.getPosition().getY();
        Direction dir = Direction.fromDelta(dx, dy); // méthode utilitaire à créer

        if (dir != null) {
            boolean deplace = gestionnaire.jouerCoup(entiteSelectionnee, dir);
            if (deplace) {
                deselectionnerEntite();
            }
        }
    }


    // --------------------
    // Initialisation et configuration
    // --------------------
    public void demarrerNouvellePartie() {
        gestionnaire.initialiserGrille();
        gestionnaire.demarrerPartie();
    }

    public void configurerGrille(int nbLignes, int nbColonnes) {
        // Recrée la grille avec les nouvelles dimensions
        Grille nouvelleGrille = new Grille(nbLignes, nbColonnes);
        gestionnaire.setGrille(nouvelleGrille); // Vérifier que setGrille existe
    }

    // --------------------
    // Méthodes utilitaires
    // --------------------
    public void quitterJeu() {
        System.exit(0);
    }

    public void pauserJeu() {
        // Implémenter pause (optionnel)
    }

    public void reprendreJeu() {
        // Implémenter reprise (optionnel)
    }
}




/**
 package controller;

 import model.entites.Entite;
 import model.entites.Robot;
 import model.entites.Intrus;
 import model.terrain.Grille;
 import model.terrain.GestionnaireJeu;
 import utils.Position;
 import view.VueJeu;

 import java.util.List;

 /**
 * Contrôleur principal du jeu
 * Coordonne le modèle (GestionnaireJeu / Grille) et la vue (VueJeu)
 */

/**
 public class ControleurJeu {

 // --------------------
 // Attributs
 // --------------------
 private final GestionnaireJeu gestionnaire;
 private final ValidationMouvement validation;

 private VueJeu vue;  // Référence vers la vue, assignée plus tard

 // --------------------
 // Constructeur
 // --------------------
 public ControleurJeu(int nbLignes, int nbColonnes) {
 this.gestionnaire = new GestionnaireJeu(nbLignes, nbColonnes);
 this.validation = new ValidationMouvement();
 this.vue = null; // sera assignée via setVue plus tard
 }

 // --------------------
 // Setter pour la vue
 // --------------------
 public void setVue(VueJeu vue) {
 this.vue = vue;
 }

 // --------------------
 // Gestion des clics / sélection
 // --------------------
 public void gererClicCase(Position pos) {
 Entite selectionnee = gestionnaire.getRobotSelectionne() != null
 ? gestionnaire.getRobotSelectionne()
 : gestionnaire.getIntrusSelectionne();

 if (selectionnee == null) {
 // Si rien n'est sélectionné, on tente de sélectionner l'entité à cette position
 CaseEntiteÀSelectionner(pos);
 } else {
 // Si entité déjà sélectionnée, tenter de la déplacer
 gererDeplacementVers(pos);
 }
 }

 private void CaseEntiteÀSelectionner(Position pos) {
 Robot robot = gestionnaire.getGrille().getRobotA(pos);
 if (robot != null) {
 gererSelectionEntite(robot);
 return;
 }
 List<Intrus> adjacents = gestionnaire.getGrille().getIntrusAdjacents(pos);
 if (!adjacents.isEmpty()) {
 gererSelectionEntite(adjacents.get(0));
 }
 }

 public void gererSelectionEntite(Entite e) {
 if (e instanceof Robot) {
 gestionnaire.selectionnerRobot((Robot) e);
 } else if (e instanceof Intrus) {
 gestionnaire.selectionnerIntrus((Intrus) e);
 }
 if (vue != null) vue.rafraichirAffichage();
 }

 public void deselectionnerEntite() {
 gestionnaire.deselectionner();
 if (vue != null) vue.rafraichirAffichage();
 }

 // --------------------
 // Déplacement
 // --------------------
 public void gererDeplacementVers(Position pos) {
 Entite selectionnee = gestionnaire.getRobotSelectionne() != null
 ? gestionnaire.getRobotSelectionne()
 : gestionnaire.getIntrusSelectionne();

 if (selectionnee == null) return;

 if (validation.mouvementValide(selectionnee, pos, gestionnaire.getGrille())) {
 gestionnaire.jouerCoup(selectionnee, pos);
 if (vue != null) vue.rafraichirAffichage();

 if (gestionnaire.partieTerminee() && vue != null) {
 vue.afficherMessageFin(gestionnaire.obtenirResultat());
 }
 }
 }

 // --------------------
 // Initialisation / configuration
 // --------------------
 public void demarrerNouvellePartie() {
 gestionnaire.initialiserGrille();
 gestionnaire.demarrerPartie();
 if (vue != null) vue.rafraichirAffichage();
 }

 public void configurerGrille(/* paramètres de configuration */
/**{
 // implémentation optionnelle selon besoins
 }

 // --------------------
 // Utilitaires
 // --------------------
 public void quitterJeu() {
 System.exit(0);
 }

 public void pauserJeu() {
 // futur développement si nécessaire
 }

 public void reprendreJeu() {
 // futur développement si nécessaire
 }

 // --------------------
 // Getters
 // --------------------
 public GestionnaireJeu getGestionnaire() {
 return gestionnaire;
 }

 public ValidationMouvement getValidation() {
 return validation;
 }

 public VueJeu getVue() {
 return vue;
 }
 }*/
