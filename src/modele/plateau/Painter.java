package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import modele.item.ItemColor;
import modele.item.Couleur;

public class Painter extends Machine {
    private Couleur couleur;

    //item qu'attend la machine
    private ItemShape shapeEnAttente = null;
    private ItemColor colorEnAttente = null;

    public Painter()
    {
        this.dimension = new Point(2, 1); // 2 de large, 1 de haut
    }

    @Override
    public boolean receiveFrom(Item item, Direction dir) {
        //definitions des input de la machine
        if (dir == Direction.West && item instanceof ItemShape is) {
            if (shapeEnAttente != null) return false;
            shapeEnAttente = is;
            return true;
        }
        if (dir == Direction.North && item instanceof ItemColor ic) {
            if (colorEnAttente != null) return false;
            colorEnAttente = ic;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFull() {
        return shapeEnAttente != null && colorEnAttente != null;
    }

    @Override
    public void work() {
        //err: cas tant qu'il n'a pas une fomre et une couleur ne fait rien et bloque la file
        if (shapeEnAttente == null || colorEnAttente == null) return;
        shapeEnAttente.paint(colorEnAttente.getColor());//peint la forme
        current.add(shapeEnAttente);//ajout de la forme peint
        //file débloquer peut a nouveau prendre une forme et une couleur a appliquer
        shapeEnAttente = null;
        colorEnAttente = null;
    }

    @Override
    public void send() {
        //peut importe la longueur de la machine elle la forme finis est envoyer au bout d la machine
        if (current.isEmpty()) return;

        //parcours de la machine
        Case caseActuelle = c;
        for (int i = 0; i < dimension.x; i++) {
            caseActuelle = c.plateau.getCase(caseActuelle, Direction.East);
            if (caseActuelle == null) return;
        }

        //envoie de la forme
        Machine m = caseActuelle.getMachine();
        if (m != null && !m.isFull()) {
            if (m.receiveFrom(current.getFirst(), Direction.West)) {
                current.removeFirst();
            }
        }
    }

}
