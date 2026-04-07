package vuecontroleur;

public class Camera {
    public int offsetX = 0;
    public int offsetY = 0;

    // Beaucoup plus de niveaux de zoom ! (De très loin à très près)
    private static final int[] ZOOM_SIZES = {16, 24, 32, 48, 64, 82, 96, 128, 160};

    // On démarre avec un zoom "normal" (82px). Tu peux modifier cet index.
    private int zoomIndex = 5;

    public int getPxCase() {
        return ZOOM_SIZES[zoomIndex];
    }

    public void zoomer() {
        if (zoomIndex < ZOOM_SIZES.length - 1) zoomIndex++;
    }

    public void dezoomer() {
        if (zoomIndex > 0) zoomIndex--;
    }

    // NOUVEAU : On donne la taille actuelle de la vue pour bloquer la caméra sur les bords
    public void deplacer(int dx, int dy, int plateauSizeX, int plateauSizeY, int viewSizeX, int viewSizeY) {
        offsetX = Math.max(0, Math.min(offsetX + dx, plateauSizeX - viewSizeX));
        offsetY = Math.max(0, Math.min(offsetY + dy, plateauSizeY - viewSizeY));
    }
}