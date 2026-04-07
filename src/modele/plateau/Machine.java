package modele.plateau;

import modele.item.Item;

import java.util.LinkedList;

public class Machine implements Runnable {
    LinkedList<Item> current;//file d'item contenue au sein d'une machine
    Case c;//case ou est poser la machine
    Direction d = Direction.North;// par défaut, pour commencer, tout est orienté au north
    //dir de la machin sur la grille (output)

    private boolean aRecuCeTick = false;

    protected Point dimension = new Point(1,1);

    public void setDirection(Direction dir) {
        this.d = dir;
    }

    public void tourner() {
        d = d.suivante();
    }

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

    public boolean getARecuCeTick() {
        return aRecuCeTick;
    }

    public void reset_ARecuCeTick() {
        aRecuCeTick = false;
    }

    public boolean isFull() {
        return !current.isEmpty();
    }

    public boolean receive(Item item) {
        if (isFull()) { return false; }
        current.add(item);
        aRecuCeTick = true;
        return true;
    }

    public boolean receiveFrom(Item item, Direction dir) {
        return receive(item); //par defaut recoit de toutes les dir
    }


    public void send() {
        if (aRecuCeTick) {return;}
        Case destination = c.plateau.getCase(c, d);
        if (destination != null) {
            Machine m = destination.getMachine();


            if (m != null && !this.current.isEmpty() && !m.isFull()) {
                if (m.receiveFrom(current.getFirst(), d.oppose())) {
                    current.removeFirst();
                }
            }
        }
    }
    //getters
    public Direction getDirection() { return d; }

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
