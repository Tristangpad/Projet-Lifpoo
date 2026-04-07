package modele.plateau;

import modele.item.Couleur;
import modele.item.Item;
import modele.item.ItemShape;
import modele.item.ItemColor;

public class Mixer extends Machine {

    private ItemColor colorGauche = null;
    private ItemColor colorDroite = null;

    public Mixer() {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    @Override
    public boolean isFull() {
        return colorGauche != null && colorDroite != null;
    }

    @Override
    public boolean receiveFrom(Item item, Direction dir, Case input) {
        //Pour les 2 entrées de la machine
        Point posInput  = c.plateau.getPosition(input);//position du tapis qui envoie l'item dans la machine
        Point posMachine = c.plateau.getPosition(c);//position de la case principale de la machine

        //case de gauche
        if (posInput.x == posMachine.x && dir == Direction.South
                && item instanceof ItemColor ic) {
            if (colorGauche != null) return false;
            colorGauche = ic;
            return true;
        }
        //case de droite
        if (posInput.x == posMachine.x + 1 && dir == Direction.South
                && item instanceof ItemColor ic) {
            if (colorDroite != null) return false;
            colorDroite = ic;
            return true;
        }
        return false;
    }

    @Override
    public void work() {
        if (colorGauche == null || colorDroite == null) return;
        colorGauche.transform(colorDroite.getColor());
        current.add(colorGauche);
        colorGauche = null;
        colorDroite = null;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        Case sortie = c.plateau.getCase(c, Direction.North);
        if (sortie == null) return;
        Machine m = sortie.getMachine();
        if (m != null && !m.isFull()) {
            if (m.receiveFrom(current.getFirst(), Direction.South, c)) {
                current.removeFirst();
            }
        }
    }
}


