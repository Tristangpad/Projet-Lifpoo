package modele.plateau;

import modele.item.Couleur;
import modele.item.Item;
import modele.item.ItemShape;
import modele.item.ItemColor;

public class Mixer extends Machine {

    public Mixer() {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    @Override
    public void work() {
        if (current.size() < 2) return;

        Item itemCouleurGauche = current.get(0);
        Item itemCouleurDroite = current.get(1);

        if (itemCouleurGauche instanceof ItemColor c1 && itemCouleurDroite instanceof ItemColor c2) {
            c1.transform(c2.getColor());//fusionne la couleur c1 avec c2
            current.remove(c2);
        }
    }
}


