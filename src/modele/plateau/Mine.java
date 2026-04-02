
package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.Random;


public class Mine extends Machine {

    private int nb_obj = 0;

    @Override
    public void work() { // TODO : modifier, suivant le gisement (fais)
        Item gisement = c.getGisement();
        if (!isFull() && new Random().nextInt(4) == 0 && gisement != null) {
            current.add(new ItemShape( ((ItemShape) gisement).getConfigItem() ));
        }
    }

    @Override
    public void send() {
        super.send();
    }


    public void tourner() {
        d = d.suivante();
    }

}