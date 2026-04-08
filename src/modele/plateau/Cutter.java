package modele.plateau;
import modele.item.Item;
import modele.item.ItemShape;

public class Cutter extends Machine {

    private boolean aDejaCoupe = false;

    public Cutter() {
        this.dimension = new Point(2, 1);
    }

    @Override
    public boolean receive(Item item) {
        boolean recu = super.receive(item);
        if (recu) aDejaCoupe = false; // nouvel item → reset
        return recu;
    }

    @Override
    public void work() {
        if (current.isEmpty() || aDejaCoupe) return;

        Item item = current.getFirst();
        if (item instanceof ItemShape) {
            ItemShape[] coupleMoitier = ((ItemShape) item).couper();
            current.removeFirst();
            current.add(coupleMoitier[0]);
            current.add(coupleMoitier[1]);
            aDejaCoupe = true; // ne recoupe pas tant que pas envoyé
        }
    }

    @Override
    public void send() {
        Case up1 = c.plateau.getCase(c, Direction.North);
        Case droite = c.plateau.getCase(c, Direction.East);
        Case up2 = c.plateau.getCase(droite, Direction.North);

        if (up1 != null && up2 != null) {
            Machine m1 = up1.getMachine();
            Machine m2 = up2.getMachine();
            if (m1 != null && m2 != null && !current.isEmpty()) {
                Item itemG = current.removeFirst();
                Item itemD = current.removeFirst();
                m1.current.add(itemG);
                m2.current.add(itemD);
                aDejaCoupe = false; // items partis → reset
            }
        }
    }
}