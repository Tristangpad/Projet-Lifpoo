
package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.Random;
import modele.item.ItemColor;

public class Mine extends Machine {

    private int nb_obj = 0;

    @Override
    public void work() { // TODO : modifier, suivant le gisement (fais)
        Item gisement = c.getGisement();
        //si la mine est sur un gisement de Forme
        if(gisement instanceof ItemShape gisementShape){
            if (!isFull() && new Random().nextInt(4) == 0 && gisementShape != null) {
                current.add(new ItemShape( (gisementShape).getConfigItem() ));
            }
        //si la mine est sur un gisement de Couleur
        } else if (gisement instanceof ItemColor gisementColor) {
            if (!isFull() && new Random().nextInt(4) == 0 && gisementColor != null) {
                current.add(new ItemColor(gisementColor.getColor()));
            }
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