package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.LinkedList;
import java.util.Random;

public class Cutter extends Machine{

    public Cutter() {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    @Override
    public void work() {
        //si vide ne fait rien
            if(current.isEmpty())
            {
                return;
            }

            Item item = current.getFirst();
            if(item instanceof ItemShape)
            {
                //crée un couple avec une moitié gauche et une moitié droite
                ItemShape[] coupleMoitier = ((ItemShape) item).couper();
                current.removeFirst();//plus besoins de l'ancienne forme
                current.add(coupleMoitier[0]);//partie gauche
                current.add(coupleMoitier[1]);//partie droite
            }

            //System.out.println(item);
    }

    @Override
    public void send() // la machine dépose un item sur ses deux sortie a la fois
    {
        //première case d'envoie
        Case up1 = c.plateau.getCase(c, Direction.North);
        //deuxième case d'envoie (passe par la case d'extention de la machine)
        Case droite = c.plateau.getCase(c, Direction.East);
        Case up2 = c.plateau.getCase(droite, Direction.North);

        //pour fonctionner les deux machines présente au dessus doivent exister et ne pas etre complet
        if (up1 != null || up2 != null) {
            Machine m1 = up1.getMachine();
            Machine m2 = up2.getMachine();
            if (m1 != null && m2 != null && !current.isEmpty() ) {
                Item itemG = current.removeFirst();
                Item itemD = current.removeFirst();

                m1.current.add(itemG);
                m2.current.add(itemD);
            }
        }
    }
}


