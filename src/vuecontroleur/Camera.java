package vuecontroleur;

public class Camera {
    public int offsetX = 20;
    public int offsetY = 20;

    private static final int[] ZOOM_SIZES = {50,56,64,72,80,88,96,104,112,120,128};

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

    public void deplacer(int dx, int dy, int plateauSizeX, int plateauSizeY, int viewSizeX, int viewSizeY) {
        offsetX = Math.max(0, Math.min(offsetX + dx, plateauSizeX - viewSizeX));
        offsetY = Math.max(0, Math.min(offsetY + dy, plateauSizeY - viewSizeY));
    }
}