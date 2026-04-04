package modele.item;

import modele.item.Couleur.*;

public class ItemColor extends Item {
    Couleur color;

    public ItemColor(Couleur color) {
        this.color = color;
    }

    public Couleur getColor() { return color; }


    public void transform(Couleur add) { // faire varier la couleur suivant la couleur ajoutée

    }

}
