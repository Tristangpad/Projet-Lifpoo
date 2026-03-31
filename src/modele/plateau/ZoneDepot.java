package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.Objects;


public class ZoneDepot extends Machine {

    private Niveau niveau;

    public ZoneDepot(Niveau n) {
        this.niveau = n;
        this.dimension = new Point(4, 4);
    }

    public void setNiveau(Niveau n) {
        this.niveau = n;
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;
        Item item = current.getFirst();
        if (item instanceof ItemShape) {
            if (niveau.verifierItem((ItemShape) item)) {
                niveau.incrementer();
            }
            current.removeFirst();
        }
    }
}
