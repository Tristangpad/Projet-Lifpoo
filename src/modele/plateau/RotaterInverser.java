package modele.plateau;
import modele.item.Item;
import modele.item.ItemShape;

public class RotaterInverser extends Machine {

    private boolean aDejaRotate = false;

    public void tourner() {
        d = d.suivante();
    }

    @Override
    public boolean receive(Item item) {
        boolean recu = super.receive(item);
        if (recu) aDejaRotate = false;
        return recu;
    }

    @Override
    public void work() {
        if (!current.isEmpty() && !aDejaRotate) {
            ((ItemShape) current.getFirst()).rotate(false);
            aDejaRotate = true;
        }
    }

    @Override
    public void send() {
        super.send();
        if (current.isEmpty()) aDejaRotate = false;
    }
}