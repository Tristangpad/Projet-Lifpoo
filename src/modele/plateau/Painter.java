package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import modele.item.ItemColor;
import modele.item.Couleur;

public class Painter extends Machine{
    private Couleur couleur;

    private ItemShape shapeEnAttente = null;
    private ItemColor colorEnAttente = null;

    public Painter()
    {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    @Override
    public boolean receiveFrom(Item item, Direction dir) {
        if (dir == Direction.West && item instanceof ItemShape is) {
            if (shapeEnAttente != null) return false;
            shapeEnAttente = is;
            return true;
        }
        if (dir == Direction.North && item instanceof ItemColor ic) {
            if (colorEnAttente != null) return false;
            colorEnAttente = ic;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFull() {
        return shapeEnAttente != null && colorEnAttente != null;
    }

    @Override
    public void work() {
        if (shapeEnAttente == null || colorEnAttente == null) return;
        shapeEnAttente.paint(colorEnAttente.getColor());
        current.add(shapeEnAttente);
        shapeEnAttente = null;
        colorEnAttente = null;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;

        Case caseActuelle = c;
        for (int i = 0; i < dimension.x; i++) {
            caseActuelle = c.plateau.getCase(caseActuelle, Direction.East);
            if (caseActuelle == null) return;
        }

        Machine m = caseActuelle.getMachine();
        if (m != null && !m.isFull()) {
            if (m.receiveFrom(current.getFirst(), Direction.West)) {
                current.removeFirst();
            }
        }
    }

}
