package modele.plateau;

public class Tapis extends Machine {

    private Direction dirInput = null;

    public void setDirInput(Direction d) { this.dirInput = d; }

    //pour savoir a quoi le tapis doit se connecter en priorité
    public Direction getDirInput() { return dirInput; }

    public void tourner() {
        d = d.suivante();
    }

    public Direction getDirection() { return d; }

    @Override
    public void send() {
        if (getARecuCeTick()) return;
        if (this.d == null || this.c == null || this.c.plateau == null) return;
        if (this.current.isEmpty()) return;

        Case destination = this.c.plateau.getCase(this.c, this.d);
        if (destination != null) {

            Machine m = destination.getMachine();
            if (m != null && !this.current.isEmpty()) {
                if (m.receiveFrom(this.current.getFirst(), this.d.oppose())) {
                    this.current.removeFirst();
                }
            }
        }
    }
}


