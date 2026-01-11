package view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Gestionnaire d'animations pour la grille de jeu.
 * Fournit des animations fluides et professionnelles pour tous les types de mouvement.
 */
public class AnimationHandler {

    private static final Duration ANIMATION_DURATION = Duration.millis(300);
    private static final Duration FAST_ANIMATION = Duration.millis(150);
    private static final Duration SLOW_ANIMATION = Duration.millis(500);

    /**
     * Anime le déplacement d'une entité d'un point à un autre.
     * Utilise TranslateTransition pour un mouvement fluide.
     */
    public void animerDeplacement(Node node, double fromX, double fromY, double toX, double toY) {
        if (node == null) return;

        Platform.runLater(() -> {
            node.setTranslateX(fromX);
            node.setTranslateY(fromY);

            TranslateTransition transition = new TranslateTransition(ANIMATION_DURATION, node);
            transition.setToX(toX);
            transition.setToY(toY);
            transition.setCycleCount(1);
            transition.setAutoReverse(false);
            transition.play();
        });
    }

    /**
     * Anime la capture d'un intrus par un robot.
     * Effet : flash rouge + zoom out + fade out.
     */
    public void animerCapture(Node node) {
        if (node == null) return;

        Platform.runLater(() -> {
            ParallelTransition parallel = new ParallelTransition();

            // Flash rouge
            DropShadow redGlow = new DropShadow();
            redGlow.setColor(Color.RED);
            redGlow.setRadius(10);
            node.setEffect(redGlow);

            // Zoom out (ScaleTransition)
            ScaleTransition scale = new ScaleTransition(FAST_ANIMATION, node);
            scale.setToX(0.3);
            scale.setToY(0.3);

            // Fade out
            FadeTransition fade = new FadeTransition(FAST_ANIMATION, node);
            fade.setToValue(0.0);

            parallel.getChildren().addAll(scale, fade);
            parallel.setOnFinished(e -> {
                node.setScaleX(1.0);
                node.setScaleY(1.0);
                node.setOpacity(1.0);
                node.setEffect(null);
            });
            parallel.play();
        });
    }

    /**
     * Anime le ramassage d'un sac d'argent par un intrus.
     * Effet : zoom in + fade out + déplacement vers le haut.
     */
    public void animerRamassage(Node node) {
        if (node == null) return;

        Platform.runLater(() -> {
            ParallelTransition parallel = new ParallelTransition();

            // Zoom in puis out
            ScaleTransition scale = new ScaleTransition(FAST_ANIMATION, node);
            scale.setToX(1.3);
            scale.setToY(1.3);

            // Fade out
            FadeTransition fade = new FadeTransition(FAST_ANIMATION, node);
            fade.setToValue(0.0);

            // Déplacement vers le haut
            TranslateTransition move = new TranslateTransition(FAST_ANIMATION, node);
            move.setToY(-50);

            parallel.getChildren().addAll(scale, fade, move);
            parallel.setOnFinished(e -> {
                node.setScaleX(1.0);
                node.setScaleY(1.0);
                node.setOpacity(1.0);
                node.setTranslateY(0);
            });
            parallel.play();
        });
    }

    /**
     * Anime l'évasion d'un intrus par une sortie.
     * Effet : fade out progressif + déplacement rapide vers le bas.
     */
    public void animerEvasion(Node node) {
        if (node == null) return;

        Platform.runLater(() -> {
            ParallelTransition parallel = new ParallelTransition();

            // Fade out progressif
            FadeTransition fade = new FadeTransition(SLOW_ANIMATION, node);
            fade.setToValue(0.0);

            // Déplacement vers le bas (sortie)
            TranslateTransition move = new TranslateTransition(SLOW_ANIMATION, node);
            move.setToY(100);

            parallel.getChildren().addAll(fade, move);
            parallel.setOnFinished(e -> {
                node.setOpacity(1.0);
                node.setTranslateY(0);
            });
            parallel.play();
        });
    }

    /**
     * Animation d'erreur : clignotement rapide de l'opacité.
     */
    public void animerErreur(Node node) {
        if (node == null) return;

        Platform.runLater(() -> {
            double originOpacity = node.getOpacity();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(0), new KeyValue(node.opacityProperty(), originOpacity)),
                    new KeyFrame(Duration.millis(100), new KeyValue(node.opacityProperty(), 0.4)),
                    new KeyFrame(Duration.millis(200), new KeyValue(node.opacityProperty(), originOpacity)),
                    new KeyFrame(Duration.millis(300), new KeyValue(node.opacityProperty(), 0.4)),
                    new KeyFrame(Duration.millis(400), new KeyValue(node.opacityProperty(), originOpacity))
            );
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    /**
     * Animation de selection : contour brillant autour de l'entité.
     */
    public void animerSelection(Node node) {
        if (node == null) return;

        Platform.runLater(() -> {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.YELLOW);
            glow.setRadius(8);
            glow.setSpread(0.5);
            node.setEffect(glow);

            // Animation pulse optionnelle
            ScaleTransition pulse = new ScaleTransition(Duration.millis(600), node);
            pulse.setToX(1.05);
            pulse.setToY(1.05);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
        });
    }

    /**
     * Retire l'animation de sélection.
     */
    public void retirerSelection(Node node) {
        if (node == null) return;

        Platform.runLater(() -> {
            node.setEffect(null);
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
    }

    /**
     * Animation de pulsation légère (utilisée pour attirer l'attention).
     */
    public void animerPulsation(Node node, int cycles) {
        if (node == null) return;

        Platform.runLater(() -> {
            ScaleTransition pulse = new ScaleTransition(Duration.millis(400), node);
            pulse.setToX(1.1);
            pulse.setToY(1.1);
            pulse.setCycleCount(cycles);
            pulse.setAutoReverse(true);
            pulse.play();
        });
    }
}
