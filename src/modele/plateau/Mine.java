package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.Random;


public class Mine extends Machine {

    private int nb_obj = 0;

    @Override
    public void work() { // TODO : modifier, suivant le gisement
        if (new Random().nextInt(4) == 0) {
            if (c == null) return;


            Item gisement = c.getGisement();
            if  (gisement == null) return;
            current.add(new ItemShape( ( (ItemShape) gisement).getConfigItem() ) );
            System.out.println("vient de creer un objet %nb_obj");
        }

    }

    @Override
    public void send() {
        super.send();
    }
}
