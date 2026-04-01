package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.Random;

public class Rotater extends Machine{

    public void tourner() {
        d = d.suivante();
    }

    @Override
    public void work() {
        if (!current.isEmpty()) {
            ((ItemShape) current.getFirst()).rotate();
        }
    }
}

