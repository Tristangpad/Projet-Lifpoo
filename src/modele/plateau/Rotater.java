package modele.plateau;
import modele.item.ItemShape;
import modele.item.Item;

public class Rotater extends Machine {

    private boolean aDejaRotate = false;

    public void tourner() {
        d = d.suivante();
    }

    @Override
    public boolean receive(Item item) {
        boolean recu = super.receive(item);
        if (recu) aDejaRotate = false; // nouvel item → reset
        return recu;
    }

    @Override
    public void work() {
        if (!current.isEmpty() && !aDejaRotate) {
            ((ItemShape) current.getFirst()).rotate(true);
            aDejaRotate = true; // ne tourne qu'une seule fois
        }
    }

    @Override
    public void send() {
        super.send();
        if (current.isEmpty()) aDejaRotate = false; // item parti → reset
    }
}