package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import modele.plateau.Point;
import java.util.LinkedList;
import java.util.Queue;

public class Machine implements Runnable {
    LinkedList<Item> current;
    Case c;
    Direction d = Direction.North; // par défaut, pour commencer, tout est orienté au north

    protected Point dimension = new Point(1,1);


    public Machine()
    {
        current = new LinkedList<Item>();
    }

    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    public void setCase(Case _c) {
        c= _c;
    }

    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0);
        } else {
            return null;
        }
    }

    public void send() // la machine dépose un item sur sa ou ses sorties
    {
        Case up = c.plateau.getCase(c, Direction.North);
        if (up != null) {
            Machine m = up.getMachine();
            ;
            if (m != null && !current.isEmpty()) {
                Item item = current.getFirst();
                m.current.add(item);
                current.remove(item);
            }
        }
    }

    public int getLargeur() {
        return dimension.x;
    }

    public int getHauteur() {
        return dimension.y;
    }

    public void work() {
    }; // action de la machine, aucune par défaut


    @Override
    public void run() {
        work();
        send();
    }



}
