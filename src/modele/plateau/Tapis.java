package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Tapis extends Machine{

    private Direction dirInput = null;

    public void setDirInput(Direction d) { this.dirInput = d; }

    public Direction getDirInput() { return dirInput; }

    public void tourner() {
        d = d.suivante();
    }

    public Direction getDirection() { return d; }

    @Override
    public void send() // la machine dépose un item sur sa ou ses sorties
    {

        Case destination = c.plateau.getCase(c, d);
        if (destination != null) {
            Machine m = destination.getMachine();
            if (m != null && !current.isEmpty()) {
                Item item = current.getFirst();
                m.current.add(item);
                current.remove(item);
            }
        }
    }
}
