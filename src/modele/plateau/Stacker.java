package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import modele.item.ItemColor;

public class Stacker extends Machine {

    private ItemShape shapeGauche = null;
    private ItemShape shapeDroite = null;

    public Stacker() {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    @Override
    public boolean receiveFrom(Item item, Direction dir) {
        if (dir == Direction.South && item instanceof ItemShape is) {
            // première forme disponible
            if (shapeGauche == null) {
                shapeGauche = is;
                return true;
            }
            if (shapeDroite == null) {
                shapeDroite = is;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFull() {
        return shapeGauche != null && shapeDroite != null;
    }

    @Override
    public void work() {
        if (current.size() < 2) return;
        if (shapeGauche == null || shapeDroite == null) return;
        shapeGauche.stack(shapeDroite);
        current.add(shapeGauche);
        shapeGauche = null;
        shapeDroite = null;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        //todo : adapter pour rotate par la suite so possible (pareil pour toutes les machines avec extention)
        Case sortie = c.plateau.getCase(c, Direction.North);
        if (sortie == null) return;
        Machine m = sortie.getMachine();
        if (m != null && !m.isFull()) {
            if (m.receiveFrom(current.getFirst(), Direction.South)) {
                current.removeFirst();
            }
        }
    }
}


