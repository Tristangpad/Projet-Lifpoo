package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.LinkedList;
import java.util.Random;

public class Cutter extends Machine{

    public Cutter() {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    private int nb_obj = 0;

    @Override
    public void work() {
            if(current.isEmpty())
            {
                return;
            }

            Item item = current.getFirst();
            if(item instanceof ItemShape)
            {
                ItemShape[] coupleMoitier = ((ItemShape) item).couper();

                current.removeFirst();

                current.add(coupleMoitier[0]);
                current.add(coupleMoitier[1]);
            }

            //System.out.println(item);
    }

    @Override
    public void send() {
        super.send();
    }
}


