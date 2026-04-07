package modele.plateau;

import modele.item.ItemShape;

public class RotaterInverser extends Machine {

    public void tourner() {
        d = d.suivante();
    }

    //tourne les items a 90d
    @Override
    public void work() {
        if (!current.isEmpty()) {
            ((ItemShape) current.getFirst()).rotate(false);
        }
    }
}

