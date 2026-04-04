
package modele.jeu;

import modele.item.ItemColor;
import modele.plateau.*;
import modele.item.ItemShape;
import static modele.item.Couleur.*;

public class Jeu extends Thread{
    private Plateau plateau;
    private Machine machineChoisie = new Tapis();//utilisation d'un supplier ?
    private Direction directionMachine = Direction.North;

    private int dernierClickX = -1;
    private int dernierClickY = -1;

    //definition des variables pour les niveaux
    private int numeroNiveau = 0;
    private Niveau niveauActuel;
    private static final Niveau[] NIVEAU = {
            new Niveau("CrCrCrCr", 5),
            new Niveau("CrCr----", 10),
            new Niveau("------Cr", 20),
    };

    public Jeu() {
        plateau = new Plateau();

        plateau.transformeCaseEnGisement(5,10, new ItemShape("Cr----Cr"));
        plateau.transformeCaseEnGisement(3,10, new ItemShape("CrCrCrCr"));
        plateau.transformeCaseEnGisement(3,3, new ItemShape("CrCrCbCr"));
        plateau.transformeCaseEnGisement(5, 3, new ItemShape("RrRrRrRr"));
        plateau.transformeCaseEnGisement(7, 3, new  ItemShape("RrCrRrCr"));
        plateau.transformeCaseEnGisement(9, 3, new  ItemShape("RbCrRrCb"));

        plateau.transformeCaseEnGisement(11, 3, new ItemColor(White));


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
        else if (machineChoisie instanceof Painter)
        { plateau.setMachine(x, y, new Painter()); }
    }

    public void slide(int x, int y) {
        if (machineChoisie instanceof Tapis) {
            if(dernierClickX != -1 && dernierClickY != -1) {
                Machine tapisInput = plateau.getCase(dernierClickX, dernierClickY).getMachine();
                if (tapisInput instanceof Tapis) {
                    ((Tapis) tapisInput).setDirection(directionMachine);
                }
            }
            Tapis t = new Tapis();
            t.setDirection(directionMachine);
            if(dernierClickX != -1 && dernierClickY != -1){
                t.setDirInput(calculLiaisonTapis(dernierClickX,dernierClickY,x,y));
            }
            plateau.setMachine(x, y, t);

            dernierClickX = x;
            dernierClickY = y;
        }
        else if (machineChoisie instanceof Mine)
        { plateau.setMachine(x, y, new Mine()); }
        else if (machineChoisie instanceof Poubelle)
        { plateau.setMachine(x, y, new Poubelle()); }
        else if (machineChoisie instanceof Cutter)
        { plateau.setMachine(x, y, new Cutter()); }
        else if (machineChoisie instanceof Rotater)
        { plateau.setMachine(x, y, new Rotater()); }
        else if (machineChoisie instanceof Painter)
        { plateau.setMachine(x, y, new Painter()); }
    }

    public Direction calculLiaisonTapis(int x1, int y1,int x2, int y2) {
        int calcXD = x2-x1;
        int calcYD = y2-y1;
        switch (calcXD+ "," + calcYD)
        {
            case "0,-1" -> {return Direction.North;}
            case "0,1"  -> {return Direction.South;}
            case "1,0"  -> {return Direction.East;}
            case "-1,0" -> {return Direction.West;}

            default     -> {return Direction.North;}
        }
    }

    public void setDirectionMachine(Direction d) {
        directionMachine = d;
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
