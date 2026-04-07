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
    public boolean receiveFrom(Item item, Direction dir, Case input) {
        //Pour les 2 entrées de la machine
        Point posInput  = c.plateau.getPosition(input);//position du tapis qui envoie l'item dans la machine
        Point posMachine = c.plateau.getPosition(c);//position de la case principale de la machine


        //case de gauche
        if (posInput.x == posMachine.x && dir == Direction.South && item instanceof ItemShape sg) {
            if (shapeGauche != null) return false;
            shapeGauche = sg;
            return true;
        }

        //case de droite
        if (posInput.x == posMachine.x + 1 && dir == Direction.South && item instanceof ItemShape sd) {
            if (shapeDroite != null) return false;
            shapeDroite = sd;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFull() {
        return shapeGauche != null && shapeDroite != null;
    }

    @Override
    public void work() {
        if (shapeGauche == null || shapeDroite == null) return;

        ItemShape itemComplet = shapeDroite.stack(shapeGauche);
        current.add(itemComplet);

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
            if (m.receiveFrom(current.getFirst(), Direction.South,c)) {
                current.removeFirst();
            }
        }
    }
}


