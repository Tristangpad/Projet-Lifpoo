package modele.item;

import modele.item.Couleur.*;

public class ItemColor extends Item {
    Couleur color;

    public ItemColor(Couleur color) {
        this.color = color;
    }

    public Couleur getColor() { return color; }


    public void transform(Couleur add) { // faire varier la couleur suivant la couleur ajoutée
        switch (this.color) {
            case Red:
                if (add == Couleur.Blue) this.color = Couleur.Purple;
                break;
            case Blue:
                switch (add) {
                    case Red:    this.color = Couleur.Purple;break;
                    case Yellow: this.color = Couleur.Green;break;
                    case White:  this.color = Couleur.Cyan;break;
                }
                break;
            case Yellow:
                if (add == Couleur.Blue) this.color = Couleur.Green;
                break;
            case White:
                if (add == Couleur.Blue) this.color = Couleur.Cyan;
                break;
        }
    }
}
