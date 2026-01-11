package observer;

/**
 * Interface représentant un sujet observable
 */
public interface Sujet {

    /**
     * Ajoute un observateur à la liste
     * @param o observateur à ajouter
     */
    void ajouterObservateur(ObservateurGrille o);

    /**
     * Retire un observateur de la liste
     * @param o observateur à retirer
     */
    void retirerObservateur(ObservateurGrille o);

    /**
     * Notifie tous les observateurs que le sujet a été modifié
     */
    void notifierObservateurs();
}
