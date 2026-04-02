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

    // Tapis.java
    @Override
    public void send() {
        if (getARecuCeTick()) return; // ← empêche le double-move
        if (this.d == null || this.c == null || this.c.plateau == null) return;

        Case destination = this.c.plateau.getCase(this.c, this.d);
        if (destination != null) {
            Machine m = destination.getMachine();
            if (m != null && !this.current.isEmpty()) {
                if (m.receive(this.current.getFirst())) { // ← receive() vérifie isFull() + set aRecuCeTick
                    this.current.removeFirst();
                }
            }
        }
    }
}
