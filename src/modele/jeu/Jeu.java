package modele.jeu;

import modele.plateau.*;
import modele.item.ItemShape;

public class Jeu extends Thread{
    private Plateau plateau;
    private Machine machineChoisie = new Tapis();//utilisation d'un supplier ?

    public Jeu() {
        plateau = new Plateau();

        plateau.transformeCaseEnGisement(5,10, new ItemShape("Cr----Cr"));
        plateau.transformeCaseEnGisement(3,10, new ItemShape("CbCbCrCr"));
        plateau.transformeCaseEnGisement(3,3, new ItemShape("CrCrCbCr"));

        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 5, new Poubelle());
        plateau.setMachine(3, 10, new Mine());
        plateau.setMachine(3, 5, new Poubelle());



        start();

    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void setMachineChoisie(Machine machine) {
        machineChoisie = machine;
    }

    public void press(int x, int y) {
        if (machineChoisie instanceof Tapis)
        { plateau.setMachine(x, y, new Tapis()); }
        else if (machineChoisie instanceof Mine)
        { plateau.setMachine(x, y, new Mine()); }
        else if (machineChoisie instanceof Poubelle)
        { plateau.setMachine(x, y, new Poubelle()); }
        else if (machineChoisie instanceof Cutter)
        { plateau.setMachine(x, y, new Cutter()); }
    }

    public void slide(int x, int y) {
        if (machineChoisie instanceof Tapis)
        { plateau.setMachine(x, y, new Tapis()); }
        else if (machineChoisie instanceof Mine)
        { plateau.setMachine(x, y, new Mine()); }
        else if (machineChoisie instanceof Poubelle)
        { plateau.setMachine(x, y, new Poubelle()); }
        else if (machineChoisie instanceof Cutter)
        { plateau.setMachine(x, y, new Cutter()); }
    }

    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            try {
                plateau.run();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }


}
