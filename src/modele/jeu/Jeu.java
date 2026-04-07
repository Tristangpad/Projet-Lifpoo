
package modele.jeu;

import modele.item.ItemColor;
import modele.plateau.*;
import modele.item.ItemShape;
import static modele.item.Couleur.*;

public class Jeu extends Thread{

    private Plateau plateau;
    private Machine machineChoisie = new Tapis();
    private Direction directionMachine = Direction.North;

    //variable pour calculer la liaison entre les tapis
    private int dernierClickX = -1;
    private int dernierClickY = -1;

    //definition des variables pour les niveaux
    private int numeroNiveau = 0;
    private Niveau niveauActuel;

    private static final Niveau[] NIVEAU = {
            //liste et créations des differents niveaux présent dans le jeux, possible d'en crées des aléatoires par la suite
            new Niveau("CrCrCrCr", 5),
            new Niveau("CrCr----", 10),
            new Niveau("------Cr", 20),
    };

    public Jeu() {
        //inti du jeux, du placement des gisements et des niveaux
        plateau = new Plateau();

        plateau.transformeCaseEnGisement(5,10, new ItemShape("Cr----Cr"));
        plateau.transformeCaseEnGisement(3,10, new ItemShape("CrCrCrCr"));
        plateau.transformeCaseEnGisement(3,3, new ItemShape("CrCrCbCr"));
        plateau.transformeCaseEnGisement(5, 3, new ItemShape("RrRrRrRr"));
        plateau.transformeCaseEnGisement(6,3, new ItemShape("SpSpSpSp"));
        plateau.transformeCaseEnGisement(7,3, new ItemShape("FbFbFbFb"));
        plateau.transformeCaseEnGisement(8, 3, new  ItemShape("RrCrRrCr"));
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
        {
            Tapis t = new Tapis();
            t.setDirection(directionMachine);
            plateau.setMachine(x, y, t);
            connectionAuto(x, y);
        }
        else if (machineChoisie instanceof Mine)
        { plateau.setMachine(x, y, new Mine()); }
        else if (machineChoisie instanceof Poubelle)
        { plateau.setMachine(x, y, new Poubelle()); }
        else if (machineChoisie instanceof Cutter)
        { plateau.setMachine(x, y, new Cutter()); }
        else if (machineChoisie instanceof Rotater)
        { plateau.setMachine(x, y, new Rotater()); }
        else if (machineChoisie instanceof RotaterInverser)
        { plateau.setMachine(x, y, new RotaterInverser()); }
        else if (machineChoisie instanceof Painter)
        { plateau.setMachine(x, y, new Painter()); }
    }

    public void slide(int x, int y) {
        //s'occupe de liaison des tapis en cas de slide
        if (machineChoisie instanceof Tapis) {
            if(dernierClickX != -1 && dernierClickY != -1) {
                //regarde le dernier click et a partir de ce dernier redeffinis l'ancien tapis pour qu'il colle au nouveau
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
            connectionAuto(x, y);
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
        else if (machineChoisie instanceof RotaterInverser)
        { plateau.setMachine(x, y, new RotaterInverser()); }
        else if (machineChoisie instanceof Painter)
        { plateau.setMachine(x, y, new Painter()); }
    }

    private void connectionAuto(int x, int y) {
        Case caseTapis = plateau.getCase(x, y);
        Machine tapis = caseTapis.getMachine();
        //err: connecte seulement les tapis
        if (!(tapis instanceof Tapis t)) return;

        //err : si le tapis a une dir deja definit par le joueur
        if (t.getDirInput() != null) return;

        //définit les 4 endroits a check pour un tapis
        Case voisinNord  = plateau.getCase(caseTapis, Direction.North);
        Case voisinSud   = plateau.getCase(caseTapis, Direction.South);
        Case voisinEst   = plateau.getCase(caseTapis, Direction.East);
        Case voisinOuest = plateau.getCase(caseTapis, Direction.West);

        //Nord
        if (voisinNord != null && voisinNord.getMachine() != null && !(voisinNord.getMachine() instanceof Tapis)
                && voisinNord.getMachine().getDirection() == Direction.South && !(voisinNord.getMachine() instanceof ZoneDepot)) {
            t.setDirInput(Direction.South);
            t.setDirection(Direction.South);
            return;
        }

        //Sud
        if (voisinSud != null && voisinSud.getMachine() != null && !(voisinSud.getMachine() instanceof Tapis)
                && voisinSud.getMachine().getDirection() == Direction.North && !(voisinSud.getMachine() instanceof ZoneDepot)) {
            t.setDirInput(Direction.North);
            t.setDirection(Direction.North);
            return;
        }

        //Est
        if (voisinEst != null && voisinEst.getMachine() != null && !(voisinEst.getMachine() instanceof Tapis)
                && voisinEst.getMachine().getDirection() == Direction.West && !(voisinOuest.getMachine() instanceof ZoneDepot)) {
            t.setDirInput(Direction.West);
            t.setDirection(Direction.West);
            return;
        }

        //Ouest
        if (voisinOuest != null && voisinOuest.getMachine() != null && !(voisinOuest.getMachine() instanceof Tapis)
                && voisinOuest.getMachine().getDirection() == Direction.East && !(voisinEst.getMachine() instanceof ZoneDepot)) {
            t.setDirInput(Direction.East);
            t.setDirection(Direction.East);
        }
    }

    public Direction calculerDirectionOpposee(Direction dir) {
        return dir.oppose();
    }

    public void TapisRefresh(int x, int y) {

        //même structure que connectionAuto mais actualise les tapis une fois placr avec un appelle a connectionAuto a chaque fois
        Case source = plateau.getCase(x, y);

        Case voisinNord  = plateau.getCase(source, Direction.North);
        Case voisinSud   = plateau.getCase(source, Direction.South);
        Case voisinEst   = plateau.getCase(source, Direction.East);
        Case voisinOuest = plateau.getCase(source, Direction.West);

        //Nord
        if (voisinNord != null && voisinNord.getMachine() instanceof Tapis) {
            Point pos = plateau.getPosition(voisinNord);
            if (pos != null) connectionAuto(pos.x, pos.y);
        }

        //Sud
        if (voisinSud != null && voisinSud.getMachine() instanceof Tapis) {
            Point pos = plateau.getPosition(voisinSud);
            if (pos != null) connectionAuto(pos.x, pos.y);
        }

        //Est
        if (voisinEst != null && voisinEst.getMachine() instanceof Tapis) {
            Point pos = plateau.getPosition(voisinEst);
            if (pos != null) connectionAuto(pos.x, pos.y);
        }

        //Ouest
        if (voisinOuest != null && voisinOuest.getMachine() instanceof Tapis) {
            Point pos = plateau.getPosition(voisinOuest);
            if (pos != null) connectionAuto(pos.x, pos.y);
        }
    }

    public Direction calculLiaisonTapis(int x1, int y1,int x2, int y2) {
        int calcXD = x2-x1;
        int calcYD = y2-y1;
        //calcule la future orientation du tapis
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
        TapisRefresh(x, y);
    }

    public void suppMachineJeu(int x, int y){
        plateau.suppMachinePlateau(x,y);
    }

    public void chargerNiveau(int num) {
        //a la création d'une partie charge le premier niveau via le tab contenant les niveaux
        // et une nouvlle Zone de Dépot est crée avec le bon l'objectif du niveau 1
        niveauActuel = NIVEAU[num];
        plateau.setMachine(9, 6, new ZoneDepot(niveauActuel));
    }

    public void niveauSuivant() {
        //permet d epasser au niveau suivant en augmentant le num de niveau
        if (numeroNiveau < NIVEAU.length - 1) {
            numeroNiveau++;
            niveauActuel = NIVEAU[numeroNiveau];
            plateau.setNiveauActuel(niveauActuel);
        }
    }

    //getters
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
                //tant que le niveau n'est pas considérer comme finis l'ont reste dans ce dernier
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
