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
    };

    public Jeu() {
        plateau = new Plateau();

        plateau.transformeCaseEnGisement(5,10, new ItemShape("Cr----Cr"));

        plateau.transformeCaseEnGisement(3,10, new ItemShape("CrCrCrCr"));
        plateau.transformeCaseEnGisement(3,3, new ItemShape("CrCrCbCr"));

        plateau.transformeCaseEnGisement(3,10, new ItemShape("CbCbCbCb"));
        plateau.transformeCaseEnGisement(10,10, new ItemShape("CrCrCrCr"));


        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 5, new Poubelle());
        plateau.setMachine(3, 10, new Mine());
        plateau.setMachine(3, 5, new Poubelle());

        plateau.transformeCaseEnGisement(2,2, new ItemShape("CrCr--Cr"));

        chargerNiveau(numeroNiveau);
        plateau.setMachine(10, 10, new Mine());



        start();

    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void setMachineChoisie(Machine machine) {
        machineChoisie = machine;
    }

    public void press(int x, int y) {
        Case caseCible = plateau.getCases()[x][y];
        Machine machineEnPlace = caseCible.getMachine();
        if (machineChoisie instanceof Tapis) {
            if(machineEnPlace instanceof Tapis){
                machineEnPlace.rotate90();
                System.out.println(machineEnPlace.getDirection());
            }
            else { plateau.setMachine(x, y, new Tapis()); }

        }
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



    public void chargerNiveau(int num) {
        niveauActuel = NIVEAU[num];
        plateau.setMachine(3, 7, new ZoneDepot(niveauActuel));
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
