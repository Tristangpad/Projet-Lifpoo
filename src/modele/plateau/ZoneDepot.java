package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.Objects;


public class ZoneDepot extends Machine {

    private Niveau niveau;

    //un dépot est forcement liée a un niveau
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
            //peut recevoir n'importe quel item mais prends uniquement en compte ceux qui correspondent a l'item
            //nécessaire pour compléter le niveau
            if (niveau.verifierItem((ItemShape) item)) {
                niveau.incrementer();
            }

        }
        //au cas ou si il y a autre chose qu'une forme pour éviter de bloquer ce dernier
        current.removeFirst();
    }
}
