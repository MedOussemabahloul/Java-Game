package utils;

import java.util.Objects;

/**
 * Représente une position (x, y) dans la grille.
 * Classe IMMUTABLE : une position ne change jamais,
 * tout déplacement retourne une nouvelle Position.
 */
public final class Position {

    private final int x;
    private final int y;

    /**
     * Constructeur
     *
     * @param x coordonnée horizontale
     * @param y coordonnée verticale
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // --------------------
    // Getters
    // --------------------

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // --------------------
    // Logique métier
    // --------------------

    /**
     * Retourne une nouvelle position déplacée selon une direction
     *
     * @param direction direction du déplacement
     * @return nouvelle Position
     */
    public Position deplacer(Direction direction) {
        return new Position(
                this.x + direction.getDx(),
                this.y + direction.getDy()
        );
    }

    /**
     * Vérifie si une autre position est adjacente (8 directions)
     *
     * @param autre autre position
     * @return true si adjacente
     */
    public boolean estAdjacente(Position autre) {
        int dx = Math.abs(this.x - autre.x);
        int dy = Math.abs(this.y - autre.y);

        return (dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0);
    }

    /**
     * Distance de Manhattan entre deux positions
     *
     * @param autre autre position
     * @return distance
     */
    public int distanceVers(Position autre) {
        return Math.abs(this.x - autre.x) + Math.abs(this.y - autre.y);
    }

    // --------------------
    // Méthodes utilitaires
    // --------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

