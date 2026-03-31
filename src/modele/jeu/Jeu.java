
package modele.jeu;

import modele.plateau.*;
import modele.item.ItemShape;

public class Jeu extends Thread{
    private Plateau plateau;
    private Machine machineChoisie = new Tapis();//utilisation d'un supplier ?

    //definition des variables pour les niveaux
    private int numeroNiveau = 0;
    private Niveau niveauActuel;
    private static final Niveau[] NIVEAU = {
            new Niveau("CrCrCrCr", 5),
            new Niveau("CrCr---", 10),
            new Niveau("------Cr", 20),
    };

    public Jeu() {
        plateau = new Plateau();

        plateau.transformeCaseEnGisement(5,10, new ItemShape("Cr----Cr"));
        plateau.transformeCaseEnGisement(3,10, new ItemShape("CrCrCrCr"));
        plateau.transformeCaseEnGisement(3,3, new ItemShape("CrCrCbCr"));

        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 5, new Poubelle());
        plateau.setMachine(3, 10, new Mine());
        plateau.setMachine(3, 5, new Poubelle());

        chargerNiveau(numeroNiveau);

        start();

    }

    public void setMachineChoisie(Machine machine) {
        machineChoisie = machine;
    }

    public void press(int x, int y) {
        if (machineChoisie instanceof Tapis)
        { plateau.setMachine(x, y, new Tapis());}
        else if (machineChoisie instanceof Mine)
        { plateau.setMachine(x, y, new Mine()); }
        else if (machineChoisie instanceof Poubelle)
        { plateau.setMachine(x, y, new Poubelle()); }
        else if (machineChoisie instanceof Cutter)
        { plateau.setMachine(x, y, new Cutter()); }
        else if (machineChoisie instanceof Rotater)
        { plateau.setMachine(x, y, new Rotater()); }
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
        else if (machineChoisie instanceof Rotater)
        { plateau.setMachine(x, y, new Rotater()); }
    }

    public void rotateM(int x, int y) {
        plateau.changerDirMachine(x,y);
    }


    public void suppMachineJeu(int x, int y){
        plateau.suppMachinePlateau(x,y);
    }

    public void chargerNiveau(int num) {
        niveauActuel = NIVEAU[num];
        plateau.setMachine(9, 6, new ZoneDepot(niveauActuel));
    }

    public void niveauSuivant() {
        if (numeroNiveau < NIVEAU.length - 1) {
            numeroNiveau++;
            niveauActuel = NIVEAU[numeroNiveau];
            plateau.setNiveauActuel(niveauActuel);
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public int getNumeroNiveau() {return numeroNiveau;}

    public Niveau getNiveauActuel() { return niveauActuel; }

    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            try {
                plateau.run();
                if (niveauActuel != null && niveauActuel.niveauFinis()) {
                    niveauSuivant();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }


}
